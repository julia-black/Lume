package com.singlelab.lume.ui.creating_event

import android.graphics.Bitmap
import com.singlelab.lume.R
import com.singlelab.lume.analytics.Analytics
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.city.City
import com.singlelab.lume.model.event.EventType
import com.singlelab.lume.model.location.MapLocation
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.creating_event.interactor.CreatingEventInteractor
import com.singlelab.lume.util.compareCities
import com.singlelab.lume.util.parseToString
import com.singlelab.lume.util.resize
import com.singlelab.lume.util.toBase64
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.auth.AuthData
import com.singlelab.net.model.event.EventRequest
import com.singlelab.net.model.event.UpdateEventRequest
import moxy.InjectViewState
import java.util.*
import javax.inject.Inject

@InjectViewState
class CreatingEventPresenter @Inject constructor(
    private val interactor: CreatingEventInteractor,
    preferences: Preferences?
) : BasePresenter<CreatingEventView>(preferences, interactor as BaseInteractor) {

    companion object {
        private const val MAX_TYPES_SIZE = 3
    }

    var currentDateStart: Calendar? = null
    var currentDateEnd: Calendar? = null

    var cityId = AuthData.cityId
    var cityName = AuthData.cityName

    var location: MapLocation? = null

    private var types: MutableList<EventType> = mutableListOf()

    private var images: MutableList<Bitmap> = mutableListOf()

    private var title: String? = null

    private var description: String? = null

    private var isOnlineEvent = false
    private var isClosedEvent = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showCurrentCity(cityId, cityName)
        viewState.showTypes(types)
    }

    fun createEvent(event: EventRequest) {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                val eventUid = interactor.createEvent(event)?.eventUid
                eventUid?.let { uid ->
                    val images = getImagesStr()
                    images?.forEachIndexed { idx, it ->
                        runOnMainThread {
                            viewState.showLoadingImages(idx + 1, images.size)
                        }
                        interactor.updateEvent(
                            UpdateEventRequest(
                                eventUid = uid,
                                extraImages = arrayListOf(it)
                            )
                        )
                    }
                }
                eventUid?.let {
                    Analytics.logCreateEvent(it, event)
                    runOnMainThread {
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

    fun setTitle(title: String) {
        this.title = title
        viewState.showTitle(title)
    }

    fun setDescription(description: String) {
        this.description = description
        viewState.showDescription(description)
    }

    fun setClosedEvent(checked: Boolean) {
        this.isClosedEvent = checked
        viewState.showClosedEvent(checked)
    }

    fun setOnlineEvent(checked: Boolean) {
        this.isOnlineEvent = checked
        viewState.showOnlineEvent(checked)
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
            viewState.showDateStart(currentDateStart!!.parseToString(Const.DATE_FORMAT_ON_CARD))
        } else {
            if (currentDateEnd == null) {
                currentDateEnd = Calendar.getInstance()
            }
            currentDateEnd!!.set(Calendar.HOUR_OF_DAY, hours)
            currentDateEnd!!.set(Calendar.MINUTE, minutes)
            viewState.showDateEnd(currentDateEnd!!.parseToString(Const.DATE_FORMAT_ON_CARD))
        }
    }

    fun addImages(images: List<Bitmap>) {
        this.images.addAll(images)
        if (this.images.size > Const.MAX_COUNT_IMAGES) {
            this.images = this.images.subList(0, Const.MAX_COUNT_IMAGES)
            viewState.showError(R.string.too_many_images)
        }
        viewState.showImages(this.images)
    }

    fun deleteImage(position: Int) {
        images.removeAt(position)
        viewState.showImages(images)
    }

    fun getPrimaryImage(): String? {
        return if (images.isNotEmpty()) {
            images[0].resize().toBase64()
        } else {
            null
        }
    }

    fun getImagesStr(): List<String>? {
        val newImages = mutableListOf<String>()
        if (images.size >= 1) {
            images.removeAt(0)
        }
        newImages.addAll(images.map { it.resize().toBase64() })
        return newImages
    }

    fun setCity(city: City) {
        this.cityId = city.cityId
        this.cityName = city.cityName
        viewState.showCurrentCity(cityId, cityName)
        this.location = null
        viewState.showLocationName(null)
    }

    fun setMapLocation(location: MapLocation) {
        this.location = location
        if (cityName != null && !cityName.compareCities(location.city)) {
            viewState.showWarningOtherCity(cityName!!)
        }
        viewState.showLocationName(location.address)
    }

    fun getLat(): Double? {
        return location?.latLong?.latitude
    }

    fun getLong(): Double? {
        return location?.latLong?.longitude
    }

    fun getAddress(): String? {
        return location?.address
    }

    fun setMainImage(position: Int) {
        val mainImage = images[position]
        images.removeAt(position)
        images.add(0, mainImage)
        viewState.showImages(images)
    }

    fun onClickType(type: Int) {
        if (types.find { it.id == type } != null) {
            removeType(type)
        } else {
            addType(type)
        }
    }

    fun getTypes() = types

    private fun addType(type: Int) {
        if (types.size < MAX_TYPES_SIZE) {
            types.add(EventType.findById(type))
            viewState.showTypes(types)
        }
    }

    private fun removeType(type: Int) {
        types.removeAll { it.id == type }
        viewState.showTypes(types)
    }
}