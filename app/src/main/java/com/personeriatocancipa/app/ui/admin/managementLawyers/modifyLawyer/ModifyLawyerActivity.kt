package com.personeriatocancipa.app.ui.admin.managementLawyers.modifyLawyer

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.domain.model.Lawyer
import com.personeriatocancipa.app.domain.usecase.IsParamRegisteredUseCase
import com.personeriatocancipa.app.domain.usecase.ModifyUseCase
import com.personeriatocancipa.app.ui.admin.managementLawyers.modifyLawyer.Fragments.ModifyLawyerStep1Fragment
import kotlinx.coroutines.launch

class ModifyLawyerActivity : AppCompatActivity() {

    private val validateUseCase = IsParamRegisteredUseCase(FirebaseUserRepository())
    private val modifyUseCase = ModifyUseCase(FirebaseUserRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val lawyer = intent.getStringExtra("uid")
        val bundle = Bundle()
        bundle.putString("lawyer", lawyer)
        val fragment = ModifyLawyerStep1Fragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }

    fun navigateToNextStep(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun validateParam(param: String, value: String, onResult: (Boolean) -> Unit) {
        lifecycleScope.launch {
            val exists = validateUseCase.execute(param, value)
            onResult(exists)
        }
    }

    fun modifyLawyer(lawyer: Lawyer, onResult: (Result<Unit>) -> Unit) {
        lifecycleScope.launch {
            val result = modifyUseCase.execute(lawyer, "abogadoData")
            onResult(result)
        }
    }

    fun finishWithSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}