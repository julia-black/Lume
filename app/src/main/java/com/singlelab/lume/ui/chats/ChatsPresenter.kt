package com.singlelab.lume.ui.chats

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.auth.AuthData
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.chats.interactor.ChatsInteractor
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class ChatsPresenter
@Inject
constructor(
    private val interactor: ChatsInteractor,
    preferences: Preferences?
) : BasePresenter<ChatsView>(preferences, interactor as BaseInteractor) {

    override fun attachView(view: ChatsView?) {
        super.attachView(view)
        loadChats()
    }

    private fun loadChats() {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                if (!AuthData.isAnon) {
                    val chats = interactor.loadChats()
                    runOnMainThread {
                        viewState.showLoading(false)
                        if (chats != null) {
                            viewState.showChats(chats)
                        } else {
                            viewState.showError("Не удалость загрузить чаты")
                        }
                    }
                } else {
                    runOnMainThread {
                        preferences?.clearAuth()
                        viewState.showLoading(false)
                        viewState.showError("Для использования чатов сначала необходимо авторизоваться")
                    }
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