package com.singlelab.lume.ui.friends.adapter

interface OnPersonItemClickListener {
    fun onPersonClick()

    fun onChatClick(personUid: String)
}