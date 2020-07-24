package com.singlelab.lume.ui.chats.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.database.entity.Chat
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.chat.ChatResponse
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.lume.database.repository.ChatsRepository as LocalChatsRepository
import com.singlelab.net.repositories.chats.ChatsRepository as RemoteChatsRepository

interface ChatsInteractor {
    @Throws(ApiException::class)
    suspend fun remoteChats(): List<ChatResponse>?

    suspend fun saveChats(chats: List<Chat>)
    suspend fun localChats(): List<Chat>
}

class DefaultChatsInteractor(
    private val remoteRepository: RemoteChatsRepository,
    private val localRepository: LocalChatsRepository
) : BaseInteractor(
    remoteRepository as BaseRepository
), ChatsInteractor {

    override suspend fun remoteChats() =
        remoteRepository.loadChats()

    override suspend fun saveChats(chats: List<Chat>) =
        localRepository.insert(chats)

    override suspend fun localChats() =
        localRepository.all()
}