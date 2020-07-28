package com.singlelab.lume.ui.chat

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.chat.common.ChatOpeningInvocationType
import com.singlelab.lume.ui.chat.common.toDbEntities
import com.singlelab.lume.ui.chat.common.toUiEntities
import com.singlelab.lume.ui.chat.interactor.ChatInteractor
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.exceptions.TimeoutException
import com.singlelab.net.model.auth.AuthData
import com.singlelab.net.model.chat.ChatMessageRequest
import com.singlelab.net.model.chat.ChatMessageResponse
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class ChatPresenter
@Inject
constructor(
    private val interactor: ChatInteractor,
    preferences: Preferences?
) : BasePresenter<ChatView>(
    preferences,
    interactor as BaseInteractor
) {
    private val chatSettings = ChatSettings()

    fun sendMessage(messageText: String) {
        invokeSuspend {
            try {
                chatSettings.chatUid?.let { chatUid ->
                    interactor.sendMessage(ChatMessageRequest(chatUid, messageText))
                }
            } catch (e: ApiException) {
                viewState.showError(e.message)
            }
        }
    }

    fun showChat(type: ChatOpeningInvocationType) {
        // TODO: Сделать прогресс бар для загрузки сообщений с сервера, изначально показывать сообщения из бд?
        chatSettings.chatType = type
        viewState.showLoading(true)
        invokeSuspend {
            try {
                val chatResponse = when (type) {
                    is ChatOpeningInvocationType.Person -> interactor.loadPersonChat(type.personUid)
                    is ChatOpeningInvocationType.Common -> interactor.loadChatByUid(type.chatUid)
                    else -> null
                }

                if (chatResponse != null) {
                    chatSettings.chatUid = chatResponse.chatUid
                    chatSettings.setLastMessageUid(chatResponse.messages)
                    saveChatMessages(chatResponse.messages)
                }

                showLocalMessages()
                syncMessages()
            } catch (e: Exception) {
                showLocalMessages()
            }
        }
    }

    private suspend fun showLocalMessages() {
        val chatUid = chatSettings.chatUid
        val chatType = chatSettings.chatType
        val currentPersonUid = AuthData.uid
        val messages = if (chatUid != null && chatType != null && currentPersonUid != null) {
            interactor.byChatUid(chatUid).toUiEntities(chatType, currentPersonUid)
        } else {
            emptyList()
        }

        runOnMainThread {
            viewState.showLoading(false)
            if (messages.isEmpty()) {
                viewState.showEmptyChat()
            } else {
                viewState.showChat(messages)
            }
        }
    }

    private suspend fun syncMessages() {
        try {
            val chatUid = chatSettings.chatUid
            val chatLastMessage = chatSettings.lastMessageUid
            if (chatUid != null && chatLastMessage != null) {
                val messagesResponse = interactor.loadNewMessages(chatUid, chatLastMessage)
                chatSettings.setLastMessageUid(messagesResponse)
                saveChatMessages(messagesResponse)
            }
            showLocalMessages()
            syncMessages()
        } catch (e: ApiException) {
            if (e is TimeoutException) {
                syncMessages()
            } else {
                runOnMainThread {
                    viewState.showError(e.message)
                }
            }
        }
    }

    private suspend fun saveChatMessages(chatResponseMessages: List<ChatMessageResponse>?) {
        if (chatResponseMessages != null) {
            interactor.saveChatMessages(chatResponseMessages.toDbEntities(chatSettings.chatUid))
        }
    }

    data class ChatSettings(
        private var _lastMessageUid: String? = null,
        var chatType: ChatOpeningInvocationType? = null,
        var chatUid: String? = null
    ) {
        val lastMessageUid = _lastMessageUid

        fun setLastMessageUid(messagesResponse: List<ChatMessageResponse>?) {
            if (!messagesResponse.isNullOrEmpty()) {
                messagesResponse.last().messageUid?.let { lastMessageUid ->
                    _lastMessageUid = lastMessageUid
                }
            }
        }
    }
}