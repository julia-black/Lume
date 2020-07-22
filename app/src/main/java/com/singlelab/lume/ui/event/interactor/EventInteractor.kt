package com.singlelab.lume.ui.event.interactor

import com.singlelab.lume.model.event.Event

interface EventInteractor {
    suspend fun getEvent(uid: String): Event?
}