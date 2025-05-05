package com.personeriatocancipa.app.ui.admin.ManagementLawyers.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.personeriatocancipa.app.databinding.ItemTemaBinding

class TemasViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemTemaBinding.bind(view)
    fun bind(tema: String) {
        binding.tvTema.text = tema
    }
}