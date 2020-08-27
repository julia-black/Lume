package com.singlelab.lume.ui.view.range_picker

import android.os.Parcelable
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import kotlinx.android.parcel.Parcelize


@Parcelize
data class RangeValidator(
    var minDate: Long,
    var maxDate: Long? = null
) : DateValidator, Parcelable {

    override fun isValid(date: Long): Boolean {
        return date >= minDate && (maxDate == null || date <= maxDate!!)
    }
}
