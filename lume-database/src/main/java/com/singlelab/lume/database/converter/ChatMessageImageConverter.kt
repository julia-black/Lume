package com.singlelab.lume.database.converter

import androidx.room.TypeConverter

class ChatMessageImageConverter {
    @TypeConverter
    fun fromMessages(messages: List<String>) =
        messages.joinToString()

    @TypeConverter
    fun toMessages(messages: String): List<String> =
        messages.split(", ").toList()
}
