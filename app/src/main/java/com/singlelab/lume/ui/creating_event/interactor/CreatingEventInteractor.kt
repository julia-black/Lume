package com.singlelab.lume.ui.creating_event.interactor

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.event.Event
import com.singlelab.data.model.event.ResponseEventUid

interface CreatingEventInteractor {
    @Throws(ApiException::class)
    suspend fun createEvent(event: Event): ResponseEventUid?
}