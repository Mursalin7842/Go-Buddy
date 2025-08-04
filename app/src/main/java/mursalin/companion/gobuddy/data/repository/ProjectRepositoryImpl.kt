package mursalin.companion.gobuddy.data.repository

import io.appwrite.services.Databases
import mursalin.companion.gobuddy.domain.model.Project
import mursalin.companion.gobuddy.domain.repository.ProjectRepository
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

const val DB_ID = "db_gobuddy"
const val PROJECTS_COLLECTION_ID = "projects"

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
}
