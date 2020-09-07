package com.singlelab.lume.ui.creating_event.interactor

import com.singlelab.net.model.event.EventRequest
import com.singlelab.net.model.event.EventUidResponse
import com.singlelab.net.model.event.UpdateEventRequest

interface CreatingEventInteractor {
    suspend fun createEvent(event: EventRequest): EventUidResponse?

    suspend fun updateEvent(request: UpdateEventRequest)
}