// app/src/main/java/com/personeriatocancipa/app/ui/chat/viewmodel/ChatBotViewModel.kt
package com.personeriatocancipa.app.ui.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.personeriatocancipa.app.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatBotViewModel(
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    data class Message(val text: String, val fromUser: Boolean)

    private val _conversation = MutableStateFlow<List<Message>>(emptyList())
    val conversation: StateFlow<List<Message>> = _conversation

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        // 1) agregamos el mensaje del usuario
        _conversation.update { it + Message(text, fromUser = true) }

        // 2) llamamos al UseCase (operador invoke)
        viewModelScope.launch {
            val res = sendMessageUseCase(text)

            // 3) manejamos el resultado con onSuccess/onFailure
            res
                .onSuccess { output ->
                    _conversation.update {
                        it + Message(output, fromUser = false)
                    }
                }
                .onFailure { error ->
                    _conversation.update {
                        it + Message("Error: ${error.message}", fromUser = false)
                    }
                }
        }
    }

    /** Factory para inyectar el UseCase con la API key */
    class Factory(
        private val useCase: SendMessageUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == ChatBotViewModel::class.java)
            return ChatBotViewModel(useCase) as T
        }
    }
}
