package com.personeriatocancipa.app.ui.user.createDate

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personeriatocancipa.app.data.repository.FirebaseDateRepository
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.domain.model.Date
import com.personeriatocancipa.app.domain.model.Lawyer
import com.personeriatocancipa.app.domain.model.User
import com.personeriatocancipa.app.domain.usecase.GetDatesByDayUseCase
import com.personeriatocancipa.app.domain.usecase.GetUserUseCase
import kotlinx.coroutines.launch

class CreateDateViewModel( private val getUserUseCase: GetUserUseCase = GetUserUseCase(FirebaseUserRepository()),
    private val getDatesByDayUseCase: GetDatesByDayUseCase = GetDatesByDayUseCase(FirebaseDateRepository())
): ViewModel(){
    var _userLoaded = false
    var user = MutableLiveData(User())
    var date = MutableLiveData(Date())
    var lawyer = MutableLiveData(Lawyer())
    var lawyerList = MutableLiveData<List<Lawyer>>()
    val dates = MutableLiveData<List<Date>>()
    val hours = MutableLiveData<List<String>>()
    val userBooked = MutableLiveData<Boolean>()

    fun loadCurrentUser(uid: String? = "") {
        val userId:String
        if (uid.isNullOrEmpty()) userId= ""
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

    fun getDatesByDay(typeEmail: String, email: String) {
        viewModelScope.launch {
            Log.i( "CreateDateViewModel", "getDatesByDay: ${date.value?.fecha}")
            val result = getDatesByDayUseCase.execute(typeEmail, email, date.value?.fecha!!)
            result.onSuccess { data ->
                dates.value = data
            }
        }
    }

    fun isUserBooked() {
        val currentDate = date.value?.fecha
        val currentHour = date.value?.hora
        viewModelScope.launch {
            val result = getDatesByDayUseCase.execute("correoCliente", date.value?.correoCliente!!, currentDate!!)
            result.onSuccess { data ->
                val booked = data.any { it.hora == currentHour && it.fecha == currentDate }
                userBooked.value = booked
                if (booked) {
                    Log.i("CreateDateViewModel", "El usuario ya tiene una cita programada para la fecha y hora seleccionadas.")
                    userBooked.value = true
                } else {
                    Log.i("CreateDateViewModel", "El usuario no tiene citas programadas para la fecha y hora seleccionadas.")
                    userBooked.value = false
                }
            }
        }
    }
}