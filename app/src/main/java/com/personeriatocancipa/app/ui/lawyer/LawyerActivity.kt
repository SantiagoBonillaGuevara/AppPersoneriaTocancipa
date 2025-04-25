package com.personeriatocancipa.app.ui.lawyer

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
import com.personeriatocancipa.app.CreateLawyerActivity
import com.personeriatocancipa.app.CreateUserActivity
import com.personeriatocancipa.app.GetLawyerDatesActivity
import com.personeriatocancipa.app.GetUserDatesActivity
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.ActivityLawyerBinding
import com.personeriatocancipa.app.ui.common.LoginActivity

class LawyerActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityLawyerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLawyerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        initUI()
        initComponents()
    }

    private fun initUI(){
        cargarNombre()
    }

    private fun initComponents(){
        binding.btnConsultarCitas.setOnClickListener{
            navigateToGetLawyerDates()
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
        val databaseRef = FirebaseDatabase.getInstance().getReference("abogadoData").child(userId)
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

    private fun navigateToGetLawyerDates(){
        val intent = Intent(this@LawyerActivity, GetLawyerDatesActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToModify(){
        val intent = Intent(this@LawyerActivity, CreateLawyerActivity::class.java)
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