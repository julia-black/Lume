package com.singlelab.lume.ui.chat.common

import com.singlelab.lume.database.entity.ChatMessage
import com.singlelab.lume.ui.chat.common.ChatMessageItem.Type.INCOMING
import com.singlelab.lume.ui.chat.common.ChatMessageItem.Type.OUTGOING
import com.singlelab.lume.util.generateImageLinkForPerson
import com.singlelab.net.model.chat.ChatMessageResponse

fun List<ChatMessageResponse>.toDbEntities(chatUid: String, personName: String, personPhoto: String): List<ChatMessage> =
    mapNotNull { it.toDbEntity(chatUid, personName, personPhoto) }

fun ChatMessageResponse.toDbEntity(chatUid: String, personName: String, personPhoto: String): ChatMessage? {
    return ChatMessage(
        uid = uId ?: return null,
        personUid = personUid ?: return null,
        chatUid = chatUid,
        text = text ?: "",
        date = date ?: "",
        personName = personName,
        personPhoto = personPhoto
    )
}

fun List<ChatMessage>.toUiEntities(currentPersonUid: String): List<ChatMessageItem> =
    map { it.toUiEntity(currentPersonUid) }

fun ChatMessage.toUiEntity(currentPersonUid: String): ChatMessageItem =
    ChatMessageItem(
        uid = uid,
        text = text,
        isError = false,
        type = if (personUid == currentPersonUid) OUTGOING else INCOMING,
        personPhoto = personPhoto.generateImageLinkForPerson(),
        personName = personName.generateImageLinkForPerson()
    )