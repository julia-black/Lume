package com.singlelab.net.model.chat

import com.google.gson.annotations.SerializedName

// Chat

data class ChatResponse(
    @SerializedName("chatUid")
    val uId: String,
    @SerializedName("isGroupChat")
    val isGroup: Boolean,
    val messages: List<ChatMessageResponse>
)

// Chats

data class ChatPreviewResponse(
    @SerializedName("chatUid")
    val uId: String,
    @SerializedName("isGroupChat")
    val isGroup: Boolean,
    val lastMessage: ChatMessageResponse
)

// Messages

data class ChatMessageResponse(
    @SerializedName("messageUid")
    val uId: String,
    @SerializedName("messageContent")
    val text: String,
    val images: List<String>,
    val personName: String,
    val personUid: String,
    val personImageUid: String
)

data class ChatMessageRequest(
    val chatUid: String,
    @SerializedName("content")
    val text: String,
    val images: List<String>
)