package com.singlelab.lume.ui.events.adapter

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loopeer.cardstack.CardStackView
import com.singlelab.lume.R
import com.singlelab.lume.model.event.EventSummary
import com.singlelab.lume.ui.events.adapter.card_event.CardDayEventAdapter
import com.singlelab.lume.ui.events.adapter.card_event.OnCollapseListener
import com.singlelab.lume.ui.view.event.OnEventItemClickListener
import kotlinx.android.synthetic.main.item_cards_events.view.*

class DaysViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_cards_events, parent, false)),
    CardStackView.ItemExpendListener, OnCollapseListener {

    var adapter: CardDayEventAdapter? = null

    fun bind(events: List<EventSummary>, listener: OnEventItemClickListener) {
        itemView.stackview_main.itemExpendListener = this
        adapter = CardDayEventAdapter(itemView.context, listener)
        itemView.stackview_main.setAdapter(adapter)
        Handler().postDelayed(
            {
                adapter?.updateData(events)
            }, 200
        )
    }

    override fun onItemExpend(expend: Boolean) {

    }

    override fun onCollapse() {
        if (itemView.stackview_main.selectPosition != -1) {
            itemView.stackview_main.clearSelectPosition()
        }
    }
}