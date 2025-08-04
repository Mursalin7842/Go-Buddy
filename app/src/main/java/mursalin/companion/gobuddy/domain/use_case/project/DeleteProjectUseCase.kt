package mursalin.companion.gobuddy.domain.use_case.project

import mursalin.companion.gobuddy.domain.repository.ProjectRepository
import javax.inject.Inject

class DeleteProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(projectId: String): Result<Unit> {
        if (projectId.isBlank()) {
            return Result.failure(IllegalArgumentException("Project ID cannot be empty."))
        }
        return repository.deleteProject(projectId)
    }
}
