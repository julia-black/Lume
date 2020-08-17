package com.singlelab.lume.ui.chats.common

data class ChatItem(
    val uid: String,
    val image: String,
    val title: String,
    val isGroup: Boolean,
    val lastMessage: String,
    val lastMessagePersonUid: String,
    val lastMessagePersonName: String,
    val isLastMessageImage: Boolean,
    val unreadMessagesCount: Int = 0
)