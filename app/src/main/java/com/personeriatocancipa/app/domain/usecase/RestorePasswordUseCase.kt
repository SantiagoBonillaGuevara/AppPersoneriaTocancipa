package com.personeriatocancipa.app.domain.usecase

import com.personeriatocancipa.app.domain.repository.AuthRepository
import com.personeriatocancipa.app.domain.repository.UserRepository

class RestorePasswordUseCase(private val authRepository: AuthRepository, private val userRepository: UserRepository) {

    suspend fun execute(email: String): Result<Unit> {
        val exists = userRepository.isParamRegistered("correo", email)
        return if (!exists) {
            Result.failure(Exception("Email is not registered"))
        } else {
            authRepository.sendPasswordReset(email)
        }
    }
}