package com.singlelab.lume.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.singlelab.lume.database.entity.ProfileEntity

@Dao
internal abstract class ProfileDao : BaseDao<ProfileEntity> {
    @Query("select * from profile")
    internal abstract suspend fun getProfile(): ProfileEntity?

    @Query("delete from profile")
    internal abstract suspend fun clear()
}