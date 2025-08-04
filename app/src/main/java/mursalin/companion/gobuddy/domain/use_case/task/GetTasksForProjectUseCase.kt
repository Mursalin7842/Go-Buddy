package mursalin.companion.gobuddy.domain.use_case.task

import mursalin.companion.gobuddy.domain.model.Task
import mursalin.companion.gobuddy.domain.repository.TaskRepository
import javax.inject.Inject

class GetTasksForProjectUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(projectId: String): Result<List<Task>> {
        if (projectId.isBlank()) {
            return Result.failure(IllegalArgumentException("Project ID cannot be empty."))
        }
        return repository.getTasksForProject(projectId)
    }
}
