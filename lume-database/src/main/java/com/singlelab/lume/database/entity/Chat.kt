package com.singlelab.lume.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class Chat(
    @PrimaryKey
    val uId: String,
    val lastMessage: String
)