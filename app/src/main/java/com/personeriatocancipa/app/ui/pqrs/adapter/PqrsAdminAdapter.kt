// app/src/main/java/com/personeriatocancipa/app/ui/pqrs/adapter/PqrsAdminAdapter.kt
package com.personeriatocancipa.app.ui.pqrs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.personeriatocancipa.app.databinding.ItemPqrsAdminBinding
import com.personeriatocancipa.app.domain.model.PqrsUiModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PqrsAdminAdapter(
    private val onClick: (PqrsUiModel) -> Unit
) : ListAdapter<PqrsUiModel, PqrsAdminAdapter.Holder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemPqrsAdminBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    class Holder(private val b: ItemPqrsAdminBinding)
        : RecyclerView.ViewHolder(b.root) {
        private val fmt = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        fun bind(item: PqrsUiModel, onClick: (PqrsUiModel) -> Unit) {
            b.tvUserName.text    = item.userName
            b.tvType.text        = item.type
            b.tvTitle.text       = item.title
            b.tvDescription.text = item.description
            b.tvDate.text        = fmt.format(Date(item.date))
            b.root.setOnClickListener { onClick(item) }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<PqrsUiModel>() {
        override fun areItemsTheSame(a: PqrsUiModel, b: PqrsUiModel) = a.id == b.id
        override fun areContentsTheSame(a: PqrsUiModel, b: PqrsUiModel) = a == b
    }
}
