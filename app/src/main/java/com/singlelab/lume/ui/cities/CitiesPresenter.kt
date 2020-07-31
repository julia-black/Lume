package com.singlelab.lume.ui.cities

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.city.City
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.cities.interactor.CitiesInteractor
import com.singlelab.net.exceptions.ApiException
import moxy.InjectViewState
import java.util.*
import javax.inject.Inject

@InjectViewState
class CitiesPresenter @Inject constructor(
    private val interactor: CitiesInteractor,
    preferences: Preferences?
) : BasePresenter<CitiesView>(preferences, interactor as BaseInteractor) {

    private var allCities: List<City>? = null

    private val cities: MutableList<City> = mutableListOf()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadCities()
    }

    private fun loadCities() {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                allCities = interactor.getCities()
                runOnMainThread {
                    viewState.showLoading(false)
                    viewState.showCities(allCities)
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(false)
                    viewState.showError(e.message)
                }
            }
        }
    }

    fun filter(queryStr: String) {
        if (queryStr.isBlank()) {
            viewState.showCities(allCities)
        } else {
            allCities?.let { allCities ->
                viewState.showCities(allCities.filter {
                    it.cityName.toUpperCase(Locale.getDefault())
                        .startsWith(queryStr.toUpperCase(Locale.getDefault()))
                })
            }
        }
    }
}