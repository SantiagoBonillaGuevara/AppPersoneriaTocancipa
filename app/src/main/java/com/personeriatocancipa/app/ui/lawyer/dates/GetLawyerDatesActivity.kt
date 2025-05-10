package com.personeriatocancipa.app.ui.lawyer.dates

import android.R
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.personeriatocancipa.app.data.repository.FirebaseDateRepository
import com.personeriatocancipa.app.databinding.ActivityGetDatesBinding
import com.personeriatocancipa.app.domain.model.Date
import com.personeriatocancipa.app.domain.usecase.CreateDateUseCase
import com.personeriatocancipa.app.domain.usecase.GetDatesUseCase
import com.personeriatocancipa.app.ui.lawyer.dates.adapter.LawyerDatesAdapter
import kotlinx.coroutines.launch

class GetLawyerDatesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGetDatesBinding
    val modifyUseCase = CreateDateUseCase(FirebaseDateRepository())
    private val getLawyerDatesUseCase = GetDatesUseCase(FirebaseDateRepository())
    private lateinit var correo: String
    private lateinit var dates: List<Date>
    lateinit var adapter : LawyerDatesAdapter
    val meses = listOf("", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetDatesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        correo = intent.getStringExtra("lawyer") ?: ""
        getDates()
        initComponents()
    }

    private fun initUI() {
        adapter = LawyerDatesAdapter(dates, onClick = { date, newState -> modify(date, newState) })
        binding.rvDates.layoutManager = LinearLayoutManager(this)
        binding.rvDates.adapter = adapter
        val mesesFiltrados = dates.mapNotNull { date -> date.fecha?.split("/")?.getOrNull(1)?.toIntOrNull() }
            .distinct().sorted().map { meses[it] }
        val añosFiltrados = dates.mapNotNull { it.fecha?.split("/")?.getOrNull(2) }
            .distinct().sorted()
        binding.spMes.setAdapter(ArrayAdapter(this@GetLawyerDatesActivity, R.layout.simple_dropdown_item_1line, mesesFiltrados))
        binding.spYear.setAdapter(ArrayAdapter(this@GetLawyerDatesActivity, android.R.layout.simple_dropdown_item_1line, añosFiltrados))
    }

    private fun initComponents() {
        binding.spMes.setOnItemClickListener { _, _, _, _ -> aplicarFiltro() }
        binding.spYear.setOnItemClickListener { _, _, _, _ -> aplicarFiltro() }
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun modify(date: Date, newState: String) {
        if(date.estado == newState) return
        date.estado= newState
        lifecycleScope.launch {
            val result = modifyUseCase.execute(date.id!!, date)
            result.onSuccess {
                Log.d("TAG", "modify: $it")
                Toast.makeText(this@GetLawyerDatesActivity, "Cita modificada", Toast.LENGTH_SHORT).show()
                adapter.updateList(dates)
                binding.spYear.setText("",false)
                binding.spMes.setText("",false)
            }.onFailure {
                Log.d("TAG", "modify: ${it.message}")
                Toast.makeText(this@GetLawyerDatesActivity, "Error al modificar la cita", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDates() {
        lifecycleScope.launch {
            val result = getLawyerDatesUseCase.execute("correoAbogado", correo)
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