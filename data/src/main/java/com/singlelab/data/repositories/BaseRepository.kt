package com.singlelab.data.repositories

import com.google.gson.Gson
import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.exceptions.RefreshTokenException
import com.singlelab.data.model.Error
import com.singlelab.data.model.ResultCoroutines
import com.singlelab.data.model.auth.ResponseAuth
import com.singlelab.data.net.ApiUnit
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.UnknownHostException

open class BaseRepository {

    private var onRefreshTokenListener: OnRefreshTokenListener? = null

    fun setOnRefreshTokenListener(listener: OnRefreshTokenListener?) {
        this.onRefreshTokenListener = listener
    }

    suspend fun <T : Any> safeApiCall(
        apiUnit: ApiUnit,
        call: suspend () -> Response<T>,
        errorMessage: String
    ): T? {
        val result: ResultCoroutines<T> = safeApiResult(apiUnit, call, errorMessage)
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
        apiUnit: ApiUnit,
        call: suspend () -> Response<T>,
        errorMessage: String
    ): ResultCoroutines<T> {
        return try {
            val response = call.invoke()
            if (response.isSuccessful && response.body() != null) {
                ResultCoroutines.Success(response.body()!!)
            } else if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                throw RefreshTokenException()
            } else if (response.errorBody() != null) {
                val errorBody = response.errorBody()!!.string()
                val error = Gson().fromJson(errorBody, Error::class.java)
                ResultCoroutines.Error(
                    ApiException(
                        error.message
                    )
                )
            } else {
                ResultCoroutines.Error(
                    ApiException(
                        errorMessage
                    )
                )
            }
        } catch (e: UnknownHostException) {
            ResultCoroutines.Error(ApiException("Отсутствует соединение с сервером"))
        } catch (e: RefreshTokenException) {
            val responseAuth = updateToken(apiUnit) //обновляем токен
            if (responseAuth is ResultCoroutines.Error) {
                responseAuth
            } else if (responseAuth is ResultCoroutines.Success<ResponseAuth>) {
                onRefreshTokenListener?.onRefreshToken(responseAuth.data)
                apiUnit.headers?.accessToken = responseAuth.data.accessToken
                safeApiResult(apiUnit, call, errorMessage) //повторно выполняем запрос
            } else {
                throw Exception()
            }
        } catch (e: Exception) {
            ResultCoroutines.Error(
                ApiException(
                    e.message ?: "Не удалось выполнить запрос"
                )
            )
        }
    }

    private suspend fun updateToken(apiUnit: ApiUnit): ResultCoroutines<ResponseAuth> {
        val refreshToken = apiUnit.headers?.refreshToken
        val uid = apiUnit.headers?.uid

        return if (!uid.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
            safeApiResult(
                apiUnit = apiUnit,
                call = {
                    apiUnit.authApi.refreshTokenAsync(
                        refreshToken,
                        uid
                    ).await()
                },
                errorMessage = "Не удалось обновить токен"
            )
        } else {
            ResultCoroutines.Error(RefreshTokenException())
        }
    }
}