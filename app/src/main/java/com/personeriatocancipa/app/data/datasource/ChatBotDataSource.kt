// app/src/main/java/com/personeriatocancipa/app/data/datasource/ChatBotDataSource.kt
package com.personeriatocancipa.app.data.datasource

import com.personeriatocancipa.app.domain.usecase.SendMessageUseCase

/**
 * Envía el mensaje al LLM (Gemini/text-bison) mediante el UseCase.
 */
class ChatBotDataSource(
    apiKey: String
) {
    // construimos internamente el SendMessageUseCase con la API key
    private val sendMessageUseCase = SendMessageUseCase(apiKey)

    /** Devuelve Result<String> con la respuesta o el error. */
    suspend fun sendMessage(userMessage: String): Result<String> =
        sendMessageUseCase(userMessage)
}
