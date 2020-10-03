package com.singlelab.lume.ui.events.interactor

import com.singlelab.lume.model.event.EventSummary
import com.singlelab.lume.model.promo.PromoReward

interface EventsInteractor {
    suspend fun getEvents(): List<EventSummary>?

    suspend fun checkPromoReward(cityId: Int): PromoReward?
}