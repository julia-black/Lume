package com.singlelab.lume.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class Profile(
    @PrimaryKey
    val personUid: String,
    val login: String,
    val name: String,
    val description: String,
    val cityId: Int,
    val cityName: String,
    val age: Int,
    val imageContentUid: String,
    val isFriend: Boolean = false
    //val friends: List<PersonResponse> = arrayListOf()
)