package com.singlelab.net.repositories.chat

import com.singlelab.net.ApiUnit
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.chat.ChatMessagesResponse
import com.singlelab.net.repositories.BaseRepository

interface ChatRepository {
    @Throws(ApiException::class)
    suspend fun loadChatByUid(chatUid: String): ChatMessagesResponse?

    @Throws(ApiException::class)
    suspend fun loadPersonChat(personUid: String): ChatMessagesResponse?
}

class DefaultChatRepository(private val apiUnit: ApiUnit) : BaseRepository(), ChatRepository {
    override suspend fun loadChatByUid(chatUid: String): ChatMessagesResponse? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.chatsApi.chatAsync(chatUid).await() },
            errorMessage = "Не удалось загрузить чат"
        )
    }

    override suspend fun loadPersonChat(personUid: String): ChatMessagesResponse? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.chatsApi.chatWithPersonAsync(personUid).await() },
            errorMessage = "Не удалось загрузить чат"
        )
    }
}