package com.singlelab.lume.ui.chat

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.chat.common.ChatMessageItem
import com.singlelab.lume.ui.chat.common.ChatOpeningInvocationType
import com.singlelab.lume.ui.chat.common.toDbEntities
import com.singlelab.lume.ui.chat.interactor.ChatInteractor
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
        //val messages = interactor.all()
        val messages = listOf(
            ChatMessageItem("1", "Привет", false, ChatMessageItem.Type.INCOMING),
            ChatMessageItem("2", "Ghbdtn", false, ChatMessageItem.Type.OUTGOING),
            ChatMessageItem("3", "Привет привет", false, ChatMessageItem.Type.INCOMING),
            ChatMessageItem(
                "4",
                "Большое сообщение Большое сообщение Большое сообщение Большое сообщение Большое сообщение Большое сообщение Большое сообщение Большое сообщение Большое сообщение Большое сообщение Большое сообщение Большое сообщение Большое сообщение Большое сообщение Большое сообщение",
                false,
                ChatMessageItem.Type.OUTGOING
            ),
            ChatMessageItem("5", "Просто сообщение", false, ChatMessageItem.Type.INCOMING),
            ChatMessageItem("6", "цифры дальше пошли", false, ChatMessageItem.Type.OUTGOING),
            ChatMessageItem("7", "7", false, ChatMessageItem.Type.INCOMING),
            ChatMessageItem("8", "8", false, ChatMessageItem.Type.INCOMING),
            ChatMessageItem("9", "9", false, ChatMessageItem.Type.OUTGOING),
            ChatMessageItem("10", "10", false, ChatMessageItem.Type.INCOMING),
            ChatMessageItem("11", "11", false, ChatMessageItem.Type.OUTGOING),
            ChatMessageItem("12", "12", false, ChatMessageItem.Type.OUTGOING),
            ChatMessageItem("13", "13", false, ChatMessageItem.Type.OUTGOING),
            ChatMessageItem("14", "НУЖНО БОЛЬШЕ СООБЩЕНИЙ ДЛЯ СКРОЛЛА", false, ChatMessageItem.Type.INCOMING),
            ChatMessageItem("15", "15", false, ChatMessageItem.Type.OUTGOING),
            ChatMessageItem("16", "16", false, ChatMessageItem.Type.OUTGOING),
            ChatMessageItem("17", "17", false, ChatMessageItem.Type.OUTGOING),
            ChatMessageItem("18", "18", false, ChatMessageItem.Type.OUTGOING),
            ChatMessageItem("19", "19", false, ChatMessageItem.Type.INCOMING),
            ChatMessageItem("20", "20", false, ChatMessageItem.Type.INCOMING),
            ChatMessageItem("21", "21", false, ChatMessageItem.Type.OUTGOING),
            ChatMessageItem("22", "22", false, ChatMessageItem.Type.INCOMING),
            ChatMessageItem("23", "23", false, ChatMessageItem.Type.OUTGOING),
            ChatMessageItem("24", "24", false, ChatMessageItem.Type.INCOMING),
            ChatMessageItem("25", "25", false, ChatMessageItem.Type.OUTGOING),
            ChatMessageItem("26", "26", false, ChatMessageItem.Type.INCOMING),
            ChatMessageItem("27", "27", false, ChatMessageItem.Type.OUTGOING),
            ChatMessageItem("28", "28", false, ChatMessageItem.Type.INCOMING),
            ChatMessageItem("29", "29", false, ChatMessageItem.Type.OUTGOING),
            ChatMessageItem("20", "30", false, ChatMessageItem.Type.OUTGOING)
        )
        runOnMainThread {
            viewState.showLoading(false)
            if (messages.isEmpty()) {
                viewState.showEmptyChat()
            } else {
                val currentPersonUid = AuthData.uid
                if (currentPersonUid != null) {
                    viewState.showChat(messages/*.toUiEntities(currentPersonUid)*/)
                } else {
                    viewState.showError("Не удалось определить пользователя")
                }
            }
        }
    }

}