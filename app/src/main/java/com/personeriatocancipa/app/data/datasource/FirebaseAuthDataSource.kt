package com.personeriatocancipa.app.data.datasource

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthDataSource {

    private val auth = FirebaseAuth.getInstance()

    fun login(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    fun sendPasswordReset(email: String): Task<Void> {
        return auth.sendPasswordResetEmail(email)
    }
}