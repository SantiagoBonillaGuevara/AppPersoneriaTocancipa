package com.personeriatocancipa.app.ui.user.modify

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.domain.model.User
import com.personeriatocancipa.app.domain.usecase.GetUserUseCase
import kotlinx.coroutines.launch

class ModifyViewModel( private val getUserUseCase: GetUserUseCase = GetUserUseCase(FirebaseUserRepository())): ViewModel(){
    var _userLoaded = false
    var user = MutableLiveData(User())

    fun loadCurrentUser(uid: String? = "") {
        val userId:String
        if (uid.isNullOrEmpty()) userId= FirebaseAuth.getInstance().currentUser?.uid ?: return
        else userId = uid
        if (_userLoaded) return
        _userLoaded = true
        viewModelScope.launch {
            val result = getUserUseCase.execute("userData",userId)
            result.onSuccess {
                if (it is User) {
                    user.value = it
                }
            }
        }
    }
}