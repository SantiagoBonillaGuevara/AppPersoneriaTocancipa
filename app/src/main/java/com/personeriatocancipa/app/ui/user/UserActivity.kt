package com.personeriatocancipa.app.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.personeriatocancipa.app.CreateDateActivity
import com.personeriatocancipa.app.GetUserDatesActivity
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.databinding.ActivityUserBinding
import com.personeriatocancipa.app.domain.model.User
import com.personeriatocancipa.app.domain.usecase.GetUserUseCase
import com.personeriatocancipa.app.ui.common.LoginActivity
import com.personeriatocancipa.app.ui.user.modify.ModifyUserActivity
import kotlinx.coroutines.launch

class UserActivity : AppCompatActivity() {

    private val getUserUseCase: GetUserUseCase = GetUserUseCase(FirebaseUserRepository())
    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        initComponents()
    }

    private fun initUI(){
        cargarNombre()
    }

    private fun initComponents(){
        binding.flDate.setOnClickListener{
            navigateToCreateDate()
        }

        binding.flSearch.setOnClickListener{
            navigateToGetUserDates()
        }

        binding.flEdit.setOnClickListener{
            navigateToModify()
        }

        binding.flChat.setOnClickListener{
            navigateToPQRS()
        }

        binding.flExit.setOnClickListener{
            navigateToLogin()
        }
    }

    private fun cargarNombre() {
        lifecycleScope.launch {
            val result = getUserUseCase.execute("userData","")
            result.onSuccess {
                if (it is User) {
                    binding.tvName.text = it.nombreCompleto
                }
            }
        }
    }

    private fun navigateToCreateDate(){
        val intent = Intent(this@UserActivity, CreateDateActivity::class.java)
        intent.putExtra("tarea", "crear")
        intent.putExtra("sujeto", "cliente")
        startActivity(intent)
    }

    private fun navigateToGetUserDates(){
        val intent = Intent(this@UserActivity, GetUserDatesActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToModify(){
        val intent = Intent(this@UserActivity, ModifyUserActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun navigateToPQRS(){
        //NAVEGA DESDE ACA AL SISTEMA DE PQRS Y CHATBOT
    }
}