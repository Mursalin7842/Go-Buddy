package mursalin.companion.gobuddy.domain.model

import java.util.Date

data class Reminder(
    val id: String,
    val taskId: String,
    val reminderTime: Date,
    val repeat: String // e.g., "daily", "weekly", "once"
)
