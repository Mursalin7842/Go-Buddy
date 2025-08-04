// FILE: app/src/main/java/mursalin/companion/gobuddy/domain/model/ChatMessage.kt
package mursalin.companion.gobuddy.domain.model

import java.util.Date

data class ChatMessage(
    val id: String,
    val text: String,
    val sender: Sender,
    val timestamp: Date = Date()
)

enum class Sender {
    USER, AI
}