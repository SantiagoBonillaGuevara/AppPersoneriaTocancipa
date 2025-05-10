package com.personeriatocancipa.app.domain.repository

import com.personeriatocancipa.app.domain.model.Date

interface DateRepository {
    suspend fun getCitas(typeEmail: String, email:String): Result<List<Date>>
    suspend fun getCitaById(id: Int): Result<Date>
    suspend fun getCitasByDay(typeEmail: String, email:String, day: String): Result<List<Date>>
    suspend fun saveDate(id: Int,date: Date): Result<Unit>
}