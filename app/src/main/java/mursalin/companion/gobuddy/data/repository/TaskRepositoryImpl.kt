package mursalin.companion.gobuddy.data.repository

import io.appwrite.ID
import io.appwrite.services.Databases
import io.appwrite.Query
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import mursalin.companion.gobuddy.data.repository.AppwriteConstants.DB_ID
import mursalin.companion.gobuddy.data.repository.AppwriteConstants.TASKS_COLLECTION_ID
import mursalin.companion.gobuddy.domain.model.Priority
import mursalin.companion.gobuddy.domain.model.Task
import mursalin.companion.gobuddy.domain.model.TaskStatus
import mursalin.companion.gobuddy.domain.repository.TaskRepository

class TaskRepositoryImpl @Inject constructor(
    private val databases: Databases
) : TaskRepository {

    // Corrected the date format to handle timezone offsets like "+00:00"
    private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())

    override suspend fun getTasksForProject(projectId: String): Result<List<Task>> {
        return try {
            val response = databases.listDocuments(
                databaseId = DB_ID,
                collectionId = TASKS_COLLECTION_ID,
                queries = listOf(Query.equal("projectId", projectId))
            )
            val tasks = response.documents.map { document ->
                Task(
                    id = document.id,
                    projectId = document.data["projectId"] as String,
                    title = document.data["title"] as String,
                    description = document.data["description"] as String,
                    dueDate = isoFormat.parse(document.data["dueDate"] as String) ?: Date(),
                    priority = Priority.valueOf(document.data["priority"] as String),
                    status = TaskStatus.valueOf(document.data["status"] as String),
                    isBlocked = document.data["isBlocked"] as Boolean,
                    createdAt = isoFormat.parse(document.data["createdAt"] as String) ?: Date()
                )
            }
            Result.success(tasks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addTask(task: Task): Result<Unit> {
        return try {
            databases.createDocument(
                databaseId = DB_ID,
                collectionId = TASKS_COLLECTION_ID,
                documentId = ID.unique(),
                data = mapOf(
                    "projectId" to task.projectId,
                    "title" to task.title,
                    "description" to task.description,
                    "dueDate" to isoFormat.format(task.dueDate),
                    "priority" to task.priority.name,
                    "status" to task.status.name,
                    "isBlocked" to task.isBlocked,
                    "createdAt" to isoFormat.format(task.createdAt)
                )
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTaskStatus(taskId: String, status: TaskStatus): Result<Unit> {
        return try {
            databases.updateDocument(
                databaseId = DB_ID,
                collectionId = TASKS_COLLECTION_ID,
                documentId = taskId,
                data = mapOf("status" to status.name)
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> {
        return try {
            databases.deleteDocument(
                databaseId = DB_ID,
                collectionId = TASKS_COLLECTION_ID,
                documentId = taskId
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
