package com.singlelab.net.model.event

class ParticipantRequest(
    val personUid: String,
    val eventUid: String,
    val participantStatus: Int
)