package com.personeriatocancipa.app.domain.usecase

import com.personeriatocancipa.app.domain.model.Date
import com.personeriatocancipa.app.domain.repository.DateRepository

class GetDateByIdUseCase (private val dateRepository: DateRepository) {
    suspend fun execute(id: Int): Result<Date> {
        return dateRepository.getCitaById(id)
    }
}