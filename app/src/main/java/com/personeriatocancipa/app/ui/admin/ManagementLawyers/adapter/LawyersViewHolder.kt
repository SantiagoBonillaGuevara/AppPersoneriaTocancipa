package com.personeriatocancipa.app.ui.admin.ManagementLawyers.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.personeriatocancipa.app.databinding.ItemLawyerBinding
import com.personeriatocancipa.app.domain.model.Horario
import com.personeriatocancipa.app.domain.model.HorarioDia
import com.personeriatocancipa.app.domain.model.Lawyer

class LawyersViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemLawyerBinding.bind(view)
    @SuppressLint("SetTextI18n")
    fun bind(lawyer: Lawyer, onLawyerChanged: (Lawyer, Boolean) -> Unit, onEditClicked: (Lawyer) -> Unit) {

        binding.tvNombre.text = lawyer.nombreCompleto
        binding.tvCorreo.text = "Correo: "+lawyer.correo
        binding.tvDocumento.text = "Documento: "+lawyer.documento
        binding.tvCargo.text = "Cargo: "+ lawyer.cargo

        binding.ivEdit.setOnClickListener {
            onEditClicked(lawyer)
        }

        binding.rvTemas.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = TemasAdapter(lawyer.temas ?: emptyList())
        }

        binding.rvHorarios.apply {
            layoutManager = LinearLayoutManager(context)
            val horariosList = horarioToList(lawyer.horario!!)
            Log.i("HorariosAdapter", "Tamaño lista horarios: ${horariosList.size} de ${lawyer.horario}")
            adapter = HorariosAdapter(horariosList)
        }

        binding.switchEstado.setOnCheckedChangeListener(null)
        binding.switchEstado.isChecked = lawyer.estado == "Activo"
        binding.switchEstado.setOnCheckedChangeListener { _, isChecked ->
            onLawyerChanged(lawyer, isChecked)
        }
    }

    fun horarioToList(horario: Horario): List<Pair<String, HorarioDia>> {
        Log.i("horarioToList", "aca llego el horario: $horario")
        return listOf(
            "Lunes" to (horario.Lunes ?: HorarioDia()),
            "Martes" to (horario.Martes ?: HorarioDia()),
            "Miércoles" to (horario.Miércoles ?: HorarioDia()),
            "Jueves" to (horario.Jueves ?: HorarioDia()),
            "Viernes" to (horario.Viernes ?: HorarioDia())
        ).filter { (_, dia) ->
            !dia.inicio.isNullOrBlank() && !dia.fin.isNullOrBlank()
        }
    }
}