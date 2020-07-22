package com.singlelab.lume.ui.creating_event.interactor

import com.singlelab.data.model.event.Event
import com.singlelab.data.model.event.ResponseEventUid

interface CreatingEventInteractor {
    suspend fun createEvent(event: Event): ResponseEventUid?
}