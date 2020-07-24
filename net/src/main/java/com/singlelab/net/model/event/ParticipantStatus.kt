package com.singlelab.net.model.event

enum class ParticipantStatus(val id: Int) {
    WAITING_FOR_APPROVE_FROM_USER(0),
    WAITING_FOR_APPROVE_FROM_EVENT(1),
    ACTIVE(2)
}