package com.singlelab.lume.ui.friends.adapter

interface OnPersonItemClickListener {
    fun onPersonClick(personUid: String)

    fun onChatClick(personUid: String)

    fun onAddToFriends(personUid: String)

    fun onInviteClick(personUid: String, eventUid: String)
}