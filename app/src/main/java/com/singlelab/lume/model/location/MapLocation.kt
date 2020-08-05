package com.singlelab.lume.model.location

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
class MapLocation(
    var address: String? = null,
    var city: String? = null,
    var latLong: LatLng? = null
) : Parcelable