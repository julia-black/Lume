package com.singlelab.lume.ui.chat.common

import com.singlelab.lume.database.entity.ChatMessage
import com.singlelab.lume.ui.chat.common.ChatMessageItem.Status
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
        personPhoto = personImageUid ?: "",
        images = images ?: emptyList()
    )
}

fun List<ChatMessage>.toUiEntities(isChatGroup: Boolean, currentPersonUid: String): List<ChatMessageItem> =
    map { it.toUiEntity(isChatGroup, currentPersonUid) }

fun ChatMessage.toUiEntity(isChatGroup: Boolean, currentPersonUid: String): ChatMessageItem =
    if (isChatGroup) toGroupItems(currentPersonUid) else toPrivateItems(currentPersonUid)

fun ChatMessage.toPrivateItems(currentPersonUid: String): PrivateChatMessageItem =
    PrivateChatMessageItem(
        uid = uid,
        text = text,
        type = if (personUid == currentPersonUid) Type.OUTGOING else Type.INCOMING,
        images = images,
        status = Status.SYNCED,
        date = date
    )

fun ChatMessage.toGroupItems(currentPersonUid: String): GroupChatMessageItem =
    GroupChatMessageItem(
        uid = uid,
        text = text,
        type = if (personUid == currentPersonUid) Type.OUTGOING else Type.INCOMING,
        personUid = personUid,
        personPhoto = personPhoto,
        personName = personName,
        images = images,
        status = Status.SYNCED,
        date = date
    )