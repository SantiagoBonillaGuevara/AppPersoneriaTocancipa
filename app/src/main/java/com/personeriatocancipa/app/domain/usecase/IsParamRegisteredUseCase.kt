package com.personeriatocancipa.app.domain.usecase

import com.personeriatocancipa.app.domain.repository.UserRepository

class IsParamRegisteredUseCase(private val userRepository: UserRepository) {

    suspend fun execute(param: String, value: String): Boolean {
        return userRepository.isParamRegistered(param, value)
    }
}