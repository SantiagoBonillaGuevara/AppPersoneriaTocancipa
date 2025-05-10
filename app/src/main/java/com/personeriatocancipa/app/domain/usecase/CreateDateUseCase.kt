package com.personeriatocancipa.app.domain.usecase

import com.personeriatocancipa.app.domain.model.Date
import com.personeriatocancipa.app.domain.repository.DateRepository

class CreateDateUseCase(private val dateRepository: DateRepository) {
    suspend fun execute(id:Int,date: Date): Result<Unit> {
        return dateRepository.saveDate(id,date)
    }
}