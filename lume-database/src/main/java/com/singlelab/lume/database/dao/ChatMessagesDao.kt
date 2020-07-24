package com.singlelab.lume.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.singlelab.lume.database.entity.ChatMessage

@Dao
internal abstract class ChatMessagesDao : BaseDao<ChatMessage> {
    @Query("select * from chat_messages")
    internal abstract suspend fun all(): List<ChatMessage>

    @Query("select * from chat_messages limit 50")
    internal abstract suspend fun last50messages(): List<ChatMessage>
}