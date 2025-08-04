package mursalin.companion.gobuddy.domain.repository

import mursalin.companion.gobuddy.domain.model.Reminder

interface ReminderRepository {
    suspend fun setReminder(reminder: Reminder): Result<Unit>
}