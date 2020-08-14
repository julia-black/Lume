package com.singlelab.net.model.chat

// Chat

data class ChatMessagesResponse(
    val chatUid: String? = null,
    val isGroupChat: Boolean? = false,
    val chatName: String? = null,
    val messages: List<ChatMessageResponse>? = emptyList(),
    val personUid: String? = null,
    val eventUid: String? = null,
    val personImageUid: String? = null,
    val eventImageUid: String? = null
)

// Chats

data class ChatResponse(
    val chatUid: String? = null,
    val isGroupChat: Boolean? = false,
    val name: String? = null,
    val lastMessage: ChatMessageResponse? = null,
    val personImageUid: String? = null,
    val eventImageUid: String? = null,
    val unreadMessagesCount: Int = 0
)

// Messages

data class ChatMessageResponse(
    val messageUid: String? = null,
    val messageContent: String? = null,
    val messageTime: String? = null,
    val images: List<String>? = emptyList(),
    val personName: String? = null,
    val personUid: String? = null,
    val personImageUid: String? = null
)

data class ChatMessageRequest(
    val chatUid: String,
    val content: String,
    val images: List<String>
)