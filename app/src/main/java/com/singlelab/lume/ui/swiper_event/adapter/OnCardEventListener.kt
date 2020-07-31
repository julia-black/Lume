package com.singlelab.lume.ui.swiper_event.adapter

interface OnCardEventListener {
    fun onLocationClick(lat: Double, long: Double, label: String)
}