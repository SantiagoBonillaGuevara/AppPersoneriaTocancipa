// app/src/main/java/com/personeriatocancipa/app/ui/pqrs/viewmodel/PqrsAdminViewModel.kt
package com.personeriatocancipa.app.ui.pqrs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.personeriatocancipa.app.domain.model.PqrsUiModel
import com.personeriatocancipa.app.domain.usecase.GetPqrsWithUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PqrsAdminViewModel(
    private val fetchUseCase: GetPqrsWithUserUseCase
) : ViewModel() {

    // 1) Estado interno mutable
    private val _pqrsUiList = MutableStateFlow<List<PqrsUiModel>>(emptyList())
    // 2) Exposición inmutable
    val pqrsUiList: StateFlow<List<PqrsUiModel>> = _pqrsUiList

    /** Dispara la carga de datos y actualiza el StateFlow */
    fun loadPqrs() {
        viewModelScope.launch {
            fetchUseCase.execute()
                .collectLatest { list ->
                    _pqrsUiList.value = list
                }
        }
    }

    /** Factory para poder inyectar el UseCase desde el Fragment */
    class Factory(
        private val fetchUseCase: GetPqrsWithUserUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == PqrsAdminViewModel::class.java)
            return PqrsAdminViewModel(fetchUseCase) as T
        }
    }
}
