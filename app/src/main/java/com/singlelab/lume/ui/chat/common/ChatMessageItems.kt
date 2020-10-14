package com.singlelab.lume.ui.chat.common

abstract class ChatMessageItem : ChatItem {
    abstract val uid: String
    abstract val text: String
    abstract val images: List<String>
    abstract val status: Status

    enum class Type(val code: Int) {
        INCOMING(0),
        OUTGOING(1),
        DATE(2)
    }

    enum class Status {
        PENDING,
        SYNCED,
        ERROR
    }

    companion object {
        const val PENDING_MESSAGE_UID = "-1"
    }
}

data class PrivateChatMessageItem(
    override val uid: String,
    override val text: String,
    override val type: ChatMessageItem.Type,
    override val images: List<String>,
    override val status: ChatMessageItem.Status,
    override val date: String
) : ChatMessageItem()


data class GroupChatMessageItem(
    override val uid: String,
    override val text: String,
    override val type: ChatMessageItem.Type,
    override val images: List<String>,
    override val status: ChatMessageItem.Status,
    override val date: String,
    val personUid: String,
    val personPhoto: String,
    val personName: String
) : ChatMessageItem()