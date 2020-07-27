package com.singlelab.lume.database.repository

import com.singlelab.lume.database.LumeDatabase
import com.singlelab.lume.database.entity.ChatMessage

interface ChatMessagesRepository {
    suspend fun insert(messages: Collection<ChatMessage>)
    suspend fun insert(message: ChatMessage)
    suspend fun byChatUid(chatUid: String): List<ChatMessage>
}

class RoomChatMessagesRepository(db: LumeDatabase) : ChatMessagesRepository {
    private val dao = db.chatMessagesDao()

    override suspend fun insert(messages: Collection<ChatMessage>) =
        dao.insertOrReplace(messages)

    override suspend fun insert(message: ChatMessage) =
        dao.insertOrReplace(message)

    override suspend fun byChatUid(chatUid: String): List<ChatMessage> =
        dao.byChatUid(chatUid)
}