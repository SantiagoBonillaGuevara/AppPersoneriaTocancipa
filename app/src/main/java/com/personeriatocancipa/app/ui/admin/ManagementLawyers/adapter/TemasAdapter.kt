package com.personeriatocancipa.app.ui.admin.ManagementLawyers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.personeriatocancipa.app.R

class TemasAdapter(var temas: List<String>) : RecyclerView.Adapter<TemasViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemasViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TemasViewHolder(layoutInflater.inflate(R.layout.item_tema, parent, false))
    }

    override fun getItemCount() = temas.size

    override fun onBindViewHolder(holder: TemasViewHolder, position: Int) {
        holder.bind(temas[position])
    }

}