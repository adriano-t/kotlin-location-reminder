package com.udacity.project4.locationreminders.savereminder

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.utils.CoroutineRule
import com.udacity.project4.locationreminders.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    private lateinit var viewModel: SaveReminderViewModel
    private lateinit var repository: FakeDataSource

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = CoroutineRule()

    @Before
    fun initViewModel() {
        repository = FakeDataSource()
        viewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(), repository)
    }


    @Test
    fun validateEnteredData_validReminder_returnTrue() {
        val reminder = ReminderDataItem(
            "title",
            "desc",
            "loc",
            1.0,
            2.0
        )
        val result = viewModel.validateEnteredData(reminder)
        assertThat(result).isTrue()
    }

    @Test
    fun validateEnteredData_locationEmpty_returnFalse() {
        val reminder = ReminderDataItem(
            "title",
            "desc",
            "",
            1.0,
            2.0
        )
        val result = viewModel.validateEnteredData(reminder)
        assertThat(result).isFalse()
        assertThat(viewModel.showSnackBarInt.getOrAwaitValue()).isEqualTo(R.string.err_select_location)
    }

    @Test
    fun validateEnteredData_titleEmpty_returnFalse() = runBlockingTest{
        val reminder = ReminderDataItem(
            "",
            "desc",
            "loc",
            1.0,
            2.0
        )
        val result = viewModel.validateEnteredData(reminder)
        assertThat(result).isFalse()
        val snackBarInt = viewModel.showSnackBarInt.getOrAwaitValue()
        assertThat(snackBarInt).isEqualTo(R.string.err_enter_title)
    }

    @Test
    fun validateAndSaveReminder_valid_showToast() {
        val reminder = ReminderDataItem(
            "title",
            "desc",
            "loc",
            1.0,
            2.0
        )
        val result = viewModel.validateAndSaveReminder(reminder)
        assertThat(result).isTrue()
        val msg = ApplicationProvider.getApplicationContext<Context>().getString(R.string.reminder_saved)
        assertThat(viewModel.showToast.value).isEqualTo(msg)
    }

    @Test
    fun validateAndSaveReminder_invalid_returnFalse() {
        val reminder = ReminderDataItem(
            "",
            "desc",
            "loc",
            1.0,
            2.0
        )
        val result = viewModel.validateAndSaveReminder(reminder)
        assertThat(result).isFalse()
    }

    @Test
    fun saveReminder_validReminder_verifyDataSource() = mainCoroutineRule.runBlockingTest {
        val reminder = ReminderDataItem("title", "desc", "loc", 1.0, 2.0)
        viewModel.validateAndSaveReminder(reminder)
        val result = repository.getReminder(reminder.id)
        assertThat(result).isNotNull()
        val savedReminder = (result as Result.Success).data
        assertThat(savedReminder.title).isEqualTo(reminder.title)
        assertThat(savedReminder.description).isEqualTo(reminder.description)
        assertThat(savedReminder.location).isEqualTo(reminder.location)
        assertThat(savedReminder.latitude).isEqualTo(reminder.latitude)
        assertThat(savedReminder.longitude).isEqualTo(reminder.longitude)
    }

    @Test
    fun onClear_resetLiveDataValues() {
        viewModel.onClear()
        assertThat(viewModel.reminderTitle.value).isNull()
        assertThat(viewModel.reminderDescription.value).isNull()
        assertThat(viewModel.reminderSelectedLocationStr.value).isNull()
        assertThat(viewModel.selectedPOI.value).isNull()
        assertThat(viewModel.latitude.value).isNull()
        assertThat(viewModel.longitude.value).isNull()
    }


}