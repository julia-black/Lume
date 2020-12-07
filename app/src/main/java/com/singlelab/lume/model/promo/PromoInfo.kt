package com.singlelab.lume.model.promo

import com.singlelab.net.model.promo.PromoInfoResponse

class PromoInfo(
    val isEventPromoRewardEnabled: Boolean = false,
    val isNewYearPromoRewardEnabled: Boolean = false
) {
    companion object {
        fun fromResponse(response: PromoInfoResponse?): PromoInfo {
            response ?: return PromoInfo(false)
            return PromoInfo(
                response.isEventPromoRewardEnabled,
                response.isNewYearPromoRewardEnabled
            )
        }
    }
}