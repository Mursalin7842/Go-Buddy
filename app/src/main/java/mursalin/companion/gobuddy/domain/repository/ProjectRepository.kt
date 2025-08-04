package mursalin.companion.gobuddy.domain.repository

import mursalin.companion.gobuddy.domain.model.Project
import java.util.Date

interface ProjectRepository {
    suspend fun getProjects(): Result<List<Project>>
    suspend fun addProject(title: String, description: String, startDate: Date, endDate: Date, status: String): Result<Unit>
    suspend fun updateProject(project: Project): Result<Unit>
    suspend fun deleteProject(projectId: String): Result<Unit>
}
