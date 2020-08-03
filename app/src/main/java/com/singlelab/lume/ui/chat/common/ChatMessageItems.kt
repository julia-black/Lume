package com.singlelab.lume.ui.chat.common

interface ChatMessageItem {
    val uid: String
    val text: String
    val type: Type
    val images: List<String>

    enum class Type(val code: Int) {
        INCOMING(0),
        OUTGOING(1)
    }
}

data class PrivateChatMessageItem(
    override val uid: String,
    override val text: String,
    override val type: ChatMessageItem.Type,
    override val images: List<String>
) : ChatMessageItem


data class GroupChatMessageItem(
    override val uid: String,
    override val text: String,
    override val type: ChatMessageItem.Type,
    override val images: List<String>,
    val personPhoto: String,
    val personName: String
) : ChatMessageItem