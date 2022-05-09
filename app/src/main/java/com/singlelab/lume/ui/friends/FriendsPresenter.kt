package com.singlelab.lume.ui.friends

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.friends.interactor.FriendsInteractor
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.auth.AuthData
import com.singlelab.net.model.person.SearchPersonRequest
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class FriendsPresenter @Inject constructor(
    private var interactor: FriendsInteractor,
    preferences: Preferences?
) : BasePresenter<FriendsView>(preferences, interactor as BaseInteractor) {

    var pageNumber = 1

    var eventUid: String? = null

    var isLoading = false

    var participantIds: Array<String>? = null

    private var friends: MutableList<Person>? = null

    private var searchResults: MutableList<Person>? = null

    private var pageSize = Const.PAGE_SIZE

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadFriends()
    }

    fun search(searchStr: String, page: Int = 1) {
        pageNumber = page
        if (searchStr.isBlank()) {
            viewState.showFriends(friends)
        } else {
            isLoading = true
            viewState.showLoading(isShow = true, withoutBackground = true)
            invokeSuspend {
                try {
                    val request = SearchPersonRequest(pageNumber, pageSize, searchStr)
                    searchResults = interactor.search(request)?.toMutableList()
                    isLoading = false
                    runOnMainThread {
                        viewState.showLoading(isShow = false, withoutBackground = true)
                        searchResults?.let {
                            viewState.showSearchResult(it, pageNumber)
                        }
                    }
                } catch (e: ApiException) {
                    isLoading = false
                    runOnMainThread {
                        viewState.showLoading(isShow = false, withoutBackground = true)
                        viewState.showError(e.message)
                    }
                }
            }
        }
    }

    fun addToFriends(personUid: String) {
        viewState.showLoading(isShow = true, withoutBackground = true)
        invokeSuspend {
            try {
                interactor.addToFriends(personUid)
                runOnMainThread {
                    searchResults?.find {
                        it.personUid == personUid
                    }?.isFriend = true
                    viewState.showLoading(isShow = false, withoutBackground = true)
                    searchResults?.let {
                        viewState.showSearchResult(it, pageNumber)
                    }
                    updateNotifications()
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(isShow = false, withoutBackground = true)
                    viewState.showError(e.message)
                }
            }
        }
    }

    fun invitePerson(personUid: String, eventUid: String, isSearchResult: Boolean) {
        viewState.showLoading(isShow = true, withoutBackground = true)
        invokeSuspend {
            try {
                interactor.invitePerson(personUid, eventUid)
                runOnMainThread {
                    if (isSearchResult) {
                        searchResults?.find {
                            it.personUid == personUid
                        }?.isInvited = true
                        searchResults?.let {
                            viewState.showSearchResult(it, pageNumber)
                        }
                    } else {
                        friends?.find {
                            it.personUid == personUid
                        }?.isInvited = true
                        friends?.removeAll { it.friendshipApprovalRequired }
                        viewState.showFriends(friends)
                    }
                    viewState.showLoading(isShow = false, withoutBackground = true)
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(isShow = false, withoutBackground = true)
                    viewState.showError(e.message)
                }
            }
        }
    }

    fun removeFriend(personUid: String, isSearchResult: Boolean) {
        viewState.showLoading(isShow = true, withoutBackground = true)
        invokeSuspend {
            try {
                interactor.removeFriend(personUid)
                runOnMainThread {
                    if (isSearchResult) {
                        searchResults?.removeAll {
                            it.personUid == personUid
                        }
                        searchResults?.let {
                            viewState.showSearchResult(it, pageNumber)
                        }
                    } else {
                        friends?.removeAll {
                            it.personUid == personUid
                        }
                        viewState.showFriends(friends)
                    }
                    viewState.showLoading(isShow = false, withoutBackground = true)
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(isShow = false, withoutBackground = true)
                    viewState.showError(e.message)
                }
            }
        }
    }

    fun confirmFriend(personUid: String, isSearchResult: Boolean) {
        viewState.showLoading(isShow = true, withoutBackground = true)
        invokeSuspend {
            try {
                interactor.confirmFriend(personUid)
                runOnMainThread {
                    if (isSearchResult) {
                        searchResults?.find {
                            it.personUid == personUid
                        }?.friendshipApprovalRequired = false
                        searchResults?.let {
                            viewState.showSearchResult(it, pageNumber)
                        }
                    } else {
                        friends?.find {
                            it.personUid == personUid
                        }?.friendshipApprovalRequired = false
                        viewState.showFriends(friends)
                    }
                    updateNotifications()
                    viewState.showLoading(isShow = false, withoutBackground = true)
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(isShow = false, withoutBackground = true)
                    viewState.showError(e.message)
                }
            }
        }
    }

    fun loadPersonsFromContacts(phones: List<String>) {
        viewState.showLoading(isShow = true, withoutBackground = true)
        invokeSuspend {
            try {
                searchResults = interactor.getPersonsFromContacts(phones)?.toMutableList()
                runOnMainThread {
                    viewState.showLoading(isShow = false, withoutBackground = true)
                    if (searchResults.isNullOrEmpty()) {
                        viewState.showEmptyPersonsFromContacts()
                    } else {
                        viewState.showContacts(searchResults!!)
                    }
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(isShow = false, withoutBackground = true)
                    viewState.showError(e.message)
                }
            }
        }
    }

    private fun loadFriends() {
        val uid = AuthData.uid ?: return
        viewState.showLoading(isShow = true, withoutBackground = true)
        invokeSuspend {
            try {
                friends = interactor.getFriends(uid)?.toMutableList()
                if (eventUid != null) {
                    friends?.removeAll { it.friendshipApprovalRequired }
                }
                runOnMainThread {
                    viewState.showLoading(isShow = false, withoutBackground = true)
                    viewState.showFriends(friends)
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(isShow = false, withoutBackground = true)
                    viewState.showError(e.message)
                }
            }
        }
    }
}