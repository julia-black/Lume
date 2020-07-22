package com.singlelab.lume.ui.chat.interactor

import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.chat.ChatRepository
import com.singlelab.lume.base.BaseInteractor

interface ChatInteractor {
}

class DefaultChatInteractor(
    private val repository: ChatRepository
) : BaseInteractor(repository as BaseRepository), ChatInteractor {

}