package com.singlelab.lume.base

import com.singlelab.data.repositories.BaseRepository
import com.singlelab.data.repositories.OnRefreshTokenListener

open class BaseInteractor(private val baseRepository: BaseRepository) {

    fun setOnRefreshTokenListener(listener: OnRefreshTokenListener?) {
        baseRepository.setOnRefreshTokenListener(listener)
    }
}