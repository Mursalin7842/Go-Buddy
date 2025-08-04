package mursalin.companion.gobuddy.domain.use_case.reminder

import mursalin.companion.gobuddy.domain.model.Reminder
import mursalin.companion.gobuddy.domain.repository.ReminderRepository
import javax.inject.Inject

class SetReminderUseCase @Inject constructor(
    private val repository: ReminderRepository
) {
    suspend operator fun invoke(reminder: Reminder): Result<Unit> {
        // Business logic for validating a reminder can go here
        return repository.setReminder(reminder)
    }
}
