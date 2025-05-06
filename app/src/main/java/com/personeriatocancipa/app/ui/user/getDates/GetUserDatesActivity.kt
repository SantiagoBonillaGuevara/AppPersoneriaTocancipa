package com.personeriatocancipa.app.ui.user.getDates

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.personeriatocancipa.app.data.repository.FirebaseDateRepository
import com.personeriatocancipa.app.databinding.ActivityGetDatesBinding
import com.personeriatocancipa.app.domain.model.Date
import com.personeriatocancipa.app.domain.usecase.GetDatesUseCase
import com.personeriatocancipa.app.ui.user.getDates.adapter.UserDatesAdapter
import kotlinx.coroutines.launch

class GetUserDatesActivity: AppCompatActivity() {

    private lateinit var binding : ActivityGetDatesBinding
    private val getUserDatesUseCase = GetDatesUseCase(FirebaseDateRepository())
    private lateinit var correo: String
    private lateinit var dates: List<Date>
    lateinit var adapter : UserDatesAdapter
    val meses = listOf("", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetDatesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        correo = intent.getStringExtra("user") ?: ""
        getDates()
        initComponents()
    }

    private fun initUI() {
        adapter = UserDatesAdapter(dates)
        binding.rvDates.layoutManager = LinearLayoutManager(this)
        binding.rvDates.adapter = adapter
        val mesesFiltrados = dates.mapNotNull { date -> date.fecha?.split("/")?.getOrNull(1)?.toIntOrNull() }
            .distinct().sorted().map { meses[it] }
        val añosFiltrados = dates.mapNotNull { it.fecha?.split("/")?.getOrNull(2) }
            .distinct().sorted()
        binding.spMes.setAdapter(ArrayAdapter(this@GetUserDatesActivity, R.layout.simple_dropdown_item_1line, mesesFiltrados))
        binding.spYear.setAdapter(ArrayAdapter(this@GetUserDatesActivity, android.R.layout.simple_dropdown_item_1line, añosFiltrados))
    }

    private fun initComponents() {
        binding.spMes.setOnItemClickListener { _, _, _, _ -> aplicarFiltro() }
        binding.spYear.setOnItemClickListener { _, _, _, _ -> aplicarFiltro() }
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun getDates() {
        lifecycleScope.launch {
            val result = getUserDatesUseCase.execute("correoCliente", correo)
            result.onSuccess {
                dates = it
                initUI()
            }
        }
    }

    private fun aplicarFiltro() {
        val mesSeleccionado = binding.spMes.text.toString()
        val yearSeleccionado = binding.spYear.text.toString()
        val mesIndex = if (mesSeleccionado.isNotEmpty()) {
            meses.indexOf(mesSeleccionado).toString().padStart(2, '0')
        } else null
        val citasFiltradas = dates.filter { date ->
            val partes = date.fecha?.split("/") ?: return@filter false
            val cumpleMes = mesIndex?.let { partes[1] == it } ?: true
            val cumpleYear = if (yearSeleccionado.isNotEmpty()) partes[2] == yearSeleccionado else true
            cumpleMes && cumpleYear
        }
        adapter.updateList(citasFiltradas)
    }
}