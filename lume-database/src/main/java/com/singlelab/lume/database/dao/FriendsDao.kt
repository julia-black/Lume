package com.singlelab.lume.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.singlelab.lume.database.entity.Person

@Dao
internal abstract class FriendsDao : BaseDao<Person> {
    @Query("select * from persons")
    internal abstract suspend fun getFriends(): List<Person>

    @Query("delete from persons")
    internal abstract suspend fun clear()
}