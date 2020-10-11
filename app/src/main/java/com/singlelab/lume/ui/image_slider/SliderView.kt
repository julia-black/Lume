package com.singlelab.lume.ui.image_slider

import android.app.DownloadManager
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
        isShowDownloadButton: Boolean = true
    )

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSuccessDeleting(position: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun saveImage(request: DownloadManager.Request)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showStartDownload()
}