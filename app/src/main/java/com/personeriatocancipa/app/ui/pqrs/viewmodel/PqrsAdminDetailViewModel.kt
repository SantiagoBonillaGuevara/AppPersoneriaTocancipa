// app/src/main/java/com/personeriatocancipa/app/ui/pqrs/viewmodel/PqrsAdminDetailViewModel.kt
package com.personeriatocancipa.app.ui.pqrs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.personeriatocancipa.app.domain.model.PqrsUiModel
import com.personeriatocancipa.app.domain.usecase.GetPqrsByIdUseCase
import com.personeriatocancipa.app.domain.usecase.GetUserUseCase
import com.personeriatocancipa.app.domain.usecase.RespondPqrsUseCase
import kotlinx.coroutines.launch

class PqrsAdminDetailViewModel(
    private val getById: GetPqrsByIdUseCase,
    private val getUser: GetUserUseCase,
    private val respond: RespondPqrsUseCase
) : ViewModel() {

    private val _pqrsDetail = MutableLiveData<PqrsUiModel>()
    val pqrsDetail: LiveData<PqrsUiModel> = _pqrsDetail

    /** 1) Carga la PQRS por id y luego resuelve el nombre de usuario */
    fun loadPqrsById(id: String) {
        viewModelScope.launch {
            getById.execute(id)
                .onSuccess { pqrs ->
                    // 2) Llamamos al use case de usuario
                    val userRes = getUser.execute("users", pqrs.userId)
                    val fullName = userRes
                        .getOrNull()
                        ?.nombreCompleto   // <-- aquí sacamos el nombre del DTO de usuario
                        ?: "Desconocido"

                    // 3) Construimos el UiModel final
                    _pqrsDetail.value = PqrsUiModel(
                        id          = pqrs.id,
                        type        = pqrs.type,
                        title       = pqrs.title,
                        description = pqrs.description,
                        date        = pqrs.date,
                        userName    = fullName
                    )
                }
                .onFailure {
                    // TODO: manejar error (p.ej. con un LiveData de error)
                }
        }
    }

    /** Dispara la respuesta de la PQRS */
    fun sendResponse(id: String, text: String) {
        viewModelScope.launch {
            respond.execute(id, text)
                .onSuccess { /* toast de "Enviado" */ }
                .onFailure { /* toast de error */ }
        }
    }

    /** Factory para inyectar los tres use cases */
    class Factory(
        private val getById: GetPqrsByIdUseCase,
        private val getUser: GetUserUseCase,
        private val respond: RespondPqrsUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == PqrsAdminDetailViewModel::class.java)
            return PqrsAdminDetailViewModel(getById, getUser, respond) as T
        }
    }
}
