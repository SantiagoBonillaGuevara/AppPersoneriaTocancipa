package com.personeriatocancipa.app.data.repository

import com.personeriatocancipa.app.data.datasource.FirebaseDateDataSource
import com.personeriatocancipa.app.domain.model.Date
import com.personeriatocancipa.app.domain.repository.DateRepository

class FirebaseDateRepository(private val dateDataSrc: FirebaseDateDataSource = FirebaseDateDataSource()):DateRepository {
    override suspend fun getCitas(typeEmail: String, email: String): Result<List<Date>> {
        return dateDataSrc.getCitasPorCorreo(typeEmail, email)
    }

    override suspend fun modifyDate(dateId: Int, date: Date): Result<Unit> {
        return dateDataSrc.updateDate(dateId, date)
    }
}