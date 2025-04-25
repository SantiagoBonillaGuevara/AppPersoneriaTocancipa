package com.personeriatocancipa.app.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.personeriatocancipa.app.CreateDateActivity
import com.personeriatocancipa.app.CreateUserActivity
import com.personeriatocancipa.app.GetUserDatesActivity
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.ActivityUserBinding
import com.personeriatocancipa.app.ui.common.LoginActivity

class UserActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        initUI()
        initComponents()
    }

    private fun initUI(){
        cargarNombre()
    }

    private fun initComponents(){
        binding.btnAgendarCita.setOnClickListener{
            navigateToCreateDate()
        }

        binding.btnVerCitas.setOnClickListener{
            navigateToGetUserDates()
        }

        binding.btnModificar.setOnClickListener{
            navigateToModify()
        }

        binding.btnSalir.setOnClickListener{
            navigateToLogin()
        }
    }

    private fun cargarNombre() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val databaseRef = FirebaseDatabase.getInstance().getReference("userData").child(userId)
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

    private fun navigateToCreateDate(){
        val intent = Intent(this@UserActivity, CreateDateActivity::class.java)
        intent.putExtra("tarea", "crear")
        intent.putExtra("sujeto", "cliente")
        startActivity(intent)
    }

    private fun navigateToGetUserDates(){
        val intent = Intent(this@UserActivity, GetUserDatesActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToModify(){
        val intent = Intent(this@UserActivity, CreateUserActivity::class.java)
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