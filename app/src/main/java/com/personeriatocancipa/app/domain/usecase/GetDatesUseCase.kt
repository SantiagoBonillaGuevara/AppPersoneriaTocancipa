package com.personeriatocancipa.app.domain.usecase

import com.personeriatocancipa.app.domain.model.Date
import com.personeriatocancipa.app.domain.repository.DateRepository

class GetDatesUseCase(private val dateRepository: DateRepository) {
    suspend fun execute(typeEmail:String, email:String): Result<List<Date>> {
        return dateRepository.getCitas(typeEmail, email)
    }
}