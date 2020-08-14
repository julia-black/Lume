package com.singlelab.net.repositories.chats

import com.singlelab.net.ApiUnit
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.chat.ChatResponse
import com.singlelab.net.repositories.BaseRepository

interface ChatsRepository {
    @Throws(ApiException::class)
    suspend fun loadChats(): List<ChatResponse>?
}

class DefaultChatsRepository(private val apiUnit: ApiUnit) : BaseRepository(apiUnit), ChatsRepository {
    override suspend fun loadChats(): List<ChatResponse>? {
        return safeApiCall(
            call = { apiUnit.chatsApi.chatsAsync().await() },
            errorMessage = "Не удалось загрузить чаты"
        )
    }
}