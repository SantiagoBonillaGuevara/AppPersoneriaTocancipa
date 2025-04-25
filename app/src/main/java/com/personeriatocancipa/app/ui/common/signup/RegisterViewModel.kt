package com.personeriatocancipa.app.ui.common.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.personeriatocancipa.app.domain.model.User

class RegisterViewModel:ViewModel() {
    var user = MutableLiveData(User())
}