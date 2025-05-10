package com.personeriatocancipa.app.ui.admin.managementDates.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.ItemDateBinding
import com.personeriatocancipa.app.domain.model.Date

class DatesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemDateBinding.bind(view)
    @SuppressLint("SetTextI18n")
    fun bind(date: Date, onEditClicked: (Date) -> Unit) {
        binding.tvTema.text = date.tema
        binding.tvFechaHora.text = date.fecha + " - " + date.hora
        binding.tvCorreo.text = date.correoCliente
        binding.tvDescripcion.text = "Descripcion: ${date.descripcion}"

        binding.ivEdit.setOnClickListener { onEditClicked(date) }

        binding.spEstadoContainer.endIconMode = TextInputLayout.END_ICON_NONE

        binding.spEstado.setText(date.estado, false)
        binding.spEstado.setTextColor(when(date.estado) {
            "Pendiente" -> itemView.context.getColor(R.color.azul)
            "Asistió" -> itemView.context.getColor(R.color.verde)
            "No Asistió" -> itemView.context.getColor(R.color.amarillo)
            "Cancelada" -> itemView.context.getColor(R.color.gray)
            else -> itemView.context.getColor(R.color.black)
        })
    }
}