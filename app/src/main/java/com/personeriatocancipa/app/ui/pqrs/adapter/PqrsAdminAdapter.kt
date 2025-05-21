package com.personeriatocancipa.app.ui.pqrs.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.personeriatocancipa.app.databinding.ItemPqrsAdminBinding
import com.personeriatocancipa.app.domain.model.Pqrs
import java.text.SimpleDateFormat
import java.util.*

class PqrsAdminAdapter(
    private val onClick: (pqrsId: String) -> Unit
) : ListAdapter<Pqrs, PqrsAdminAdapter.PqrsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PqrsViewHolder {
        val binding = ItemPqrsAdminBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PqrsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PqrsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PqrsViewHolder(private val b: ItemPqrsAdminBinding) :
        RecyclerView.ViewHolder(b.root), View.OnClickListener {

        private var current: Pqrs? = null

        init {
            b.root.setOnClickListener(this)
        }

        fun bind(item: Pqrs) {
            current = item
            b.tvType.text = item.type
            b.tvTitle.text = item.title
            b.tvStatus.text = item.status

            // Formatea la fecha (suponiendo que item.date es un Long timestamp)
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            b.tvDate.text = sdf.format(Date(item.date))

            // Mostrar el identificador del usuario (o su email/nombre si ya está cargado)
            b.tvUser.text = item.userId
        }

        override fun onClick(v: View?) {
            current?.let { onClick(it.id) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Pqrs>() {
        override fun areItemsTheSame(old: Pqrs, new: Pqrs) = old.id == new.id
        override fun areContentsTheSame(old: Pqrs, new: Pqrs) = old == new
    }
}
