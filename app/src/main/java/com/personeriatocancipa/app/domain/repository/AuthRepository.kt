package com.personeriatocancipa.app.domain.repository

import com.personeriatocancipa.app.domain.model.Role

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun getRole(userId:String): Result<Role>
    suspend fun isEmailRegistered(email: String): Boolean
    suspend fun sendPasswordReset(email: String): Result<Unit>
}