// FILE: app/src/main/java/mursalin/companion/gobuddy/domain/model/Project.kt
package mursalin.companion.gobuddy.domain.model

import java.util.Date

data class Project(
    val id: String,
    val userId: String,
    val title: String,
    val description: String,
    val startDate: Date,
    val endDate: Date,
    val status: String, // e.g., "Ongoing", "Done"
    val progress: Float = 0f // Calculated property
)
