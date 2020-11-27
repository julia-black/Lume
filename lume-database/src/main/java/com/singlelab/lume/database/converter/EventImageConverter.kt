package com.singlelab.lume.database.converter

import androidx.room.TypeConverter

class EventImageConverter {
    @TypeConverter
    fun fromImages(images: List<String>) =
       images.joinToString()

    @TypeConverter
    fun toImages(images: String): List<String> =
        images.split(", ").toList()
}