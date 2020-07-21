package com.singlelab.lume.ui.event

import com.singlelab.lume.R

enum class EventType(val typeId: Int, val titleRes: Int, val colorRes: Int) {
    PARTY(0, R.string.party, R.color.colorParty),
    BOOZE(1, R.string.boozy, R.color.colorDark);

    companion object {
        fun findById(typeId: Int): EventType? {
            return values().find {
                it.typeId == typeId
            }
        }
    }
}