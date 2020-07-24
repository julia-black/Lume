package com.singlelab.lume.ui.participants

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.participants.interactor.ParticipantsInteractor
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class ParticipantsPresenter @Inject constructor(
    private var interactor: ParticipantsInteractor,
    preferences: Preferences?
) : BasePresenter<ParticipantsView>(preferences, interactor as BaseInteractor) {

    var eventUid: String? = null

}