package com.personeriatocancipa.app.domain.usecase

import com.personeriatocancipa.app.domain.repository.AuthRepository

class RestorePasswordUseCase(private val repository: AuthRepository) {
    suspend fun execute(email: String): Result<Unit> {
        val exists = repository.isEmailRegistered(email)
        return if (!exists) {
            Result.failure(Exception("Email is not registered"))
        } else {
            repository.sendPasswordReset(email)
        }
    }
}