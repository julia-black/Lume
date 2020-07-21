package com.singlelab.lume.ui.event.interactor

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.event.Event

interface EventInteractor {
    @Throws(ApiException::class)
    suspend fun getEvent(uid: String): Event?
}