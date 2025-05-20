package com.personeriatocancipa.app.domain.usecase

import com.personeriatocancipa.app.domain.model.Pqrs
import com.personeriatocancipa.app.domain.repository.PqrsRepository

class CreatePqrsUseCase(
    private val repo: PqrsRepository
) {
    suspend fun execute(pqrs: Pqrs) = repo.createPqrs(pqrs)
}
