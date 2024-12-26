package com.personeriatocancipa.app

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar


class CrearCuenta : AppCompatActivity() {

    //Crea variables de Layout
    private lateinit var gridConsultar: LinearLayout
    private lateinit var txtConsultar: EditText
    private lateinit var txtAnuncio: TextView
    private lateinit var txtNombre: EditText
    private lateinit var txtClave: EditText
    private lateinit var txtConfirmarClave: EditText
    private lateinit var txtDocumento: EditText
    private lateinit var txtEdad: EditText
    private lateinit var txtDireccion: EditText
    private lateinit var txtTelefono: EditText
    private lateinit var txtCorreo: EditText
    private lateinit var txtFecha: EditText
    private lateinit var txtEtario: EditText
    private lateinit var txtGrupoEtnico: EditText
    private lateinit var spTipoDocumento: Spinner
    private lateinit var spSexo: Spinner
    private lateinit var spEscolaridad: Spinner
    private lateinit var spGrupo: Spinner
    private lateinit var spComunidad: Spinner
    private lateinit var spEstado: Spinner
    private lateinit var spSector: Spinner
    private lateinit var spIdentidad: Spinner
    private lateinit var spOrientacion: Spinner
    private lateinit var spNacionalidad: Spinner
    private lateinit var spDiscapacidad: Spinner
    private lateinit var spEstrato: Spinner
    private lateinit var btnConsultar: Button
    private lateinit var btnSalir: Button
    private lateinit var btnSignUp: Button
    private lateinit var btnModificar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnTogglePassword: Button
    private lateinit var btnToggleCheckPassword: Button
    private lateinit var btnFecha: Button
    private lateinit var tvClave: TextView
    private lateinit var tvConfirmarClave: TextView
    private lateinit var tvEstado: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var calendar: Calendar
    private var tarea: String = ""
    private var sujeto: String = ""
    private var usuario: String = ""
    private var uidConsultado: String = ""
    private var seleccionFecha = Calendar.getInstance()
    private var seleccionHora=""

    @SuppressLint("ResourceType", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)

        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()

        tarea = intent.getStringExtra("tarea").toString()
        sujeto = intent.getStringExtra("sujeto").toString()
        usuario = intent.getStringExtra("usuario").toString()
        // Manejo valores de Combo Box

        // Sexo
        spSexo = findViewById(R.id.spSexo)
        ArrayAdapter.createFromResource(
            this,
            R.array.opcionesSexo,
            R.drawable.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
            spSexo.adapter = adapter
        }

        // Escolaridad
        spEscolaridad = findViewById(R.id.spEscolaridad)
        ArrayAdapter.createFromResource(
            this,
            R.array.opcionesEscolaridad,
            R.drawable.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
            spEscolaridad.adapter = adapter
        }

        // Grupo Étnico
        spGrupo = findViewById(R.id.spGrupo)
        ArrayAdapter.createFromResource(
            this,
            R.array.opcionesGrupo,
            R.drawable.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
            spGrupo.adapter = adapter
        }

        // Comunidad Vulnerable
        spComunidad = findViewById(R.id.spComunidad)
        ArrayAdapter.createFromResource(
            this,
            R.array.opcionesComunidad,
            R.drawable.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
            spComunidad.adapter = adapter
        }

        // Estado
        spEstado = findViewById(R.id.spEstado)
        ArrayAdapter.createFromResource(
            this,
            R.array.opcionesEstadoCargo,
            R.drawable.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
            spEstado.adapter = adapter
        }

        // Tipo de Documento
        spTipoDocumento = findViewById(R.id.spTipoDocumento)
        ArrayAdapter.createFromResource(
            this,
            R.array.opcionesTipoDocumento,
            R.drawable.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
            spTipoDocumento.adapter = adapter
        }

        // Sector y/o vereda
        spSector = findViewById(R.id.spSector)
        ArrayAdapter.createFromResource(
            this,
            R.array.opcionesSector,
            R.drawable.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
            spSector.adapter = adapter
        }

        // Identidad de Género
        spIdentidad = findViewById(R.id.spIdentidad)
        ArrayAdapter.createFromResource(
            this,
            R.array.opcionesIdentidad,
            R.drawable.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
            spIdentidad.adapter = adapter
        }

        // Orientación Sexual
        spOrientacion = findViewById(R.id.spOrientacion)
        ArrayAdapter.createFromResource(
            this,
            R.array.opcionesOrientacion,
            R.drawable.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
            spOrientacion.adapter = adapter
        }

