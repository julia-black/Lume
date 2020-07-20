package com.singlelab.lume.ui.chats.interactor

import com.singlelab.data.repositories.BaseRepository
import com.singlelab.data.repositories.chat.ChatRepository
import com.singlelab.lume.base.BaseInteractor

interface ChatInteractor {
}

class DefaultChatInteractor(
    private val repository: ChatRepository
) : BaseInteractor(repository as BaseRepository), ChatInteractor {

}