package com.singlelab.net.repositories

import com.google.gson.Gson
import com.singlelab.net.ApiUnit
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.exceptions.NotConnectionException
import com.singlelab.net.exceptions.RefreshTokenException
import com.singlelab.net.exceptions.TimeoutException
import com.singlelab.net.model.ErrorResponse
import com.singlelab.net.model.ResultCoroutines
import com.singlelab.net.model.auth.AuthData
import com.singlelab.net.model.auth.AuthResponse
import com.singlelab.net.model.person.PersonNotificationsResponse
import com.singlelab.net.model.promo.PromoInfoResponse
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseRepository(private val apiUnit: ApiUnit) {

    private var onRefreshTokenListener: OnRefreshTokenListener? = null

    fun setOnRefreshTokenListener(listener: OnRefreshTokenListener?) {
        this.onRefreshTokenListener = listener
    }

    suspend fun getNotifications(): PersonNotificationsResponse? {
        return safeApiCall(
            call = { apiUnit.personApi.getNotificationsAsync().await() },
            errorMessage = "Не удалось получить уведомления"
        )
    }

    suspend fun getPromo(): PromoInfoResponse? {
        return safeApiCall(
            call = { apiUnit.promoApi.getPromoAsync().await() },
            errorMessage = "Не удалось получить промо-акции"
        )
    }

    suspend fun <T : Any> safeApiCall(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): T? {
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
            val errorBody = response.errorBody()?.string()
            if (response.isSuccessful && response.body() != null) {
                ResultCoroutines.Success(response.body()!!)
            } else if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                throw RefreshTokenException()
            } else if (!errorBody.isNullOrEmpty()) {
                try {
                    if (response.code() == HttpURLConnection.HTTP_BAD_METHOD) {
                        AuthData.isAnon = true
                    }
                    val error = Gson().fromJson(errorBody, ErrorResponse::class.java)
                    ResultCoroutines.Error(ApiException(error.message, error.errorCode))
                } catch (e: Exception) {
                    ResultCoroutines.Error(ApiException(errorMessage))
                }
            } else {
                ResultCoroutines.Error(ApiException(errorMessage))
            }
        } catch (e: UnknownHostException) {
            ResultCoroutines.Error(NotConnectionException())
        } catch (e: RefreshTokenException) {
            when (val responseAuth = updateToken(apiUnit)) { //обновляем токен
                is ResultCoroutines.Success<AuthResponse> -> {
                    onRefreshTokenListener?.onRefreshToken(responseAuth.data)
                    AuthData.accessToken = responseAuth.data.accessToken
                    safeApiResult(call, errorMessage) //повторно выполняем запрос
                }
                else -> {
                    onRefreshTokenListener?.onRefreshTokenFailed()
                    throw e
                }
            }
        } catch (e: SocketTimeoutException) {
            ResultCoroutines.Error(TimeoutException())
        } catch (e: Exception) {
            ResultCoroutines.Error(
                ApiException(e.message ?: "Не удалось выполнить запрос")
            )
        }
    }

    private suspend fun updateToken(apiUnit: ApiUnit): ResultCoroutines<AuthResponse> {
        val refreshToken = AuthData.refreshToken
        val uid = AuthData.uid

        return if (!uid.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
            safeApiResult(
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