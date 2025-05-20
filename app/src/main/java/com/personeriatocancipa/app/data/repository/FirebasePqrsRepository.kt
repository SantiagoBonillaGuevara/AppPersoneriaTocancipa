package com.personeriatocancipa.app.data.repository

import com.personeriatocancipa.app.data.datasource.FirebasePqrsDataSource
import com.personeriatocancipa.app.domain.model.Pqrs
import com.personeriatocancipa.app.domain.repository.PqrsRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementación de PqrsRepository usando FirebasePqrsDataSource.
 */
class FirebasePqrsRepository(
    private val ds: FirebasePqrsDataSource = FirebasePqrsDataSource()
) : PqrsRepository {

    override suspend fun createPqrs(pqrs: Pqrs): Result<Unit> =
        ds.createPqrs(pqrs)

    override fun getMyPqrs(userId: String): Flow<List<Pqrs>> =
        ds.getMyPqrs(userId)

    override suspend fun getPqrsById(id: String): Result<Pqrs> =
        ds.getPqrsById(id)

    override fun getAllPqrs(): Flow<List<Pqrs>> =
        ds.getAllPqrs()

    override suspend fun respondPqrs(id: String, response: String): Result<Unit> =
        ds.respondPqrs(id, response)
}
