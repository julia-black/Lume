package com.singlelab.lume.ui.events.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.singlelab.lume.R
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.event.EventSummary
import com.singlelab.lume.util.generateImageLink
import com.singlelab.lume.util.parse
import com.singlelab.net.model.event.ParticipantStatus
import kotlinx.android.synthetic.main.item_event.view.*

class EventsViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_event, parent, false)) {

    fun bind(event: EventSummary, listener: OnEventItemClickListener) {
        itemView.name.text = event.name
        itemView.description.text = event.description
        itemView.date.text =
            event.startTime.parse(Const.DATE_FORMAT_TIME_ZONE, Const.DATE_FORMAT_OUTPUT)

        event.eventImageContentUid?.let {
            Glide.with(itemView)
                .load(it.generateImageLink())
                .into(itemView.image)
        }
        itemView.setOnClickListener {
            listener.onClickEvent(event.eventUid)
        }
        when (event.participantStatus) {
            ParticipantStatus.WAITING_FOR_APPROVE_FROM_EVENT -> {
                itemView.participant_status.visibility = View.VISIBLE
                itemView.participant_status.text =
                    itemView.context.getString(R.string.waiting_for_approve_event)
            }
            else -> {
                itemView.participant_status.visibility = View.GONE
            }
        }
        if (event.anyPersonWaitingForApprove) {
            itemView.icon_notifications.visibility = View.VISIBLE
        } else {
            itemView.icon_notifications.visibility = View.GONE
        }
    }
}