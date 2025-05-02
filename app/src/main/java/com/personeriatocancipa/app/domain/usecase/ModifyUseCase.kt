package com.personeriatocancipa.app.domain.usecase

import com.personeriatocancipa.app.domain.model.RegistrableUser
import com.personeriatocancipa.app.domain.repository.UserRepository

class ModifyUseCase(private val repository: UserRepository) {
    suspend fun execute(user: RegistrableUser, node: String): Result<Unit> {
        return repository.addUserToDatabase(node, user)
    }
}