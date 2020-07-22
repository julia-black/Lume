package com.singlelab.lume.base

import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.OnRefreshTokenListener

open class BaseInteractor(private val baseRepository: BaseRepository) {

    fun setOnRefreshTokenListener(listener: OnRefreshTokenListener?) {
        baseRepository.setOnRefreshTokenListener(listener)
    }
}