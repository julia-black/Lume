package com.singlelab.lume.ui.participants

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.participants.interactor.ParticipantsInteractor
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.event.ParticipantRequest
import com.singlelab.net.model.event.ParticipantStatus
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class ParticipantsPresenter @Inject constructor(
    private var interactor: ParticipantsInteractor,
    preferences: Preferences?
) : BasePresenter<ParticipantsView>(preferences, interactor as BaseInteractor) {

    var withNotApproved: Boolean = false

    var eventUid: String? = null

    var participants: MutableList<Person>? = null

    fun approvePerson(personUid: String, eventUid: String) {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                interactor.approvePerson(
                    ParticipantRequest(
                        personUid,
                        eventUid,
                        ParticipantStatus.ACTIVE.id
                    )
                )
                participants?.forEach {
                    it.participantStatus = ParticipantStatus.ACTIVE
                }
                runOnMainThread {
                    viewState.showLoading(false)
                    participants?.let {
                        viewState.showParticipants(it.toList())
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

    fun rejectPerson(personUid: String, eventUid: String) {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                interactor.rejectPerson(personUid, eventUid)
                participants?.removeAll {
                    it.personUid == personUid
                }
                runOnMainThread {
                    viewState.showLoading(false)
                    participants?.let {
                        viewState.showParticipants(it.toList())
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