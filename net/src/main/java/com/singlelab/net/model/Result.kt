package com.singlelab.net.model

import com.singlelab.net.exceptions.ApiException

sealed class ResultCoroutines<out T : Any> {
    data class Success<out T : Any>(val data: T) : ResultCoroutines<T>()
    data class Error(val exception: ApiException) : ResultCoroutines<Nothing>()
}