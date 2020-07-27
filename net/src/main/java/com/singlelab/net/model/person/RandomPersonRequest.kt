package com.singlelab.net.model.person

class RandomPersonRequest(
    val eventUid: String,
    val minAge: Int? = null,
    val maxAge: Int? = null
)