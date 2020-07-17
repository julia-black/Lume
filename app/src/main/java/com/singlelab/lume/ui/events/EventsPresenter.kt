package com.singlelab.lume.ui.events

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.events.interactor.EventsInteractor
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class EventsPresenter @Inject constructor(
    private val interactor: EventsInteractor,
    preferences: Preferences?
) : BasePresenter<EventsView>(preferences, interactor as BaseInteractor) {

}