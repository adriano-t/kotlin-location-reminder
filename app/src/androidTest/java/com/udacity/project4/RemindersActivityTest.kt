package com.udacity.project4

import android.app.Activity
import android.app.Application
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.util.DataBindingIdlingResource
import com.udacity.project4.util.monitorActivity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get

@RunWith(AndroidJUnit4::class)
@LargeTest
//END TO END test to black box test the app
// Extended Koin Test - embed autoclose @after method to close Koin after every test
class RemindersActivityTest : AutoCloseKoinTest() {

    private lateinit var repository: ReminderDataSource
    private lateinit var appContext: Application
    private val idlingResource = DataBindingIdlingResource()


    /**
     * As we use Koin as a Service Locator Library to develop our code, we'll also use Koin to test our code.
     * at this step we will initialize Koin related code to be able to use it in out testing.
     */
    @Before
    fun init() {
        stopKoin()//stop the original app koin
        appContext = getApplicationContext()
        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    appContext, get() as ReminderDataSource
                )
            }
            single {
                SaveReminderViewModel(
                    appContext, get() as ReminderDataSource
                )
            }
            single { RemindersLocalRepository(get()) as ReminderDataSource }
            single { LocalDB.createRemindersDao(appContext) }
        }
        //declare a new koin module
        startKoin {
            modules(listOf(myModule))
        }
        //Get our real repository
        repository = get()

        //clear the data to start fresh
        runBlocking {
            repository.deleteAllReminders()
        }
    }


    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun addReminder_whenValidInput_shouldDisplayInList() {
        // Start up the Reminders screen.
        val activityScenario = ActivityScenario.launch(RemindersActivity::class.java)
        idlingResource.monitorActivity(activityScenario)

        // Click on the FAB to add a new reminder.
        onView(withId(R.id.addReminderFAB)).perform(click())

        // Add reminder title and description.
        onView(withId(R.id.reminderTitle)).perform(typeText("Test Title"))
        onView(withId(R.id.reminderDescription)).perform(typeText("Test Description"))

        // Close the keyboard.
        closeSoftKeyboard()

        // Open map
        onView(withId(R.id.selectLocation)).perform(click())

        // Perform a long click on the map at the center
        onView(withId(R.id.map)).perform(performLongClick())

        // Press save button
        onView(withId(R.id.save_button)).perform(click())

        // Save the reminder.
        onView(withId(R.id.saveReminder)).perform(click())

        // Verify that the reminder is displayed in the list.
        onView(withText("Test Title")).check(matches(isDisplayed()))
        onView(withText("Test Description")).check(matches(isDisplayed()))

        // Make sure the activity is closed before resetting the db.
        activityScenario.close()
    }

    private fun performLongClick(): ViewAction {
        val mapCoordinates = CoordinatesProvider { view ->
            // Define the coordinates for the center of the map view
            val screenPos = IntArray(2)
            view.getLocationOnScreen(screenPos)
            val screenX = screenPos[0] + view.width / 2
            val screenY = screenPos[1] + view.height / 2
            floatArrayOf(screenX.toFloat(), screenY.toFloat())
        }
        return GeneralClickAction(Tap.LONG, mapCoordinates, Press.FINGER)
    }

    @Test
    fun displayNoDataMessage_whenNoReminders() {
        // Start up the Reminders screen.
        val activityScenario = ActivityScenario.launch(RemindersActivity::class.java)
        idlingResource.monitorActivity(activityScenario)

        // Verify that the "No Data" message is displayed.
        onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))

        // Close the activity.
        activityScenario.close()
    }

    @Test
    fun addReminder_withMissingTitle_showsError() {
        // Start up the Reminders screen.
        val activityScenario = ActivityScenario.launch(RemindersActivity::class.java)
        idlingResource.monitorActivity(activityScenario)

        // Click on the FAB to add a new reminder.
        onView(withId(R.id.addReminderFAB)).perform(click())

        // Leave the title blank and add a description.
        onView(withId(R.id.reminderDescription)).perform(typeText("Test Description"))

        // Close the keyboard.
        closeSoftKeyboard()

        // Select a location.
        onView(withId(R.id.selectLocation)).perform(click())

        // Perform a long click on the map at the center
        onView(withId(R.id.map)).perform(performLongClick())

        // Press save button
        onView(withId(R.id.save_button)).perform(click())

        // Attempt to save the reminder.
        onView(withId(R.id.saveReminder)).perform(click())

        // Verify that the error message is displayed.
        onView(withText(R.string.err_enter_title)).check(matches(isDisplayed()))

        // Close the activity.
        activityScenario.close()
    }


    @Test
    fun navigateFromListToSaves() {
        // Start up the Reminders screen.
        val activityScenario = ActivityScenario.launch(RemindersActivity::class.java)
        idlingResource.monitorActivity(activityScenario)

        // Click on the FAB to add a new reminder.
        onView(withId(R.id.addReminderFAB)).perform(click())

        // Verify that the Save Reminder screen is displayed.
        onView(withId(R.id.reminderTitle)).check(matches(isDisplayed()))

        // Navigate back to the Reminders List.
        pressBack()

        // Verify that the Reminders List is displayed.
        onView(withId(R.id.addReminderFAB)).check(matches(isDisplayed()))

        // Close the activity.
        activityScenario.close()
    }

    fun waitFor(delay: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "wait for $delay milliseconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }

    @Test
    fun testShowToastMessage() {
        // Start up the Reminders screen.
        val activityScenario = ActivityScenario.launch(RemindersActivity::class.java)
        idlingResource.monitorActivity(activityScenario)

        // Click on the FAB to add a new reminder.
        onView(withId(R.id.addReminderFAB)).perform(click())

        // Add reminder title and description.
        onView(withId(R.id.reminderTitle)).perform(typeText("Test Title"))
        onView(withId(R.id.reminderDescription)).perform(typeText("Test Description"))

        // Close the keyboard.
        closeSoftKeyboard()

        // Open map
        onView(withId(R.id.selectLocation)).perform(click())

        // Perform a long click on the map at the center
        onView(withId(R.id.map)).perform(performLongClick())

        // Press save button
        onView(withId(R.id.save_button)).perform(click())

        // Save the reminder.
        onView(withId(R.id.saveReminder)).perform(click())

        onView(withText(R.string.reminder_saved)).inRoot(
            withDecorView(
                not(`is`(getActivity(activityScenario).getWindow().getDecorView()))
            )
        ).check(matches(isDisplayed()))

        // Make sure the activity is closed before resetting the db.
        activityScenario.close()
    }

    private fun getActivity(activityScenario: ActivityScenario<RemindersActivity>): Activity {
        lateinit var activity: Activity
        activityScenario.onActivity {
            activity = it
        }
        return activity
    }

}
