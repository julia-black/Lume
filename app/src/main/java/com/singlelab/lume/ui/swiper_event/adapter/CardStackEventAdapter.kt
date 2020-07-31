package com.singlelab.lume.ui.swiper_event.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.lume.model.event.Event

class CardStackEventAdapter(
    private var events: List<Event> = emptyList(),
    private val listener: OnCardEventListener
) : RecyclerView.Adapter<CardEventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardEventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CardEventViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CardEventViewHolder, position: Int) {
        holder.bind(events[position], listener)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    fun setEvents(events: List<Event>) {
        this.events = events
        notifyDataSetChanged()
    }

    fun getEvents(): List<Event> {
        return events
    }
}