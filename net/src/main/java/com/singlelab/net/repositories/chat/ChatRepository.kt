package com.singlelab.net.repositories.chat

import com.singlelab.net.ApiUnit
import com.singlelab.net.model.chat.ChatMessageRequest
import com.singlelab.net.model.chat.ChatMessageResponse
import com.singlelab.net.model.chat.ChatMessagesResponse
import com.singlelab.net.repositories.BaseRepository

interface ChatRepository {
    suspend fun loadNewMessages(chatUid: String, lastMessageUid: String?): List<ChatMessageResponse>?
    suspend fun sendMessage(message: ChatMessageRequest): ChatMessageResponse?
    suspend fun loadPersonChat(personUid: String): ChatMessagesResponse?
    suspend fun loadChatByUid(chatUid: String): ChatMessagesResponse?
}

class DefaultChatRepository(
    private val apiUnit: ApiUnit
) : BaseRepository(), ChatRepository {

    private val chatsApi = apiUnit.chatsApi

    override suspend fun loadNewMessages(chatUid: String, lastMessageUid: String?) =
        safeApiCall(
            apiUnit = apiUnit,
            call = { chatsApi.loadNewMessageAsync(chatUid, lastMessageUid).await() },
            errorMessage = "Не удалось получить сообщение"
        )

    override suspend fun sendMessage(message: ChatMessageRequest) =
        safeApiCall(
            apiUnit = apiUnit,
            call = { chatsApi.sendMessageAsync(message).await() },
            errorMessage = "Не удалось отправить сообщение"
        )

    override suspend fun loadPersonChat(personUid: String) =
        safeApiCall(
            apiUnit = apiUnit,
            call = { chatsApi.chatWithPersonAsync(personUid).await() },
            errorMessage = "Не удалось загрузить чат"
        )

    override suspend fun loadChatByUid(chatUid: String) =
        safeApiCall(
            apiUnit = apiUnit,
            call = { chatsApi.chatAsync(chatUid).await() },
            errorMessage = "Не удалось загрузить чат"
        )
}