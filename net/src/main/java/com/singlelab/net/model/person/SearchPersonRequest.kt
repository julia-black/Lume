package com.singlelab.net.model.person

class SearchPersonRequest(
    val pageNumber: Int,
    val pageSize: Int,
    val query: String? = null,
    val cityId: Int? = null
)