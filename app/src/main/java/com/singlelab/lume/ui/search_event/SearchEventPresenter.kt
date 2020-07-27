package com.singlelab.lume.ui.search_event

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.search_event.interactor.SearchEventInteractor
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.event.SearchEventRequest
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class SearchEventPresenter @Inject constructor(
    private val interactor: SearchEventInteractor,
    preferences: Preferences?
) : BasePresenter<SearchEventView>(preferences, interactor as BaseInteractor) {

    fun search(searchStr: String) {
        if (searchStr.isEmpty()) {
            viewState.showEmptyQuery()
        } else {
            viewState.showLoading(true)
            invokeSuspend {
                try {
                    val searchResults = interactor.search(
                        SearchEventRequest(name = searchStr)
                    )
                    runOnMainThread {
                        viewState.showLoading(false)
                        viewState.showSearchResults(searchResults)
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
}