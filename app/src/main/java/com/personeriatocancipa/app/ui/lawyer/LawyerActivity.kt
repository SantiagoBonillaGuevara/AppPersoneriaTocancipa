package com.personeriatocancipa.app.ui.lawyer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.databinding.ActivityLawyerBinding
import com.personeriatocancipa.app.domain.model.Lawyer
import com.personeriatocancipa.app.domain.usecase.GetUserUseCase
import com.personeriatocancipa.app.ui.common.LoginActivity
import com.personeriatocancipa.app.ui.lawyer.dates.GetLawyerDatesActivity
import kotlinx.coroutines.launch

class LawyerActivity : AppCompatActivity() {

    private val getUserUseCase: GetUserUseCase = GetUserUseCase(FirebaseUserRepository())
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityLawyerBinding
    private lateinit var lawyer: Lawyer

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
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) cargarNombre()
        }
    }

    private fun cargarNombre() {
        lifecycleScope.launch {
            val result = getUserUseCase.execute("abogadoData","")
            result.onSuccess {
                if (it is Lawyer) {
                    lawyer = it
                    binding.tvName.text = it.nombreCompleto
                }
            }
        }
    }

    private fun navigateToGetLawyerDates(){
        val intent = Intent(this@LawyerActivity, GetLawyerDatesActivity::class.java)
        intent.putExtra("lawyer", lawyer.correo)
        startActivity(intent)
    }

    private fun navigateToModify(){
        val intent = Intent(this@LawyerActivity, EditLawyerActivity::class.java)
        launcher.launch(intent)
    }
    private fun navigateToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        finish()
        startActivity(intent)
    }
}