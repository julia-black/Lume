package com.singlelab.lume.ui.event.interactor

import com.singlelab.data.model.event.Event

interface EventInteractor {
    suspend fun getEvent(uid: String): Event?
}