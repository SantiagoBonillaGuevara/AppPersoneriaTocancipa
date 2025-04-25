package com.personeriatocancipa.app.ui.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.personeriatocancipa.app.ManagementActivity
import com.personeriatocancipa.app.databinding.ActivityAdminBinding
import com.personeriatocancipa.app.ui.common.LoginActivity

class AdminActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        cargarNombre()

        binding.btnGestionarUsuarios.setOnClickListener{
            val intent = Intent(this@AdminActivity, ManagementActivity::class.java)
            intent.putExtra("tipo", "usuario")
            startActivity(intent)
        }

        binding.btnGestionarCitas.setOnClickListener{
            val intent = Intent(this@AdminActivity, ManagementActivity::class.java)
            intent.putExtra("tipo", "cita")
            startActivity(intent)
        }

        binding.btnSalir.setOnClickListener{
            navigateToLogin()
        }
    }

    private fun cargarNombre() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val databaseRef = FirebaseDatabase.getInstance().getReference("AdminData").child(userId)
        val userNombreRef = databaseRef.child("nombreCompleto")

        userNombreRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Aquí obtienes el valor del rol
                val userNombre = snapshot.getValue(String::class.java)
                userNombre?.let {
                    cargarPrimerNombre(it)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Maneja cualquier error de lectura de la base de datos
                Log.w("FirebaseDatabase", "Error al obtener el nombre del usuario.", error.toException())
            }
        })
    }

    private fun cargarPrimerNombre(nombreCompleto: String) {
        val primerNombre = nombreCompleto.split(" ")[0]
        binding.txtUsuario.text = "Bienvenido(a), señor(a) $primerNombre"
    }

    private fun navigateToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        finish()
        startActivity(intent)
    }
}