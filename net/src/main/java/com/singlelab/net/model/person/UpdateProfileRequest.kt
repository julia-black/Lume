package com.singlelab.net.model.person

class UpdateProfileRequest(
    val name: String? = null,
    val description: String? = null,
    val age: Int? = null,
    val cityId: Int? = null
)