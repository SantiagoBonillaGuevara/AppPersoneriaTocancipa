package com.personeriatocancipa.app.domain.repository

import com.personeriatocancipa.app.domain.model.Pqrs
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de operaciones sobre PQRS.
 */
interface PqrsRepository {
    suspend fun createPqrs(pqrs: Pqrs): Result<Unit>
    fun getMyPqrs(userId: String): Flow<List<Pqrs>>
    suspend fun getPqrsById(id: String): Result<Pqrs>
    fun getAllPqrs(): Flow<List<Pqrs>>
    suspend fun respondPqrs(id: String, response: String): Result<Unit>
}
