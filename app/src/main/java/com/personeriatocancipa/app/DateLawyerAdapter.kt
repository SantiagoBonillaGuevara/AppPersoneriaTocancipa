package com.personeriatocancipa.app

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.graphics.drawable.GradientDrawable
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class DateLawyerAdapter(private var dates: List<Date>) :
    RecyclerView.Adapter<DateLawyerAdapter.CitaViewHolder>() {

    // Método para actualizar las citas
    fun actualizarCitas(citasActualizadas: List<Date>) {
        dates = citasActualizadas
        notifyDataSetChanged()  // Notifica que los datos han cambiado para actualizar el RecyclerView
    }

    inner class CitaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTema: TextView = view.findViewById(R.id.tvTema)
        val tvId: TextView = view.findViewById(R.id.tvID)
        val tvFechaHora: TextView = view.findViewById(R.id.tvFechaHora)
        val tvCorreoCliente: TextView = view.findViewById(R.id.tvCorreoCliente)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val spEstado: Spinner = view.findViewById(R.id.spEstado)
        val itemContainer: View = view.findViewById(R.id.itemContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cita_abogado, parent, false)
        return CitaViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = dates[position]

        val mDbRef = FirebaseDatabase.getInstance().getReference("userData")
        var nombreCliente = ""
        val query = mDbRef.orderByChild("correo").equalTo(cita.correoCliente)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    nombreCliente = snap.child("nombreCompleto").value.toString()
                    holder.tvCorreoCliente.text = applyBoldStyle("Nombre Cliente: ", nombreCliente)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CitaAdapterAbogado", "Error al obtener el nombre del cliente: ${error.message}")
            }
        })

        holder.tvTema.text = applyBoldStyle("Tema: ", cita.tema.toString())
        holder.tvId.text = applyBoldStyle("ID: ", cita.id.toString())
        holder.tvFechaHora.text = applyBoldStyle("Fecha y hora: ", "${cita.fecha} a las ${cita.hora}")
        holder.tvDescripcion.text = applyBoldStyle("Descripción: ", cita.descripcion.toString())

        // Configura el adaptador del Spinner con estilo
        val estados = holder.itemView.context.resources.getStringArray(R.array.opcionesEstado)
        val adapter = ArrayAdapter.createFromResource(
            holder.itemView.context,
            R.array.opcionesEstado,
            R.drawable.spinner_itemestadocita
        )
        adapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
        holder.spEstado.adapter = adapter

        // Selecciona el estado actual
        val estadoIndex = estados.indexOf(cita.estado)
        if (estadoIndex >= 0) {
            holder.spEstado.setSelection(estadoIndex)
        }

        // **Actualizar el contorno inicial según el estado**
        actualizarContorno(holder.itemContainer, cita.estado, holder)
        actualizarContornoSpinner(holder.spEstado, cita.estado, holder)

        // Listener para el cambio de estado
        holder.spEstado.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val nuevoEstado = estados[position]

                // Cambiar dinámicamente el color del texto seleccionado
                val textView = holder.spEstado.selectedView as? TextView
                textView?.setTextColor(getColorForEstado(nuevoEstado, holder.itemView.context))

                // Verificar si el estado realmente ha cambiado
                if (cita.estado != nuevoEstado) {
                    cita.estado = nuevoEstado
                    actualizarEstadoEnFirebase(cita)

                    // Actualizar el contorno del ítem
                    actualizarContorno(holder.itemContainer, nuevoEstado, holder)

                    // Mostrar el mensaje de estado actualizado
                    Toast.makeText(
                        holder.itemView.context,
                        "Estado actualizado a: $nuevoEstado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

    }

    override fun getItemCount(): Int = dates.size

    private fun applyBoldStyle(label: String, value: String): SpannableString {
        val fullText = "$label$value"
        val spannable = SpannableString(fullText)
        spannable.setSpan(
            StyleSpan(Typeface.BOLD), 0, label.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    private fun actualizarEstadoEnFirebase(date: Date) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("citas")
        databaseReference.child(date.id.toString()).child("estado").setValue(date.estado)
    }

    private fun getColorForEstado(estado: String, context: android.content.Context): Int {
        return when (estado.toLowerCase()) {
            "cancelada" -> context.getColor(R.color.Rojo)
            "asistió" -> context.getColor(R.color.verde)
            "no asistió" -> context.getColor(R.color.grisClaro)
            "pendiente" -> context.getColor(R.color.azul)
            else -> context.getColor(android.R.color.black) // Color por defecto
        }
    }

    private fun actualizarContorno(itemContainer: View, estado: String?, holder: CitaViewHolder) {
        val color = getColorForEstado(estado ?: "", holder.itemView.context)
        val background = itemContainer.background
        if (background is GradientDrawable) {
            background.setStroke(7, color) // Cambiar grosor y color del contorno
        }
    }

    private fun actualizarContornoSpinner(spinner: Spinner, estado: String?, holder: CitaViewHolder) {
        val color = getColorForEstado(estado ?: "", holder.itemView.context)
        val background = spinner.background
        if (background is GradientDrawable) {
            background.setStroke(5, color) // Cambiar grosor y color del contorno del Spinner
        }
    }

}
