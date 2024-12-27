package com.personeriatocancipa.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Bienvenida : AppCompatActivity() {

    private lateinit var txtCorreo: EditText
    private lateinit var txtClave: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var btnRecuperarPassword: Button
    private lateinit var btnTogglePassword: Button
    private lateinit var mAuth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        mAuth = FirebaseAuth.getInstance()

        //Obtiene valores de Layout
        txtCorreo = findViewById(R.id.txtCorreo)
        txtClave = findViewById(R.id.txtClave)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)
        btnRecuperarPassword = findViewById(R.id.btnRecuperarPassword)
        btnTogglePassword = findViewById(R.id.btnTogglePassword)


        //Crea eventListener para clicks en "Log In"
        btnLogin.setOnClickListener(){
            val correo = txtCorreo.text.toString()
            val clave = txtClave.text.toString()
            login(correo, clave)
        }

        //Crea eventListener para clicks en "Sign Up"
        btnSignUp.setOnClickListener(){
            signup()
            txtCorreo.text.clear()
            txtClave.text.clear()
        }

        //Crea eventListener para clicks en "Recuperar Contraseña"
        btnRecuperarPassword.setOnClickListener(){
            recuperarPassword()
        }

        // Botón Ver Contraseña
        btnTogglePassword = findViewById(R.id.btnTogglePassword)
        btnTogglePassword.setOnClickListener { v: View? ->
            if (txtClave.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                txtClave.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                txtClave.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            txtClave.setSelection(txtClave.text.length) // Mantener cursor al final
        }
    }

    private fun signup() {
        val intent = Intent(this@Bienvenida, CrearCuenta::class.java)
        intent.putExtra("tarea","crear")
        intent.putExtra("usuario","cliente")
        startActivity(intent)
    }

    private fun login(correo: String?, clave: String?) {
        //Login de usuario
        if(correo.isNullOrEmpty() || clave.isNullOrEmpty()){
            Toast.makeText(
                this@Bienvenida,
                "¡Ingresa información!",
                Toast.LENGTH_SHORT
            ) .show()
        }else{
            mAuth.signInWithEmailAndPassword(correo, clave)
                .addOnCompleteListener(this){
                        task ->
                    if(task.isSuccessful){
                        showRoleScreen()
                        txtCorreo.text.clear()
                        txtClave.text.clear()
                    }else{
                        Toast.makeText(
                            this@Bienvenida,
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
                        val intent = Intent(this@Bienvenida, InterfazCliente::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@Bienvenida,
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
                        val intent = Intent(this@Bienvenida, InterfazAbogado::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(
                            this@Bienvenida,
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
                        val intent = Intent(this@Bienvenida, InterfazAdmin::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(
                            this@Bienvenida,
                            "¡Esta cuenta ha sido desactivada!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@Bienvenida,
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

    private fun recuperarPassword(){
        val intent = Intent(this@Bienvenida, RecuperarPassword::class.java)
        startActivity(intent)
    }

}