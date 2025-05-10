package com.personeriatocancipa.app.ui.user.createDate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.domain.model.Lawyer

class LawyersAvailableAdapter(var lawyers: List<Lawyer>, private val onEditClicked: (Lawyer) -> Unit) : RecyclerView.Adapter<LawyersAvailableViewHolder>(
) {

    private var selectedPosition = -1
    var onItemClicked: ((Lawyer) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawyersAvailableViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        return LawyersAvailableViewHolder(layoutInflater.inflate(R.layout.item_lawyer, parent, false))
    }

    override fun getItemCount()= lawyers.size

    override fun onBindViewHolder(holder: LawyersAvailableViewHolder, position: Int) {
        holder.bind(lawyers[position], onEditClicked) { selectedLawyer ->
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
            onItemClicked?.invoke(selectedLawyer)
        }

        // Cambiar el color de fondo dependiendo si el item está seleccionado o no
        if (position == selectedPosition) {
            holder.binding.root.backgroundTintList = ContextCompat.getColorStateList(holder.itemView.context, R.color.sky_blue)
        } else {
            holder.binding.root.backgroundTintList = ContextCompat.getColorStateList(holder.itemView.context, android.R.color.white)
        }
    }
}