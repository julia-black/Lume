package com.singlelab.lume.model.event

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class FilterEvent(
    var distance: Distance = Distance.FAR,
    val selectedTypes: MutableList<EventType> = mutableListOf(),
    var cityId: Int? = null,
    var cityName: String? = null,
    var longitude: Double? = null,
    var latitude: Double? = null,
    var isExceptOnline: Boolean = false,
    var isOnlyOnline: Boolean = false
) : Parcelable {
    fun isOnlineForRequest(): Boolean? {
        return when {
            isExceptOnline -> false
            isOnlyOnline -> true
            else -> null
        }
    }
}