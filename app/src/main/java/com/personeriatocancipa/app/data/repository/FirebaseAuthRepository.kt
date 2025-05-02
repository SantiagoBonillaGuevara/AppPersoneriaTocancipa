package com.personeriatocancipa.app.data.repository

import com.personeriatocancipa.app.data.datasource.FirebaseAuthDataSource
import com.personeriatocancipa.app.domain.repository.AuthRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthRepository(
    private val authDataSrc: FirebaseAuthDataSource = FirebaseAuthDataSource()
): AuthRepository {

    override suspend fun login(email: String, password: String): Result<String> = suspendCoroutine { cont ->
        authDataSrc.login(email, password)
            .addOnSuccessListener {
                val uid = it.user?.uid
                if (uid != null) {
                    cont.resume(Result.success(uid))
                } else {
                    cont.resume(Result.failure(Exception("UID not found")))
                }
            }
            .addOnFailureListener{
                cont.resume(Result.failure(it))
            }
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> = suspendCoroutine { cont ->
        authDataSrc.sendPasswordReset(email)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) cont.resume(Result.success(Unit))
                else cont.resume(Result.failure(task.exception ?: Exception("Error sending password reset email")))
            }
    }
}