package com.singlelab.lume.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey
    val uid: String,
    val text: String,
    val date: String,
    val personUid: String
    //val images: List<String>
)