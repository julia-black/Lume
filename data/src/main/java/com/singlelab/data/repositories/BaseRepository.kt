package com.singlelab.data.repositories

import com.singlelab.data.model.ResultCoroutines
import com.singlelab.data.net.ApiException
import retrofit2.Response
import java.net.UnknownHostException

open class BaseRepository {

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, errorMessage: String): T? {
        val result: ResultCoroutines<T> = safeApiResult(call, errorMessage)
        var data: T? = null

        when (result) {
            is ResultCoroutines.Success ->
                data = result.data
            is ResultCoroutines.Error -> {
                throw result.exception
            }
        }
        return data
    }

    private suspend fun <T : Any> safeApiResult(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): ResultCoroutines<T> {
        return try {
            val response = call.invoke()
            if (response.isSuccessful && response.body() != null) {
                ResultCoroutines.Success(response.body()!!)
            } else { //todo добавить обработку ошибок с сервера
                ResultCoroutines.Error(ApiException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))
            }
        } catch (e: UnknownHostException) {
            ResultCoroutines.Error(ApiException("Отсутствует соединение с сервером"))
        } catch (e: Exception) {
            ResultCoroutines.Error(ApiException(e.message ?: "Не удалось выполнить запрос"))
        }
    }
}