package com.singlelab.lume.util

import com.singlelab.lume.model.event.EventSummary

fun List<EventSummary>.compare(list: List<EventSummary>): Boolean {
    if (size != list.size) {
        return false
    } else {
        forEachIndexed { index, eventSummary ->
            if (eventSummary.eventUid != list[index].eventUid ||
                eventSummary.participantStatus != list[index].participantStatus ||
                eventSummary.startTime != list[index].startTime ||
                eventSummary.endTime != list[index].endTime ||
                eventSummary.status != list[index].status
            ) {
                return false
            }
        }
        return true
    }
}