package com.personeriatocancipa.app.domain.repository

import com.personeriatocancipa.app.domain.model.Date

interface DateRepository {
    suspend fun getCitas(typeEmail: String, email:String): Result<List<Date>>
    suspend fun modifyDate(dateId: Int, date: Date): Result<Unit>
}