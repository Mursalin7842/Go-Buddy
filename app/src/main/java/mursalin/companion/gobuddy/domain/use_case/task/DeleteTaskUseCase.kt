package mursalin.companion.gobuddy.domain.use_case.task

import mursalin.companion.gobuddy.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: String): Result<Unit> {
        if (taskId.isBlank()) {
            return Result.failure(IllegalArgumentException("Task ID cannot be empty."))
        }
        return repository.deleteTask(taskId)
    }
}
