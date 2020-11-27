package com.singlelab.lume.ui.reg

import android.graphics.Bitmap
import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface RegistrationView : LoadingView, ErrorView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onRegistration()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showName(name: String?)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showAge(age: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showDescription(description: String?)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showCity(cityName: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showImage(bitmap: Bitmap?)
}