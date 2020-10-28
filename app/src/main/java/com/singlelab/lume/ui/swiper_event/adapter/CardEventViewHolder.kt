package com.singlelab.lume.ui.swiper_event.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.singlelab.lume.R
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.event.Event
import com.singlelab.lume.util.generateImageLink
import com.singlelab.lume.util.generateMiniImageLink
import com.singlelab.lume.util.getLocationName
import com.singlelab.lume.util.parse
import kotlinx.android.synthetic.main.item_card_event.view.*

class CardEventViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_card_event, parent, false)) {

    fun bind(event: Event, listener: OnCardEventListener) {
        itemView.title.text = event.name
        itemView.description.text = event.description
        itemView.start_date.text =
            event.startTime.parse(Const.DATE_FORMAT_TIME_ZONE, Const.DATE_FORMAT_ON_CARD)

        if (event.eventPrimaryImageContentUid != null) {
            event.eventPrimaryImageContentUid.let {
                Glide.with(itemView)
                    .load(it.generateImageLink())
                    .thumbnail(0.3f)
                    .into(itemView.image)
            }
            itemView.image.setOnClickListener {
                val images = mutableListOf<String>()
                images.add(event.eventPrimaryImageContentUid)
                images.addAll(event.images ?: arrayListOf())
                listener.onImageClick(images)
            }
        }

        if (event.types.size < 3) {
            itemView.emoji_card_three.visibility = View.GONE
        }
        if (event.types.size < 2) {
            itemView.emoji_card_two.visibility = View.GONE
        }
        event.types.forEachIndexed { index, eventType ->
            when (index) {
                0 -> {
                    Glide.with(itemView)
                        .load(ContextCompat.getDrawable(itemView.context, eventType.resId))
                        .into(itemView.emoji_one)
                }
                1 -> {
                    Glide.with(itemView)
                        .load(ContextCompat.getDrawable(itemView.context, eventType.resId))
                        .into(itemView.emoji_two)
                }
                2 -> {
                    Glide.with(itemView)
                        .load(ContextCompat.getDrawable(itemView.context, eventType.resId))
                        .into(itemView.emoji_three)
                }
            }
        }
        event.administrator?.let {
            if (it.imageContentUid != null) {
                Glide.with(itemView.context)
                    .load(it.imageContentUid.generateMiniImageLink())
                    .into(itemView.image_administrator)
            }
            itemView.administrator_name.text = it.name

            itemView.administrator.setOnClickListener { view ->
                listener.onAdministratorClick(it.personUid)
            }
            itemView.image_administrator.setOnClickListener { view ->
                listener.onAdministratorClick(it.personUid)
            }
        }

        if (event.isOnline) {
            itemView.text_location.text = itemView.context.getString(R.string.online)
            itemView.icon_location.setImageResource(R.drawable.ic_online)
        } else {
            itemView.text_location.text =
                itemView.context.getLocationName(event.xCoordinate, event.yCoordinate) ?: event.cityName
            if (event.xCoordinate != null && event.xCoordinate > 0
                && event.yCoordinate != null && event.yCoordinate > 0
            ) {
                itemView.text_location.setOnClickListener {
                    listener.onLocationClick(event.xCoordinate, event.yCoordinate, event.name)
                }
            }
        }
    }
}