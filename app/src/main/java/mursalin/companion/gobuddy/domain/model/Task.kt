package mursalin.companion.gobuddy.domain.model

import java.util.Date

// Defines the specific types for Priority, which was causing type mismatch errors.
enum class Priority {
    LOW, MEDIUM, HIGH
}

// Defines the specific types for TaskStatus, which was also causing type mismatch errors.
enum class TaskStatus {
    TODO, DOING, DONE, STUCK
}

// The main data model for a Task, now using the correct Enum types.
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
