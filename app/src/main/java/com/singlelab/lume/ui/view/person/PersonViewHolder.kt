package com.singlelab.lume.ui.view.person

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.singlelab.lume.R
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.util.generateImageLinkForPerson
import com.singlelab.net.model.event.ParticipantStatus
import kotlinx.android.synthetic.main.item_person.view.*


class PersonViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_person, parent, false)) {

    fun bind(
        person: Person,
        eventUid: String? = null,
        isInviting: Boolean = false,
        listener: OnPersonItemClickListener
    ) {
        itemView.name.text = person.name

        if (person.imageContentUid == null) {
            itemView.image_person.setImageResource(R.drawable.ic_profile)
        } else {
            Glide.with(itemView)
                .load(person.imageContentUid.generateImageLinkForPerson())
                .into(itemView.image_person)
        }
        itemView.setOnClickListener {
            listener.onPersonClick(person.personUid)
        }
        itemView.button_chat.setOnClickListener {
            listener.onChatClick(person.name, person.personUid)
        }
        if (person.isFriend) {
            itemView.button_add_to_friends.visibility = View.GONE
        } else {
            itemView.button_add_to_friends.visibility = View.VISIBLE
            itemView.button_add_to_friends.setOnClickListener {
                listener.onAddToFriends(person.personUid)
            }
        }

        if (eventUid != null) {
            itemView.button_chat.visibility = View.GONE
            itemView.button_add_to_friends.visibility = View.GONE
            if (person.isInvited || person.participantStatus == ParticipantStatus.ACTIVE) {
                itemView.button_invite.visibility = View.GONE
                itemView.button_reject.visibility = View.GONE
            } else {
                itemView.button_invite.visibility = View.VISIBLE
                itemView.button_invite.setOnClickListener {
                    listener.onAcceptClick(person.personUid, eventUid)
                }

                if (isInviting) {
                    itemView.button_reject.visibility = View.GONE
                } else {
                    itemView.button_reject.visibility = View.VISIBLE
                    itemView.button_reject.setOnClickListener {
                        listener.onRejectClick(person.personUid, eventUid)
                    }
                }
            }
        } else {
            itemView.button_chat.visibility = View.VISIBLE
            itemView.button_invite.visibility = View.GONE
        }
    }
}