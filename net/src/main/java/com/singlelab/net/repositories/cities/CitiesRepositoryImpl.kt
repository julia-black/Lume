package com.singlelab.net.repositories.cities

import com.singlelab.net.ApiUnit
import com.singlelab.net.model.city.CityResponse
import com.singlelab.net.repositories.BaseRepository

class CitiesRepositoryImpl(
    private val apiUnit: ApiUnit
) : CitiesRepository, BaseRepository(apiUnit) {
    override suspend fun getCities(): List<CityResponse>? {
        return safeApiCall(
            call = { apiUnit.citiesApi.getCitiesAsync().await() },
            errorMessage = "Не удалось получить список городов"
        )
    }
}