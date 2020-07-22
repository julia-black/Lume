package com.singlelab.lume.ui.friends.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.singlelab.data.model.profile.Person
import com.singlelab.lume.R
import com.singlelab.lume.util.generateImageLink
import kotlinx.android.synthetic.main.item_person.view.*


class PersonViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_person, parent, false)) {

    fun bind(person: Person, listener: OnPersonItemClickListener) {
        itemView.name.text = person.name
        person.imageContentUid?.let { imageUid ->
            Glide.with(itemView)
                .load(imageUid.generateImageLink())
                .into(itemView.image_person)
        }
        itemView.image_person.setOnClickListener {
            listener.onPersonClick()
        }
        itemView.name.setOnClickListener {
            listener.onPersonClick()
        }
        itemView.button_chat.setOnClickListener {
            listener.onChatClick(person.personUid)
        }
    }
}