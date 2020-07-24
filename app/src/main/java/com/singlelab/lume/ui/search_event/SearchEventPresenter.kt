package com.singlelab.lume.ui.search_event

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.search_event.interactor.SearchEventInteractor
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class SearchEventPresenter @Inject constructor(
    private val interactor: SearchEventInteractor,
    preferences: Preferences?
) : BasePresenter<SearchEventView>(preferences, interactor as BaseInteractor) {
}