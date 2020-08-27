package com.singlelab.lume.model.event

import com.singlelab.lume.R

enum class EventStatus(val id: Int, val titleRes: Int? = null) {
    PREPARING(0),
    IN_PROGRESS(1),
    ENDED(2, R.string.title_ended),
    CANCELLED(3, R.string.title_cancel);

    companion object {
        fun findById(id: Int): EventStatus {
            return values().find {
                it.id == id
            } ?: PREPARING
        }
    }
}