package com.singlelab.lume.ui.search_event.interactor

import com.singlelab.lume.model.event.EventSummary
import com.singlelab.net.model.event.SearchEventRequest

interface SearchEventInteractor {
    suspend fun search(searchEventRequest: SearchEventRequest): List<EventSummary>?
}