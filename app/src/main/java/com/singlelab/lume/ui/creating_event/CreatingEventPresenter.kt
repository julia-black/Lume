package com.singlelab.lume.ui.creating_event

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.city.City
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.creating_event.interactor.CreatingEventInteractor
import com.singlelab.lume.util.parseToString
import com.singlelab.lume.util.toBase64
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.auth.AuthData
import com.singlelab.net.model.event.EventRequest
import moxy.InjectViewState
import java.util.*
import javax.inject.Inject

@InjectViewState
class CreatingEventPresenter @Inject constructor(
    private val interactor: CreatingEventInteractor,
    preferences: Preferences?
) : BasePresenter<CreatingEventView>(preferences, interactor as BaseInteractor) {

    var currentDateStart: Calendar? = null
    var currentDateEnd: Calendar? = null

    var cityId = AuthData.cityId
    var cityName = AuthData.cityName

    var locationName: String? = null
    private var locationCoordinate: LatLng? = null

    private var images: MutableList<Bitmap> = mutableListOf()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showCurrentCity(cityId, cityName)
    }

    fun createEvent(event: EventRequest) {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                val eventUid = interactor.createEvent(event)?.eventUid
                runOnMainThread {
                    viewState.showLoading(false)
                    eventUid?.let {
                        viewState.onCompleteCreateEvent(it)
                    }
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(false)
                    viewState.showError(e.message)
                }
            }
        }
    }

    fun saveCurrentDate(year: Int, month: Int, day: Int, isStart: Boolean) {
        if (isStart) {
            if (currentDateStart == null) {
                currentDateStart = Calendar.getInstance()
            }
            currentDateStart!!.set(Calendar.YEAR, year)
            currentDateStart!!.set(Calendar.MONTH, month)
            currentDateStart!!.set(Calendar.DAY_OF_MONTH, day)
        } else {
            if (currentDateEnd == null) {
                currentDateEnd = Calendar.getInstance()
            }
            currentDateEnd!!.set(Calendar.YEAR, year)
            currentDateEnd!!.set(Calendar.MONTH, month)
            currentDateEnd!!.set(Calendar.DAY_OF_MONTH, day)
        }
    }

    fun saveCurrentTime(hours: Int, minutes: Int, isStart: Boolean) {
        if (isStart) {
            if (currentDateStart == null) {
                currentDateStart = Calendar.getInstance()
            }
            currentDateStart!!.set(Calendar.HOUR_OF_DAY, hours)
            currentDateStart!!.set(Calendar.MINUTE, minutes)
            viewState.showDateStart(currentDateStart!!.parseToString(Const.DATE_FORMAT_OUTPUT))
        } else {
            if (currentDateEnd == null) {
                currentDateEnd = Calendar.getInstance()
            }
            currentDateEnd!!.set(Calendar.HOUR_OF_DAY, hours)
            currentDateEnd!!.set(Calendar.MINUTE, minutes)
            viewState.showDateEnd(currentDateEnd!!.parseToString(Const.DATE_FORMAT_OUTPUT))
        }
    }

    fun addImage(bitmap: Bitmap?) {
        bitmap?.let {
            images.add(it)
            viewState.addImage(bitmap)
        }
    }

    fun deleteImage(position: Int) {
        images.removeAt(position)
    }

    fun getPrimaryImage(): String? {
        return if (images.isNotEmpty()) {
            images[0].toBase64()
        } else {
            null
        }
    }

    fun getImagesStr(): List<String>? {
        return images.map {
            it.toBase64()
        }
    }

    fun setCity(city: City) {
        this.cityId = city.cityId
        this.cityName = city.cityName
        viewState.showCurrentCity(cityId, cityName)

        this.locationName = null
        this.locationCoordinate = null
        viewState.showLocationName(null)
    }

    fun setLocation(locationName: String, locationCoordinate: LatLng) {
        this.locationName = locationName
        this.locationCoordinate = locationCoordinate
        viewState.showLocationName(locationName)
    }

    fun getLat(): Double? {
        return locationCoordinate?.latitude
    }

    fun getLong(): Double? {
        return locationCoordinate?.longitude
    }
}