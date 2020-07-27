package com.singlelab.lume.ui.chat

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.chat.common.*
import com.singlelab.lume.ui.chat.interactor.ChatInteractor
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.exceptions.TimeoutException
import com.singlelab.net.model.auth.AuthData
import com.singlelab.net.model.chat.ChatMessageRequest
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class ChatPresenter
@Inject
constructor(
    private val interactor: ChatInteractor,
    preferences: Preferences?
) : BasePresenter<ChatView>(preferences, interactor as BaseInteractor) {

    private var currentChatUid: String = ""
    private var lastMessageUid: String? = null

    fun sendMessage(messageText: String) {
        invokeSuspend {
            try {
                interactor.sendMessage(ChatMessageRequest(currentChatUid, messageText))
            } catch (e: ApiException) {
                viewState.showError(e.message)
            }
        }
    }

    fun showChat(type: ChatOpeningInvocationType) {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                when (type) {
                    is ChatOpeningInvocationType.Person -> interactor.loadPersonChat(type.personUid)
                    is ChatOpeningInvocationType.Common -> interactor.loadChatByUid(type.chatUid)
                    else -> null
                }?.let { chat ->
                    currentChatUid = chat.uId ?: ""
                    chat.messages?.let { messages ->
                        interactor.saveChatMessages(messages.toDbEntities())
                        if (messages.isNotEmpty()) {
                            messages.last().uId?.let { lastMessageUid ->
                                this.lastMessageUid = lastMessageUid
                            }
                        }
                    }
                }
                showLocalMessages()
                loadNewMessage()
                runOnMainThread { viewState.showError("Сообщения синхронизованы с сервером") } // TODO: Нужно для демонстрации работы бд, удалить
            } catch (e: ApiException) {
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

    private fun loadNewMessage() {
        invokeSuspend {
            try {
                val newMessagesResponse = interactor.loadNewMessages(currentChatUid, lastMessageUid)
                newMessagesResponse?.let { messageResponse ->
                    val messages = messageResponse.toDbEntities()
                    interactor.saveChatMessages(messages)
                    if (messages.isNotEmpty()) {
                        this.lastMessageUid = messages.last().uid
                    }
                    showLocalMessages()
                    loadNewMessage()
                }
            } catch (e: ApiException) {
                if (e is TimeoutException) {
                    loadNewMessage()
                } else {
                    runOnMainThread {
                        viewState.showError(e.message)
                    }
                }
            }
        }
    }
}