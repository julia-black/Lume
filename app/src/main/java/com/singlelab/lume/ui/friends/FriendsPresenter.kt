package com.singlelab.lume.ui.friends

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.friends.interactor.FriendsInteractor
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.auth.AuthData
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class FriendsPresenter @Inject constructor(
    private var interactor: FriendsInteractor,
    preferences: Preferences?
) : BasePresenter<FriendsView>(preferences, interactor as BaseInteractor) {

    var eventUid: String? = null

    private var friends: List<Person>? = null

    private var searchResults: List<Person>? = null

    private var pageNumber = 1

    private var pageSize = Const.PAGE_SIZE

    fun loadFriends() {
        val uid = AuthData.uid ?: return
        viewState.showLoading(true)
        invokeSuspend {
            try {
                friends = interactor.getFriends(uid)
                runOnMainThread {
                    viewState.showLoading(false)
                    viewState.showFriends(friends)
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(false)
                    viewState.showError(e.message)
                }
            }
        }
    }

    fun search(searchStr: String) {
        if (searchStr.isEmpty()) {
            viewState.showFriends(friends)
        } else {
            viewState.showLoading(true)
            invokeSuspend {
                try {
                    searchResults = interactor.search(searchStr, pageNumber, pageSize)
                    runOnMainThread {
                        viewState.showLoading(false)
                        viewState.showSearchResult(searchResults)
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

    fun addToFriends(personUid: String) {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                interactor.addToFriends(personUid)
                runOnMainThread {
                    searchResults?.find {
                        it.personUid == personUid
                    }?.isFriend = true
                    viewState.showLoading(false)
                    viewState.showSearchResult(searchResults)
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(false)
                    viewState.showError(e.message)
                }
            }
        }
    }

    fun invitePerson(personUid: String, eventUid: String, isSearchResult: Boolean) {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                interactor.invitePerson(personUid, eventUid)
                runOnMainThread {
                    if (isSearchResult) {
                        searchResults?.find {
                            it.personUid == personUid
                        }?.isInvited = true
                        viewState.showSearchResult(searchResults)
                    } else {
                        friends?.find {
                            it.personUid == personUid
                        }?.isInvited = true
                        viewState.showFriends(friends)
                    }
                    viewState.showLoading(false)
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