package com.singlelab.net.model.promo

class PromoRequest(
    val eventUid: String?,
    val accountingNumber: String,
    val images: List<String> = arrayListOf()
)