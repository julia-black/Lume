package com.singlelab.lume.ui.chat

import android.graphics.Bitmap
import com.singlelab.lume.analytics.Analytics
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.chat.common.*
import com.singlelab.lume.ui.chat.interactor.ChatInteractor
import com.singlelab.lume.util.resize
import com.singlelab.lume.util.toBase64
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.exceptions.TimeoutException
import com.singlelab.net.model.auth.AuthData
import com.singlelab.net.model.chat.ChatMessageRequest
import com.singlelab.net.model.chat.ChatMessageResponse
import com.singlelab.net.model.chat.ChatMessagesResponse
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
    var page: Int = 1
    val chatSettings = ChatSettings()

    private var isLoading: Boolean = false
    private var hasOldMessages: Boolean = true

    fun showChat(chatUid: String? = null, type: ChatOpeningInvocationType? = null, page: Int = 1) {
        // TODO: Сделать прогресс бар для загрузки сообщений с сервера, изначально показывать сообщения из бд?
        if (page == 1) {
            Analytics.logOpenChat()
            viewState.showLoading(true)
        }
        invokeSuspend {
            try {
                isLoading = true
                val chatResponse = when (type) {
                    is ChatOpeningInvocationType.Person -> interactor.loadPersonChat(
                        type.personUid,
                        page
                    )
                    is ChatOpeningInvocationType.Common -> interactor.loadChatByUid(
                        type.chatUid,
                        page
                    )
                    else -> {
                        if (chatUid != null) {
                            interactor.loadChatByUid(
                                chatUid,
                                page
                            )
                        } else {
                            null
                        }
                    }
                }
                if (chatResponse != null) {
                    setChatType(type, chatUid, chatResponse)
                    chatSettings.chatUid = chatResponse.chatUid
                    chatSettings.eventUid = chatResponse.eventUid
                    chatSettings.personUid = chatResponse.personUid
                    chatSettings.isMuted = chatResponse.isMuted
                    chatSettings.setLastMessageUid(chatResponse.messages)
                    runOnMainThread {
                        if (chatResponse.unreadMessagesCount > 0) {
                            updateNotifications()
                        }
                        viewState.showTitle(chatSettings.chatType?.title)
                        viewState.showMute(chatSettings.isMuted)
                    }
                    isLoading = false
                    if (chatResponse.messages.isNullOrEmpty()) {
                        hasOldMessages = false
                    } else {
                        saveChatMessages(chatResponse.messages)
                    }
                    showLocalMessages()
                    syncMessages()
                } else {
                    runOnMainThread { viewState.showError("Ошибка при загрузке чата") }
                }
            } catch (e: Exception) {
                isLoading = false
                showLocalMessages()
            }
        }
    }

    private fun setChatType(
        type: ChatOpeningInvocationType?,
        chatUid: String?,
        chatResponse: ChatMessagesResponse
    ) {
        if (type != null) {
            chatSettings.chatType = type
        } else if (chatUid != null) {
            chatSettings.chatType =
                if (chatResponse.isGroupChat != null && chatResponse.isGroupChat!! && chatResponse.chatName != null) {
                    ChatOpeningInvocationType.Common(chatResponse.chatName!!, chatUid, true)
                } else {
                    ChatOpeningInvocationType.Person(chatResponse.chatName!!, chatUid, false)
                }
        }
    }

    fun sendMessage(messageText: String, images: List<Bitmap>) {
        invokeSuspend {
            try {
                runOnMainThread { viewState.enableMessageSending(false) }
                val chatUid = chatSettings.chatUid
                if (chatUid != null) {
                    val compressedImages = images.map { it.resize().toBase64() }
                    val newMessage = interactor.sendMessage(
                        ChatMessageRequest(
                            chatUid,
                            messageText,
                            compressedImages
                        )
                    )
                    if (newMessage != null) {
                        chatSettings.setLastMessageUid(newMessage)
                        val messageEntity = newMessage.toDbEntity(chatUid)
                        if (messageEntity != null) {
                            interactor.saveChatMessage(messageEntity)
                            val currentPersonUid = AuthData.uid
                            val chatType = chatSettings.chatType
                            if (chatType != null && currentPersonUid != null) {
                                val message =
                                    messageEntity.toUiEntity(chatType.isGroup, currentPersonUid)
                                Analytics.logSendMessage()
                                runOnMainThread {
                                    viewState.showNewMessage(message)
                                }
                            }
                        }
                    }
                }
                runOnMainThread { viewState.enableMessageSending(true) }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showError(e.message)
                    viewState.enableMessageSending(true)
                }
            }
        }
    }

    fun onChatTitleClick() {
        if (chatSettings.chatType != null && chatSettings.chatType!!.isGroup) {
            chatSettings.eventUid?.let {
                viewState.navigateToEvent(it)
            }
        } else {
            chatSettings.personUid?.let {
                viewState.navigateToPerson(it)
            }
        }
    }

    fun onChatMuteClick() {
        viewState.showMuteDialog(chatSettings.isMuted)
    }

    fun muteChat() {
        chatSettings.chatUid?.let { uid ->
            viewState.showLoading(true)
            invokeSuspend {
                try {
                    interactor.muteChat(uid, !chatSettings.isMuted)
                    chatSettings.isMuted = !chatSettings.isMuted
                    runOnMainThread {
                        viewState.showLoading(false)
                        viewState.showMute(chatSettings.isMuted)
                    }
                } catch (e: ApiException) {
                    runOnMainThread {
                        viewState.showLoading(false)
                        viewState.showError(e.message)
                    }
                }
            }
        }
    }

    fun isNeedLoading() = !isLoading && hasOldMessages

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
                viewState.showChat(messages, page)
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
                runOnMainThread { viewState.showError(e.message) }
            }
        }
    }

    private suspend fun saveChatMessages(chatResponseMessages: List<ChatMessageResponse>?) {
        if (chatResponseMessages != null) {
            interactor.saveChatMessages(chatResponseMessages.toDbEntities(chatSettings.chatUid))
        }
    }

    val isGroup: Boolean = chatSettings.chatType != null && chatSettings.chatType!!.isGroup

    data class ChatSettings(
        private var _lastMessageUid: String? = null,
        var chatType: ChatOpeningInvocationType? = null,
        var chatUid: String? = null,
        var eventUid: String? = null,
        var personUid: String? = null,
        var isMuted: Boolean = false
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

        fun setLastMessageUid(messageResponse: ChatMessageResponse) {
            _lastMessageUid = messageResponse.messageUid
        }
    }
}