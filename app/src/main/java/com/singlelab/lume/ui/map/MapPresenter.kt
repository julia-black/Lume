package com.singlelab.lume.ui.map

import com.google.android.gms.maps.model.LatLng
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class MapPresenter @Inject constructor() : MvpPresenter<MapView>() {

    fun searchPlace(queryStr: String) {
        viewState.searchPlace(queryStr)
    }

    fun searchPlace(latLng: LatLng) {
        viewState.searchPlace(latLng)
    }
}