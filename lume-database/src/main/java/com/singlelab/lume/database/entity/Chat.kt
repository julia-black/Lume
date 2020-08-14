package com.singlelab.lume.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class Chat(
    @PrimaryKey
    val uid: String,
    val title: String,
    val isGroup: Boolean,
    val image: String,
    val lastMessage: String,
    val lastMessagePersonUid: String,
    val unreadMessagesCount: Int = 0
)