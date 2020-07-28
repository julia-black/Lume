package com.singlelab.lume.ui.chats.common

import com.singlelab.lume.database.entity.Chat
import com.singlelab.net.model.chat.ChatResponse

fun List<ChatResponse>.toDbEntities(): List<Chat> =
    mapNotNull { it.toDbEntity() }

fun ChatResponse.toDbEntity(): Chat? {
    return Chat(
        uid = chatUid ?: return null,
        title = name ?: "",
        lastMessage = lastMessage?.messageContent ?: "",
        isGroup = isGroupChat ?: false,
        image = (if (isGroupChat == true) eventImageUid else personImageUid) ?: "",
        privateChatPersonUid = if (isGroupChat == true) lastMessage?.personUid ?: "" else ""
    )
}

fun List<Chat>.toUiEntities(): List<ChatItem> =
    map { it.toDbEntity() }

fun Chat.toDbEntity(): ChatItem =
    ChatItem(
        uid = uid,
        title = title,
        image = image,
        isGroup = isGroup,
        lastMessage = lastMessage,
        personUid = privateChatPersonUid
    )