package com.personeriatocancipa.app.ui.admin.managementDates.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.domain.model.Date


class DatesAdapter(
    private var dates: List<Date>,
    private val onEditClicked: (Date) -> Unit
): RecyclerView.Adapter<DatesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
        return DatesViewHolder(view)
    }

    override fun getItemCount()= dates.size

    override fun onBindViewHolder(holder: DatesViewHolder, position: Int) {
        holder.bind(dates[position], onEditClicked)
    }

    fun updateList(newDates : List<Date>) {
        dates = newDates
        notifyDataSetChanged()
    }
}