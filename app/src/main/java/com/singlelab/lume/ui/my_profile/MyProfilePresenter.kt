package com.singlelab.lume.ui.my_profile

import com.singlelab.lume.ui.my_profile.interactor.MyProfileInteractor
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class MyProfilePresenter @Inject constructor(private var interactor: MyProfileInteractor) :
    MvpPresenter<MyProfileView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        interactor.loadProfile()
        viewState.showProfile()
    }
}