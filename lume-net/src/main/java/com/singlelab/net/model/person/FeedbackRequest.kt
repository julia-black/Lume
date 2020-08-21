package com.singlelab.net.model.person

class FeedbackRequest(
    val text: String,
    val operatingSystem: String,
    val phoneModel: String,
    val applicationVersion: String,
    val images: List<String> = arrayListOf()
)