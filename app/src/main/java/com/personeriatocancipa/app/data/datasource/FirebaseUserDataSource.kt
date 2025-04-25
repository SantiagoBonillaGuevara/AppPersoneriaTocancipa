package com.personeriatocancipa.app.data.datasource

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.personeriatocancipa.app.domain.model.Role
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume

class FirebaseUserDataSource {

    private val db = FirebaseDatabase.getInstance()

    suspend fun getUserRole(userId: String): Result<Role> = suspendCoroutine { cont ->
        val paths = UserCollectionProvider().getCollections()

        fun checkNextPath(paths: List<Pair<String, Role>>) {
            if (paths.isEmpty()) {
                cont.resume(Result.failure(Exception("Rol no encontrado")))
                return
            }

            val (path, role) = paths.first()
            db.getReference(path).child(userId).get()
                .addOnSuccessListener {
                    val estado = it.child("estado").value?.toString()
                    if (it.exists() && estado=="Activo") {
                        cont.resume(Result.success(role))
                    } else {
                        Log.i("FirebaseUserDataSource", "Rol no encontrado en $path")
                        checkNextPath(paths.drop(1))
                    }
                }
                .addOnFailureListener {
                    cont.resume(Result.failure(it))
                }
        }

        checkNextPath(paths)
    }

    suspend fun isEmailRegistered(email: String): Boolean = suspendCoroutine { cont ->
        val paths:List<String> = UserCollectionProvider().getCollectionsName()

        fun check(pathsLeft: List<String>) {
            if (pathsLeft.isEmpty()) {
                cont.resume(false)
                return
            }

            val ref = db.getReference(pathsLeft.first())
            ref.orderByChild("correo").equalTo(email).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        cont.resume(true)
                    } else {
                        check(pathsLeft.drop(1))
                    }
                }
                .addOnFailureListener {
                    cont.resume(false) // podrías registrar log si lo necesitas
                }
        }

        check(paths)
    }
}