package com.personeriatocancipa.app.domain.usecase

import com.personeriatocancipa.app.domain.model.Date
import com.personeriatocancipa.app.domain.repository.DateRepository

class ModifyDateUseCase(private val repository: DateRepository) {
    suspend fun execute(dateId: Int, updatedDate: Date): Result<Unit> {
        return repository.modifyDate(dateId, updatedDate)
    }
}