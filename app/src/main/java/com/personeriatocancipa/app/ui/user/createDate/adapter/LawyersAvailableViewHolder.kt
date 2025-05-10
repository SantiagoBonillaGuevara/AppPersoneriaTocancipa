package com.personeriatocancipa.app.ui.user.createDate.adapter

import android.annotation.SuppressLint
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.personeriatocancipa.app.databinding.ItemLawyerBinding
import com.personeriatocancipa.app.domain.model.Horario
import com.personeriatocancipa.app.domain.model.HorarioDia
import com.personeriatocancipa.app.domain.model.Lawyer
import com.personeriatocancipa.app.ui.admin.managementLawyers.adapter.HorariosAdapter
import com.personeriatocancipa.app.ui.admin.managementLawyers.adapter.TemasAdapter

class LawyersAvailableViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemLawyerBinding.bind(view)
    @SuppressLint("SetTextI18n")
    fun bind(lawyer: Lawyer, onEditClicked: (Lawyer) -> Unit, onClick: (Lawyer) -> Unit) {
        binding.tvNombre.text = lawyer.nombreCompleto
        binding.tvCorreo.text = "Correo: "+lawyer.correo
        binding.tvDocumento.text = "Documento: "+lawyer.documento
        binding.tvCargo.text = "Cargo: "+ lawyer.cargo
        binding.switchEstado.isVisible = false
        binding.ivEdit.isVisible = false
        binding.rvTemas.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = TemasAdapter(lawyer.temas ?: emptyList())
        }
        binding.rvHorarios.apply {
            layoutManager = LinearLayoutManager(context)
            val horariosList = horarioToList(lawyer.horario!!)
            adapter = HorariosAdapter(horariosList)
        }
        binding.root.setOnClickListener{
            onClick(lawyer)
            onEditClicked(lawyer)
        }
    }

    fun horarioToList(horario: Horario): List<Pair<String, HorarioDia>> {
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