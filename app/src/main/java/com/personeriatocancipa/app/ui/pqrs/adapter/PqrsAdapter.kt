package com.personeriatocancipa.app.ui.pqrs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.personeriatocancipa.app.databinding.ItemPqrsBinding
import com.personeriatocancipa.app.domain.model.Pqrs

class PqrsAdapter(
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<PqrsAdapter.ViewHolder>() {

    private var items: List<Pqrs> = emptyList()

    fun submitList(list: List<Pqrs>) {
        items = list
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemPqrsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPqrsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pqrs = items[position]
        holder.binding.tvTitle.text = pqrs.title
        holder.binding.tvType.text = pqrs.type
        holder.binding.root.setOnClickListener {
            onClick(pqrs.id)
        }
    }
}
