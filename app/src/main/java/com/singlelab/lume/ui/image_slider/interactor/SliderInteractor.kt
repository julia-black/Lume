package com.singlelab.lume.ui.image_slider.interactor

interface SliderInteractor {
    suspend fun deleteImage(eventUid: String, imageUid: String)
}