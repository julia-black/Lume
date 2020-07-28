package com.singlelab.lume.ui.cities.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.model.city.City
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.cities.CitiesRepository

class CitiesInteractorImpl(private val repository: CitiesRepository) : CitiesInteractor,
    BaseInteractor(repository as BaseRepository) {
    override suspend fun getCities(): List<City>? {
        return repository.getCities()?.mapNotNull {
            City.fromResponse(it)
        }
    }
}