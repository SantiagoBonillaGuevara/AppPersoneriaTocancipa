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
        val query = db.orderByChild(typeEmail).equalTo(email)
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

    suspend fun updateDate(dateId: Int, updatedDate: Date): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            db.child(dateId.toString()).setValue(updatedDate).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}