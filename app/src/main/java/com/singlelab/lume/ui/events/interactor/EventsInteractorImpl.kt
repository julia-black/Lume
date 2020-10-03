package com.singlelab.lume.ui.events.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.model.event.EventSummary
import com.singlelab.lume.model.promo.PromoReward
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.events.EventsRepository

class EventsInteractorImpl(private val repository: EventsRepository) : EventsInteractor,
    BaseInteractor(repository as BaseRepository) {
    override suspend fun getEvents(): List<EventSummary>? {
        return repository.getEvents()?.mapNotNull {
            EventSummary.fromResponse(it)
        }
    }

    override suspend fun checkPromoReward(cityId: Int): PromoReward? {
        return PromoReward.fromResponse(repository.checkCityForPromoReward(cityId))
    }
}