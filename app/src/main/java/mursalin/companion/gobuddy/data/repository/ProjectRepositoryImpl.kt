package mursalin.companion.gobuddy.data.repository

import io.appwrite.services.Databases
import mursalin.companion.gobuddy.domain.model.Project
import mursalin.companion.gobuddy.domain.repository.ProjectRepository
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val databases: Databases
) : ProjectRepository {

    private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

    override suspend fun getProjects(): Result<List<Project>> {
        return try {
            val response = databases.listDocuments(
                databaseId = DB_ID,
                collectionId = PROJECTS_COLLECTION_ID
            )
            val projects = response.documents.map { document ->
                Project(
                    id = document.id,
                    userId = document.data["userId"] as String,
                    title = document.data["title"] as String,
                    description = document.data["description"] as String,
                    status = document.data["status"] as String,
                    startDate = isoFormat.parse(document.data["startDate"] as String) ?: Date(),
                    endDate = isoFormat.parse(document.data["endDate"] as String) ?: Date()
                )
            }
            Result.success(projects)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addProject(title: String, description: String, startDate: Date, endDate: Date, status: String): Result<Unit> {
        // Appwrite logic to add a project would go here.
        return Result.success(Unit)
    }

    override suspend fun updateProject(project: Project): Result<Unit> {
        // Appwrite logic to update a project would go here.
        return Result.success(Unit)
    }

    override suspend fun deleteProject(projectId: String): Result<Unit> {
        // Appwrite logic to delete a project would go here.
        return Result.success(Unit)
    }
}