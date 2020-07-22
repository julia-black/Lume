package com.singlelab.net.repositories

import com.google.gson.Gson
import com.singlelab.net.ApiUnit
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.exceptions.RefreshTokenException
import com.singlelab.net.model.auth.AuthData
import com.singlelab.net.model.ErrorResponse
import com.singlelab.net.model.ResultCoroutines
import com.singlelab.net.model.auth.AuthResponse
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
            val errorBody = response.errorBody()?.string()
            if (response.isSuccessful && response.body() != null) {
                ResultCoroutines.Success(response.body()!!)
            } else if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                throw RefreshTokenException()
            } else if (!errorBody.isNullOrEmpty()) {
                try {
                    val error = Gson().fromJson(errorBody, ErrorResponse::class.java)
                    ResultCoroutines.Error(
                        ApiException(
                            error.message
                        )
                    )
                } catch (e: Exception) {
                    ResultCoroutines.Error(
                        ApiException(
                            errorMessage
                        )
                    )
                }
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
            when (val responseAuth = updateToken(apiUnit)) { //обновляем токен
                is ResultCoroutines.Success<AuthResponse> -> {
                    onRefreshTokenListener?.onRefreshToken(responseAuth.data)
                    AuthData.accessToken = responseAuth.data.accessToken
                    safeApiResult(apiUnit, call, errorMessage) //повторно выполняем запрос
                }
                else -> {
                    onRefreshTokenListener?.onRefreshTokenFailed()
                    throw e
                }
            }
        } catch (e: Exception) {
            ResultCoroutines.Error(
                ApiException(
                    e.message ?: "Не удалось выполнить запрос"
                )
            )
        }
    }

    private suspend fun updateToken(apiUnit: ApiUnit): ResultCoroutines<AuthResponse> {
        val refreshToken = AuthData.refreshToken
        val uid = AuthData.uid

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