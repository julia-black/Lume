package com.singlelab.lume.ui.chat.common

import com.singlelab.lume.database.entity.ChatMessage
import com.singlelab.lume.ui.chat.common.ChatMessageItem.Type
import com.singlelab.net.model.chat.ChatMessageResponse

fun List<ChatMessageResponse>.toDbEntities(chatUid: String?): List<ChatMessage> =
    mapNotNull { it.toDbEntity(chatUid) }

fun ChatMessageResponse.toDbEntity(chatUid: String?): ChatMessage? {
    return ChatMessage(
        uid = messageUid ?: return null,
        personUid = personUid ?: return null,
        chatUid = chatUid ?: return null,
        text = messageContent ?: "",
        date = messageTime ?: "",
        personName = personName ?: "",
        personPhoto = personImageUid ?: ""
    )
}

fun List<ChatMessage>.toUiEntities(isChatGroup: Boolean, currentPersonUid: String): List<ChatMessageItem> =
    map { if (isChatGroup) it.toGroupItems(currentPersonUid) else it.toPrivateItems(currentPersonUid) }

fun ChatMessage.toPrivateItems(currentPersonUid: String): PrivateChatMessageItem =
    PrivateChatMessageItem(
        uid = uid,
        text = text,
        type = if (personUid == currentPersonUid) Type.OUTGOING else Type.INCOMING
    )

fun ChatMessage.toGroupItems(currentPersonUid: String): GroupChatMessageItem =
    GroupChatMessageItem(
        uid = uid,
        text = text,
        type = if (personUid == currentPersonUid) Type.OUTGOING else Type.INCOMING,
        personPhoto = personPhoto,
        personName = personName
    )