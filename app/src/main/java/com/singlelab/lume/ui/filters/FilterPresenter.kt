package com.singlelab.lume.ui.filters

import com.singlelab.lume.pref.Preferences
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class FilterPresenter @Inject constructor(preferences: Preferences?) : MvpPresenter<FilterView>()