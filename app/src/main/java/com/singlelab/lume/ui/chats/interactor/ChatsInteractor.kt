package com.singlelab.lume.ui.chats.interactor

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.chat.ChatInfo
import com.singlelab.data.repositories.BaseRepository
import com.singlelab.data.repositories.chats.ChatsRepository
import com.singlelab.lume.base.BaseInteractor

interface ChatsInteractor {
    @Throws(ApiException::class)
    suspend fun loadChats(): List<ChatInfo>
}

class DefaultChatsInteractor(
    private val repository: ChatsRepository
) : BaseInteractor(repository as BaseRepository), ChatsInteractor {
    override suspend fun loadChats() =
        repository.loadChats()
}