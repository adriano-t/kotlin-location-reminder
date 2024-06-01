package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.number.OrderingComparison
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    // testing implementation to the RemindersLocalRepository.kt

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase
    private lateinit var remindersLocalRepository: RemindersLocalRepository

    @Before
    fun createDatabaseAndRepository() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()

        remindersLocalRepository = RemindersLocalRepository(
            database.reminderDao(),
            Dispatchers.Main
        )
    }

    @After
    fun closeDatabase() = database.close()

    @Test
    fun saveReminder_retrievesReminder() = runTest {
        val reminder = ReminderDTO("title", "description", "location", 1.0, 1.0)
        remindersLocalRepository.saveReminder(reminder)

        val result = (remindersLocalRepository.getReminder(reminder.id) as Result.Success).data

        assertThat(result.id, `is`(reminder.id))
        assertThat(result.title, `is`(reminder.title))
        assertThat(result.description, `is`(reminder.description))
        assertThat(result.location, `is`(reminder.location))
        assertThat(result.latitude, `is`(reminder.latitude))
        assertThat(result.longitude, `is`(reminder.longitude))
    }

    @Test
    fun getReminders_retrievesSavedReminders() = runTest {
        // Two new reminders saved in the database.
        val reminder1 = ReminderDTO("title1", "description1", "location1", 1.0, 1.0)
        val reminder2 = ReminderDTO("title2", "description2", "location2", 2.0, 2.0)
        remindersLocalRepository.saveReminder(reminder1)
        remindersLocalRepository.saveReminder(reminder2)

        val result = remindersLocalRepository.getReminders()  as Result.Success

        // Both reminders are returned.
        val reminders = result.data
        assertThat(reminders.size, `is`(2))
        assertThat(reminders[0].id, `is`(reminder1.id))
        assertThat(reminders[1].id, `is`(reminder2.id))
    }

    @Test
    fun getReminder_nonExistentId_returnsError() = runTest {
        val randomId = "random_weofkpwok"
        val result = remindersLocalRepository.getReminder(randomId) as Result.Error
        assertThat(result, instanceOf(Result.Error::class.java))
        assertThat(result.message, `is`("Reminder not found!"))
    }

    @Test
    fun deleteAllReminders_noRemindersRetrieved() = runTest {
        val reminder = ReminderDTO("title", "description", "location", 1.0, 1.0)
        remindersLocalRepository.saveReminder(reminder)
        remindersLocalRepository.deleteAllReminders()
        val result = remindersLocalRepository.getReminders()
        result as Result.Success
        assertThat(result.data.isEmpty(), `is`(true))
    }

    @Test
    fun saveReminder_listSizeGreaterThanOne() = runTest {
        val reminder1 = ReminderDTO("title1", "desc1", "location1", 1.0, 1.0)
        val reminder2 = ReminderDTO("title2", "desc2", "location2", 2.0, 2.0)
        remindersLocalRepository.saveReminder(reminder1)
        remindersLocalRepository.saveReminder(reminder2)
        val result = remindersLocalRepository.getReminders() as Result.Success
        assertThat(result.data.size, OrderingComparison.greaterThan(1))
    }
}
