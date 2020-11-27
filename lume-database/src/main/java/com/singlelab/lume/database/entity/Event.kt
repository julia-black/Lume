package com.singlelab.lume.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.singlelab.lume.database.converter.EventImageConverter
import com.singlelab.lume.database.converter.EventTypesConverter

@Entity(tableName = "events_in_swipes")
@TypeConverters(
    EventImageConverter::class,
    EventTypesConverter::class
)

data class Event(
    @PrimaryKey
    val eventUid: String,
    val name: String,
    val minAge: Int = -1,
    val maxAge: Int = -1,
    val xCoordinate: Double = -1.0,
    val yCoordinate: Double = -1.0,
    val description: String,
    val startTime: String,
    val endTime: String,
    val chatUid: String,
    val status: Int = 0,
    val types: List<Int>,
//    val participants: List<Person> = listOf(),
//    val administrator: Person? = null,
    val isOpenForInvitations: Boolean = true,
    val eventPrimaryImageContentUid: String = "",
    val images: List<String> = listOf(),
    val cityId: Int = -1,
    val cityName: String = "",
    val isOnline: Boolean,
    val promoRequestUid: String = ""
)
