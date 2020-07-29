package com.singlelab.lume.ui.filters

import com.singlelab.lume.model.city.City
import com.singlelab.lume.model.event.Distance
import com.singlelab.lume.model.event.FilterEvent
import com.singlelab.lume.pref.Preferences
import com.singlelab.net.model.auth.AuthData
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class FilterPresenter @Inject constructor(preferences: Preferences?) : MvpPresenter<FilterView>() {
    var isEvent = true
    var filterEvent = FilterEvent(cityId = AuthData.cityId, cityName = AuthData.cityName)

    fun changeDistance(distance: Int) {
        filterEvent.distance = Distance.find(distance)
        viewState.showDistance(filterEvent.distance)
    }

    fun setCity(city: City) {
        filterEvent.cityId = city.cityId
        filterEvent.cityName = city.cityName
        viewState.showCity(city.cityName)
    }
}