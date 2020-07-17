package com.singlelab.lume.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.data.model.event.EventSummary

class EventsAdapter(private val list: List<EventSummary>) :
    RecyclerView.Adapter<EventsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EventsViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val event = list[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int = list.size

}