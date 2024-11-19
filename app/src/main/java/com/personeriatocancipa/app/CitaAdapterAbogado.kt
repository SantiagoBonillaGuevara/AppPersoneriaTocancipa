package com.personeriatocancipa.app

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CitaAdapterAbogado(private val citas: List<Cita>) :
    RecyclerView.Adapter<CitaAdapterAbogado.CitaViewHolder>() {

    inner class CitaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTema: TextView = view.findViewById(R.id.tvTema)
        val tvId: TextView = view.findViewById(R.id.tvID)
        val tvFechaHora: TextView = view.findViewById(R.id.tvFechaHora)
        val tvCorreoCliente: TextView = view.findViewById(R.id.tvCorreoCliente)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cita_abogado, parent, false)
        return CitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = citas[position]

        holder.tvTema.text = applyBoldStyle("Tema: ", cita.tema.toString())
        holder.tvId.text = applyBoldStyle("ID: ", cita.id.toString())
        holder.tvFechaHora.text = applyBoldStyle("Fecha y hora: ", "${cita.fecha} a las ${cita.hora}")
        holder.tvCorreoCliente.text = applyBoldStyle("Correo Cliente: ", cita.correoCliente.toString())
        holder.tvDescripcion.text = applyBoldStyle("Descripción: ", cita.descripcion.toString())
        holder.tvEstado.text = applyBoldStyle("Estado: ", cita.estado.toString())
    }

    override fun getItemCount(): Int = citas.size

    private fun applyBoldStyle(label: String, value: String): SpannableString {
        val fullText = "$label$value"
        val spannable = SpannableString(fullText)
        spannable.setSpan(
            StyleSpan(Typeface.BOLD), 0, label.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }
}