package com.personeriatocancipa.app.domain.repository.ChatBotRepository

interface ChatBotRepository {
    suspend fun sendMessage(userMessage: String): Result<String>
}
