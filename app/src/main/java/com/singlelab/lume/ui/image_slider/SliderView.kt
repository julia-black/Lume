package com.singlelab.lume.ui.image_slider

import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType


interface SliderView : LoadingView, ErrorView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showImages(links: List<String>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showOptionsButton(
        isShowDeleteButton: Boolean = false,
        isShowDownloadButton: Boolean = false
    )

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSuccessDeleting(position: Int)
}