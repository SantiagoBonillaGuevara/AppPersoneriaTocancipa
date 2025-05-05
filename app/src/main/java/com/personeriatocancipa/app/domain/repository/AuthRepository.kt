package com.personeriatocancipa.app.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun sendPasswordReset(email: String): Result<Unit>
}