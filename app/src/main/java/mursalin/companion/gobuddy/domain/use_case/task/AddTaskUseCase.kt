package mursalin.companion.gobuddy.domain.use_case.task

import mursalin.companion.gobuddy.domain.model.Task
import mursalin.companion.gobuddy.domain.repository.TaskRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task): Result<Unit> {
        if (task.title.isBlank()) {
            return Result.failure(IllegalArgumentException("Task title cannot be empty."))
        }
        return repository.addTask(task)
    }
}
