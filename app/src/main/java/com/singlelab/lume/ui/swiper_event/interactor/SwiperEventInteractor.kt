package com.singlelab.lume.ui.swiper_event.interactor

import com.singlelab.lume.model.event.Event

interface SwiperEventInteractor {
    suspend fun getEvent(uid: String): Event?
}