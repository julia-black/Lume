package com.singlelab.net.repositories.chat

import com.singlelab.net.ApiUnit
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.chat.ChatMessageRequest
import com.singlelab.net.model.chat.ChatMessageResponse
import com.singlelab.net.model.chat.ChatMessagesResponse
import com.singlelab.net.repositories.BaseRepository

interface ChatRepository {
    @Throws(ApiException::class)
    suspend fun loadChatByUid(chatUid: String): ChatMessagesResponse?

    @Throws(ApiException::class)
    suspend fun loadPersonChat(personUid: String): ChatMessagesResponse?

    @Throws(ApiException::class)
    suspend fun sendMessage(message: ChatMessageRequest): ChatMessageResponse?

    @Throws(ApiException::class)
    suspend fun loadNewMessages(
        chatUid: String,
        lastMessageUid: String?
    ): List<ChatMessageResponse>?
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

    override suspend fun sendMessage(message: ChatMessageRequest): ChatMessageResponse? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.chatsApi.sendMessageAsync(message).await() },
            errorMessage = "Не удалось отправить сообщение"
        )
    }

    override suspend fun loadNewMessages(
        chatUid: String,
        lastMessageUid: String?
    ): List<ChatMessageResponse>? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.chatsApi.loadNewMessageAsync(chatUid, lastMessageUid).await() },
            errorMessage = "Не удалось получить сообщение"
        )
    }
}