package com.singlelab.lume.ui.view.person

interface OnPersonItemClickListener {
    fun onPersonClick(personUid: String)

    fun onChatClick(personName: String, personUid: String)

    fun onAddToFriends(personUid: String)

    fun onAcceptClick(personUid: String, eventUid: String)

    fun onRejectClick(personUid: String, eventUid: String)

    fun onRemoveFriendClick(personUid: String)

    fun onConfirmFriendClick(personUid: String)
}