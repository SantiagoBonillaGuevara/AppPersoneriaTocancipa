package com.personeriatocancipa.app.ui.admin.ManagementLawyers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.domain.model.HorarioDia

class HorariosAdapter(private val horarios: List<Pair<String, HorarioDia>>):
RecyclerView.Adapter<HorariosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorariosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HorariosViewHolder(layoutInflater.inflate(R.layout.item_horario, parent, false))
    }

    override fun getItemCount() = horarios.size

    override fun onBindViewHolder(holder: HorariosViewHolder, position: Int) {
        holder.bind(horarios[position])
    }
}