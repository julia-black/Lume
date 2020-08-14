package com.singlelab.lume.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.singlelab.lume.database.dao.ChatMessagesDao
import com.singlelab.lume.database.dao.ChatsDao
import com.singlelab.lume.database.entity.Chat
import com.singlelab.lume.database.entity.ChatMessage

@Database(
    version = 2,
    entities = [
        Chat::class,
        ChatMessage::class
    ],
    exportSchema = false
)
abstract class LumeDatabase : RoomDatabase() {
    internal abstract fun chatsDao(): ChatsDao
    internal abstract fun chatMessagesDao(): ChatMessagesDao

    companion object {
        fun create(context: Context): LumeDatabase =
            Room.databaseBuilder(context, LumeDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()

        private const val DATABASE_NAME = "lumedatabase.name"
    }
}