// app/src/main/java/com/personeriatocancipa/app/data/repository/ChatBotRepository.kt
package com.personeriatocancipa.app.data.repository

import com.personeriatocancipa.app.BuildConfig
import com.personeriatocancipa.app.data.datasource.ChatBotDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Capa de repositorio para el ChatBot.
 */
class ChatBotRepository(
    private val ds: ChatBotDataSource = ChatBotDataSource(BuildConfig.GEMINI_API_KEY)
) {
    /** Envía el mensaje y retorna Result<String> */
    suspend fun sendMessage(userMessage: String): Result<String> =
        withContext(Dispatchers.IO) {
            ds.sendMessage(userMessage)
        }
}
