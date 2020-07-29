package com.singlelab.lume.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey
    val personUid: String,
    var accessToken: String,
    var refreshToken: String,
    var cityId: Int? = null,
    var cityName: String? = null
) {
    fun updateAuth(accessToken: String, refreshToken: String) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    fun updateCity(cityId: Int, cityName: String) {
        this.cityId = cityId
        this.cityName = cityName
    }
}