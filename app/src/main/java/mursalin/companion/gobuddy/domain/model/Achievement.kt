package mursalin.companion.gobuddy.domain.model

import java.util.Date

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    // FIX: Changed from ImageVector to String to match the database attribute.
    val iconName: String,
    val dateEarned: Date? = null
)