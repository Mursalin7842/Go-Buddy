// FILE: app/src/main/java/mursalin/companion/gobuddy/domain/repository/ChatRepository.kt
package mursalin.companion.gobuddy.domain.repository

import mursalin.companion.gobuddy.domain.model.ChatMessage

interface ChatRepository {
    suspend fun sendMessage(message: String): Result<ChatMessage>
}