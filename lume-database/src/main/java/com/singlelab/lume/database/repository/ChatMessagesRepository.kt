package com.singlelab.lume.database.repository

import com.singlelab.lume.database.LumeDatabase
import com.singlelab.lume.database.entity.ChatMessage

interface ChatMessagesRepository {
    suspend fun insert(messages: Collection<ChatMessage>)
    suspend fun insert(message: ChatMessage)
    suspend fun all(): List<ChatMessage>
    suspend fun last50messages(): List<ChatMessage>
}

class RoomChatMessagesRepository(db: LumeDatabase) : ChatMessagesRepository {
    private val dao = db.chatMessagesDao()

    override suspend fun insert(messages: Collection<ChatMessage>) =
        dao.insertOrReplace(messages)

    override suspend fun insert(message: ChatMessage) =
        dao.insertOrReplace(message)

    override suspend fun all(): List<ChatMessage> =
        dao.all()

    override suspend fun last50messages(): List<ChatMessage> =
        dao.last50messages()
}