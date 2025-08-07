package mursalin.companion.gobuddy.domain.model

import java.util.Date

enum class Priority {
    LOW, MEDIUM, HIGH
}

enum class TaskStatus {
    TODO, DOING, DONE, STUCK
}

data class Task(
    val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val dueDate: Date,
    val priority: Priority,
    val status: TaskStatus,
    val isBlocked: Boolean,
    val createdAt: Date
)
