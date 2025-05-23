// app/src/main/java/com/personeriatocancipa/app/domain/usecase/GetPqrsWithUserUseCase.kt
package com.personeriatocancipa.app.domain.usecase

import com.personeriatocancipa.app.domain.model.PqrsUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPqrsWithUserUseCase(
    private val getAllPqrsUseCase: GetAllPqrsUseCase,
    private val getUserUseCase: GetUserUseCase
) {
    /**
     * Emite listas de PqrsUiModel, donde userName viene de RegistrableUser.nombreCompleto
     */
    fun execute(): Flow<List<PqrsUiModel>> = flow {
        getAllPqrsUseCase().collect { pqrsList ->
            val uiList = pqrsList.map { pqrs ->
                val userResult = getUserUseCase.execute("users", pqrs.userId)
                val fullName = userResult
                    .getOrNull()
                    ?.nombreCompleto
                    ?: "Desconocido"

                PqrsUiModel(
                    id          = pqrs.id,
                    type        = pqrs.type,
                    title       = pqrs.title,
                    description = pqrs.description,
                    date        = pqrs.date,
                    userName    = fullName
                )
            }
            emit(uiList)
        }
    }
}
