package com.singlelab.net.model.person

class ProfileResponse(
    val personUid: String? = null,
    val name: String? = null,
    val description: String? = null,
    val age: Int? = null,
    val imageContentUid: String? = null,
    val friends: List<PersonResponse> = arrayListOf()
)