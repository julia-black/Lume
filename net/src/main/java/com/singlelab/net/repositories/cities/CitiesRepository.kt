package com.singlelab.net.repositories.cities

import com.singlelab.net.model.city.CityResponse

interface CitiesRepository {
    suspend fun getCities(): List<CityResponse>?
}