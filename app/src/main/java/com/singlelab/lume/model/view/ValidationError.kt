package com.singlelab.lume.model.view

import com.singlelab.lume.R

enum class ValidationError(val titleResId: Int) {
    UNFILLED_FIELDS(R.string.enter_fields),
    EMPTY_PHOTO(R.string.empty_photo),
    EMPTY_CITY(R.string.choose_city),
    EMPTY_LOGIN(R.string.empty_login),
    EMPTY_NAME(R.string.empty_name),
    EMPTY_DESCRIPTION(R.string.empty_description),
    EMPTY_AGE(R.string.empty_age),
    EMPTY_TITLE(R.string.empty_title),
    EMPTY_DESCRIPTION_EVENT(R.string.empty_description_event),
    EMPTY_START_TIME(R.string.empty_start_time),
    EMPTY_END_TIME(R.string.empty_end_time),
    INVALID_LOGIN(R.string.invalid_login),
    INVALID_AGE(R.string.invalid_age)
}