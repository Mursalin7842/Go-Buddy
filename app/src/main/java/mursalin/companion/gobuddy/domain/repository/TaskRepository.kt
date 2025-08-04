package mursalin.companion.gobuddy.domain.repository

import mursalin.companion.gobuddy.domain.model.Task
import mursalin.companion.gobuddy.domain.model.TaskStatus

interface TaskRepository {
    suspend fun getTasksForProject(projectId: String): Result<List<Task>>
    suspend fun addTask(task: Task): Result<Unit>
    suspend fun updateTaskStatus(taskId: String, status: TaskStatus): Result<Unit>
    suspend fun deleteTask(taskId: String): Result<Unit>
}