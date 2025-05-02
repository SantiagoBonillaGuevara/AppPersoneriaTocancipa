package com.personeriatocancipa.app.domain.usecase

import com.personeriatocancipa.app.domain.model.RegistrableUser
import com.personeriatocancipa.app.domain.repository.UserRepository

class GetUserUseCase(private val repository: UserRepository) {
    suspend fun execute(node:String,uid: String): Result<RegistrableUser> {
        return repository.getUserById(node, uid)
    }
}