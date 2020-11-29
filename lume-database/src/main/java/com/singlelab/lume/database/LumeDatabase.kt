package com.singlelab.lume.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.singlelab.lume.database.dao.*
import com.singlelab.lume.database.entity.*

@Database(
    version = 12,
    entities = [
        Chat::class,
        ChatMessage::class,
        EventSummary::class,
        Profile::class,
        Person::class
    ],
    exportSchema = false
)
abstract class LumeDatabase : RoomDatabase() {
    internal abstract fun chatsDao(): ChatsDao
    internal abstract fun chatMessagesDao(): ChatMessagesDao
    internal abstract fun eventsSummaryDao(): EventsSummaryDao
    internal abstract fun profileDao(): ProfileDao
    internal abstract fun friendsDao(): FriendsDao

    companion object {
        fun create(context: Context): LumeDatabase =
            Room.databaseBuilder(context, LumeDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()

        private const val DATABASE_NAME = "lumedatabase.name"
    }
}