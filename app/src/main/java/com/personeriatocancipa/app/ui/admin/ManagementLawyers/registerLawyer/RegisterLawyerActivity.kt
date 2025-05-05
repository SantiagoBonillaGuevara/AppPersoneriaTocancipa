package com.personeriatocancipa.app.ui.admin.ManagementLawyers.registerLawyer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.domain.model.Lawyer
import com.personeriatocancipa.app.domain.model.User
import com.personeriatocancipa.app.domain.usecase.IsParamRegisteredUseCase
import com.personeriatocancipa.app.domain.usecase.RegisterUseCase
import com.personeriatocancipa.app.ui.admin.ManagementLawyers.registerLawyer.fragments.RegisterLawyerStep1Fragment
import kotlinx.coroutines.launch

class RegisterLawyerActivity : AppCompatActivity() {

    private val validateUseCase = IsParamRegisteredUseCase(FirebaseUserRepository())
    private val registerUseCase = RegisterUseCase(FirebaseUserRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, RegisterLawyerStep1Fragment())
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

    fun registerLawyer(password: String, lawyer: Lawyer, onResult: (Result<Unit>) -> Unit) {
        lifecycleScope.launch {
            val result = registerUseCase.execute(password, lawyer, "abogadoData")
            onResult(result)
        }
    }
}