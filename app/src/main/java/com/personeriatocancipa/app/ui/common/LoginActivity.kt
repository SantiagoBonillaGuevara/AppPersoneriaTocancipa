package com.personeriatocancipa.app.ui.common

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.personeriatocancipa.app.data.repository.FirebaseAuthRepository
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.databinding.ActivityLoginBinding
import com.personeriatocancipa.app.domain.model.Role
import com.personeriatocancipa.app.domain.usecase.LoginUseCase
import com.personeriatocancipa.app.ui.admin.AdminActivity
import com.personeriatocancipa.app.ui.lawyer.LawyerActivity
import com.personeriatocancipa.app.ui.pqrs.PqrsAdminActivity
import com.personeriatocancipa.app.ui.user.UserActivity
import com.personeriatocancipa.app.ui.common.signup.RegisterActivity
import com.personeriatocancipa.app.ui.common.RestorePasswordActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginUseCase =
        LoginUseCase(FirebaseAuthRepository(), FirebaseUserRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
    }

    private fun initComponents() {
        binding.btnLogin.setOnClickListener { login() }
        binding.txtSignup.setOnClickListener { signup() }
        binding.txtOlvidoContrasena.setOnClickListener { restorePassword() }
    }

    private fun signup() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun restorePassword() {
        startActivity(Intent(this, RestorePasswordActivity::class.java))
    }

    private fun login() {
        val correo = binding.txtCorreo.text.toString()
        val clave  = binding.txtContrasena.text.toString()
        if (correo.isBlank() || clave.isBlank()) {
            Toast.makeText(this, "¡Ingrese correo y contraseña!", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            val result = loginUseCase.execute(correo, clave)
            result.onSuccess { role ->
                // Dependiendo del rol, lanzamos la Activity correspondiente
                val intent = when (role) {
                    Role.USER       -> Intent(this@LoginActivity, UserActivity::class.java)
                    Role.LAWYER     -> Intent(this@LoginActivity, LawyerActivity::class.java)
                    Role.ADMIN      -> Intent(this@LoginActivity, AdminActivity::class.java)
                    Role.ADMIN_PQRS -> Intent(this@LoginActivity, PqrsAdminActivity::class.java)
                }
                startActivity(intent)
                finish()
            }.onFailure { error ->
                Toast.makeText(
                    this@LoginActivity,
                    "¡Hubo un error!: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
