package com.personeriatocancipa.app.ui.admin.managementLawyers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.databinding.ActivityManageUsersBinding
import com.personeriatocancipa.app.domain.model.Lawyer
import com.personeriatocancipa.app.domain.usecase.GetAllUsersUseCase
import com.personeriatocancipa.app.domain.usecase.ModifyUseCase
import com.personeriatocancipa.app.ui.admin.managementLawyers.adapter.LawyersAdapter
import com.personeriatocancipa.app.ui.admin.managementLawyers.modifyLawyer.ModifyLawyerActivity
import com.personeriatocancipa.app.ui.admin.managementLawyers.registerLawyer.RegisterLawyerActivity
import kotlinx.coroutines.launch

class ManageLawyersActivity : AppCompatActivity() {

    private val getAllUsersUseCase: GetAllUsersUseCase = GetAllUsersUseCase(FirebaseUserRepository())
    private val modifyUseCase = ModifyUseCase(FirebaseUserRepository())
    private lateinit var binding: ActivityManageUsersBinding
    private lateinit var lawyers: List<Lawyer>
    private lateinit var adapter: LawyersAdapter
    private lateinit var editLawyerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getAllLawyers()
        initComponents()
    }

    private fun initUI() {
       adapter = LawyersAdapter(lawyers,
            onEditClicked = { lawyer ->
                navigateToEditLawyer(lawyer.uid!!)
            },
            onLawyerChanged = { lawyer, isChecked ->
                modifyState(lawyer, isChecked)
            }
        )
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = adapter
    }

    private fun initComponents() {
        binding.ivAdd.setOnClickListener {
            navigateToCreateLawyer()
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.etDocumento.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                searchByDocument(s.toString())
            }
        })

        editLawyerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) getAllLawyers()
        }
    }

    private fun searchByDocument(document: String) {
        val filteredList = lawyers.filter { lawyer ->
            lawyer.documento.toString().contains(document, ignoreCase = true)
        }
        adapter.updateList(filteredList)
    }

    private fun navigateToCreateLawyer() {
        val intent = Intent(this, RegisterLawyerActivity::class.java)
        editLawyerLauncher.launch(intent)
    }

    private fun getAllLawyers() {
        lifecycleScope.launch {
            val result = getAllUsersUseCase.execute("abogadoData")
            result.onSuccess { lawyerList ->
                lawyers = lawyerList as List<Lawyer>
                initUI()
            }.onFailure { error ->
                Toast.makeText(this@ManageLawyersActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun modifyState(lawyer: Lawyer, isChecked: Boolean) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar cambio de estado")
            .setMessage("¿Estás seguro de cambiar el estado del usuario a ${if (isChecked) "Activo" else "Inactivo"}?")
            .setPositiveButton("Sí") { _, _ ->
                lawyer.estado = if (isChecked) "Activo" else "Inactivo"
                lifecycleScope.launch {
                    val result = modifyUseCase.execute(lawyer, "abogadoData")
                    result.onSuccess {
                        Toast.makeText(this@ManageLawyersActivity, "Estado modificado", Toast.LENGTH_SHORT).show()
                        adapter.notifyItemChanged(lawyers.indexOf(lawyer))
                    }.onFailure { error ->
                        Toast.makeText(this@ManageLawyersActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                        lawyer.estado = if (isChecked) "Inactivo" else "Activo"
                        adapter.notifyItemChanged(lawyers.indexOf(lawyer))
                    }
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
                val position = lawyers.indexOf(lawyer)
                adapter.notifyItemChanged(position) // Revertir el cambio visual
            }
            .show()
    }

    private fun navigateToEditLawyer(lawyerId: String) {
        val intent = Intent(this, ModifyLawyerActivity::class.java)
        intent.putExtra("uid", lawyerId)
        editLawyerLauncher.launch(intent)
    }
}