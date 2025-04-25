package com.personeriatocancipa.app.domain.usecase

import com.personeriatocancipa.app.domain.model.Role
import com.personeriatocancipa.app.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {

    suspend fun execute(email: String, password: String): Result<Role>{
        val loginResult = repository.login(email, password)
        return if (loginResult.isSuccess){
            val uid = loginResult.getOrNull()!!
            repository.getRole(uid)
        } else{
            Result.failure(loginResult.exceptionOrNull()!!)
        }
    }
}