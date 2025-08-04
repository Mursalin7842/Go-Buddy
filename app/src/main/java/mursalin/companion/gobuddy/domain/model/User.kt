// =================================================================================
// 📁 DATA LAYER
// =================================================================================

// ---------------------------------------------------------------------------------
// 📦 model
// ---------------------------------------------------------------------------------

// FILE: app/src/main/java/mursalin/companion/gobuddy/domain/model/User.kt
package mursalin.companion.gobuddy.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val theme: String = "system",
    val streakCount: Int = 0
)