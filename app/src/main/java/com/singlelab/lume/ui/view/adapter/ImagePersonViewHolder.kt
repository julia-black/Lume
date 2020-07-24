package com.singlelab.lume.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.R
import com.singlelab.lume.util.generateImageLink
import kotlinx.android.synthetic.main.item_person.view.*

class ImagePersonViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_image_person, parent, false)) {

    fun bind(person: Person, listener: OnPersonImageClickListener?) {
        person.imageContentUid?.let { imageUid ->
            Glide.with(itemView)
                .load(imageUid.generateImageLink())
                .into(itemView.image_person)
        }
        itemView.image_person.setOnClickListener {
            listener?.onPersonClick(person.personUid)
        }
    }
}