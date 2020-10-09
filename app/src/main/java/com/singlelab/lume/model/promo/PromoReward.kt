package com.singlelab.lume.model.promo

import com.singlelab.net.model.promo.PromoRewardResponse

class PromoReward(
    val isCitySuitableForPromoReward: Boolean = false,
    val numberOfCityPromoEvents: Int = 0
) {
    companion object {
        fun fromResponse(response: PromoRewardResponse?): PromoReward? {
            response ?: return null
            return PromoReward(
                response.isCitySuitableForPromoReward,
                response.numberOfCityPromoEvents
            )
        }
    }
}