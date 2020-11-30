package com.singlelab.lume.ui.view.person

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.singlelab.lume.R
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.util.PluralsUtil
import com.singlelab.lume.util.generateMiniImageLink
import com.singlelab.net.model.event.ParticipantStatus
import kotlinx.android.synthetic.main.item_person.view.*


class PersonViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_person, parent, false)) {

    fun bind(
        person: Person,
        eventUid: String? = null,
        participantIds: Array<String>? = null,
        isInviting: Boolean = false,
        isAdministrator: Boolean = false,
        listener: OnPersonItemClickListener
    ) {
        itemView.name.text = person.name

        val age = PluralsUtil.getString(
            person.age,
            "год",
            "года",
            "года",
            "года",
            "лет"
        )
        itemView.age_and_city.text = "$age, ${person.cityName}"

        if (person.imageContentUid != null) {
            Glide.with(itemView)
                .load(person.imageContentUid.generateMiniImageLink())
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

            itemView.button_reject.setOnClickListener {
                listener.onRejectClick(person.personUid, eventUid)
            }

            if (person.isInvited || person.participantStatus == ParticipantStatus.ACTIVE) {
                itemView.button_accept.visibility = View.GONE
                itemView.button_invite.visibility = View.GONE
                itemView.button_reject.visibility = View.GONE
                if (isAdministrator) {
                    itemView.button_reject.visibility = View.VISIBLE
                }
            } else {
                if (isInviting) {
                    itemView.button_invite.visibility = View.VISIBLE
                    itemView.button_invite.setOnClickListener {
                        listener.onInviteClick(person.personUid, eventUid)
                    }
                    itemView.button_accept.visibility = View.GONE
                    itemView.button_reject.visibility = View.GONE
                } else {
                    itemView.button_accept.visibility = View.VISIBLE
                    itemView.button_accept.setOnClickListener {
                        listener.onAcceptClick(person.personUid, eventUid)
                    }
                    itemView.button_reject.visibility = View.VISIBLE
                    itemView.button_invite.visibility = View.GONE
                }
            }
            if (isInviting && isPersonAlreadyInEvent(participantIds, person.personUid)) {
                itemView.button_accept.visibility = View.GONE
                itemView.button_invite.visibility = View.GONE
            }
        } else {
            if (person.friendshipApprovalRequired) {
                itemView.button_accept.visibility = View.VISIBLE
                itemView.button_reject.visibility = View.VISIBLE
                itemView.button_accept.setOnClickListener {
                    listener.onConfirmFriendClick(person.personUid)
                }
                itemView.button_reject.setOnClickListener {
                    listener.onRemoveFriendClick(person.personUid)
                }
                itemView.button_chat.visibility = View.GONE
            } else {
                itemView.button_accept.visibility = View.GONE
                itemView.button_reject.visibility = View.GONE
                itemView.button_chat.visibility = View.VISIBLE
            }
        }
    }

    private fun isPersonAlreadyInEvent(ids: Array<String>?, personUid: String) =
        ids?.find { it == personUid } != null
}