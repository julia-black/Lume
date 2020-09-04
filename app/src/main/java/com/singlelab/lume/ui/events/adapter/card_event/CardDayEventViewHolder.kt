package com.singlelab.lume.ui.events.adapter.card_event

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.loopeer.cardstack.CardStackView
import com.singlelab.lume.R
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.event.EventSummary
import com.singlelab.lume.ui.view.event.OnEventItemClickListener
import com.singlelab.lume.util.getLocationName
import com.singlelab.lume.util.parse
import com.singlelab.net.model.event.ParticipantStatus
import kotlinx.android.synthetic.main.item_day_event.view.*

class CardDayEventViewHolder(view: View) :
    CardStackView.ViewHolder(view) {

    override fun onItemExpand(b: Boolean) {
    }

    fun onBind(event: EventSummary, position: Int, listener: OnEventItemClickListener) {
        itemView.title.text = event.name
        itemView.description.text = event.description
        itemView.time.text =
            event.startTime.parse(Const.DATE_FORMAT_TIME_ZONE, Const.DATE_FORMAT_ONLY_TIME)

        itemView.content.setOnClickListener {
            listener.onClickEvent(event.eventUid)
        }
        itemView.button_chat.setOnClickListener {
            event.chatUid?.let {
                listener.onClickChat(event.name, event.chatUid)
            }
        }

        event.types.forEachIndexed { index, eventType ->
            when (index) {
                0 -> {
                    itemView.emoji_card_one.visibility = View.VISIBLE
                    itemView.emoji_one.setImageResource(eventType.resId)
                }
                1 -> {
                    itemView.emoji_card_two.visibility = View.VISIBLE
                    itemView.emoji_two.setImageResource(eventType.resId)
                }
                2 -> {
                    itemView.emoji_card_three.visibility = View.VISIBLE
                    itemView.emoji_three.setImageResource(eventType.resId)
                }
            }
        }

        if (event.isOnline) {
            itemView.text_location.text = itemView.context.getString(R.string.online)
            itemView.icon_location.setImageResource(R.drawable.ic_online)
        } else {
            itemView.text_location.text =
                context?.getLocationName(event.xCoordinate, event.yCoordinate)
                    ?: itemView.context.getString(R.string.unavailable_location_short)
        }
        if (position % 2 == 0) {
            itemView.shape.alpha = 1f
        } else {
            itemView.shape.alpha = 0.7f
        }
        when (event.participantStatus) {
            ParticipantStatus.WAITING_FOR_APPROVE_FROM_EVENT -> {
                itemView.status_card.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorGray
                    )
                )
                itemView.participant_status.text =
                    itemView.context.getString(R.string.waiting_for_approve_event)
                itemView.status_card.visibility = View.VISIBLE
            }
            ParticipantStatus.WAITING_FOR_APPROVE_FROM_USER -> {
                itemView.participant_status.text =
                    itemView.context.getString(R.string.waiting_for_approve_user)
                itemView.status_card.visibility = View.VISIBLE
            }
            else -> {
                if (event.chatUid != null) {
                    itemView.button_chat.visibility = View.VISIBLE
                }
                itemView.status_card.visibility = View.GONE
            }
        }
        if (event.anyPersonWaitingForApprove) {
            itemView.status_card.visibility = View.VISIBLE
            itemView.participant_status.text =
                itemView.context.getString(R.string.new_participants)
        }

        if (itemView.status_card.isVisible) {
            itemView.content.setPadding(
                0,
                0,
                0,
                itemView.resources.getDimension(R.dimen.margin_big).toInt()
            )
        }
    }
}