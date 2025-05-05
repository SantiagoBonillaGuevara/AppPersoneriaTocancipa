package com.personeriatocancipa.app.domain.repository

import com.personeriatocancipa.app.domain.model.RegistrableUser
import com.personeriatocancipa.app.domain.model.Role

interface UserRepository {
    suspend fun getRole(userId:String): Result<Role>
    suspend fun isParamRegistered(param: String, value: String): Boolean
    suspend fun registerUser(email: String, password: String): Result<String>
    suspend fun addUserToDatabase(node: String, user: RegistrableUser): Result<Unit>
    suspend fun getUserById(node:String, uid: String): Result<RegistrableUser>
    suspend fun getUsers(node: String): Result<List<RegistrableUser>>
}