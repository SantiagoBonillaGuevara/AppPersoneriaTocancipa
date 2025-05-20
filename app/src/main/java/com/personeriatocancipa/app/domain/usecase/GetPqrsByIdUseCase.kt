package com.personeriatocancipa.app.domain.usecase

import com.personeriatocancipa.app.domain.repository.PqrsRepository

class GetPqrsByIdUseCase(
    private val repo: PqrsRepository
) {
    suspend fun execute(id: String) = repo.getPqrsById(id)
}
