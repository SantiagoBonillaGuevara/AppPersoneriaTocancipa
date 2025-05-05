package com.personeriatocancipa.app.domain.usecase

import com.personeriatocancipa.app.domain.model.RegistrableUser
import com.personeriatocancipa.app.domain.repository.UserRepository

class GetAllUsersUseCase(private val repository: UserRepository) {
    suspend fun execute(node: String): Result<List<RegistrableUser>> {
        return repository.getUsers(node)
    }
}