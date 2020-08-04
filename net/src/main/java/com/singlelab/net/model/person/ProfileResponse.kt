package com.singlelab.net.model.person

class ProfileResponse(
    val personUid: String,
    val name: String,
    val description: String? = null,
    val cityId: Int,
    val cityName: String,
    val age: Int,
    val imageContentUid: String? = null,
    val isFriend: Boolean = false,
    val friends: List<PersonResponse> = arrayListOf()
)