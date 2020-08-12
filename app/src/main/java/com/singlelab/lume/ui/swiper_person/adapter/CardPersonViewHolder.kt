package com.singlelab.lume.ui.swiper_person.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.singlelab.lume.R
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.util.generateImageLink
import kotlinx.android.synthetic.main.item_card_person.view.*

class CardPersonViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_card_person, parent, false)) {

    fun bind(person: Person) {
        itemView.name_age.text = "${person.name}, ${person.age}"
        itemView.login.text = "@${person.login}"
        itemView.description.text = person.description
        itemView.city.text = person.cityName
        if (person.imageContentUid == null) {
            itemView.image.visibility = View.INVISIBLE
        } else {
            itemView.image.visibility = View.VISIBLE
            Glide.with(itemView)
                .load(person.imageContentUid.generateImageLink())
                .into(itemView.image)
        }
    }
}