package com.singlelab.data.model

import com.singlelab.data.net.ApiException

sealed class ResultCoroutines<out T : Any> {
    data class Success<out T : Any>(val data: T) : ResultCoroutines<T>()
    data class Error(val exception: ApiException) : ResultCoroutines<Nothing>()
}