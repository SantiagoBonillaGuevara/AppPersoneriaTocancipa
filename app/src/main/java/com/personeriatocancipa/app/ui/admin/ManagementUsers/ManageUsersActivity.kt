package com.personeriatocancipa.app.ui.admin.ManagementUsers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.databinding.ActivityManageUsersBinding
import com.personeriatocancipa.app.domain.model.User
import com.personeriatocancipa.app.domain.usecase.GetAllUsersUseCase
import com.personeriatocancipa.app.domain.usecase.ModifyUseCase
import com.personeriatocancipa.app.ui.admin.ManagementUsers.adapter.UsersAdapter
import com.personeriatocancipa.app.ui.common.signup.RegisterActivity
import com.personeriatocancipa.app.ui.user.modify.ModifyUserActivity
import kotlinx.coroutines.launch

class ManageUsersActivity : AppCompatActivity() {

    private val getAllUsersUseCase: GetAllUsersUseCase = GetAllUsersUseCase(FirebaseUserRepository())
    private val modifyUseCase = ModifyUseCase(FirebaseUserRepository())
    private lateinit var binding: ActivityManageUsersBinding
    private lateinit var users: List<User>
    private lateinit var adapter: UsersAdapter
    private lateinit var editUserLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getAllUsers()
        initComponents()
    }

    private fun initUI() {
        adapter = UsersAdapter(users,
            onUserChanged = { user, isChecked->
                modifyState(user, isChecked)
            },
            onEditClicked = { user->
                navigateToEditUser(user.uid!!)
            }
        )
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = adapter
    }

    private fun initComponents() {
        binding.ivAdd.setOnClickListener {
            navigateToCreateUser()
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

        editUserLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                getAllUsers()
            }
        }
    }

    private fun searchByDocument(document: String) {
        val filteredList = users.filter { user ->
            user.documento.toString().contains(document, ignoreCase = true)
        }
        adapter.updateList(filteredList)
    }

    private fun modifyState(user: User, isChecked: Boolean) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar cambio de estado")
            .setMessage("¿Estás seguro de cambiar el estado del usuario a ${if (isChecked) "Activo" else "Inactivo"}?")
            .setPositiveButton("Sí") { _, _ ->
                user.estado = if (isChecked) "Activo" else "Inactivo"
                lifecycleScope.launch {
                    val result = modifyUseCase.execute(user, "userData")
                    result.onSuccess {
                        Toast.makeText(this@ManageUsersActivity, "Estado modificado", Toast.LENGTH_SHORT).show()
                        adapter.notifyItemChanged(users.indexOf(user))
                    }.onFailure { error ->
                        Toast.makeText(this@ManageUsersActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                        user.estado = if (isChecked) "Inactivo" else "Activo"
                        adapter.notifyItemChanged(users.indexOf(user))
                    }
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
                val position = users.indexOf(user)
                adapter.notifyItemChanged(position) // Revertir el cambio visual
            }
            .show()
    }


    private fun getAllUsers() {
        lifecycleScope.launch {
            val result = getAllUsersUseCase.execute("userData")
            result.onSuccess { userList ->
                users = userList as List<User>
                initUI()
            }.onFailure { error ->
                Toast.makeText(this@ManageUsersActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToCreateUser() {
        val intent = Intent(this@ManageUsersActivity, RegisterActivity::class.java)
        editUserLauncher.launch(intent)
    }

    private fun navigateToEditUser(userId: String) {
        val intent = Intent(this, ModifyUserActivity::class.java)
        intent.putExtra("uid", userId)
        editUserLauncher.launch(intent)
    }
}