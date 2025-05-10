package com.personeriatocancipa.app.ui.admin

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.databinding.ActivityModifyAdminBinding
import com.personeriatocancipa.app.domain.model.Admin
import com.personeriatocancipa.app.domain.usecase.GetUserUseCase
import com.personeriatocancipa.app.domain.usecase.IsParamRegisteredUseCase
import com.personeriatocancipa.app.domain.usecase.ModifyUseCase
import kotlinx.coroutines.launch

class ModifyAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModifyAdminBinding
    private val getUserUseCase: GetUserUseCase = GetUserUseCase(FirebaseUserRepository())
    private val modifyUseCase = ModifyUseCase(FirebaseUserRepository())
    private val validateUseCase = IsParamRegisteredUseCase(FirebaseUserRepository())
    private lateinit var admin:Admin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUser()
        initComponents()
    }

    private fun initUI() {
        binding.txtNombre.setText(admin.nombreCompleto)
        binding.txtDocumento.setText(admin.documento)
    }

    private fun initComponents() {
        binding.ivClose.setOnClickListener {
            finish()
        }
        binding.btnGuardar.setOnClickListener{
            validParams()
        }
    }

    private fun getUser() {
        lifecycleScope.launch {
            val result = getUserUseCase.execute("AdminData", "")
            result.onSuccess {
                if (it is Admin) {
                    admin = it
                    initUI()
                }
            }
        }
    }

    private fun validParams() {
        val nombre = binding.txtNombre.text.toString()
        val documento = binding.txtDocumento.text.toString()
        if (nombre.isEmpty() || documento.isEmpty()) {
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        if(documento == admin.documento) {
            modifyAdmin()
        }
        lifecycleScope.launch {
            val exists = validateUseCase.execute("documento", documento)
            if (exists) {
                Toast.makeText(this@ModifyAdminActivity, "El documento ya está registrado", Toast.LENGTH_SHORT).show()
            } else {
                modifyAdmin()
            }
        }
    }

    private fun modifyAdmin(){
        admin.nombreCompleto = binding.txtNombre.text.toString()
        admin.documento = binding.txtDocumento.text.toString()
        lifecycleScope.launch {
            val result =modifyUseCase.execute(admin, "AdminData")
            result.onSuccess {
                Toast.makeText(this@ModifyAdminActivity, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            }.onFailure { error ->
                Toast.makeText(this@ModifyAdminActivity, "Error al actualizar: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}