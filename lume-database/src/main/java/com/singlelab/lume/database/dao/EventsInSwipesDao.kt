package com.singlelab.lume.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.singlelab.lume.database.entity.Event

@Dao
internal abstract class EventsInSwipesDao : BaseDao<Event> {
//    @Query("select 1 from events_in_swipes")
//    internal abstract suspend fun getEvent(): Event

    @Query("delete from events_in_swipes")
    internal abstract suspend fun clear()
}