package com.singlelab.net.model.chat

import com.google.gson.annotations.SerializedName

// Chat

data class ChatMessagesResponse(
    @SerializedName("chatUid")
    val uId: String? = null,
    val chatName: String? = null,
    @SerializedName("isGroupChat")
    val isGroup: Boolean? = false,
    val messages: List<ChatMessageResponse>? = emptyList()
)

// Chats

data class ChatResponse(
    @SerializedName("chatUid")
    val uId: String? = null,
    @SerializedName("isGroupChat")
    val isGroup: Boolean? = false,
    @SerializedName("name")
    val title: String? = null,
    val lastMessage: ChatMessageResponse? = null
)

// Messages

data class ChatMessageResponse(
    @SerializedName("messageUid")
    val uId: String? = null,
    @SerializedName("messageContent")
    val text: String? = null,
    val images: List<String>? = emptyList(),
    val personName: String? = null,
    val personUid: String? = null,
    val personImageUid: String? = null,
    @SerializedName("messageTime")
    val date: String? = null
)

data class ChatMessageRequest(
    val chatUid: String? = null,
    @SerializedName("content")
    val text: String? = null,
    val images: List<String>? = emptyList()
)