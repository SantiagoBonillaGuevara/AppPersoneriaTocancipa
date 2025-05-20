package com.personeriatocancipa.app.domain.usecase

import com.personeriatocancipa.app.domain.model.Pqrs
import com.personeriatocancipa.app.domain.repository.PqrsRepository
import kotlinx.coroutines.flow.Flow

class GetAllPqrsUseCase(
    private val repo: PqrsRepository
) {
    operator fun invoke(): Flow<List<Pqrs>> = repo.getAllPqrs()
}
