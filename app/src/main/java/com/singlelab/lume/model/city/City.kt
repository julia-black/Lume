package com.singlelab.lume.model.city

import android.os.Parcelable
import com.singlelab.net.model.city.CityResponse
import kotlinx.android.parcel.Parcelize

@Parcelize
class City(
    val cityId: Int? = null,
    val cityName: String
) : Parcelable {
    companion object {
        fun fromResponse(cityResponse: CityResponse): City? {
            return if (cityResponse == null) {
                null
            } else {
                City(cityResponse.cityId, cityResponse.cityName)
            }
        }
    }
}