package com.singlelab.lume.ui.receive_reward.interactor

import com.singlelab.net.model.promo.PromoRequest

interface ReceiveRewardInteractor {
    suspend fun sendPromoRequest(request: PromoRequest)
}