        // Nacionalidad
        spNacionalidad = findViewById(R.id.spNacionalidad)
        ArrayAdapter.createFromResource(
            this,
            R.array.opcionesNacionalidad,
            R.drawable.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
            spNacionalidad.adapter = adapter
        }

        // Discapacidad
        spDiscapacidad = findViewById(R.id.spDiscapacidad)
        ArrayAdapter.createFromResource(
            this,
            R.array.opcionesDiscapacidad,
            R.drawable.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
            spDiscapacidad.adapter = adapter
        }

        // Estrato
        spEstrato = findViewById(R.id.spEstrato)
        ArrayAdapter.createFromResource(
            this,
            R.array.opcionesEstrato,
            R.drawable.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
            spEstrato.adapter = adapter
        }


        txtClave = findViewById(R.id.txtClave)
        txtConfirmarClave = findViewById(R.id.txtConfirmarClave)

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


        //Obtiene demás elementos de Layout
        gridConsultar = findViewById(R.id.gridConsultar)
        txtConsultar = findViewById(R.id.txtConsultar)
        txtAnuncio = findViewById(R.id.txtAnuncio)
        txtNombre = findViewById(R.id.txtNombre)
        txtDocumento = findViewById(R.id.txtDocumento)
        txtEdad = findViewById(R.id.txtEdad)
        txtDireccion = findViewById(R.id.txtDireccion)
        txtTelefono = findViewById(R.id.txtTelefono)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtFecha = findViewById(R.id.txtFecha)
        txtEtario = findViewById(R.id.txtEtario)
        btnConsultar = findViewById(R.id.btnConsultar)
        btnFecha = findViewById(R.id.btnFecha)
        btnSalir = findViewById(R.id.btnSalir)
        btnSignUp = findViewById(R.id.btnSignUp)
        btnModificar = findViewById(R.id.btnModificar)
        btnEliminar = findViewById(R.id.btnEliminar)
        tvClave = findViewById(R.id.tvClave)
        tvConfirmarClave = findViewById(R.id.tvConfirmarClave)
        tvEstado = findViewById(R.id.tvEstado)

        if(tarea == "crear"){
            txtAnuncio.text = "Crear Cuenta"
            gridConsultar.visibility = GridLayout.GONE
            btnSignUp.visibility = Button.VISIBLE
            btnModificar.visibility = Button.GONE
            btnEliminar.visibility = Button.GONE
            tvEstado.visibility = TextView.GONE
            spEstado.visibility = Spinner.GONE
        }else{
            txtAnuncio.text = "Gestión de Cuenta"
            gridConsultar.visibility = View.VISIBLE
            tvEstado.visibility = TextView.VISIBLE
            spEstado.visibility = Spinner.VISIBLE
            txtDocumento.isEnabled = false
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
                    if(sujeto == "propio"){
                        gridConsultar.visibility = GridLayout.GONE
                        tvEstado.visibility = TextView.GONE
                        spEstado.visibility = Spinner.GONE
                    }
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

        btnFecha.setOnClickListener {
            calendar = Calendar.getInstance()
            seleccionarFecha()
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
                procesarCreacionCuenta()
            }

            builder.setNegativeButton("No") { dialog, which ->
                // Cancelar el flujo
                dialog.dismiss()
                Toast.makeText(
                    this@CrearCuenta,
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
                    this@CrearCuenta,
                    "Ingrese cédula para modificar",
                    Toast.LENGTH_SHORT,
                ).show()
                return@setOnClickListener
            }else{
                if (!verificarCampos(campos)) {
                    Toast.makeText(
                        this@CrearCuenta,
                        "Diligencie todos los datos",
                        Toast.LENGTH_SHORT,
                    ).show()
                    return@setOnClickListener
                }else{
                    val nombre = campos[0]
                    val documento = campos[3]
                    val edad = campos[4].toInt()
                    val direccion = campos[5]
                    val telefono = campos[6]
                    val correo = campos[7]
                    val sexo = campos[8]
                    val escolaridad = campos[9]
                    val grupo = campos[10]
                    val siGrupo = campos[11]
                    val comunidad = campos[12]
                    val estado = campos[13]

                    mDbRef = FirebaseDatabase.getInstance().getReference("userData")
                    """mDbRef.child(uidConsultado).setValue(
                        Usuario(nombre, documento, edad, direccion, telefono,
                            correo, sexo, escolaridad, siGrupo, grupo, comunidad, estado, uidConsultado))"""
                    Toast.makeText(
                        this@CrearCuenta,
                        "Usuario modificado exitosamente",
                        Toast.LENGTH_SHORT,
                    ).show()
                    finish()
                }
            }
        }

