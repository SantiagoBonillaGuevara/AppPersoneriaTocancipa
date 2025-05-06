package com.personeriatocancipa.app.ui.lawyer.dates.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.domain.model.Date

class LawyerDatesAdapter(
    private var dates: List<Date>,
    private val onClick: (Date, String) -> Unit
) : RecyclerView.Adapter<LawyerDatesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawyerDatesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
        return LawyerDatesViewHolder(view)
    }

    override fun onBindViewHolder(holder: LawyerDatesViewHolder, position: Int) {
        holder.bind(dates[position], onClick)
    }

    override fun getItemCount()= dates.size
    fun updateList(newDates : List<Date>) {
        dates = newDates
        notifyDataSetChanged()
    }
}