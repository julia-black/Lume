package com.singlelab.lume.ui.view.event

interface OnEventItemClickListener {
    fun onClickEvent(uid: String)

    fun onClickChat(eventName: String, chatUid: String)
}