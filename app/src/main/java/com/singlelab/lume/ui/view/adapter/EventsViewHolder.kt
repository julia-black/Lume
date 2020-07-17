package com.singlelab.lume.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.data.model.event.EventSummary
import com.singlelab.lume.R
import kotlinx.android.synthetic.main.item_event.view.*

class EventsViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_event, parent, false)) {

    fun bind(event: EventSummary) {
        itemView.name.text = event.name
        itemView.description.text = event.description
    }
}