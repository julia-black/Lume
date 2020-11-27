package com.singlelab.lume.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.singlelab.lume.database.entity.Profile

@Dao
internal abstract class ProfileDao : BaseDao<Profile> {
    @Query("select * from profile limit 1")
    internal abstract suspend fun getProfile(): Profile?

    @Query("delete from profile")
    internal abstract suspend fun clear()
}