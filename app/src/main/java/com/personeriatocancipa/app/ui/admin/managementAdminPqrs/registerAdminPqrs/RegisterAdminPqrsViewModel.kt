package com.personeriatocancipa.app.ui.admin.managementAdminPqrs.registerAdminPqrs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.personeriatocancipa.app.domain.model.Admin

class RegisterAdminPqrsViewModel : ViewModel() {
    val admin = MutableLiveData(Admin(null,"","","",""))
}
