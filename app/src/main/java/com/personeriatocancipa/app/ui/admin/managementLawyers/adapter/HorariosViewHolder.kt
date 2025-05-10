package com.personeriatocancipa.app.ui.admin.managementLawyers.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.personeriatocancipa.app.databinding.ItemHorarioBinding
import com.personeriatocancipa.app.domain.model.HorarioDia

class HorariosViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemHorarioBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun bind(horario: Pair<String, HorarioDia>) {
        binding.tvDia.text = horario.first
        binding.tvRango.text = "${horario.second.inicio} - ${horario.second.fin}"
    }
}