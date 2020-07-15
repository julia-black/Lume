package com.singlelab.lume.ui.my_profile

import androidx.navigation.NavController
import com.singlelab.lume.ui.my_profile.interactor.MyProfileInteractor
import com.singlelab.lume.ui.my_profile.router.MyProfileRouter
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class MyProfilePresenter @Inject constructor(
    private var interactor: MyProfileInteractor,
    private var router: MyProfileRouter
) :
    MvpPresenter<MyProfileView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        interactor.loadProfile()
        viewState.showProfile()
    }

    fun navigateToAuth(navController: NavController) {
        router.navigateToAuth(navController)
    }
}