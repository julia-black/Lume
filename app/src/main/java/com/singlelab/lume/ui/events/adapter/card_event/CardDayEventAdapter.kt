package com.singlelab.lume.ui.events.adapter.card_event

import android.content.Context
import android.view.ViewGroup
import com.loopeer.cardstack.CardStackView
import com.loopeer.cardstack.StackAdapter
import com.singlelab.lume.R
import com.singlelab.lume.model.event.EventSummary
import com.singlelab.lume.ui.view.event.OnEventItemClickListener

class CardDayEventAdapter(context: Context?, private val listener: OnEventItemClickListener) :
    StackAdapter<EventSummary>(context) {
    override fun bindView(data: EventSummary, position: Int, holder: CardStackView.ViewHolder) {
        if (holder is CardDayEventViewHolder) {
            holder.onBind(data, position, listener)
        }
    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): CardStackView.ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_day_event, parent, false)
        return CardDayEventViewHolder(view)
    }

    override fun getItemViewType(position: Int) = R.layout.item_day_event
}