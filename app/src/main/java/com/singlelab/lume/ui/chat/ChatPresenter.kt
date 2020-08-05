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

    fun showChat(type: ChatOpeningInvocationType) {
        // TODO: Сделать прогресс бар для загрузки сообщений с сервера, изначально показывать сообщения из бд?
        viewState.showLoading(true)
        invokeSuspend {
            try {
                val chatResponse = when (type) {
                    is ChatOpeningInvocationType.Person -> interactor.loadPersonChat(type.personUid)
                    is ChatOpeningInvocationType.Common -> interactor.loadChatByUid(type.chatUid)
                    else -> null
                }

                if (chatResponse != null) {
                    chatSettings.chatType = type
                    chatSettings.chatUid = chatResponse.chatUid
                    chatSettings.setLastMessageUid(chatResponse.messages)
                    saveChatMessages(chatResponse.messages)
                    showLocalMessages()
                    syncMessages()
                } else {
                    runOnMainThread {
                        viewState.showError("Ошибка при загрузке чата")
                    }
                }
            } catch (e: Exception) {
                showLocalMessages()
            }
        }
    }

    fun sendMessage(messageText: String, images: List<String>) {
        invokeSuspend {
            try {
                val chatUid = chatSettings.chatUid
                if (chatUid != null) {
                    val newMessage = interactor.sendMessage(ChatMessageRequest(chatUid, messageText, images))
                    if (newMessage != null) {
                        val messageEntity = newMessage.toDbEntity(chatUid)
                        if (messageEntity != null) {
                            interactor.saveChatMessage(messageEntity)
                            val currentPersonUid = AuthData.uid
                            val chatType = chatSettings.chatType
                            if (chatType != null && currentPersonUid != null) {
                                val message = messageEntity.toUiEntity(chatType.isGroup, currentPersonUid)
                                runOnMainThread {
                                    viewState.showNewMessage(message)
                                }
                            }
                        }
                    }
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showError(e.message)
                }
            }
        }
    }

    private suspend fun showLocalMessages() {
        val chatUid = chatSettings.chatUid
        val chatType = chatSettings.chatType
        val currentPersonUid = AuthData.uid
        val messages = if (chatUid != null && chatType != null && currentPersonUid != null) {
            interactor.byChatUid(chatUid).toUiEntities(chatType.isGroup, currentPersonUid)
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
            if (chatUid != null) {
                val messagesResponse = interactor.loadNewMessages(chatUid, chatLastMessage)
                chatSettings.setLastMessageUid(messagesResponse)
                saveChatMessages(messagesResponse)
                showLocalMessages()
            }
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
        val lastMessageUid
            get() = _lastMessageUid

        fun setLastMessageUid(messagesResponse: List<ChatMessageResponse>?) {
            if (!messagesResponse.isNullOrEmpty()) {
                messagesResponse.last().messageUid?.let { lastMessageUid ->
                    _lastMessageUid = lastMessageUid
                }
            }
        }
    }
}