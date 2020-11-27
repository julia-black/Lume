package com.singlelab.lume.database.converter

import androidx.room.TypeConverter

class EventTypesConverter {
    @TypeConverter
    fun fromTypes(types: List<Int>) =
        types.joinToString()

    @TypeConverter
    fun toTypes(types: String): List<Int> =
        types.split(", ").map { it.toInt() }

}