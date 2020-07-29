package com.singlelab.lume.ui.creating_event.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.database.repository.ProfileRepository
import com.singlelab.lume.model.city.City
import com.singlelab.net.model.event.EventRequest
import com.singlelab.net.model.event.EventUidResponse
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.events.EventsRepository

class CreatingEventInteractorImpl(
    private val eventRepository: EventsRepository,
    private val profileRepository: ProfileRepository
) :
    CreatingEventInteractor,
    BaseInteractor(eventRepository as BaseRepository) {
    override suspend fun createEvent(event: EventRequest): EventUidResponse? {
        return eventRepository.createEvent(event)
    }

    override suspend fun getCurrentCity(): City? {
        val profile = profileRepository.getProfile()
        return if (profile != null) {
            if (profile.cityId != null && profile.cityName != null) {
                City(profile.cityId!!, profile.cityName!!)
            } else {
                null
            }
        } else {
            null
        }
    }
}