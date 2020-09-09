package com.singlelab.lume.model.view

import com.singlelab.lume.R

enum class ValidationError(val titleResId: Int) {
    UNFILLED_FIELDS(R.string.enter_fields),
    EMPTY_PHOTO(R.string.empty_photo),
    EMPTY_CITY(R.string.choose_city),
    EMPTY_LOGIN(R.string.empty_login),
    EMPTY_NAME(R.string.empty_name),
    EMPTY_DESCRIPTION(R.string.empty_description),
    EMPTY_AGE(R.string.empty_age)
}