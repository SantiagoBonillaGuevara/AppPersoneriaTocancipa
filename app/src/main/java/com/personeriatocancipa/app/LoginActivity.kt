package com.personeriatocancipa.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.personeriatocancipa.app.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        initComponents()
    }

    private fun initComponents() {
        binding.btnLogin.setOnClickListener(){
            login()
        }

        binding.btnSignUp.setOnClickListener(){
            signup()
        }

        binding.btnRecuperarPassword.setOnClickListener(){
            restorePassword()
        }

        binding.btnTogglePassword.setOnClickListener {
            togglePasswordVisibility()
        }
    }

    private fun signup() {
        val intent = Intent(this@LoginActivity, CreateUserActivity::class.java)
        intent.putExtra("tarea","crear")
        intent.putExtra("usuario","cliente")
        startActivity(intent)
    }

    private fun login() {
        val correo = binding.txtCorreo.text.toString()
        val clave = binding.txtClave.text.toString()
        //Login de usuario
        if(correo.isNullOrEmpty() || clave.isNullOrEmpty()){
            Toast.makeText(this@LoginActivity, "¡Ingresa información!", Toast.LENGTH_SHORT).show()
        }else{
            mAuth.signInWithEmailAndPassword(correo, clave)
                .addOnCompleteListener(this){
                        task ->
                    if(task.isSuccessful){
                        showRoleScreen()
                        binding.txtCorreo.text.clear()
                        binding.txtClave.text.clear()
                    }else{
                        Toast.makeText(
                            this@LoginActivity,
                            "¡Hubo un error!",
                            Toast.LENGTH_SHORT
                        ) .show()
                    }
                }
        }
    }

    private fun showRoleScreen() {
        buscarSiCliente()
    }

    private fun buscarSiCliente(){
        println("Buscando Cliente")
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val databaseRefCliente = FirebaseDatabase.getInstance().getReference("userData").child(userId)

        databaseRefCliente.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                println(snapshot)
                if (snapshot.exists()) {
                    val estado = snapshot.child("estado").value.toString()
                    if (estado == "Activo") {
                        val intent = Intent(this@LoginActivity, UserActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "¡Esta cuenta ha sido desactivada!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    buscarSiAbogado()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "Error al leer datos", error.toException())
            }
        })
    }

    private fun buscarSiAbogado(){
        println("Buscando Abogado")
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val databaseRefAbogado = FirebaseDatabase.getInstance().getReference("abogadoData").child(userId)

        databaseRefAbogado.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                println(snapshot)
                if (snapshot.exists()) {
                    val estado = snapshot.child("estado").value.toString()
                    if(estado == "Activo") {
                        val intent = Intent(this@LoginActivity, LawyerActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(
                            this@LoginActivity,
                            "¡Esta cuenta ha sido desactivada!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    buscarSiAdmin()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "Error al leer datos", error.toException())
            }
        })
    }

    private fun buscarSiAdmin(){
        println("Buscando Admin")
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val databaseRefAdmin = FirebaseDatabase.getInstance().getReference("AdminData").child(userId)

        println(userId)
        databaseRefAdmin.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                println(snapshot)
                if (snapshot.exists()) {
                    val estado = snapshot.child("estado").value.toString()
                    if(estado == "Activo") {
                        val intent = Intent(this@LoginActivity, AdminActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(
                            this@LoginActivity,
                            "¡Esta cuenta ha sido desactivada!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "¡Usuario no encontrado!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "Error al leer datos", error.toException())
            }
        })
    }

    private fun restorePassword(){
        val intent = Intent(this@LoginActivity, RestorePasswordActivity::class.java)
        startActivity(intent)
    }

    private fun togglePasswordVisibility(){
        if (binding.txtClave.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            binding.txtClave.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            binding.txtClave.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        binding.txtClave.setSelection(binding.txtClave.text.length) // Mantener cursor al final
    }

}