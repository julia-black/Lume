package com.singlelab.lume.ui.feedback

import android.graphics.Bitmap
import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface FeedbackView : LoadingView, ErrorView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSuccessSendFeedback()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showImages(images: List<Bitmap>)
}