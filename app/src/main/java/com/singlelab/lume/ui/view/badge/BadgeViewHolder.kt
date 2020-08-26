package com.singlelab.lume.ui.view.badge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.singlelab.lume.R
import com.singlelab.lume.model.profile.Badge
import com.singlelab.lume.util.generateImageLink
import kotlinx.android.synthetic.main.item_badge.view.*

class BadgeViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_badge, parent, false)) {

    fun bind(badge: Badge) {
        itemView.title.text = badge.name
        itemView.description.text = badge.description

        Glide.with(itemView)
            .load(badge.badgeImageUid.generateImageLink())
            .into(itemView.image)

        if (badge.isReceived) {
            itemView.image.alpha = 1f
            itemView.title.alpha = 1f
            itemView.description.alpha = 1f
        } else {
            itemView.image.alpha = 0.2f
            itemView.title.alpha = 0.2f
            itemView.description.alpha = 0.2f
        }
    }
}