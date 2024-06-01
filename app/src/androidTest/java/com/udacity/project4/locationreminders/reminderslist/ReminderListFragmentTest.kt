package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.util.DataBindingIdlingResource
import com.udacity.project4.util.monitorFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
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
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@MediumTest
class ReminderListFragmentTest : AutoCloseKoinTest() {




    private lateinit var repository: ReminderDataSource
    private lateinit var appContext: Application
    private val idlingResource = DataBindingIdlingResource()

    @Before
    fun init() {
        stopKoin() // stop the original app koin
        appContext = getApplicationContext()
        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            single {
                SaveReminderViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            single { RemindersLocalRepository(get()) as ReminderDataSource }
            single { LocalDB.createRemindersDao(appContext) }
        }
        // declare a new koin module
        startKoin {
            modules(listOf(myModule))
        }
        // Get our real repository
        repository = get()

        // clear the data to start fresh
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

    // test the navigation of the fragment
    @Test
    fun testNavigationToSaveReminderFragment() {
        // Launch the fragment
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        idlingResource.monitorFragment(scenario)

        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // Click on the FAB to navigate to SaveReminderFragment
        onView(withId(R.id.addReminderFAB)).perform(click())

        // Verify that we navigate to the SaveReminderFragment
        verify(navController).navigate(ReminderListFragmentDirections.toSaveReminder())
        scenario.close()
    }

    // test the displayed data on the UI.
    @Test
    fun testDisplayNoData() {
        // Launch the fragment
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        idlingResource.monitorFragment(scenario)
        // Verify that "No Data" text is displayed
        onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))
        scenario.close()
    }

    // testing for the error messages.
    @Test
    fun testErrorMessages() {
        // Launch the fragment
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        idlingResource.monitorFragment(scenario)

        // Simulate error scenario
        runBlocking {
            repository.deleteAllReminders()
        }

        // Verify error message
        onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))
        scenario.close()
    }
}
