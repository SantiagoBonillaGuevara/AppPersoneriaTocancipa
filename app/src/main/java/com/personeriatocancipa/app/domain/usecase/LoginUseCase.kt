package com.personeriatocancipa.app.domain.usecase

import com.personeriatocancipa.app.domain.model.Role
import com.personeriatocancipa.app.domain.repository.AuthRepository
import com.personeriatocancipa.app.domain.repository.UserRepository

class LoginUseCase(private val authRepository: AuthRepository, private val userRepository: UserRepository) {

    suspend fun execute(email: String, password: String): Result<Role>{
        val loginResult = authRepository.login(email, password)
        return if (loginResult.isSuccess){
            val uid = loginResult.getOrNull()!!
            userRepository.getRole(uid)
        } else{
            Result.failure(loginResult.exceptionOrNull()!!)
        }
    }
}