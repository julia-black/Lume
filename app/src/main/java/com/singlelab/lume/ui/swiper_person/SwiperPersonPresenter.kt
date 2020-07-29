package com.singlelab.lume.ui.swiper_person

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.swiper_person.interactor.SwiperPersonInteractor
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.event.ParticipantRequest
import com.singlelab.net.model.event.ParticipantStatus
import com.singlelab.net.model.person.RandomPersonRequest
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class SwiperPersonPresenter @Inject constructor(
    private val interactor: SwiperPersonInteractor,
    preferences: Preferences?
) : BasePresenter<SwiperPersonView>(preferences, interactor as BaseInteractor) {

    var eventUid: String? = null

    var person: Person? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadRandomPerson()
    }

    fun loadRandomPerson() {
        eventUid ?: return
        viewState.showLoading(true)
        invokeSuspend {
            try {
                val randomPersonRequest = RandomPersonRequest(eventUid = eventUid!!)
                person = interactor.getRandomPerson(randomPersonRequest)
                runOnMainThread {
                    viewState.showLoading(false)
                    person?.let {
                        viewState.showPerson(it)
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

    fun invitePerson() {
        eventUid ?: return
        person ?: return
        viewState.showLoading(true)
        invokeSuspend {
            try {
                interactor.invitePerson(
                    ParticipantRequest(
                        person!!.personUid,
                        eventUid!!,
                        ParticipantStatus.WAITING_FOR_APPROVE_FROM_USER.id
                    )
                )
                runOnMainThread {
                    viewState.toAcceptedPerson(person!!, eventUid!!)
                    person = null
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

    fun rejectPerson() {
        eventUid ?: return
        person ?: return
        viewState.showLoading(true)
        invokeSuspend {
            try {
                interactor.rejectPerson(eventUid!!, person!!.personUid)
                runOnMainThread {
                    person = null
                    viewState.showLoading(false)
                    loadRandomPerson()
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