package com.singlelab.lume.ui.friends.adapter

interface OnPersonItemClickListener {
    fun onPersonClick(personUid: String)

    fun onChatClick(personUid: String)
}