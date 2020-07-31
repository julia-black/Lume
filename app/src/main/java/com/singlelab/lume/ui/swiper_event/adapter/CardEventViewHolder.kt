package com.singlelab.lume.ui.swiper_event.adapter

import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.singlelab.lume.R
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.event.Event
import com.singlelab.lume.ui.event.EventType
import com.singlelab.lume.util.generateImageLinkForEvent
import com.singlelab.lume.util.generateImageLinkForPerson
import com.singlelab.lume.util.parse
import com.singlelab.lume.util.removePostalCode
import kotlinx.android.synthetic.main.item_card_event.view.*
import java.io.IOException
import java.util.*

class CardEventViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_card_event, parent, false)) {

    private val geoCoder: Geocoder by lazy { Geocoder(itemView.context, Locale.getDefault()) }

    fun bind(event: Event, listener: OnCardEventListener) {
        itemView.title.text = event.name
        itemView.description.text = event.description
        itemView.start_date.text =
            event.startTime.parse(Const.DATE_FORMAT_TIME_ZONE, Const.DATE_FORMAT_OUTPUT)

        if (event.eventPrimaryImageContentUid == null) {
            itemView.image.visibility = View.INVISIBLE
        } else {
            itemView.image.visibility = View.VISIBLE
            event.eventPrimaryImageContentUid.let {
                Glide.with(itemView)
                    .load(it.generateImageLinkForEvent())
                    .into(itemView.image)
            }
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
        event.administrator?.let {
            itemView.administrator.text =
                itemView.context.getString(R.string.administrator, it.name)

            if (it.imageContentUid == null) {
                itemView.image_administrator.setImageResource(R.drawable.ic_profile)
            } else {
                Glide.with(itemView.context)
                    .load(it.imageContentUid.generateImageLinkForPerson())
                    .into(itemView.image_administrator)
            }
        }

        if (event.isOnline) {
            itemView.text_location.visibility = View.INVISIBLE
            itemView.text_online.visibility = View.VISIBLE
        } else {
            itemView.text_online.visibility = View.INVISIBLE
            itemView.text_location.visibility = View.VISIBLE
            itemView.text_location.text = getLocationName(event.xCoordinate, event.yCoordinate)
            if (event.xCoordinate != null && event.yCoordinate != null) {
                itemView.text_location.setOnClickListener {
                    listener.onLocationClick(event.xCoordinate, event.yCoordinate, event.name)
                }
            }
        }
    }

    private fun getLocationName(xCoordinate: Double?, yCoordinate: Double?): String? {
        if (xCoordinate == null || yCoordinate == null) {
            return null
        }
        return try {
            val addresses: List<Address> =
                geoCoder.getFromLocation(xCoordinate, yCoordinate, 1)
            if (addresses.isNotEmpty()) {
                addresses[0].getAddressLine(0).removePostalCode(addresses[0].postalCode)
            } else {
                null
            }
        } catch (e: IOException) {
            null
        }
    }
}