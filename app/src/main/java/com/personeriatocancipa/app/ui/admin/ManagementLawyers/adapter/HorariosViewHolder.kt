package com.personeriatocancipa.app.ui.admin.ManagementLawyers.adapter

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
        Log.i( "horarioToList", "aca llego el horario al viewholder: $horario")
        binding.tvDia.text = horario.first
        binding.tvRango.text = "${horario.second.inicio} - ${horario.second.fin}"
    }
}