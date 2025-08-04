package mursalin.companion.gobuddy.domain.repository

import mursalin.companion.gobuddy.domain.model.ChatMessage

interface ChatRepository {
    suspend fun sendMessage(message: String): Result<ChatMessage>
}