package com.singlelab.lume.ui.chat

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.event.Event
import com.singlelab.lume.model.profile.Profile
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.chat.common.ChatOpeningInvocationType
import com.singlelab.lume.ui.chat.common.toDbEntities
import com.singlelab.lume.ui.chat.common.toUiEntities
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
    private var currentEvent: Event? = null
    private var currentPerson: Profile? = null

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
                val chatResponse = when (type) {
                    is ChatOpeningInvocationType.Person -> interactor.loadPersonChat(type.personUid)
                    is ChatOpeningInvocationType.Common -> interactor.loadChatByUid(type.chatUid)
                    else -> null
                }

                when (type) {
                    is ChatOpeningInvocationType.Person -> currentPerson = interactor.loadPerson(type.personUid)
                    // fill currentEvent
                }

                chatResponse?.let { chat ->
                    currentChatUid = chat.uId ?: ""
                    chat.messages?.let { messages ->
                        interactor.saveChatMessages(messages.toDbEntities(currentChatUid, currentPerson?.name ?: "", currentPerson?.imageContentUid ?: ""))
                        if (messages.isNotEmpty()) {
                            messages.last().uId?.let { lastMessageUid ->
                                this.lastMessageUid = lastMessageUid
                            }
                        }
                    }
                }
                showLocalMessages()
                loadNewMessage()
            } catch (e: ApiException) {
                showLocalMessages()
            }
        }
    }

    private suspend fun showLocalMessages() {
        val messages = interactor.byChatUid(currentChatUid)
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
                    val messages = messageResponse.toDbEntities(currentChatUid, currentPerson?.name ?: "", currentPerson?.imageContentUid ?: "")
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