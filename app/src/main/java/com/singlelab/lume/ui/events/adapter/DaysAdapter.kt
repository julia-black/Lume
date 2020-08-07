package com.singlelab.lume.ui.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.singlelab.lume.model.event.EventSummary


class DaysAdapter(
    private val days: List<Pair<CalendarDay, List<EventSummary>>>,
    private val listener: OnEventItemClickListener
) : RecyclerView.Adapter<DaysViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DaysViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        val events = days[position].second
        holder.bind(events, listener)
    }

    override fun getItemCount(): Int = days.size

    fun getList() = days
}