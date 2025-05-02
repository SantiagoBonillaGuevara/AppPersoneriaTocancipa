package com.personeriatocancipa.app.ui.common

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.personeriatocancipa.app.data.repository.FirebaseAuthRepository
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.databinding.ActivityRestorePasswordBinding
import com.personeriatocancipa.app.domain.usecase.RestorePasswordUseCase
import kotlinx.coroutines.launch

class RestorePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRestorePasswordBinding
    private val useCase = RestorePasswordUseCase(FirebaseAuthRepository(), FirebaseUserRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestorePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
    }

    private fun initComponents(){
        binding.btnRestorePassword.setOnClickListener(){
            onResetClicked()
        }
        binding.ivClose.setOnClickListener(){
            navigateToLogin()
        }
    }

    private fun onResetClicked(){
        val email = binding.txtCorreo.text.toString()
        if(email.isBlank()){
            Toast.makeText(this, "Ingrese un correo", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            val result = useCase.execute(email)
            result.onSuccess {
                Toast.makeText(this@RestorePasswordActivity, "Correo enviado. Revise su bandeja de entrada o su carpeta de 'No Deseados'", Toast.LENGTH_LONG).show()
            }.onFailure {
                Toast.makeText(this@RestorePasswordActivity, "Error al enviar correo: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        finish()
        startActivity(intent)
    }
}