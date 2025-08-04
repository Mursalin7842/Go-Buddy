package mursalin.companion.gobuddy.domain.use_case.project

import mursalin.companion.gobuddy.domain.repository.ProjectRepository
import java.util.Date
import javax.inject.Inject

class AddProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(title: String, description: String, startDate: Date, endDate: Date): Result<Unit> {
        if (title.isBlank()) {
            return Result.failure(IllegalArgumentException("Project title cannot be empty."))
        }
        return repository.addProject(title, description, startDate, endDate, "Ongoing")
    }
}
