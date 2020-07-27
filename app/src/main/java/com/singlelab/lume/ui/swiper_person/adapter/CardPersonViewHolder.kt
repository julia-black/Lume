package com.singlelab.lume.ui.swiper_person.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.singlelab.lume.R
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.util.generateImageLinkForPerson
import kotlinx.android.synthetic.main.item_card_person.view.*

class CardPersonViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_card_person, parent, false)) {

    fun bind(person: Person) {
        itemView.name.text = person.name
        itemView.description.text = person.description
        itemView.age.text = itemView.context.getString(R.string.person_age, person.age)
        if (person.imageContentUid == null) {
            itemView.image.visibility = View.INVISIBLE
        } else {
            itemView.image.visibility = View.VISIBLE
            Glide.with(itemView)
                .load(person.imageContentUid.generateImageLinkForPerson())
                .into(itemView.image)
        }
    }
}