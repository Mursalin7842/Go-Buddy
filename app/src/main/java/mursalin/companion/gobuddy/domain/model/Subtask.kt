package mursalin.companion.gobuddy.domain.model

data class Subtask(
    val id: String,
    val taskId: String,
    val title: String,
    val isCompleted: Boolean
)