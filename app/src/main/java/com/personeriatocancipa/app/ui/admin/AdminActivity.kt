package com.personeriatocancipa.app.ui.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.personeriatocancipa.app.ManagementActivity
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.databinding.ActivityAdminBinding
import com.personeriatocancipa.app.domain.model.Admin
import com.personeriatocancipa.app.domain.usecase.GetUserUseCase
import com.personeriatocancipa.app.ui.admin.ManagementLawyers.ManageLawyersActivity
import com.personeriatocancipa.app.ui.admin.ManagementUsers.ManageUsersActivity
import com.personeriatocancipa.app.ui.common.LoginActivity
import kotlinx.coroutines.launch

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private val getUserUseCase: GetUserUseCase = GetUserUseCase(FirebaseUserRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cargarNombre()

        binding.flUser.setOnClickListener{
            navigateToManageUsers()
        }

        binding.flLawyer.setOnClickListener{
            navigateToManageLawyers()
        }

        binding.flDate.setOnClickListener{
            navigateToManageDates()
        }

        binding.flEdit.setOnClickListener {
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
            val result = getUserUseCase.execute("AdminData","")
            result.onSuccess {
                if (it is Admin) {
                    binding.tvName.text = it.nombreCompleto
                }
            }
        }
    }

    private fun navigateToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun navigateToManageUsers(){
        val intent = Intent(this@AdminActivity, ManageUsersActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToManageLawyers(){
        val intent = Intent(this@AdminActivity, ManageLawyersActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToManageDates(){
        val intent = Intent(this@AdminActivity, ManagementActivity::class.java)
        intent.putExtra("tipo", "cita")
        startActivity(intent)
    }

    private fun navigateToModify(){
        val intent = Intent(this@AdminActivity, ModifyAdminActivity::class.java)
        launcher.launch(intent)
    }
}