package com.singlelab.lume.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

internal interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(roomEntities: Collection<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(roomEntity: T)

    @Update
    suspend fun update(roomEntity: T)

    @Delete
    suspend fun delete(roomEntity: T)
}