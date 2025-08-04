package mursalin.companion.gobuddy.domain.use_case.project

import mursalin.companion.gobuddy.domain.model.Project
import mursalin.companion.gobuddy.domain.repository.ProjectRepository
import javax.inject.Inject

class GetProjectsUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(): Result<List<Project>> {
        return repository.getProjects()
    }
}