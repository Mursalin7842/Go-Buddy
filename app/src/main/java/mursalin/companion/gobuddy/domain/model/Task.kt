// FILE: app/src/main/java/mursalin/companion/gobuddy/domain/model/Task.kt
package mursalin.companion.gobuddy.domain.model

import java.util.Date

data class Task(
    val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val dueDate: Date,
    val priority: Priority,
    val status: TaskStatus,
    val isBlocked: Boolean = false,
    val createdAt: Date = Date()
)

enum class TaskStatus {
    TODO, DOING, DONE, STUCK
}

enum class Priority {
    LOW, MEDIUM, HIGH
}
