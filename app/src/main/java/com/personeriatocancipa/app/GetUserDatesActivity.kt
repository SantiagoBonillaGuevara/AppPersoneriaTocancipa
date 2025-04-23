package com.personeriatocancipa.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.Calendar
import java.util.Locale

class GetUserDatesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var citasList: MutableList<Date>
    private lateinit var adapter: DateUserAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var btnSalir: Button
    private lateinit var spinnerAnioFiltro: Spinner
    private lateinit var spinnerMesFiltro: Spinner

    private var mesesList: MutableList<String> = mutableListOf()
    private var aniosList: MutableList<String> = mutableListOf()
    private var mesesPorAnio: MutableMap<String, MutableList<String>> = mutableMapOf()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_user_dates)

        recyclerView = findViewById(R.id.recyclerViewCitaCliente)
        btnSalir = findViewById(R.id.btnSalir)
        recyclerView.layoutManager = LinearLayoutManager(this)
        spinnerAnioFiltro = findViewById(R.id.spinnerAnioFiltro)
        spinnerMesFiltro = findViewById(R.id.spinnerMesFiltro)

        citasList = mutableListOf()
        adapter = DateUserAdapter(citasList)
        recyclerView.adapter = adapter

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("citas")

        cargarCitas()

        // Configuración del Spinner de Mes
        val mesAdapter = ArrayAdapter(this, R.drawable.spinner_item, mesesList)
        mesAdapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
        spinnerMesFiltro.adapter = mesAdapter

        // Configuración del Spinner de Año
        val anioAdapter = ArrayAdapter(this, R.drawable.spinner_item, aniosList)
        anioAdapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
        spinnerAnioFiltro.adapter = anioAdapter

        // Listener para el filtro de año
        spinnerAnioFiltro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val anioSeleccionado = parentView.getItemAtPosition(position)?.toString() ?: ""
                actualizarMesesPorAnio(anioSeleccionado)
                filtrarCitasPorMesYAnio(spinnerMesFiltro.selectedItem?.toString() ?: "", anioSeleccionado)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        // Listener para el filtro de mes
        spinnerMesFiltro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val mesSeleccionado = parentView.getItemAtPosition(position)?.toString() ?: ""
                filtrarCitasPorMesYAnio(mesSeleccionado, spinnerAnioFiltro.selectedItem?.toString() ?: "")
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        btnSalir.setOnClickListener {
            finish()
        }
    }

    private fun cargarCitas() {
        val userEmail = auth.currentUser?.email ?: ""
        Log.d("CorreoUsuario", "Correo autenticado: $userEmail")

        databaseReference.orderByChild("correoCliente").equalTo(userEmail)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    citasList.clear()
                    mesesList.clear()
                    aniosList.clear()
                    mesesPorAnio.clear()

                    for (citaSnapshot in snapshot.children) {
                        val date = citaSnapshot.getValue(Date::class.java)
                        if (date != null && !date.fecha.isNullOrEmpty()) {
                            val mes = obtenerMesDeFecha(date.fecha!!)
                            val anio = obtenerAnioDeFecha(date.fecha!!)

                            // Agregar meses y años únicos a las listas
                            if (!aniosList.contains(anio)) {
                                aniosList.add(anio)
                            }

                            if (!mesesPorAnio.containsKey(anio)) {
                                mesesPorAnio[anio] = mutableListOf()
                            }

                            if (!mesesPorAnio[anio]!!.contains(mes)) {
                                mesesPorAnio[anio]!!.add(mes)
                            }

                            citasList.add(date)
                        } else {
                            Log.d("ConsultarCitas", "Cita sin fecha válida: ${citaSnapshot.key}")
                        }
                    }

                    // Ordenar las listas
                    aniosList.sort()
                    mesesList.sort()

                    // Actualizar adaptadores de Spinner
                    (spinnerAnioFiltro.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                    actualizarMesesPorAnio(aniosList.firstOrNull() ?: "")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ConsultarCitas", "Error al cargar citas: ${error.message}")
                    Toast.makeText(this@GetUserDatesActivity, "Error al cargar citas", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun actualizarMesesPorAnio(anio: String) {
        mesesList.clear()
        if (mesesPorAnio.containsKey(anio)) {
            mesesList.addAll(mesesPorAnio[anio]!!)
            mesesList.sort()
        }

        // Notificar al adaptador que los datos cambiaron
        (spinnerMesFiltro.adapter as ArrayAdapter<String>).notifyDataSetChanged()

        // Asegurarse de que haya elementos en mesesList antes de llamar a setSelection
        if (mesesList.isNotEmpty()) {
            spinnerMesFiltro.setSelection(0) // Seleccionar el primer mes disponible
        } else {
            Log.d("ActualizarMeses", "La lista de meses está vacía para el año $anio")
        }
    }



    private fun obtenerMesDeFecha(fecha: String): String {
        val parts = fecha.split("-")
        return if (parts.size >= 2) {
            val mesIndex = parts[1].toIntOrNull() ?: 0
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.MONTH, mesIndex - 1)
            calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) ?: ""
        } else ""
    }

    private fun obtenerAnioDeFecha(fecha: String): String {
        val parts = fecha.split("-")
        return if (parts.size >= 3) parts[2] else ""
    }

    private fun filtrarCitasPorMesYAnio(mes: String, anio: String) {
        val citasFiltradas = citasList.filter { cita ->
            val mesCita = obtenerMesDeFecha(cita.fecha ?: "")
            val anioCita = obtenerAnioDeFecha(cita.fecha ?: "")
            (mes.isEmpty() || mesCita == mes) && (anio.isEmpty() || anioCita == anio)
        }
        adapter.actualizarCitas(citasFiltradas)
    }
}
