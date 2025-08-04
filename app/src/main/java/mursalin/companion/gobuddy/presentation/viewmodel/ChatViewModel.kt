package mursalin.companion.gobuddy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mursalin.companion.gobuddy.domain.model.ChatMessage
import mursalin.companion.gobuddy.domain.model.Sender
import mursalin.companion.gobuddy.domain.repository.ChatRepository
import javax.inject.Inject

data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val currentMessage: String = "",
    val isAiTyping: Boolean = false
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()

    fun onMessageChanged(message: String) {
        _state.update { it.copy(currentMessage = message) }
    }

    fun sendMessage() {
        if (_state.value.currentMessage.isBlank()) return

        val userMessage = ChatMessage(
            id = System.currentTimeMillis().toString(),
            text = _state.value.currentMessage,
            sender = Sender.USER
        )

        _state.update {
            it.copy(
                messages = it.messages + userMessage,
                currentMessage = "",
                isAiTyping = true
            )
        }

        viewModelScope.launch {
            chatRepository.sendMessage(userMessage.text)
                .onSuccess { aiResponse ->
                    _state.update {
                        it.copy(
                            messages = it.messages + aiResponse,
                            isAiTyping = false
                        )
                    }
                }
                .onFailure {
                    // Handle error, maybe show an error message
                    _state.update { it.copy(isAiTyping = false) }
                }
        }
    }
}