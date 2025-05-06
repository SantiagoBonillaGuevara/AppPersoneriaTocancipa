package com.personeriatocancipa.app.ui.lawyer.dates.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.ItemDateBinding
import com.personeriatocancipa.app.domain.model.Date

class LawyerDatesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemDateBinding.bind(view)
    @SuppressLint("SetTextI18n")
    fun bind(date: Date, onClick: (Date, String) -> Unit) {
        binding.tvTema.text = date.tema
        binding.tvFechaHora.text = date.fecha + " - " + date.hora
        binding.tvCorreo.text = date.correoCliente
        binding.tvDescripcion.text = "Descripcion: ${date.descripcion}"
        binding.ivEdit.isVisible = false

        val adapter = ArrayAdapter.createFromResource(itemView.context, R.array.opcionesEstado, android.R.layout.simple_dropdown_item_1line)
        binding.spEstado.setText("", false)
        binding.spEstado.setAdapter(adapter)
        binding.spEstado.setText(date.estado, false)
        binding.spEstado.setTextColor(when(date.estado) {
            "Pendiente" -> itemView.context.getColor(R.color.azul)
            "Asistió" -> itemView.context.getColor(R.color.verde)
            "No Asistió" -> itemView.context.getColor(R.color.amarillo)
            "Cancelada" -> itemView.context.getColor(R.color.gray)
            else -> itemView.context.getColor(R.color.black)
        })

        binding.spEstado.setOnItemClickListener { _, _, _, _ -> onClick(date, binding.spEstado.text.toString()) }
    }

}