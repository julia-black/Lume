package com.singlelab.lume.ui.map

import com.google.android.gms.maps.model.LatLng
import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MapView : LoadingView, ErrorView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun searchPlace(queryStr: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun searchPlace(latLng: LatLng)
}