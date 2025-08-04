// FILE: app/src/main/java/mursalin/companion/gobuddy/domain/model/Achievement.kt
package mursalin.companion.gobuddy.domain.model

import androidx.compose.ui.graphics.vector.ImageVector
import java.util.Date

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val dateEarned: Date? = null
)