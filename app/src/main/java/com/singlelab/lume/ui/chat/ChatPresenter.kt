package com.singlelab.lume.ui.chat

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.chat.common.ChatOpeningInvocationType
import com.singlelab.lume.ui.chat.common.toDbEntities
import com.singlelab.lume.ui.chat.common.toUiEntities
import com.singlelab.lume.ui.chat.interactor.ChatInteractor
import com.singlelab.lume.ui.chats.common.toUiEntities
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.auth.AuthData
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class ChatPresenter
@Inject
constructor(
    private val interactor: ChatInteractor,
    preferences: Preferences?
) : BasePresenter<ChatView>(preferences, interactor as BaseInteractor) {

    fun showChat(type: ChatOpeningInvocationType) {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                val chatMessagesResponse = when (type) {
                    is ChatOpeningInvocationType.Person -> interactor.loadPersonChat(type.personUid)
                    is ChatOpeningInvocationType.Common -> interactor.loadChatByUid(type.chatUid)
                    else -> null
                }
                chatMessagesResponse?.messages?.let { messages ->
                    interactor.saveChatMessages(messages.toDbEntities())
                }
                showLocalMessages()
                runOnMainThread { viewState.showError("Сообщения синхронизованы с сервером") } // TODO: Нужно для демонстрации работы бд, удалить
            } catch (e: ApiException) {
                // Если не получилось загрузить чат, показываем чат из БД
                showLocalMessages()
                runOnMainThread { viewState.showError("Нет сети, показаны сообщения из БД") } // TODO: Нужно для демонстрации работы бд, удалить
            }
        }
    }

    private suspend fun showLocalMessages() {
        val messages = interactor.all()
        runOnMainThread {
            viewState.showLoading(false)
            if (messages.isEmpty()) {
                viewState.showEmptyChat()
            } else {
                val currentPersonUid = AuthData.uid
                if (currentPersonUid != null) {
                    viewState.showChat(messages.toUiEntities(currentPersonUid))
                } else {
                    viewState.showError("Не удалось определить пользователя")
                }
            }
        }
    }

}