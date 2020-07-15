package com.singlelab.lume.ui.auth

import android.util.Log
import com.singlelab.lume.model.Const
import com.singlelab.lume.ui.auth.interactor.AuthInteractor
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class AuthPresenter @Inject constructor(private var interactor: AuthInteractor) :
    MvpPresenter<AuthView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        Log.d(Const.LOG_TAG, "view attach auth")
    }
}