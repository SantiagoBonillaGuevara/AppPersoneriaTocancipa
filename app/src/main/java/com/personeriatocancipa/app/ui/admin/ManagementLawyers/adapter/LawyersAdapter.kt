package com.personeriatocancipa.app.ui.admin.ManagementLawyers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.domain.model.Lawyer

class LawyersAdapter(
    var lawyers: List<Lawyer>,
    private val onLawyerChanged: (Lawyer, Boolean) -> Unit,
    private val onEditClicked: (Lawyer) -> Unit
) : RecyclerView.Adapter<LawyersViewHolder>(
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawyersViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        return LawyersViewHolder(layoutInflater.inflate(R.layout.item_lawyer, parent, false))
    }

    override fun getItemCount()= lawyers.size

    override fun onBindViewHolder(holder: LawyersViewHolder, position: Int) {
        holder.bind(lawyers[position], onLawyerChanged, onEditClicked)
    }

    fun updateList(newLawyers: List<Lawyer>) {
        lawyers = newLawyers
        notifyDataSetChanged()
    }
}