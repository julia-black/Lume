package com.singlelab.lume.ui.chat.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.database.entity.ChatMessage
import com.singlelab.net.model.chat.ChatMessageRequest
import com.singlelab.net.model.chat.ChatMessageResponse
import com.singlelab.net.model.chat.ChatMessagesResponse
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.lume.database.repository.ChatMessagesRepository as LocalChatRepository
import com.singlelab.net.repositories.chat.ChatRepository as RemoteChatRepository

interface ChatInteractor {
    suspend fun loadNewMessages(
        chatUid: String,
        lastMessageUid: String?
    ): List<ChatMessageResponse>?

    suspend fun sendMessage(message: ChatMessageRequest): ChatMessageResponse?
    suspend fun loadPersonChat(personUid: String, page: Int): ChatMessagesResponse?
    suspend fun loadChatByUid(chatUid: String, page: Int): ChatMessagesResponse?

    suspend fun saveChatMessages(messages: List<ChatMessage>)
    suspend fun saveChatMessage(message: ChatMessage)
    suspend fun byChatUid(chatUid: String): List<ChatMessage>
    suspend fun muteChat(chatUid: String, isMute: Boolean)
}

class DefaultChatInteractor(
    private val remoteRepository: RemoteChatRepository,
    private val localRepository: LocalChatRepository
) : BaseInteractor(
    remoteRepository as BaseRepository
), ChatInteractor {

    override suspend fun loadNewMessages(chatUid: String, lastMessageUid: String?) =
        remoteRepository.loadNewMessages(chatUid, lastMessageUid)

    override suspend fun sendMessage(message: ChatMessageRequest) =
        remoteRepository.sendMessage(message)

    override suspend fun loadPersonChat(personUid: String, page: Int) =
        remoteRepository.loadPersonChat(personUid, page)

    override suspend fun loadChatByUid(chatUid: String, page: Int) =
        remoteRepository.loadChatByUid(chatUid, page)

    override suspend fun saveChatMessages(messages: List<ChatMessage>) =
        localRepository.insert(messages)

    override suspend fun saveChatMessage(message: ChatMessage) =
        localRepository.insert(message)

    override suspend fun byChatUid(chatUid: String) =
        localRepository.byChatUid(chatUid)

    override suspend fun muteChat(chatUid: String, isMute: Boolean) {
        remoteRepository.muteChat(chatUid, isMute)
    }
}