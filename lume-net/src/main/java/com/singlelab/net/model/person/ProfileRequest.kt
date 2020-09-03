package com.singlelab.net.model.person

class ProfileRequest(
    var login: String? = null,
    var name: String? = null,
    var description: String? = null,
    var age: Int? = null,
    var cityId: Int? = null,
    var image: String? = null,
    var token: String? = null
)