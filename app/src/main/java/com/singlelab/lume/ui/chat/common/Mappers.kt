package com.singlelab.lume.ui.chat.common

import com.singlelab.lume.database.entity.ChatMessage
import com.singlelab.lume.ui.chat.common.ChatMessageItem.Type.INCOMING
import com.singlelab.lume.ui.chat.common.ChatMessageItem.Type.OUTGOING
import com.singlelab.net.model.chat.ChatMessageResponse

fun List<ChatMessageResponse>.toDbEntities(): List<ChatMessage> =
    mapNotNull { it.toUiEntity() }

fun ChatMessageResponse.toUiEntity(): ChatMessage? {
    return ChatMessage(
        uid = uId ?: return null,
        personUid = personUid ?: return null,
        text = text ?: "",
        date = date ?: ""
    )
}

fun List<ChatMessage>.toUiEntities(currentPersonUid: String): List<ChatMessageItem> =
    map { it.toUiEntity(currentPersonUid) }

fun ChatMessage.toUiEntity(currentPersonUid: String): ChatMessageItem =
    ChatMessageItem(
        uid = uid,
        text = text,
        isError = false,
        type = if (personUid == currentPersonUid) OUTGOING else INCOMING
    )