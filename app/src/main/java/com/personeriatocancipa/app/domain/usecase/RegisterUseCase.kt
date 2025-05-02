package com.personeriatocancipa.app.domain.usecase

import com.personeriatocancipa.app.domain.model.RegistrableUser
import com.personeriatocancipa.app.domain.repository.UserRepository

class RegisterUseCase(private val repository: UserRepository) {

    suspend fun execute(password: String, user: RegistrableUser,node: String): Result<Unit> {
        val email = user.correo ?: return Result.failure(Exception("El correo no puede ser nulo"))
        val authResult = repository.registerUser(email, password)
        return if (authResult.isSuccess) {
            val uid = authResult.getOrNull()!!
            repository.addUserToDatabase(node, user.withUid(uid))
        } else {
            Result.failure(authResult.exceptionOrNull()!!)
        }
    }
}