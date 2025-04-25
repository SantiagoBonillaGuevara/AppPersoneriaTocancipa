package com.personeriatocancipa.app.ui.common

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.personeriatocancipa.app.ui.admin.AdminActivity
import com.personeriatocancipa.app.CreateUserActivity
import com.personeriatocancipa.app.ui.lawyer.LawyerActivity
import com.personeriatocancipa.app.ui.user.UserActivity
import com.personeriatocancipa.app.data.repository.FirebaseAuthRepository
import com.personeriatocancipa.app.databinding.ActivityLoginBinding
import com.personeriatocancipa.app.domain.model.Role
import com.personeriatocancipa.app.domain.usecase.LoginUseCase
import com.personeriatocancipa.app.ui.common.signup.RegisterActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginUseCase = LoginUseCase(FirebaseAuthRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
    }

    private fun initComponents() {
        binding.btnLogin.setOnClickListener(){
            login()
        }

        binding.btnSignUp.setOnClickListener(){
            signup()
        }

        binding.btnRecuperarPassword.setOnClickListener(){
            restorePassword()
        }

        binding.btnTogglePassword.setOnClickListener {
            togglePasswordVisibility()
        }
    }

    private fun signup() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        /*intent.putExtra("tarea","crear")
        intent.putExtra("usuario","cliente")*/
        startActivity(intent)
    }

    private fun login() {
        val correo = binding.txtCorreo.text.toString()
        val clave = binding.txtClave.text.toString()

        lifecycleScope.launch {
            val result = loginUseCase.execute(correo, clave)
            result.onSuccess { role->
                val intent = when (role){
                    Role.USER -> Intent(this@LoginActivity, UserActivity::class.java)
                    Role.LAWYER -> Intent(this@LoginActivity, LawyerActivity::class.java)
                    Role.ADMIN -> Intent(this@LoginActivity, AdminActivity::class.java)
                }
                startActivity(intent)
                finish()
            }.onFailure {
                Toast.makeText(this@LoginActivity, "¡Hubo un error!: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun restorePassword(){
        val intent = Intent(this@LoginActivity, RestorePasswordActivity::class.java)
        startActivity(intent)
    }

    private fun togglePasswordVisibility(){
        if (binding.txtClave.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            binding.txtClave.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            binding.txtClave.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        binding.txtClave.setSelection(binding.txtClave.text.length) // Mantener cursor al final
    }
}