        btnEliminar.setOnClickListener{
            if(uidConsultado.isEmpty()){
                Toast.makeText(
                    this@CrearCuenta,
                    "Ingrese cédula para eliminar",
                    Toast.LENGTH_SHORT,
                ).show()
                return@setOnClickListener
            }else{
                mDbRef = FirebaseDatabase.getInstance().getReference("userData")
                mDbRef.child(uidConsultado).removeValue()
                Toast.makeText(
                    this@CrearCuenta,
                    "Usuario eliminado exitosamente",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

        if (sujeto == "propio"){
            mDbRef = FirebaseDatabase.getInstance().getReference("userData")
            val query = mDbRef.orderByChild("correo").equalTo(mAuth.currentUser?.email)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    println(snapshot)
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            uidConsultado = it.key.toString()
                            println(it)
                            val nombre = it.child("nombreCompleto").value.toString()
                            val documento = it.child("documento").value.toString()
                            val edad = it.child("edad").value.toString()
                            val direccion = it.child("direccion").value.toString()
                            val telefono = it.child("telefono").value.toString()
                            val correo = it.child("correo").value.toString()
                            val sexo = it.child("sexo").value.toString()
                            val escolaridad = it.child("escolaridad").value.toString()
                            val grupo = it.child("grupo").value.toString()
                            val grupoSi = it.child("grupoSi").value.toString()
                            val comunidad = it.child("comunidad").value.toString()
                            val estado = it.child("estado").value.toString()

                            txtNombre.setText(nombre)
                            txtDocumento.setText(documento)
                            txtEdad.setText(edad)
                            txtDireccion.setText(direccion)
                            txtTelefono.setText(telefono)
                            txtCorreo.setText(correo)
                            spSexo.setSelection((spSexo.adapter as ArrayAdapter<String>).getPosition(sexo))
                            spEscolaridad.setSelection((spEscolaridad.adapter as ArrayAdapter<String>).getPosition(escolaridad))
                            spGrupo.setSelection((spGrupo.adapter as ArrayAdapter<String>).getPosition(grupo))
                            spComunidad.setSelection((spComunidad.adapter as ArrayAdapter<String>).getPosition(comunidad))
                            txtGrupoEtnico.setText(grupoSi)
                            spEstado.setSelection((spEstado.adapter as ArrayAdapter<String>).getPosition(estado))
                        }
                        if(tarea == "modificar"){
                            btnModificar.visibility = Button.VISIBLE
                        }
                    } else {
                        Toast.makeText(
                            this@CrearCuenta,
                            "No se encontró un usuario con la cédula ingresada",
                            Toast.LENGTH_LONG,
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@CrearCuenta,
                        "Error al consultar la base de datos",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun seleccionarFecha(){
        DatePickerDialog(this, { _, year, month, day ->
            val fechaSeleccionada = Calendar.getInstance().apply { set(year, month, day) }

            seleccionFecha = fechaSeleccionada
            txtFecha.setText("$day-${month + 1}-$year")

            // Calcular edad
            val edad = Calendar.getInstance().get(Calendar.YEAR) - year
            txtEdad.setText(edad.toString())

            // Calcular grupo etario
            val grupoEtario = when {
                edad < 5 -> "Primera Infancia"
                edad in 6..11 -> "Infancia"
                edad in 12..18 -> "Adolescencia"
                edad in 19..26 -> "Juventud"
                edad in 27..59 -> "Adultez"
                else -> "Persona Mayor"
            }
            txtEtario.setText(grupoEtario)

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun procesarCreacionCuenta() {
        val campos = conseguirCampos()
        if (!verificarCampos(campos)) {
            Toast.makeText(
                this@CrearCuenta,
                "Diligencie todos los datos",
                Toast.LENGTH_SHORT,
            ).show()
            return
        } else {
            val nombre = campos[0]
            val tipoDocumento = campos[1]
            val documento = campos[2]
            val fechaNacimiento = campos[3]
            val edad = campos[4].toInt()
            val grupoEtario = campos[5]
            val direccion = campos[6]
            val sector = campos[7]
            val telefono = campos[8]
            val correo = campos[9]
            val sexo = campos[10]
            val identidad = campos[11]
            val orientacion = campos[12]
            val nacionalidad = campos[13]
            val escolaridad = campos[14]
            val grupoEtnico = campos[15]
            val discapacidad = campos[16]
            val estrato = campos[17]
            val comunidad = campos[18]
            val clave = campos[19]
            val confirmarClave = campos[20]
            val estado = campos[21]

            if (clave != confirmarClave) {
                Toast.makeText(
                    this@CrearCuenta,
                    "Las contraseñas no coinciden",
                    Toast.LENGTH_SHORT,
                ).show()
                return
            } else {
                signUp(nombre, tipoDocumento, documento, fechaNacimiento, edad,
                    grupoEtario, direccion, sector, telefono, correo, sexo,
                    identidad, orientacion, nacionalidad, escolaridad, grupoEtnico,
                    discapacidad, estrato, comunidad, clave, estado)
            }
        }
    }

    private fun signUp(
        nombre: String,
        tipoDocumento: String,
        documento: String,
        fechaNacimiento: String,
        edad: Int,
        grupoEtario: String,
        direccion: String,
        sector: String,
        telefono: String,
        correo: String,
        sexo: String,
        identidad: String,
        orientacion: String,
        nacionalidad: String,
        escolaridad: String,
        grupoEtnico: String,
        discapacidad: String,
        estrato: String,
        comunidad: String,
        clave: String,
        estado: String
    ) {

        // Buscar en RealtimeDatabase si ya existe un usuario con el mismo documento
        mDbRef = FirebaseDatabase.getInstance().getReference("userData")
        var query = mDbRef.orderByChild("documento").equalTo(documento)
        // Buscar si ese número de documento ya está registrado
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Buscar si ese número de documento está registrado, pero con otro tipo
                    query = mDbRef.orderByChild("tipoDocumento").equalTo(tipoDocumento)
                    query.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                Toast.makeText(
                                    this@CrearCuenta,
                                    "Ya existe un usuario con ese número y tipo de documento",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            } else {
                                // Buscar si ese correo está registrado en Usuarios
                                mDbRef = FirebaseDatabase.getInstance().getReference("userData")
                                query = mDbRef.orderByChild("correo").equalTo(correo)
                                query.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            Toast.makeText(
                                                this@CrearCuenta,
                                                "Ese correo ya está registrado",
                                                Toast.LENGTH_SHORT,
                                            ).show()
                                        } else {
                                            // Buscar si ese correo está registrado en Abogados
                                            mDbRef = FirebaseDatabase.getInstance().getReference("abogadoData")
                                            query = mDbRef.orderByChild("correo").equalTo(correo)
                                            query.addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                    if (snapshot.exists()) {
                                                        Toast.makeText(
                                                            this@CrearCuenta,
                                                            "Ese correo ya está registrado",
                                                            Toast.LENGTH_SHORT,
                                                        ).show()
                                                    } else {
                                                        // Buscar si ese correo está registrado en Administradores
                                                        mDbRef = FirebaseDatabase.getInstance().getReference("AdminData")
                                                        query = mDbRef.orderByChild("correo").equalTo(correo)
                                                        query.addListenerForSingleValueEvent(object : ValueEventListener {
                                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                                if (snapshot.exists()) {
                                                                    Toast.makeText(
                                                                        this@CrearCuenta,
                                                                        "Ese correo ya está registrado",
                                                                        Toast.LENGTH_SHORT,
                                                                    ).show()
                                                                } else {
                                                                    // Ese número no está registrado, se puede proceder a crear la cuenta
                                                                    mAuth.createUserWithEmailAndPassword(correo, clave)
                                                                        .addOnCompleteListener(this@CrearCuenta) { task ->
                                                                            if (task.isSuccessful) {
                                                                                addUserToDatabase(
                                                                                    nombre, tipoDocumento, documento, fechaNacimiento, edad.toString(),
                                                                                    grupoEtario, direccion, sector, telefono,
                                                                                    correo, sexo, identidad, orientacion, nacionalidad,
                                                                                    escolaridad, grupoEtnico, discapacidad, estrato, comunidad,
                                                                                    estado, mAuth.currentUser?.uid!!
                                                                                )

                                                                                println(mAuth.currentUser?.uid)
                                                                                if (usuario == "cliente") {
                                                                                    val intent = Intent(this@CrearCuenta, InterfazCliente::class.java)
                                                                                    finish()
                                                                                    startActivity(intent)
                                                                                } else {
                                                                                    Toast.makeText(
                                                                                        this@CrearCuenta,
                                                                                        "Usuario creado exitosamente",
                                                                                        Toast.LENGTH_SHORT,
                                                                                    ).show()
                                                                                }
                                                                            } else {
                                                                                Toast.makeText(
                                                                                    this@CrearCuenta,
                                                                                    "Ha ocurrido un error",
                                                                                    Toast.LENGTH_SHORT,
                                                                                ).show()
                                                                            }
                                                                        }
                                                                }
                                                            }

                                                            override fun onCancelled(error: DatabaseError) {
                                                                Toast.makeText(
                                                                    this@CrearCuenta,
                                                                    "Error al consultar la base de datos",
                                                                    Toast.LENGTH_SHORT,
                                                                ).show()
                                                            }
                                                        })
                                                    }
                                                }

                                                override fun onCancelled(error: DatabaseError) {
                                                    Toast.makeText(
                                                        this@CrearCuenta,
                                                        "Error al consultar la base de datos",
                                                        Toast.LENGTH_SHORT,
                                                    ).show()
                                                }
                                            })
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(
                                            this@CrearCuenta,
                                            "Error al consultar la base de datos",
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                    }
                                })
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@CrearCuenta,
                                "Error al consultar la base de datos",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    })
                } else {
                    // Ese número no está registrado, se puede proceder a crear la cuenta
                    mAuth.createUserWithEmailAndPassword(correo, clave)
                        .addOnCompleteListener(this@CrearCuenta) { task ->
                            if (task.isSuccessful) {
                                addUserToDatabase(
                                    nombre, tipoDocumento, documento, fechaNacimiento, edad.toString(),
                                    grupoEtario, direccion, sector, telefono,
                                    correo, sexo, identidad, orientacion, nacionalidad,
                                    escolaridad, grupoEtnico, discapacidad, estrato, comunidad,
                                    estado, mAuth.currentUser?.uid!!
                                )

                                println(mAuth.currentUser?.uid)
                                if (usuario == "cliente") {
                                    val intent = Intent(this@CrearCuenta, InterfazCliente::class.java)
                                    finish()
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        this@CrearCuenta,
                                        "Usuario creado exitosamente",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    this@CrearCuenta,
                                    "Ha ocurrido un error",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@CrearCuenta,
                    "Error al consultar la base de datos",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        })
    }

    private fun addUserToDatabase(
        nombre: String,
        tipoDocumento: String,
        documento: String,
        fechaNacimiento: String,
        edad: String,
        grupoEtario: String,
        direccion: String,
        sector: String,
        telefono: String,
        correo: String,
        sexo: String,
        identidad: String,
        orientacion: String,
        nacionalidad: String,
        escolaridad: String,
        grupoEtnico: String,
        discapacidad: String,
        estrato: String,
        comunidad: String,
        estado: String,
        uid: String
    ) {
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("userData").child(uid).setValue(
            Usuario(nombre, tipoDocumento, documento, fechaNacimiento,
                grupoEtario, edad.toInt(), direccion, sector, telefono,
                correo, sexo, identidad, orientacion, nacionalidad,
                escolaridad, grupoEtnico, discapacidad, estrato,
                comunidad, estado, uid))
        Toast.makeText(
            this@CrearCuenta,
            "Cuenta creada exitosamente",
            Toast.LENGTH_SHORT,
        ).show()
    }

    private fun conseguirCampos(): Array<String> {
        val nombre = txtNombre.text.toString()
        val tipoDocumento = spTipoDocumento.selectedItem.toString()
        val documento = txtDocumento.text.toString()
        val fechaNacimiento = txtFecha.text.toString()
        val edad = txtEdad.text.toString()
        val grupoEtario = txtEtario.text.toString()
        val direccion = txtDireccion.text.toString()
        val sector = spSector.selectedItem.toString()
        val telefono = txtTelefono.text.toString()
        val correo = txtCorreo.text.toString()
        val sexo = spSexo.selectedItem.toString()
        val identidad = spIdentidad.selectedItem.toString()
        val orientacion = spOrientacion.selectedItem.toString()
        val nacionalidad = spNacionalidad.selectedItem.toString()
        val escolaridad = spEscolaridad.selectedItem.toString()
        val grupoEtnico = spGrupo.selectedItem.toString()
        val discpacidad = spDiscapacidad.selectedItem.toString()
        val estrato = spEstrato.selectedItem.toString()
        val comunidad = spComunidad.selectedItem.toString()
        val clave = txtClave.text.toString()
        val confirmarClave = txtConfirmarClave.text.toString()
        val estado = spEstado.selectedItem.toString()

        return arrayOf(nombre, tipoDocumento, documento, fechaNacimiento, edad, grupoEtario, direccion, sector, telefono, correo, sexo, identidad, orientacion, nacionalidad, escolaridad, grupoEtnico, discpacidad, estrato, comunidad, clave, confirmarClave, estado)
    }

    private fun verificarCampos(campos: Array<String>): Boolean {
       //Verifica que todos los campos estén diligenciados
        if((tarea == "modificar") && (campos[0].isEmpty() || campos[2].isEmpty() || campos[6].isEmpty() || campos[8].isEmpty() || campos[9].isEmpty())){
            return false
        }else if (tarea == "crear" && (campos[0].isEmpty() || campos[2].isEmpty() || campos[6].isEmpty() || campos[8].isEmpty() || campos[9].isEmpty() || campos[19].isEmpty()|| campos[20].isEmpty())) {
            return false
        }
        return true
    }

    private fun disableFields(){
        txtNombre.isEnabled = false
        txtClave.isEnabled = false
        txtConfirmarClave.isEnabled = false
        btnTogglePassword.isEnabled = false
        btnToggleCheckPassword.isEnabled = false
        txtDocumento.isEnabled = false
        txtEdad.isEnabled = false
        txtDireccion.isEnabled = false
        txtTelefono.isEnabled = false
        txtCorreo.isEnabled = false
        spSexo.isEnabled = false
        spEscolaridad.isEnabled = false
        spGrupo.isEnabled = false
        spComunidad.isEnabled = false
        txtGrupoEtnico.isEnabled = false
        spEstado.isEnabled = false
    }

    private fun consultarPorCedula() {
        val cedula = txtConsultar.text.toString()
        if (cedula.isEmpty()) {
            Toast.makeText(
                this@CrearCuenta,
                "Ingrese cédula para consultar",
                Toast.LENGTH_SHORT,
            ).show()
            return
        }
        mDbRef = FirebaseDatabase.getInstance().getReference("userData")

        val query = mDbRef.orderByChild("documento").equalTo(cedula)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                println(snapshot)
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        uidConsultado = it.key.toString()
                        println(it)
                        val nombre = it.child("nombreCompleto").value.toString()
                        val documento = it.child("documento").value.toString()
                        val edad = it.child("edad").value.toString()
                        val direccion = it.child("direccion").value.toString()
                        val telefono = it.child("telefono").value.toString()
                        val correo = it.child("correo").value.toString()
                        val sexo = it.child("sexo").value.toString()
                        val escolaridad = it.child("escolaridad").value.toString()
                        val grupo = it.child("grupo").value.toString()
                        val grupoSi = it.child("grupoSi").value.toString()
                        val comunidad = it.child("comunidad").value.toString()
                        val estado = it.child("estado").value.toString()

                        txtNombre.setText(nombre)
                        txtDocumento.setText(documento)
                        txtEdad.setText(edad)
                        txtDireccion.setText(direccion)
                        txtTelefono.setText(telefono)
                        txtCorreo.setText(correo)
                        spSexo.setSelection((spSexo.adapter as ArrayAdapter<String>).getPosition(sexo))
                        spEscolaridad.setSelection((spEscolaridad.adapter as ArrayAdapter<String>).getPosition(escolaridad))
                        spGrupo.setSelection((spGrupo.adapter as ArrayAdapter<String>).getPosition(grupo))
                        spComunidad.setSelection((spComunidad.adapter as ArrayAdapter<String>).getPosition(comunidad))
                        txtGrupoEtnico.setText(grupoSi)
                        spEstado.setSelection((spEstado.adapter as ArrayAdapter<String>).getPosition(estado))
                    }
                    if(tarea == "modificar"){
                        btnModificar.visibility = Button.VISIBLE
                    }else if(tarea == "eliminar"){
                        btnEliminar.visibility = Button.VISIBLE
                    }
                } else {
                    Toast.makeText(
                        this@CrearCuenta,
                        "No se encontró un usuario con la cédula ingresada",
                        Toast.LENGTH_LONG,
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@CrearCuenta,
                    "Error al consultar la base de datos",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        })

    }
}