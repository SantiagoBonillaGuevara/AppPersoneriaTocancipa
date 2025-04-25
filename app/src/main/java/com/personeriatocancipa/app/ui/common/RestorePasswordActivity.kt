package com.personeriatocancipa.app.ui.common

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.data.repository.FirebaseAuthRepository
import com.personeriatocancipa.app.databinding.ActivityRestorePasswordBinding
import com.personeriatocancipa.app.domain.usecase.RestorePasswordUseCase
import kotlinx.coroutines.launch

class RestorePasswordActivity : AppCompatActivity() {

    /*private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference*/
    private lateinit var binding: ActivityRestorePasswordBinding
    private val useCase = RestorePasswordUseCase(FirebaseAuthRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestorePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        mAuth = FirebaseAuth.getInstance()
        initComponents()
    }

    private fun initComponents(){
        binding.btnRestablecer.setOnClickListener(){
//            reestablecerContraseña()
            onResetClicked()
        }

        binding.btnVolver.setOnClickListener(){
            navigateToLogin()
        }
    }

    /*private fun reestablecerContraseña(){
        val correo = binding.txtCorreo.text.toString()
        if(correo.isEmpty()){
            Toast.makeText(this, "Ingrese un correo", Toast.LENGTH_SHORT).show()
        }else{
            // Verificar si el correo está registrado en Admin.
            mDbRef = FirebaseDatabase.getInstance().getReference("AdminData")
            var query = mDbRef.orderByChild("correo").equalTo(correo)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Si encuentra en Admin.
                        restablecer(correo)
                    } else {
                        // Si no es Admin.
                        // Verificar si el correo está registrado en Abogados
                        mDbRef = FirebaseDatabase.getInstance().getReference("abogadoData")
                        query = mDbRef.orderByChild("correo").equalTo(correo)
                        query.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    // Si encuentra en Abogados
                                    restablecer(correo)
                                } else {
                                    // Si no es Abogado
                                    // Verificar si el correo está registrado en Cliente
                                    mDbRef = FirebaseDatabase.getInstance().getReference("userData")
                                    query = mDbRef.orderByChild("correo").equalTo(correo)
                                    query.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.exists()) {
                                                restablecer(correo)
                                            } else {
                                                Toast.makeText(
                                                    this@RestorePasswordActivity,
                                                    "Correo no registrado",
                                                    Toast.LENGTH_SHORT,
                                                ).show()
                                            }
                                        }
                                        override fun onCancelled(error: DatabaseError) {
                                            Toast.makeText(
                                                this@RestorePasswordActivity,
                                                "Error al consultar la base de datos",
                                                Toast.LENGTH_SHORT,
                                            ).show()
                                        }
                                    })
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(
                                    this@RestorePasswordActivity,
                                    "Error al consultar la base de datos",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        })
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@RestorePasswordActivity,
                        "Error al consultar la base de datos",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            })
        }
    }
    private fun restablecer(correo: String) {
        mAuth.sendPasswordResetEmail(correo)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Email sent
                    Toast.makeText(this, "Correo enviado. Revise su bandeja de entrada o su carpeta de 'No Deseados'", Toast.LENGTH_LONG).show()
                } else {
                    // Email not sent
                    Toast.makeText(this, "Error al enviar correo", Toast.LENGTH_SHORT).show()
                }
            }
    }*/
    private fun onResetClicked(){
        val email = binding.txtCorreo.text.toString()
        if(email.isBlank()){
            Toast.makeText(this, "Ingrese un correo", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            val result = useCase.execute(email)
            result.onSuccess {
                Toast.makeText(this@RestorePasswordActivity, "Correo enviado. Revise su bandeja de entrada o su carpeta de 'No Deseados'", Toast.LENGTH_LONG).show()
            }.onFailure {
                Toast.makeText(this@RestorePasswordActivity, "Error al enviar correo: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        finish()
        startActivity(intent)
    }
}