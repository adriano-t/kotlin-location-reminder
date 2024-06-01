package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.number.OrderingComparison.greaterThan
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase

    @Before
    fun createDatabase() {
        // Using an in-memory database so that the information stored here disappears when the process is killed.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDatabase() = database.close()

    @Test
    fun saveReminder_getReminderById() = runBlockingTest {
        val reminderDto = ReminderDTO("title", "desc", "location", 1.2, 3.4)
        database.reminderDao().saveReminder(reminderDto)
        val result = database.reminderDao().getReminderById(reminderDto.id)
        assertThat(result as ReminderDTO, notNullValue())
        assertThat(result.id, `is`(reminderDto.id))
        assertThat(result.title, `is`(reminderDto.title))
        assertThat(result.description, `is`(reminderDto.description))
        assertThat(result.location, `is`(reminderDto.location))
        assertThat(result.latitude, `is`(reminderDto.latitude))
        assertThat(result.longitude, `is`(reminderDto.longitude))
    }

    @Test
    fun getReminders_isEmpty() = runBlockingTest {
        val reminders = database.reminderDao().getReminders()
        assertThat(reminders.size, `is`(0))
    }

    @Test
    fun getReminders_OneReminderAdded_isNotEmpty() = runBlockingTest {
        val reminder = ReminderDTO("title", "description", "location", 0.0, 0.0)
        database.reminderDao().saveReminder(reminder)
        val reminders = database.reminderDao().getReminders()

        assertThat(reminders.size, greaterThan(0))
        assertThat(reminders[0].title, `is`(reminder.title))
        assertThat(reminders[0].description, `is`(reminder.description))
        assertThat(reminders[0].location, `is`(reminder.location))
        assertThat(reminders[0].latitude, `is`(reminder.latitude))
        assertThat(reminders[0].longitude, `is`(reminder.longitude))
    }

    @Test
    fun getReminderById_NoReminders_ReturnsNull() = runBlockingTest {
        val reminder = database.reminderDao().getReminderById("1")
        assertThat(reminder, `is`(CoreMatchers.nullValue()))
    }

    @Test
    fun getReminderById_OneReminder_ReturnsReminder() = runBlockingTest {
        val reminder = ReminderDTO("title", "description", "location", 0.0, 0.0)
        database.reminderDao().saveReminder(reminder)
        val result = database.reminderDao().getReminderById(reminder.id)

        assertThat(result, `is`(notNullValue()))
        assertThat(result?.title, `is`(reminder.title))
        assertThat(result?.description, `is`(reminder.description))
        assertThat(result?.location, `is`(reminder.location))
        assertThat(result?.latitude, `is`(reminder.latitude))
        assertThat(result?.longitude, `is`(reminder.longitude))
    }

    @Test
    fun deleteAllReminders_OneReminderAdded_ReturnsEmptyList() = runBlockingTest {
        val reminder = ReminderDTO("title", "description", "location", 0.0, 0.0)
        database.reminderDao().saveReminder(reminder)
        assertThat(database.reminderDao().getReminders().size, `is`(1))
        database.reminderDao().deleteAllReminders()

        val reminders = database.reminderDao().getReminders()
        assertThat(reminders.size, `is`(0))
    }

    @Test
    fun getReminders_TwoRemindersAdded_ReturnsTwoReminders() = runBlockingTest {
        val reminder1 = ReminderDTO("title1", "desc1", "loc1", 1.2, 3.4)
        val reminder2 = ReminderDTO("title2", "desc2", "loc2", 5.6, 7.8)

        database.reminderDao().saveReminder(reminder1)
        database.reminderDao().saveReminder(reminder2)

        val remindersList = database.reminderDao().getReminders()
        assertThat(remindersList, `is`(notNullValue()))
        assertThat(remindersList.size, `is`(2))
        assertThat(remindersList[0].id, `is`(reminder1.id))
        assertThat(remindersList[1].id, `is`(reminder2.id))
    }

}
