package com.singlelab.lume.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.singlelab.lume.database.converter.EventTypesConverter

@Entity(tableName = "events")
@TypeConverters(EventTypesConverter::class)

data class EventSummary(
    @PrimaryKey
    val eventUid: String,
    val name: String,
    val description: String,
    val startTime: String,
    val endTime: String,
    val types: List<Int>,
    val eventPrimaryImageContentUid: String?,
    val xCoordinate: Double?,
    val yCoordinate: Double?,
    val status: Int,
    val isAdministrator: Boolean = false,
    val participantStatus: Int,
    val anyPersonWaitingForApprove: Boolean,
    val isOnline: Boolean,
    val chatUid: String? = null,
    val cityName: String? = null
)
