package com.singlelab.lume.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.singlelab.lume.database.entity.Chat

@Dao
internal abstract class ChatsDao : BaseDao<Chat> {
    @Query("select * from chats")
    internal abstract suspend fun all(): List<Chat>
}