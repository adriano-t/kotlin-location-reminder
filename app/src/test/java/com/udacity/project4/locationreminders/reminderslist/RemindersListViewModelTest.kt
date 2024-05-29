package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.utils.CoroutineRule
import com.udacity.project4.locationreminders.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    private lateinit var viewModel: RemindersListViewModel
    private lateinit var repository: FakeDataSource

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = CoroutineRule()

    @Before
    fun initViewModel() {
        repository = FakeDataSource()
        viewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(), repository)
    }

    @Test
    fun loadReminders_checkLoading() {
        mainCoroutineRule.pauseDispatcher()
        viewModel.loadReminders()
        assertThat(viewModel.showLoading.getOrAwaitValue()).isTrue()
        mainCoroutineRule.resumeDispatcher()
        assertThat(viewModel.showLoading.getOrAwaitValue()).isFalse()
    }

    @Test
    fun loadReminders_add_notEmpty() = mainCoroutineRule.runBlockingTest {
        val reminder = ReminderDTO("title", "desc", "locationName", 1.0, 2.0)
        repository.saveReminder(reminder)
        viewModel.loadReminders()
        assertThat(viewModel.remindersList.getOrAwaitValue()).isNotEmpty()
    }

    @Test
    fun loadReminders_error_showSnackBar() {
        repository.setErrorState(true)
        viewModel.loadReminders()
        assertThat(viewModel.showSnackBar.getOrAwaitValue()).isEqualTo("Error Getting Reminders")
    }

}