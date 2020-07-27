package com.singlelab.lume.ui.chats.common

import com.singlelab.lume.database.entity.Chat
import com.singlelab.lume.util.generateImageLinkForEvent
import com.singlelab.lume.util.generateImageLinkForPerson
import com.singlelab.net.model.chat.ChatResponse

fun List<ChatResponse>.toDbEntities(): List<Chat> =
    mapNotNull { it.toDbEntity() }

fun ChatResponse.toDbEntity(): Chat? {
    return Chat(
        uid = uId ?: return null,
        title = title ?: "",
        lastMessage = lastMessage?.text ?: "",
        isGroup = isGroup ?: false,
        image = if (isGroup == true) "" else lastMessage?.personImageUid ?: ""
    )
}

fun List<Chat>.toUiEntities(): List<ChatItem> =
    map { it.toDbEntity() }

fun Chat.toDbEntity(): ChatItem =
    ChatItem(
        uid = uid,
        title = title,
        image = if (isGroup) image.generateImageLinkForEvent() else image.generateImageLinkForPerson(),
        isGroup = isGroup,
        lastMessage = lastMessage
    )