package com.singlelab.lume.ui.image_slider.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.net.model.event.EventImageRequest
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.events.EventsRepository

class SliderInteractorImpl(private val repository: EventsRepository) : SliderInteractor,
    BaseInteractor(repository as BaseRepository) {
    override suspend fun deleteImage(eventUid: String, imageUid: String) {
        repository.removeImage(EventImageRequest(eventUid, imageUid))
    }
}