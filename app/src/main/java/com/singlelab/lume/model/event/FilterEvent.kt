package com.singlelab.lume.model.event

import android.os.Parcelable
import com.singlelab.lume.ui.event.EventType
import kotlinx.android.parcel.Parcelize

@Parcelize
class FilterEvent(
    var distance: Distance = Distance.FAR,
    val selectedTypes: MutableList<EventType> = mutableListOf(),
    var cityId: Int? = null,
    var cityName: String? = null,
    var longitude: Double? = null,
    var latitude: Double? = null
) : Parcelable