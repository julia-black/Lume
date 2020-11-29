package com.singlelab.lume.util

import com.singlelab.lume.model.event.EventSummary

fun List<EventSummary>.compare(list: List<EventSummary>): Boolean {
    if (size != list.size) {
        return false
    } else {
        forEachIndexed { index, eventSummary ->
            if (eventSummary.eventUid != list[index].eventUid) {
                return false
            }
        }
        return true
    }
}