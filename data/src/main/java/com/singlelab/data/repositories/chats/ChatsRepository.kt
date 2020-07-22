package com.singlelab.data.repositories.chats

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.chat.ChatInfo
import com.singlelab.data.net.ApiUnit
import com.singlelab.data.repositories.BaseRepository

interface ChatsRepository {
    @Throws(ApiException::class)
    suspend fun loadChats(): List<ChatInfo>
}

class DefaultChatsRepository(private val apiUnit: ApiUnit) : BaseRepository(), ChatsRepository {
    @Throws(ApiException::class)
    override suspend fun loadChats() =
        listOf(
            ChatInfo("", "Chat 1 title"),
            ChatInfo("", "Chat 2 title"),
            ChatInfo("", "Chat 3 title"),
            ChatInfo("", "Chat 4 title"),
            ChatInfo("", "Chat 5 title"),
            ChatInfo("", "Chat 6 title"),
            ChatInfo("", "Chat 7 title"),
            ChatInfo("", "Chat 8 title"),
            ChatInfo("", "Chat 9 title"),
            ChatInfo("", "Chat 10 title"),
            ChatInfo("", "Chat 11 title"),
            ChatInfo("", "Chat 12 title"),
            ChatInfo("", "Chat 13 title"),
            ChatInfo("", "Chat 14 title"),
            ChatInfo("", "Chat 15 title"),
            ChatInfo("", "Chat 16 title"),
            ChatInfo("", "Chat 17 title"),
            ChatInfo("", "Chat 18 title"),
            ChatInfo("", "Chat 19 title"),
            ChatInfo("", "Chat 20 title")
        )
}