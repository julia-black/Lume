package com.singlelab.lume.ui.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.singlelab.lume.model.event.EventSummary
import com.singlelab.lume.ui.events.adapter.card_event.OnCollapseListener
import com.singlelab.lume.ui.view.event.OnEventItemClickListener


class DaysAdapter(
    private val days: List<Pair<CalendarDay, List<EventSummary>>>,
    private val listener: OnEventItemClickListener
) : RecyclerView.Adapter<DaysViewHolder>() {

    private val collapsedListeners: MutableList<OnCollapseListener> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DaysViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        val events = days[position].second
        holder.bind(events, listener)
        collapsedListeners.add(holder)
    }

    override fun getItemCount(): Int = days.size

    fun getList() = days

    fun collapseCards() {
        collapsedListeners.forEach {
            it.onCollapse()
        }
    }
}