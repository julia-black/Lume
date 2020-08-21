package com.singlelab.lume.model.view

import com.singlelab.lume.R

enum class ValidationError(val titleResId: Int) {
    UNFILLED_FIELDS(R.string.enter_fields),
    EMPTY_PHOTO(R.string.empty_photo)
}