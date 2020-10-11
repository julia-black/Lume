package com.singlelab.lume.ui.receive_reward

import android.graphics.Bitmap
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.receive_reward.interactor.ReceiveRewardInteractor
import com.singlelab.lume.util.isValidCardNum
import com.singlelab.lume.util.resize
import com.singlelab.lume.util.toBase64
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.promo.PromoRequest
import moxy.InjectViewState
import javax.inject.Inject


@InjectViewState
class ReceiveRewardPresenter @Inject constructor(
    private val interactor: ReceiveRewardInteractor,
    preferences: Preferences?
) : BasePresenter<ReceiveRewardView>(preferences, interactor as BaseInteractor) {

    companion object {
        private const val MAX_COUNT_IMAGE = 5
    }

    private var images: MutableList<Bitmap> = mutableListOf()

    private var eventUid: String? = null

    fun setEventUid(eventUid: String) {
        this.eventUid = eventUid
    }

    fun onApplyReward(cardNum: String) {
        if (cardNum.isBlank()) {
            viewState.showEmptyCardNum()
        } else if (!cardNum.isValidCardNum()) {
            viewState.showInvalidCardNum()
        } else if (images.isEmpty()) {
            viewState.showEmptyImages()
        } else {
            viewState.showLoading(true)
            val promoRequest = generateRequest(cardNum, images)
            invokeSuspend {
                try {
                    interactor.sendPromoRequest(promoRequest)
                    runOnMainThread {
                        viewState.showSuccess()
                        viewState.showLoading(false)
                    }
                } catch (e: ApiException) {
                    runOnMainThread {
                        viewState.showLoading(false)
                        viewState.showError(e.message)
                    }
                }
            }
        }
    }

    fun addImages(images: List<Bitmap>) {
        this.images.addAll(images)
        if (this.images.size > MAX_COUNT_IMAGE) {
            this.images = this.images.subList(0, MAX_COUNT_IMAGE)
            viewState.showError(R.string.too_many_images)
        }
        viewState.showImages(this.images)
    }

    fun deleteImage(position: Int) {
        this.images.removeAt(position)
        viewState.showImages(this.images)
    }

    private fun generateRequest(cardNum: String, images: List<Bitmap>): PromoRequest {
        return PromoRequest(
            eventUid = eventUid,
            accountingNumber = cardNum,
            images = images.map { it.resize().toBase64() }
        )
    }
}