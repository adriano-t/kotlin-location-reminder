package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource : ReminderDataSource {

    // Create a fake data source to act as a double to the real data source
    private val reminderDTOs = mutableListOf<ReminderDTO>()
    private var error = false;

    fun setErrorState(value: Boolean) {
        error = value
    }

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return if (error) {
            Result.Error("Error Getting Reminders")
        } else {
            Result.Success(reminderDTOs)
        }
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminderDTOs.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (error) {
            return Result.Error("Error retrieving reminder $id")
        }

        val reminder = reminderDTOs.find { r -> r.id == id }
        return if (reminder != null) {
            Result.Success(reminder)
        } else {
            Result.Error("Could not find reminder $id")
        }
    }

    override suspend fun deleteAllReminders() {
        reminderDTOs.clear()
    }


}