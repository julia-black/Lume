package com.singlelab.net.model.chat

// Chats

data class ChatInfo(
    val image: String,
    val title: String
)

// Messages

data class ChatMessage(
    val id: String,
    val message: String,
    val status: Status,
    val type: Type
) {
    enum class Status {
        PENDING,
        ERROR
    }

    enum class Type(val code: Int) {
        INCOMING(0),
        OUTGOING(1)
    }
}