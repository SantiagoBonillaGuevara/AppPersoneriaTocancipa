package com.personeriatocancipa.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CreateAdminActivity : AppCompatActivity() {
    private lateinit var gridConsultar: LinearLayout
    private lateinit var txtAnuncio:TextView
    private lateinit var txtNombre: EditText
    private lateinit var txtConsultar: EditText
    private lateinit var txtClave: EditText
    private lateinit var txtConfirmarClave: EditText
    private lateinit var txtDocumento: EditText
    private lateinit var spEstado: Spinner
    private lateinit var txtCorreo: EditText
    private lateinit var btnConsultar: Button
    private lateinit var btnSalir: Button
    private lateinit var btnSignUp: Button
    private lateinit var btnModificar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnTogglePassword: Button
    private lateinit var btnToggleCheckPassword: Button
    private lateinit var tvClave: TextView
    private lateinit var tvConfirmarClave: TextView
    private lateinit var tvDocumento: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private var tarea: String = ""
    private var uidConsultado: String = ""

    @SuppressLint("ResourceType", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_admin)

        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()

        spEstado = findViewById(R.id.spEstado)
        ArrayAdapter.createFromResource(
            this,
            R.array.opcionesEstadoCargo,
            R.drawable.spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
            spEstado.adapter = adapter
        }
        gridConsultar = findViewById(R.id.gridConsultar)
        txtAnuncio = findViewById(R.id.txtAnuncio)
        txtConsultar = findViewById(R.id.txtConsultar)
        txtNombre = findViewById(R.id.txtNombre)
        txtClave = findViewById(R.id.txtClave)
        txtConfirmarClave = findViewById(R.id.txtConfirmarClave)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtDocumento = findViewById(R.id.txtDocumento)
        btnSalir = findViewById(R.id.btnSalir)
        btnEliminar = findViewById(R.id.btnEliminar)
        btnConsultar = findViewById(R.id.btnConsultar)
        btnModificar = findViewById(R.id.btnModificar)
        btnTogglePassword = findViewById(R.id.btnTogglePassword)
        btnToggleCheckPassword = findViewById(R.id.btnToggleCheckPassword)
        btnSignUp = findViewById(R.id.btnSignUp)
        tvClave = findViewById(R.id.tvClave)
        tvConfirmarClave = findViewById(R.id.tvConfirmarClave)
        tvDocumento = findViewById(R.id.tvDocumento)
        tarea = intent.getStringExtra("tarea").toString()

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

        // Botón Ver Confirmar Contraseña
        btnToggleCheckPassword = findViewById(R.id.btnToggleCheckPassword)
        btnToggleCheckPassword.setOnClickListener { v: View? ->
            if (txtConfirmarClave.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                txtConfirmarClave.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                txtConfirmarClave.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            txtConfirmarClave.setSelection(txtConfirmarClave.text.length) // Mantener cursor al final
        }

        if(tarea == "crear"){
            txtAnuncio.text = "Crear Administrador"
            gridConsultar.visibility = GridLayout.GONE
            btnSignUp.visibility = Button.VISIBLE
            btnModificar.visibility = Button.GONE
            btnEliminar.visibility = Button.GONE
        }else {
            txtAnuncio.text = "Gestión de Administrador"
            gridConsultar.visibility = View.VISIBLE
            tvDocumento.visibility = TextView.GONE
            tvDocumento.isEnabled = false
            txtDocumento.visibility = EditText.GONE
            txtDocumento.isEnabled = false
            txtCorreo.isEnabled = false
            when (tarea) {
                "consultar" -> {
                    btnSignUp.visibility = Button.GONE
                    btnModificar.visibility = Button.GONE
                    btnEliminar.visibility = Button.GONE
                    txtClave.visibility = EditText.GONE
                    btnTogglePassword.visibility = Button.GONE
                    txtConfirmarClave.visibility = EditText.GONE
                    btnToggleCheckPassword.visibility = Button.GONE
                    tvClave.visibility = TextView.GONE
                    tvConfirmarClave.visibility = TextView.GONE
                    disableFields()
                }
                "modificar" -> {
                    btnSignUp.visibility = Button.GONE
                    btnModificar.visibility = Button.GONE
                    btnEliminar.visibility = Button.GONE
                    txtClave.visibility = EditText.GONE
                    btnTogglePassword.visibility = Button.GONE
                    txtConfirmarClave.visibility = EditText.GONE
                    btnToggleCheckPassword.visibility = Button.GONE
                    tvClave.visibility = TextView.GONE
                    tvConfirmarClave.visibility = TextView.GONE
                }
                else -> {
                    btnSignUp.visibility = Button.GONE
                    btnModificar.visibility = Button.GONE
                    btnEliminar.visibility = Button.GONE
                    txtClave.visibility = EditText.GONE
                    btnTogglePassword.visibility = Button.GONE
                    txtConfirmarClave.visibility = EditText.GONE
                    btnToggleCheckPassword.visibility = Button.GONE
                    tvClave.visibility = TextView.GONE
                    tvConfirmarClave.visibility = TextView.GONE
                    disableFields()
                }
            }
        }
        btnSalir.setOnClickListener {
            finish()
        }

        btnSignUp.setOnClickListener {
            // Crear y configurar el diálogo inicial
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Política de Tratamiento de Datos Personales")
            builder.setMessage("Señor(a) usuario(a), autoriza a la Personería de Tocancipá, para la recolección, consulta, almacenamiento, uso, traslado o eliminación de sus datos personales, con el fin de: adelantar las gestiones, actuaciones e intervenciones que permitan el restablecimiento y goce de sus derechos, invitar a eventos de participación ciudadana u organizados por la entidad,  información con fines estadísticos, enviar información a entidades autorizadas cuando la solicitud lo amerite, evaluar la calidad del servicio y contactar al titular en los casos que se considere necesario. \n" +
                    "\n" +
                    "No es obligatorio para la prestación del servicio, suministrar los datos personales de carácter sensible o de niños, niñas y adolescentes. Se exime el tratamiento de datos de niños, niñas y adolescentes, salvo aquellos datos que sean de naturaleza pública. \n" +
                    "Como titular de la información tiene derecho a conocer, actualizar y rectificar sus datos personales, así como  suprimir o revocar la autorización otorgada para su tratamiento, pedir prueba de la autorización otorgada al responsable del tratamiento y ser informado sobre el uso que le han dado a los mismos, presentar quejas ante la Superintendencia de Industria y Comercio SIC por infracción a la ley y acceder en forma gratuita a sus datos personales, acorde al o establecido en los artículos 15, 20 y 74 de la Constitución Política, , la Ley 1581 del 2012, el Decreto Reglamentario 1377 de 2013, la Ley 1266 del 31 de 2008, el Decreto Reglamentario 1727 de  2009, y demás normatividad vigente relacionada. \n" +
                    "Consulte la Política de Tratamiento de Datos Personales Personería Municipal de Tocancipá adoptada mediante Resolución Administrativa 051 de 2020 en el siguiente link https://www.personeria-tocancipa.gov.co/politicas-y-lineamientos/politica-de-tratamiento-de-datos\n" +
                    "¿Acepta términos y condiciones?\n")

            // Agregar botones de acción
            builder.setPositiveButton("Sí") { dialog, which ->
                // Continuar con la creación de la cuenta
                val campos = conseguirCampos()
                if (!verificarCampos(campos)) {
                    Toast.makeText(
                        this@CreateAdminActivity,
                        "Diligencie todos los datos",
                        Toast.LENGTH_SHORT,
                    ).show()
                    return@setPositiveButton
                } else{
                    val nombre = campos[0]
                    val clave = campos[1]
                    val confirmarClave = campos[2]
                    val documento = campos[3]
                    val correo = campos[4]
                    val estado = campos[5]

                    if(clave != confirmarClave){
                        Toast.makeText(
                            this@CreateAdminActivity,
                            "Las contraseñas no coinciden",
                            Toast.LENGTH_SHORT,
                        ).show()
                        return@setPositiveButton
                    }else{
                        signUp(nombre, clave, documento,
                            correo, estado)
                    }
                }
            }

            builder.setNegativeButton("No") { dialog, which ->
                // Cancelar el flujo
                dialog.dismiss()
                Toast.makeText(
                    this@CreateAdminActivity,
                    "Debe aceptar la política de tratamiento de datos personales para continuar",
                    Toast.LENGTH_SHORT,
                ).show()
            }

            // Mostrar el diálogo de permiso de habeas data
            builder.create().show()
        }

        btnConsultar.setOnClickListener{
            consultarPorCedula()
        }

        btnModificar.setOnClickListener{
            val campos = conseguirCampos()
            if(campos[3].isEmpty()){
                Toast.makeText(
                    this@CreateAdminActivity,
                    "Ingrese cédula para modificar",
                    Toast.LENGTH_SHORT,
                ).show()
                return@setOnClickListener
            }else{
                if (!verificarCampos(campos)) {
                    Toast.makeText(
                        this@CreateAdminActivity,
                        "Diligencie todos los datos",
                        Toast.LENGTH_SHORT,
                    ).show()
                    return@setOnClickListener
                } else{
                    val nombre = campos[0]
                    val documento = campos[3]
                    val correo = campos[4]
                    val estado = campos[5]

                    mDbRef = FirebaseDatabase.getInstance().getReference("AdminData")
                    mDbRef.child(uidConsultado).setValue(
                        Admin(documento,nombre,correo,
                            estado))
                    Toast.makeText(
                        this@CreateAdminActivity,
                        "Administrador modificado exitosamente",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }

        btnEliminar.setOnClickListener{
            if(uidConsultado.isEmpty()){
                Toast.makeText(
                    this@CreateAdminActivity,
                    "Ingrese cédula para eliminar",
                    Toast.LENGTH_SHORT,
                ).show()
                return@setOnClickListener
            }else{
                mDbRef = FirebaseDatabase.getInstance().getReference("AdminData")
                mDbRef.child(uidConsultado).removeValue()
                Toast.makeText(
                    this@CreateAdminActivity,
                    "Administrador eliminado exitosamente",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }


    }
    private fun disableFields(){
        txtNombre.isEnabled = false
        txtClave.isEnabled = false
        txtConfirmarClave.isEnabled = false
        btnTogglePassword.isEnabled = false
        btnToggleCheckPassword.isEnabled = false
        txtDocumento.isEnabled = false
        txtCorreo.isEnabled = false
        spEstado.isEnabled = false
    }

    private fun signUp(
        nombre: String,
        clave:String,
        documento:String,
        correo:String,
        estado:String
    )
    {
        // Buscar en RealtimeDatabase si ya existe un usuario con el mismo documento
        mDbRef = FirebaseDatabase.getInstance().getReference("AdminData")
        val query = mDbRef.orderByChild("cedula").equalTo(documento)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(
                        this@CreateAdminActivity,
                        "Ya existe un usuario con la cédula ingresada",
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    mAuth.createUserWithEmailAndPassword(correo,clave)
                        .addOnCompleteListener(this@CreateAdminActivity){
                                task ->
                            if (task.isSuccessful){
                                addUserToDatabase(nombre, documento, correo, estado, mAuth.currentUser?.uid!!)
                                Toast.makeText(
                                    this@CreateAdminActivity,
                                    "Administrador creado exitosamente",
                                    Toast.LENGTH_SHORT,
                                ).show()
                                val intent = Intent(this@CreateAdminActivity, AdminActivity::class.java)
                                finish()
                                startActivity(intent)
                            }
                        }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@CreateAdminActivity,
                    "Error al consultar la base de datos",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        })
    }
    private fun conseguirCampos(): Array<String> {
        val nombre = txtNombre.text.toString()
        val clave = txtClave.text.toString()
        val confirmarClave = txtConfirmarClave.text.toString()
        val documento = txtDocumento.text.toString()
        val correo = txtCorreo.text.toString()
        val estado = spEstado.selectedItem.toString()

        return arrayOf(nombre, clave, confirmarClave, documento, correo, estado)
    }
    private fun verificarCampos(campos: Array<String>): Boolean {
        //Verifica que todos los campos estén diligenciados
        return if(tarea != "crear"){
            !(campos[0].isEmpty() || campos[4].isEmpty())
        }else{
            !(campos[0].isEmpty() || campos[1].isEmpty() || campos[2].isEmpty() || campos[3].isEmpty() || campos[4].isEmpty())
        }
    }

    private fun addUserToDatabase(nombre: String, documento: String, correo: String, estado: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("AdminData").child(uid).setValue(
            Admin(documento, nombre, correo, estado))
        Toast.makeText(
            this@CreateAdminActivity,
            "Cuenta creada exitosamente",
            Toast.LENGTH_SHORT,
        ).show()

    }

    private fun consultarPorCedula() {
        val cedula = txtConsultar.text.toString()
        if (cedula.isEmpty()) {
            Toast.makeText(
                this@CreateAdminActivity,
                "Ingrese cédula para consultar",
                Toast.LENGTH_SHORT,
            ).show()
            return
        }
        mDbRef = FirebaseDatabase.getInstance().getReference("AdminData")

        val query = mDbRef.orderByChild("cedula").equalTo(cedula)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                println(snapshot)
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        uidConsultado = it.key.toString()
                        println(it)
                        val nombre = it.child("nombreCompleto").value.toString()
                        println(nombre)
                        val documento = it.child("cedula").value.toString()
                        val estado = it.child("estado").value.toString()
                        val correo = it.child("correo").value.toString()

                        txtNombre.setText(nombre)
                        txtClave.setText("********")
                        txtDocumento.setText(documento)
                        txtCorreo.setText(correo)
                        spEstado.setSelection((spEstado.adapter as ArrayAdapter<String>).getPosition(estado))
                    }
                    if(tarea == "modificar"){
                        btnModificar.visibility = Button.VISIBLE
                    }else if(tarea == "eliminar"){
                        btnEliminar.visibility = Button.VISIBLE
                    }
                } else {
                    Toast.makeText(
                        this@CreateAdminActivity,
                        "No se encontró un usuario con la cédula ingresada",
                        Toast.LENGTH_LONG,
                    ).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@CreateAdminActivity,
                    "Error al consultar la base de datos",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        })
    }
}