package com.singlelab.lume.ui.filters.event

import com.singlelab.lume.model.city.City
import com.singlelab.lume.model.event.Distance
import com.singlelab.lume.model.event.EventType
import com.singlelab.lume.model.event.FilterEvent
import com.singlelab.lume.model.profile.FilterPerson
import com.singlelab.lume.pref.Preferences
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class FilterPresenter @Inject constructor(preferences: Preferences?) : MvpPresenter<FilterView>() {

    var filterEvent: FilterEvent? = null
    var filterPerson: FilterPerson? = null

    fun isEvent() = filterEvent != null

    override fun attachView(view: FilterView?) {
        super.attachView(view)
        filterEvent?.let {
            viewState.showTypes(it.selectedTypes)
        }
    }

    fun changeDistance(distance: Int) {
        filterEvent?.let {
            it.distance = Distance.find(distance)
            viewState.showDistance(it.distance)
        }
    }

    fun setCity(city: City?) {
        filterEvent?.let {
            it.cityId = city?.cityId
            it.cityName = city?.cityName
            viewState.showCity(city?.cityName)
        }
        filterPerson?.let {
            it.cityId = city?.cityId
            it.cityName = city?.cityName
            viewState.showCity(city?.cityName)
        }
    }

    fun setCheckedEventType(eventType: EventType, checked: Boolean) {
        filterEvent ?: return
        if (checked) {
            if (!filterEvent!!.selectedTypes.contains(eventType))
                filterEvent!!.selectedTypes.add(eventType)
        } else {
            filterEvent!!.selectedTypes.removeAll { it == eventType }
        }
    }

    fun setUserLocation(longitude: Double, latitude: Double) {
        filterEvent?.let {
            it.longitude = longitude
            it.latitude = latitude
        }
    }

    fun setDate(firstDate: Long?, secondDate: Long?) {
        filterEvent?.minimalStartTime = firstDate
        filterEvent?.maximalEndTime = secondDate
        viewState.showDate(firstDate, secondDate)
    }

    fun onClickType(type: Int) {
        filterEvent ?: return
        if (filterEvent!!.selectedTypes.find { it.id == type } != null) {
            removeType(type)
        } else {
            addType(type)
        }
    }

    private fun addType(type: Int) {
        filterEvent?.selectedTypes?.apply {
            add(EventType.findById(type))
            viewState.showTypes(this)
        }
    }

    private fun removeType(type: Int) {
        filterEvent?.selectedTypes?.apply {
            removeAll { it.id == type }
            viewState.showTypes(this)
        }
    }
}