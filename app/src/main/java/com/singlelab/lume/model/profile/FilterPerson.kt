package com.singlelab.lume.model.profile

import android.os.Parcelable
import com.singlelab.lume.model.Const
import kotlinx.android.parcel.Parcelize

@Parcelize
class FilterPerson(
    var cityId: Int? = null,
    var cityName: String? = null,
    var minAge: Int = Const.MIN_AGE,
    var maxAge: Int = Const.MAX_AGE
) : Parcelable