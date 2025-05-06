package com.personeriatocancipa.app.ui.admin.ManagementLawyers.modifyLawyer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.domain.model.Lawyer
import com.personeriatocancipa.app.domain.usecase.GetUserUseCase
import kotlinx.coroutines.launch

class ModifyLawyerViewModel( private val getUserUseCase: GetUserUseCase = GetUserUseCase(FirebaseUserRepository())):
    ViewModel() {
    var _lawyerLoaded = false
    var lawyer = MutableLiveData(Lawyer())

    fun loadCurrentUser(uid: String? = "") {
        val userId:String
        if (uid.isNullOrEmpty()) userId= ""
        else userId = uid
        if (_lawyerLoaded) return
        _lawyerLoaded = true
        viewModelScope.launch {
            val result = getUserUseCase.execute("abogadoData",userId)
            result.onSuccess {
                if (it is Lawyer) {
                    lawyer.value = it
                }
            }
        }
    }
}