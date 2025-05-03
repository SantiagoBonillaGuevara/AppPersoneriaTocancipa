package com.personeriatocancipa.app.ui.lawyer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.personeriatocancipa.app.CreateLawyerActivity
import com.personeriatocancipa.app.GetLawyerDatesActivity
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.databinding.ActivityLawyerBinding
import com.personeriatocancipa.app.domain.model.Lawyer
import com.personeriatocancipa.app.domain.usecase.GetUserUseCase
import com.personeriatocancipa.app.ui.common.LoginActivity
import kotlinx.coroutines.launch

class LawyerActivity : AppCompatActivity() {

    private val getUserUseCase: GetUserUseCase = GetUserUseCase(FirebaseUserRepository())
    private lateinit var binding: ActivityLawyerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLawyerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        initComponents()
    }

    private fun initUI(){
        cargarNombre()
    }

    private fun initComponents(){
        binding.flSearch.setOnClickListener{
            navigateToGetLawyerDates()
        }

        binding.flEdit.setOnClickListener{
            navigateToModify()
        }

        binding.flExit.setOnClickListener{
            navigateToLogin()
        }
    }

    private fun cargarNombre() {
        lifecycleScope.launch {
            val result = getUserUseCase.execute("abogadoData","")
            result.onSuccess {
                if (it is Lawyer) {
                    binding.tvName.text = it.nombreCompleto
                }
            }
        }
    }


    private fun navigateToGetLawyerDates(){
        val intent = Intent(this@LawyerActivity, GetLawyerDatesActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToModify(){
        val intent = Intent(this@LawyerActivity, CreateLawyerActivity::class.java)
        intent.putExtra("tarea", "modificar")
        intent.putExtra("sujeto", "propio")
        startActivity(intent)
    }
    private fun navigateToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        finish()
        startActivity(intent)
    }
}