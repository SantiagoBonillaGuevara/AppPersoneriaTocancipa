package com.personeriatocancipa.app.data.repository

import com.personeriatocancipa.app.data.datasource.FirebaseUserDataSource
import com.personeriatocancipa.app.domain.model.RegistrableUser
import com.personeriatocancipa.app.domain.model.Role
import com.personeriatocancipa.app.domain.repository.UserRepository

class FirebaseUserRepository(
    private val userDataSrc: FirebaseUserDataSource = FirebaseUserDataSource()
):UserRepository {

    override suspend fun isParamRegistered(param: String, value: String): Boolean{
        return userDataSrc.isParamRegistered(param, value)
    }

    override suspend fun getRole(userId: String): Result<Role>{
        return userDataSrc.getUserRole(userId)
    }

    override suspend fun registerUser(email: String, password: String): Result<String> {
        return userDataSrc.registerWithEmailAndPassword(email, password)
    }

    override suspend fun addUserToDatabase(node: String, user: RegistrableUser): Result<Unit> {
        return userDataSrc.addUserToNode(node, user)
    }

    override suspend fun getUserById(node: String, uid: String): Result<RegistrableUser> {
        return userDataSrc.getUserById(node, uid)
    }

    override suspend fun getUsers(node: String): Result<List<RegistrableUser>> {
        return userDataSrc.getUsers(node)
    }

}