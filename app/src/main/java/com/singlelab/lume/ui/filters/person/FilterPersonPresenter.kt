package com.singlelab.lume.ui.filters.person

import com.singlelab.lume.model.city.City
import com.singlelab.lume.model.profile.FilterPerson
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class FilterPersonPresenter @Inject constructor() : MvpPresenter<FilterPersonView>() {

    var filterPerson: FilterPerson? = null

    fun setCity(city: City?) {
        filterPerson?.let {
            it.cityId = city?.cityId
            it.cityName = city?.cityName
            viewState.showCity(city?.cityName)
        }
    }
}