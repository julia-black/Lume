package com.singlelab.lume.database.repository

import com.singlelab.lume.database.LumeDatabase
import com.singlelab.lume.database.entity.Chat

interface ChatsRepository {
    suspend fun insert(chats: Collection<Chat>)
    suspend fun insert(chat: Chat)
    suspend fun all(): List<Chat>
    suspend fun clear()
}

class RoomChatsRepository(db: LumeDatabase) : ChatsRepository {
    private val dao = db.chatsDao()

    override suspend fun insert(chats: Collection<Chat>) =
        dao.insertOrReplace(chats)

    override suspend fun insert(chat: Chat) =
        dao.insertOrReplace(chat)

    override suspend fun all() =
        dao.all()

    override suspend fun clear() =
        dao.clear()
}