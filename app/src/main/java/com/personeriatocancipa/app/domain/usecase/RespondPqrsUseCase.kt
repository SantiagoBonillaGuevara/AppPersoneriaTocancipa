package com.personeriatocancipa.app.domain.usecase

import com.personeriatocancipa.app.domain.repository.PqrsRepository

class RespondPqrsUseCase(
    private val repo: PqrsRepository
) {
    suspend fun execute(id: String, response: String) =
        repo.respondPqrs(id, response)
}
