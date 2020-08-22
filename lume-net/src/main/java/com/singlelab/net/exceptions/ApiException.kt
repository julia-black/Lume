package com.singlelab.net.exceptions

open class ApiException(override val message: String, val errorCode: Int? = null) : Exception()