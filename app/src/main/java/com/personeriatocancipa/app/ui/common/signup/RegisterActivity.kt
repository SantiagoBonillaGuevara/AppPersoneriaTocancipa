package com.personeriatocancipa.app.ui.common.signup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.domain.model.User
import com.personeriatocancipa.app.domain.usecase.IsParamRegisteredUseCase
import com.personeriatocancipa.app.domain.usecase.RegisterUseCase
import com.personeriatocancipa.app.ui.common.signup.fragments.RegisterStep1Fragment
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private val validateUseCase = IsParamRegisteredUseCase(FirebaseUserRepository())
    private val registerUseCase = RegisterUseCase(FirebaseUserRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, RegisterStep1Fragment())
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

    fun registerUser(password: String, user: User, onResult: (Result<Unit>) -> Unit) {
        lifecycleScope.launch {
            val result = registerUseCase.execute(password, user, "userData")
            onResult(result)
        }
    }
}