package mursalin.companion.gobuddy.domain.use_case.project

import mursalin.companion.gobuddy.domain.model.Project
import mursalin.companion.gobuddy.domain.repository.ProjectRepository
import javax.inject.Inject

class UpdateProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(project: Project): Result<Unit> {
        if (project.title.isBlank()) {
            return Result.failure(IllegalArgumentException("Project title cannot be empty."))
        }
        return repository.updateProject(project)
    }
}
