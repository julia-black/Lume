package com.singlelab.lume.ui.chat.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.database.entity.ChatMessage
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.chat.ChatMessagesResponse
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.lume.database.repository.ChatMessagesRepository as LocalChatRepository
import com.singlelab.net.repositories.chat.ChatRepository as RemoteChatRepository

interface ChatInteractor {
    @Throws(ApiException::class)
    suspend fun loadChatByUid(chatUid: String): ChatMessagesResponse?

    @Throws(ApiException::class)
    suspend fun loadPersonChat(personUid: String): ChatMessagesResponse?

    suspend fun saveChatMessages(messages: List<ChatMessage>)

    suspend fun all(): List<ChatMessage>
}

class DefaultChatInteractor(
    private val remoteRepository: RemoteChatRepository,
    private val localRepository: LocalChatRepository
) : BaseInteractor(
    remoteRepository as BaseRepository
), ChatInteractor {

    override suspend fun loadChatByUid(chatUid: String) =
        remoteRepository.loadChatByUid(chatUid)

    override suspend fun loadPersonChat(personUid: String) =
        remoteRepository.loadPersonChat(personUid)

    override suspend fun saveChatMessages(messages: List<ChatMessage>) =
        localRepository.insert(messages)

    override suspend fun all() =
        localRepository.all()
}