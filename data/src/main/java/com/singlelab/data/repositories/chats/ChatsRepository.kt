package com.singlelab.data.repositories.chats

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.chats.ChatsInfoItem
import com.singlelab.data.repositories.BaseRepository

interface ChatsRepository {
    @Throws(ApiException::class)
    suspend fun loadChats(): List<ChatsInfoItem>
}

class DefaultChatsRepository : BaseRepository(), ChatsRepository {
    @Throws(ApiException::class)
    override suspend fun loadChats() =
        listOf(
            ChatsInfoItem("", "Chat 1 title"),
            ChatsInfoItem("", "Chat 2 title"),
            ChatsInfoItem("", "Chat 3 title"),
            ChatsInfoItem("", "Chat 4 title"),
            ChatsInfoItem("", "Chat 5 title"),
            ChatsInfoItem("", "Chat 6 title"),
            ChatsInfoItem("", "Chat 7 title"),
            ChatsInfoItem("", "Chat 8 title"),
            ChatsInfoItem("", "Chat 9 title"),
            ChatsInfoItem("", "Chat 10 title"),
            ChatsInfoItem("", "Chat 11 title"),
            ChatsInfoItem("", "Chat 12 title"),
            ChatsInfoItem("", "Chat 13 title"),
            ChatsInfoItem("", "Chat 14 title"),
            ChatsInfoItem("", "Chat 15 title"),
            ChatsInfoItem("", "Chat 16 title"),
            ChatsInfoItem("", "Chat 17 title"),
            ChatsInfoItem("", "Chat 18 title"),
            ChatsInfoItem("", "Chat 19 title"),
            ChatsInfoItem("", "Chat 20 title")
        )
}