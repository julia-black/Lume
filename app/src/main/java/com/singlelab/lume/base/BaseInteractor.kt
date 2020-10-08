package com.singlelab.lume.base

import com.singlelab.lume.model.profile.PersonNotifications
import com.singlelab.lume.model.promo.PromoInfo
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.OnRefreshTokenListener

open class BaseInteractor(private val baseRepository: BaseRepository) {

    fun setOnRefreshTokenListener(listener: OnRefreshTokenListener?) {
        baseRepository.setOnRefreshTokenListener(listener)
    }

    suspend fun getNotifications(): PersonNotifications {
        return PersonNotifications.fromResponse(baseRepository.getNotifications())
    }

    suspend fun getPromo(): PromoInfo? {
        return PromoInfo.fromResponse(baseRepository.getPromo())
    }
}