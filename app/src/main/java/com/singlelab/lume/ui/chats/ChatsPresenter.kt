package com.singlelab.lume.ui.chats

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.chats.common.toDbEntities
import com.singlelab.lume.ui.chats.common.toUiEntities
import com.singlelab.lume.ui.chats.interactor.ChatsInteractor
import com.singlelab.net.exceptions.ApiException
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class ChatsPresenter
@Inject
constructor(
    private val interactor: ChatsInteractor,
    preferences: Preferences?
) : BasePresenter<ChatsView>(
    preferences,
    interactor as BaseInteractor
) {
    override fun attachView(view: ChatsView?) {
        super.attachView(view)
        showChats()
    }

    private fun showChats() {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                // TODO: Сделать прогресс бар для загрузки чатов с сервера, изначально показывать чаты из бд?
                val remoteChats = interactor.remoteChats()
                if (remoteChats != null) {
                    interactor.saveChats(remoteChats.toDbEntities())
                }
                showLocalChats()
            } catch (e: ApiException) {
                showLocalChats()
            }
        }
    }

    private suspend fun showLocalChats() {
        val chats = interactor.localChats().toUiEntities()
        runOnMainThread {
            viewState.showLoading(false)
            if (chats.isEmpty()) {
                viewState.showEmptyChats()
            } else {
                viewState.showChats(chats)
            }
        }
    }
}