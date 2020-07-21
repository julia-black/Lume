package com.singlelab.lume.ui.swiper_event.interactor

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.event.Event

interface SwiperEventInteractor {
    @Throws(ApiException::class)
    suspend fun getEvent(uid: String): Event?
}