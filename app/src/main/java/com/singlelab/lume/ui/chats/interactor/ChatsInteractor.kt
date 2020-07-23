package com.singlelab.lume.ui.chats.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.chat.ChatInfo
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.lume.database.repository.ChatsRepository as LocalChatsRepository
import com.singlelab.net.repositories.chats.ChatsRepository as RemoteChatsRepository

interface ChatsInteractor {
    @Throws(ApiException::class)
    suspend fun loadChats(): List<ChatInfo>
}

class DefaultChatsInteractor(
    private val remoteRepository: RemoteChatsRepository,
    private val localRepository: LocalChatsRepository
) : BaseInteractor(remoteRepository as BaseRepository), ChatsInteractor {
    override suspend fun loadChats() =
        remoteRepository.loadChats()
}