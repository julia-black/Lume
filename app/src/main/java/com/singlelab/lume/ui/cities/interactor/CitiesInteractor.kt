package com.singlelab.lume.ui.cities.interactor

import com.singlelab.lume.model.city.City

interface CitiesInteractor {
    suspend fun getCities(): List<City>?
}