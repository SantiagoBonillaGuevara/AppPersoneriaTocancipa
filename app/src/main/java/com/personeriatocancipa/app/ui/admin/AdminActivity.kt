package com.personeriatocancipa.app.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.databinding.ActivityAdminBinding
import com.personeriatocancipa.app.domain.model.Admin
import com.personeriatocancipa.app.domain.usecase.GetUserUseCase
import com.personeriatocancipa.app.ui.admin.managementAdminPqrs.ManageAdminPqrsActivity
import com.personeriatocancipa.app.ui.admin.managementDates.ManageDatesActivity
import com.personeriatocancipa.app.ui.admin.managementLawyers.ManageLawyersActivity
import com.personeriatocancipa.app.ui.admin.managementUsers.ManageUsersActivity
import com.personeriatocancipa.app.ui.admin.reports.ReportsActivity
import com.personeriatocancipa.app.ui.common.LoginActivity
import com.personeriatocancipa.app.ui.common.signup.RegisterActivity
import kotlinx.coroutines.launch

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private val getUserUseCase = GetUserUseCase(FirebaseUserRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cargarNombre()
        initComponents()
    }

    private fun cargarNombre() {
        lifecycleScope.launch {
            val result = getUserUseCase.execute("AdminData", "")
            result.onSuccess { user ->
                if (user is Admin) {
                    binding.tvName.text = user.nombreCompleto
                }
            }
        }
    }

    private fun initComponents() {
        // Editar cuenta
        binding.flEdit.setOnClickListener {
            startActivity(Intent(this, ModifyAdminActivity::class.java))
        }

        // Gestionar abogados
        binding.flLawyer.setOnClickListener {
            startActivity(Intent(this, ManageLawyersActivity::class.java))
        }

        // Gestionar usuarios
        binding.flUser.setOnClickListener {
            startActivity(Intent(this, ManageUsersActivity::class.java))
        }

        binding.flDate.setOnClickListener {
            startActivity(Intent(this, ManageDatesActivity::class.java))
        }

        // NUEVO: Gestionar Admin PQRS
        binding.flAdminPqrsManage.setOnClickListener {
            startActivity(Intent(this, ManageAdminPqrsActivity::class.java))
        }

        // Informes
        binding.flData.setOnClickListener {
            startActivity(Intent(this, ReportsActivity::class.java))
        }

        // Salir / Logout
        binding.flExit.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Para recargar nombre tras editar datos
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                cargarNombre()
            }
        }
    }
}
