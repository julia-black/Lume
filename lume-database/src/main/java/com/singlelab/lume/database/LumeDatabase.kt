package com.singlelab.lume.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.singlelab.lume.database.dao.ChatsDao
import com.singlelab.lume.database.dao.ProfileDao
import com.singlelab.lume.database.entity.Chat
import com.singlelab.lume.database.entity.ProfileEntity

@Database(
    version = 3,
    entities = [
        Chat::class,
        ProfileEntity::class
    ],
    exportSchema = false
)
abstract class LumeDatabase : RoomDatabase() {
    internal abstract fun chatsDao(): ChatsDao

    internal abstract fun profileDao(): ProfileDao

    companion object {
        fun create(context: Context): LumeDatabase =
            Room.databaseBuilder(context, LumeDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()

        private const val DATABASE_NAME = "lumedatabase.name"
    }
}