package com.singlelab.lume.ui.swiper_event.adapter

interface OnCardEventListener {
    fun onLocationClick(lat: Double, long: Double, label: String)

    fun onAdministratorClick(personUid: String)

    fun onImageClick(images: List<String>)

    fun onReportClick()
}