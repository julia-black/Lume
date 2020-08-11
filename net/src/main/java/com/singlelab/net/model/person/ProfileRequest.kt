package com.singlelab.net.model.person

class ProfileRequest(
    val login: String? = null,
    val name: String? = null,
    val description: String? = null,
    val age: Int? = null,
    val cityId: Int? = null,
    val image: String? = null
)