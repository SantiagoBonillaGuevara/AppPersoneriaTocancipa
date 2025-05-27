package com.personeriatocancipa.app.ui.admin.managementAdminPqrs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.databinding.ActivityManageUsersBinding
import com.personeriatocancipa.app.domain.model.Admin
import com.personeriatocancipa.app.domain.usecase.GetAllUsersUseCase
import com.personeriatocancipa.app.domain.usecase.ModifyUseCase
import com.personeriatocancipa.app.ui.admin.managementAdminPqrs.adapter.AdminPqrsAdapter
import com.personeriatocancipa.app.ui.admin.managementAdminPqrs.registerAdminPqrs.RegisterAdminPqrsActivity
import com.personeriatocancipa.app.ui.common.LoginActivity
import kotlinx.coroutines.launch

class ManageAdminPqrsActivity : AppCompatActivity() {
    private lateinit var b: ActivityManageUsersBinding
    private lateinit var list: List<Admin>
    private lateinit var adapter: AdminPqrsAdapter

    private val getAll = GetAllUsersUseCase(FirebaseUserRepository())
    private val modify = ModifyUseCase  (FirebaseUserRepository())

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode==Activity.RESULT_OK) loadList()
    }

    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        b = ActivityManageUsersBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.ivAdd.setOnClickListener {
            launcher.launch(
                Intent(this, RegisterAdminPqrsActivity::class.java)
            )
        }
        b.ivBack.setOnClickListener {
            finish()
        }

        b.etDocumento.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, i: Int, i2: Int, i3: Int){}
            override fun onTextChanged(s: CharSequence?, i: Int, i2: Int, i3: Int){}
            override fun afterTextChanged(s: Editable?) {
                val q = s.toString()
                adapter.updateList(list.filter { it.documento?.contains(q,true)==true })
            }
        })

        b.rvUsers.layoutManager = LinearLayoutManager(this)
        loadList()
    }

    private fun loadList() {
        lifecycleScope.launch {
            getAll.execute("AdminPqrsData")
                .onSuccess { users ->
                    list = users as List<Admin>
                    adapter = AdminPqrsAdapter(
                        list,
                        onEdit = { adm ->
                            launcher.launch(
                                Intent(this@ManageAdminPqrsActivity, ModifyAdminPqrsActivity::class.java)
                                    .putExtra("uid", adm.uid)
                            )
                        },
                        onState = { adm,chk ->
                            AlertDialog.Builder(this@ManageAdminPqrsActivity)
                                .setTitle("Cambiar estado")
                                .setMessage("¿Activo = $chk ?")
                                .setPositiveButton("Sí"){_,_->
                                    adm.estado = if(chk)"Activo" else "Inactivo"
                                    lifecycleScope.launch {
                                        modify.execute(adm,"AdminPqrsData")
                                            .onSuccess { adapter.notifyItemChanged(list.indexOf(adm)) }
                                            .onFailure{ e->
                                                Toast.makeText(this@ManageAdminPqrsActivity,"Error: ${e.message}",Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                }
                                .setNegativeButton("No"){d,_->
                                    d.dismiss()
                                    adapter.notifyItemChanged(list.indexOf(adm))
                                }
                                .show()
                        }
                    )
                    b.rvUsers.adapter = adapter
                }
                .onFailure { e ->
                    Toast.makeText(this@ManageAdminPqrsActivity,"Error: ${e.message}",Toast.LENGTH_SHORT).show()
                }
        }
    }
}
