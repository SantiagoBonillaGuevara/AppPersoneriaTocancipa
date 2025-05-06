package com.personeriatocancipa.app.ui.lawyer

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.databinding.ActivityModifyAdminBinding
import com.personeriatocancipa.app.domain.model.Lawyer
import com.personeriatocancipa.app.domain.usecase.GetUserUseCase
import com.personeriatocancipa.app.domain.usecase.IsParamRegisteredUseCase
import com.personeriatocancipa.app.domain.usecase.ModifyUseCase
import kotlinx.coroutines.launch

class EditLawyerActivity : AppCompatActivity() {

    private val getUserUseCase: GetUserUseCase = GetUserUseCase(FirebaseUserRepository())
    private val modifyUseCase = ModifyUseCase(FirebaseUserRepository())
    private val validateUseCase = IsParamRegisteredUseCase(FirebaseUserRepository())
    private lateinit var lawyer: Lawyer
    private lateinit var binding: ActivityModifyAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUser()
        initComponents()
    }

    private fun initUI() {
        binding.txtNombre.setText(lawyer.nombreCompleto)
        binding.txtDocumento.setText(lawyer.documento)
    }

    private fun initComponents() {
        binding.ivClose.setOnClickListener {
            finish()
        }
        binding.btnGuardar.setOnClickListener {
            validParams()
        }
    }

    private fun getUser() {
        lifecycleScope.launch {
            val result = getUserUseCase.execute("abogadoData", "")
            result.onSuccess {
                if (it is Lawyer) {
                    lawyer = it
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
        if (documento == lawyer.documento) {
            modifyLawyer()
            return
        }
        lifecycleScope.launch {
            val exists = validateUseCase.execute("documento", documento)
            if (exists) {
                Toast.makeText(this@EditLawyerActivity, "El documento ya está registrado", Toast.LENGTH_SHORT
                ).show()
            } else {
                modifyLawyer()
            }
        }
    }

    private fun modifyLawyer() {
        lawyer.nombreCompleto = binding.txtNombre.text.toString()
        lawyer.documento = binding.txtDocumento.text.toString()
        lifecycleScope.launch {
            val result = modifyUseCase.execute(lawyer, "abogadoData")
            result.onSuccess {
                Toast.makeText(this@EditLawyerActivity, "Datos actualizados correctamente", Toast.LENGTH_SHORT
                ).show()
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }
}