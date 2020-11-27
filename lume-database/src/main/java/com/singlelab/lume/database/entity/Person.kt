package com.singlelab.lume.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class Person(
    @PrimaryKey
    val personUid: String,
    val name: String,
    val login: String,
    val description: String,
    val age: Int,
    val cityName: String,
    val imageContentUid: String,
    val isFriend: Boolean = false,
    val friendshipApprovalRequired: Boolean = false,
    val participantStatus: Int = -1
)