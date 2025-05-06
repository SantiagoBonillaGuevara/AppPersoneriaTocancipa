package com.personeriatocancipa.app.ui.user.getDates.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.domain.model.Date

class UserDatesAdapter(private var dates: List<Date>) : RecyclerView.Adapter<UserDatesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDatesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
        return UserDatesViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserDatesViewHolder, position: Int) {
        holder.bind(dates[position])
    }

    override fun getItemCount()= dates.size
    fun updateList(newDates : List<Date>) {
        dates = newDates
        notifyDataSetChanged()
    }
}