package com.singlelab.lume.ui.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.singlelab.data.model.consts.Const
import com.singlelab.data.model.event.EventSummary
import com.singlelab.lume.R
import com.singlelab.lume.util.generateImageLink
import com.singlelab.lume.util.parse
import kotlinx.android.synthetic.main.item_event.view.*

class EventsViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_event, parent, false)) {

    fun bind(event: EventSummary) {
        itemView.name.text = event.name
        itemView.description.text = event.description
        itemView.date.text =
            event.startTime.parse(Const.DATE_FORMAT_TIME_ZONE, Const.DATE_FORMAT_OUTPUT)

        event.eventImageContentUid?.let {
            Glide.with(itemView)
                .load(it.generateImageLink())
                .into(itemView.image)
        }
    }
}