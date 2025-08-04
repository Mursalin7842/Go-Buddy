package mursalin.companion.gobuddy.data.repository

import io.appwrite.ID
import io.appwrite.services.Databases
import mursalin.companion.gobuddy.domain.model.ChatMessage
import mursalin.companion.gobuddy.domain.model.Sender
import mursalin.companion.gobuddy.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val databases: Databases
) : ChatRepository {

    override suspend fun sendMessage(message: String): Result<ChatMessage> {
        return try {
            val aiResponse = ChatMessage(
                id = ID.unique(),
                text = "This is a simulated AI response to: '$message'",
                sender = Sender.AI
            )
            Result.success(aiResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}