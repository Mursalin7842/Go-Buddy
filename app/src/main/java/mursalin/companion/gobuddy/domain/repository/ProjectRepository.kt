package mursalin.companion.gobuddy.domain.repository

import mursalin.companion.gobuddy.domain.model.Project

interface ProjectRepository {
    suspend fun getProjects(): Result<List<Project>>
    suspend fun getProjectById(projectId: String): Result<Project> // New function
    suspend fun addProject(project: Project): Result<Unit>
    suspend fun updateProject(project: Project): Result<Unit>
    suspend fun deleteProject(projectId: String): Result<Unit>
}
