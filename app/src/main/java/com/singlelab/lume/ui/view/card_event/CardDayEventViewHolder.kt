package com.singlelab.lume.ui.view.card_event

import android.view.View
import androidx.core.content.ContextCompat
import com.loopeer.cardstack.CardStackView
import com.singlelab.lume.R
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.event.EventSummary
import com.singlelab.lume.ui.events.adapter.OnEventItemClickListener
import com.singlelab.lume.util.parse
import com.singlelab.net.model.event.ParticipantStatus
import kotlinx.android.synthetic.main.item_day_event.view.*

class CardDayEventViewHolder(view: View) :
    CardStackView.ViewHolder(view) {

    override fun onItemExpand(b: Boolean) {

    }

    fun onBind(event: EventSummary, position: Int, listener: OnEventItemClickListener) {
        itemView.name.text = event.name
        itemView.description.text = event.description
        itemView.date.text =
            event.startTime.parse(Const.DATE_FORMAT_TIME_ZONE, Const.DATE_FORMAT_ONLY_TIME)
        itemView.content.setOnClickListener {
            listener.onClickEvent(event.eventUid)
        }
        if (position % 2 == 0) {
            itemView.strip.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.colorAccent
                )
            )
        } else {
            itemView.strip.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.colorAccentLight
                )
            )
        }
        when (event.participantStatus) {
            ParticipantStatus.WAITING_FOR_APPROVE_FROM_EVENT -> {
                itemView.participant_status.visibility = View.VISIBLE
                itemView.participant_status.text =
                    itemView.context.getString(R.string.waiting_for_approve_event)
            }
            ParticipantStatus.WAITING_FOR_APPROVE_FROM_USER -> {
                itemView.participant_status.visibility = View.VISIBLE
                itemView.participant_status.text =
                    itemView.context.getString(R.string.waiting_for_approve_user)
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