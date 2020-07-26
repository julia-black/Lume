package com.singlelab.lume.ui.swiper_event.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.singlelab.lume.R
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.event.Event
import com.singlelab.lume.ui.event.EventType
import com.singlelab.lume.util.generateImageLink
import com.singlelab.lume.util.parse
import kotlinx.android.synthetic.main.item_card_event.view.*

class CardEventViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_card_event, parent, false)) {

    fun bind(event: Event) {
        itemView.title.text = event.name
        itemView.description.text = event.description
        itemView.start_date.text =
            event.startTime.parse(Const.DATE_FORMAT_TIME_ZONE, Const.DATE_FORMAT_OUTPUT)

        event.eventImageContentUid?.let {
            Glide.with(itemView)
                .load(it.generateImageLink())
                .into(itemView.image)
        }
        val eventType = EventType.findById(event.type)
        eventType?.let {
            itemView.context.let { safeContext ->
                itemView.type.text = itemView.context.getString(it.titleRes)
                itemView.type.backgroundTintList =
                    ContextCompat.getColorStateList(safeContext, it.colorRes)
            }
        }
        if (event.minAge == null && event.maxAge == null) {
            itemView.age.visibility = View.GONE
        } else if (event.maxAge == null) {
            itemView.age.text = itemView.context.getString(R.string.age_from, event.minAge)
        } else if (event.minAge == null) {
            itemView.age.text = itemView.context.getString(R.string.age_to, event.maxAge)
        } else {
            itemView.age.text = itemView.context.getString(
                R.string.age_from_to,
                event.minAge,
                event.maxAge
            )
        }
        itemView.count_participants.text = itemView.context.getString(
            R.string.count_participants,
            event.participants.size
        )
        event.administrator?.let {
            itemView.administrator.text =
                itemView.context.getString(R.string.administrator, it.name)
            it.imageContentUid?.let { imageUid ->
                showImage(itemView.image_administrator, imageUid)
            }
        }
    }

    private fun showImage(imageView: ImageView, imageUid: String) {
        Glide.with(itemView.context)
            .load(imageUid.generateImageLink())
            .into(imageView)
    }
}