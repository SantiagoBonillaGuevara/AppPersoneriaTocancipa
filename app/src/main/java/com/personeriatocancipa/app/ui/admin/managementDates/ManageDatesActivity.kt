package com.personeriatocancipa.app.ui.admin.managementDates

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.personeriatocancipa.app.data.repository.FirebaseDateRepository
import com.personeriatocancipa.app.databinding.ActivityGetDatesBinding
import com.personeriatocancipa.app.domain.model.Date
import com.personeriatocancipa.app.domain.usecase.GetDatesUseCase
import com.personeriatocancipa.app.ui.admin.managementDates.adapter.DatesAdapter
import com.personeriatocancipa.app.ui.admin.managementDates.editDate.EditDateActivity
import kotlinx.coroutines.launch

class ManageDatesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGetDatesBinding
    private val getDatesUseCase = GetDatesUseCase(FirebaseDateRepository())
    private lateinit var dates: List<Date>
    private lateinit var adapter : DatesAdapter
    val meses = listOf("", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetDatesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDates()
        initComponents()
    }

    private fun initUI() {
        adapter = DatesAdapter(dates, onEditClicked = { date -> editDate(date) })
        binding.rvDates.layoutManager = LinearLayoutManager(this)
        binding.rvDates.adapter = adapter
        val mesesFiltrados = dates.mapNotNull { date -> date.fecha?.split("/")?.getOrNull(1)?.toIntOrNull() }
            .distinct().sorted().map { meses[it] }
        val añosFiltrados = dates.mapNotNull { it.fecha?.split("/")?.getOrNull(2) }
            .distinct().sorted()
        binding.spMes.setAdapter(ArrayAdapter(this@ManageDatesActivity, R.layout.simple_dropdown_item_1line, mesesFiltrados))
        binding.spYear.setAdapter(ArrayAdapter(this@ManageDatesActivity, android.R.layout.simple_dropdown_item_1line, añosFiltrados))
    }

    private fun initComponents() {
        binding.spMes.setOnItemClickListener { _, _, _, _ -> aplicarFiltro() }
        binding.spYear.setOnItemClickListener { _, _, _, _ -> aplicarFiltro() }
        binding.ivBack.setOnClickListener {
            finish()
        }
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) getDates()
        }
    }

    private fun getDates() {
        lifecycleScope.launch {
            val result = getDatesUseCase.execute("", "")
            result.onSuccess {
                Log.i( "Dates", it.toString())
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

    private fun editDate(date: Date) {
        val intent = Intent(this, EditDateActivity::class.java)
        intent.putExtra("id", date.id.toString())
        launcher.launch(intent)
    }
}