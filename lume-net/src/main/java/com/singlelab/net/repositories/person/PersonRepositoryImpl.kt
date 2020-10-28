package com.singlelab.net.repositories.person

import com.singlelab.net.ApiUnit
import com.singlelab.net.model.person.*
import com.singlelab.net.repositories.BaseRepository

class PersonRepositoryImpl(
    private val apiUnit: ApiUnit
) : PersonRepository, BaseRepository(apiUnit) {

    override suspend fun getProfile(): ProfileResponse? {
        return safeApiCall(
            call = { apiUnit.personApi.getProfileAsync().await() },
            errorMessage = "Не удалось получить профиль"
        )
    }

    override suspend fun getProfile(personUid: String): ProfileResponse? {
        return safeApiCall(
            call = { apiUnit.personApi.getProfileAsync(personUid).await() },
            errorMessage = "Не удалось получить профиль"
        )
    }

    override suspend fun getFriends(personUid: String): List<PersonResponse>? {
        return safeApiCall(
            call = { apiUnit.personApi.getFriendsAsync(personUid).await() },
            errorMessage = "Не удалось получить друзей"
        )
    }

    override suspend fun addToFriends(personUid: String) {
        safeApiCall(
            call = { apiUnit.personApi.addToFriendsAsync(personUid).await() },
            errorMessage = "Не удалось добавить в друзья"
        )
    }

    override suspend fun removeFromFriends(personUid: String) {
        safeApiCall(
            call = { apiUnit.personApi.removeFromFriendsAsync(personUid).await() },
            errorMessage = "Не удалось удалить из друзей"
        )
    }

    override suspend fun updateProfile(profileRequest: ProfileRequest): ProfileResponse? {
        return safeApiCall(
            call = { apiUnit.personApi.updateProfileAsync(profileRequest).await() },
            errorMessage = "Не удалось обновить профиль"
        )
    }

    override suspend fun removePushToken() {
        safeApiCall(
            call = { apiUnit.personApi.removePushTokenAsync().await() },
            errorMessage = "Не удалось удалить токен"
        )
    }

    override suspend fun addFeedback(request: FeedbackRequest) {
        safeApiCall(
            call = { apiUnit.personApi.addFeedbackAsync(request).await() },
            errorMessage = "Не удалось отправить отзыв"
        )
    }

    override suspend fun getBadges(personUid: String): List<BadgeResponse>? {
        return safeApiCall(
            call = { apiUnit.personApi.getBadgesAsync(personUid).await() },
            errorMessage = "Не удалось получить бейджи"
        )
    }

    override suspend fun sendReport(reportPersonRequest: ReportPersonRequest) {
        safeApiCall(
            call = { apiUnit.personApi.sendReportAsync(reportPersonRequest).await() },
            errorMessage = "Не удалось отправить жалобу"
        )
    }
}