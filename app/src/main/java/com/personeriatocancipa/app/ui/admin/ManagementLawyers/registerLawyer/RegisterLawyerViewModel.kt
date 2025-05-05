package com.personeriatocancipa.app.ui.admin.ManagementLawyers.registerLawyer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.personeriatocancipa.app.domain.model.Lawyer

class RegisterLawyerViewModel: ViewModel() {
    var lawyer= MutableLiveData(Lawyer())
}