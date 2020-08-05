package com.singlelab.lume.ui.view.person_short

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.singlelab.lume.R
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.util.generateImageLinkForPerson
import kotlinx.android.synthetic.main.item_person.view.*

class PersonShortViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_person_short, parent, false)) {

    fun bind(person: Person, listener: OnPersonShortClickListener?) {
        itemView.name.text = person.name

        if (person.imageContentUid == null) {
            itemView.image_person.setImageResource(R.drawable.ic_profile)
        } else {
            Glide.with(itemView)
                .load(person.imageContentUid.generateImageLinkForPerson())
                .into(itemView.image_person)
        }
        itemView.setOnClickListener {
            listener?.onPersonClick(person.personUid)
        }
    }
}