package com.personeriatocancipa.app.data.datasource

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.personeriatocancipa.app.domain.model.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseDateDataSource {
    private val db = FirebaseDatabase.getInstance().getReference("citas")

    suspend fun getCitasPorCorreo(typeEmail:String ,email: String): Result<List<Date>> = suspendCoroutine { cont ->
        val query = if (typeEmail.isEmpty() && email.isEmpty()) db  //no aplica el filtro si no le llegan parametros
        else db.orderByChild(typeEmail).equalTo(email) //aplica el filtro si le llegan parametros

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dates = snapshot.children.mapNotNull { it.getValue(Date::class.java) }
                cont.resume(Result.success(dates))
            }
            override fun onCancelled(error: DatabaseError) {
                cont.resume(Result.failure(error.toException()))
            }
        })
    }

    suspend fun getCitasPorDia(typeEmail:String ,email: String, dia: String): Result<List<Date>> = suspendCoroutine { cont ->
        val query = db.orderByChild(typeEmail).equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dates = snapshot.children.mapNotNull { it.getValue(Date::class.java) }.filter { it.fecha == dia }
                cont.resume(Result.success(dates))
            }
            override fun onCancelled(error: DatabaseError) {
                cont.resume(Result.failure(error.toException()))
            }
        })
    }

    suspend fun saveCita(id: Int, date: Date): Result<Unit> = suspendCoroutine { cont ->
        try {
            db.child(id.toString()).setValue(date.copy(id = id))
                .addOnSuccessListener { cont.resume(Result.success(Unit)) }
                .addOnFailureListener { exception -> cont.resume(Result.failure(exception)) }
        } catch (e: Exception) {
            cont.resume(Result.failure(e))
        }
    }

    suspend fun getDateById(id: Int): Result<Date> = suspendCoroutine { cont ->
        db.child(id.toString()).get().addOnSuccessListener { snapshot ->
            val date = snapshot.getValue(Date::class.java)
            if (date != null) cont.resume(Result.success(date))
            else cont.resume(Result.failure(Exception("No se encontró la cita con id: $id")))
        }.addOnFailureListener { exception ->
            cont.resume(Result.failure(exception))
        }
    }

}