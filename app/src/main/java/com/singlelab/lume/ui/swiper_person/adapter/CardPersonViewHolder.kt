package com.singlelab.lume.ui.swiper_person.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.custom.sliderimage.logic.SliderImage
import com.singlelab.lume.R
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.util.generateImageLink
import kotlinx.android.synthetic.main.item_card_person.view.*

class CardPersonViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_card_person, parent, false)) {

    fun bind(person: Person) {
        val age =
            itemView.resources.getQuantityString(R.plurals.age_plurals, person.age, person.age)
        itemView.name.text = "${person.name}, $age"
        itemView.login.text = "@${person.login}"
        itemView.description.text = person.description
        itemView.city.text = person.cityName
        if (!person.imageContentUid.isNullOrEmpty()) {
            Glide.with(itemView)
                .load(person.imageContentUid.generateImageLink())
                .thumbnail(0.1f)
                .into(itemView.image)
            itemView.image.setOnClickListener {
                showFullScreenImage(person.imageContentUid)
            }
        }
    }

    private fun showFullScreenImage(imageContentUid: String) {
        itemView.context?.let {
            val links = listOf(imageContentUid.generateImageLink())
            SliderImage.openfullScreen(it, links, 0)
        }
    }
}