package com.singlelab.lume.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.singlelab.lume.database.dao.ChatsDao
import com.singlelab.lume.database.entity.Chat

@Database(
    version = 1,
    entities = [
        Chat::class
    ],
    exportSchema = false
)
abstract class LumeDatabase : RoomDatabase() {
    internal abstract fun chatsDao(): ChatsDao

    companion object {
        fun create(context: Context): LumeDatabase =
            Room.databaseBuilder(context, LumeDatabase::class.java, DATABASE_NAME)
                .build()

        private const val DATABASE_NAME = "lumedatabase.name"
    }
}