package com.singlelab.data.model.auth

class Headers(
    var accessToken: String?,
    var refreshToken: String?,
    val uid: String?
)