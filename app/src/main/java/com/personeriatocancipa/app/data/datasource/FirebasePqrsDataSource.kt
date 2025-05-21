package com.personeriatocancipa.app.data.datasource

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.personeriatocancipa.app.domain.model.Pqrs
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * DataSource que usa Firebase Realtime Database para almacenar PQRS.
 */
class FirebasePqrsDataSource {

    private val db = FirebaseDatabase.getInstance().getReference("pqrs")

    // Crea una nueva PQRS (se guarda con todos los campos, incluyendo response si viene vacío o nulo)
    suspend fun createPqrs(pqrs: Pqrs): Result<Unit> = suspendCoroutine { cont ->
        try {
            db.child(pqrs.id)
                .setValue(pqrs)
                .addOnSuccessListener { cont.resume(Result.success(Unit)) }
                .addOnFailureListener { e -> cont.resume(Result.failure(e)) }
        } catch (e: Exception) {
            cont.resume(Result.failure(e))
        }
    }

    // Devuelve solo las PQRS del usuario autenticado
    fun getMyPqrs(userId: String): Flow<List<Pqrs>> = callbackFlow {
        val sub = db.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = snapshot.children
                        .mapNotNull { it.getValue(Pqrs::class.java) }
                    trySend(list)
                }
                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            })
        awaitClose { db.removeEventListener(sub) }
    }

    // Consulta individual por ID
    suspend fun getPqrsById(id: String): Result<Pqrs> = suspendCoroutine { cont ->
        db.child(id).get()
            .addOnSuccessListener { snap ->
                val p = snap.getValue(Pqrs::class.java)
                if (p != null) cont.resume(Result.success(p))
                else cont.resume(Result.failure(Exception("PQRS no encontrada: $id")))
            }
            .addOnFailureListener { e -> cont.resume(Result.failure(e)) }
    }

    // Devuelve todas las PQRS para el administrador
    fun getAllPqrs(): Flow<List<Pqrs>> = callbackFlow {
        val sub = db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children
                    .mapNotNull { it.getValue(Pqrs::class.java) }
                trySend(list)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { db.removeEventListener(sub) }
    }

    // Permite guardar una respuesta del admin en el campo "response"
    suspend fun respondPqrs(id: String, response: String): Result<Unit> = suspendCoroutine { cont ->
        try {
            db.child(id)
                .child("response")
                .setValue(response)
                .addOnSuccessListener { cont.resume(Result.success(Unit)) }
                .addOnFailureListener { e -> cont.resume(Result.failure(e)) }
        } catch (e: Exception) {
            cont.resume(Result.failure(e))
        }
    }
}
