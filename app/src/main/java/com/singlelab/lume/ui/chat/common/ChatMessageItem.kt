package com.singlelab.lume.ui.chat.common

data class ChatMessageItem(
    val uid: String,
    val text: String,
    val isError: Boolean,
    val type: Type,
    val personPhoto: String,
    val personName: String
) {
    enum class Type(val code: Int) {
        INCOMING(0),
        OUTGOING(1)
    }
}