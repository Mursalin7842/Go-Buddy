package mursalin.companion.gobuddy.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import mursalin.companion.gobuddy.domain.model.Priority
import mursalin.companion.gobuddy.domain.model.Task

@Composable
fun TaskCard(task: Task) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            PriorityTag(priority = task.priority)
        }
    }
}

@Composable
fun PriorityTag(priority: Priority) {
    val (text, color) = when (priority) {
        Priority.HIGH -> "High" to MaterialTheme.colorScheme.errorContainer
        Priority.MEDIUM -> "Medium" to MaterialTheme.colorScheme.tertiaryContainer
        Priority.LOW -> "Low" to MaterialTheme.colorScheme.secondaryContainer
    }
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .background(color, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}
