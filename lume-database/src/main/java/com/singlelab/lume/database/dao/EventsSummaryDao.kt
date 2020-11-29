package com.singlelab.lume.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.singlelab.lume.database.entity.EventSummary

@Dao
internal abstract class EventsSummaryDao : BaseDao<EventSummary> {
    @Query("select * from events")
    internal abstract suspend fun all(): List<EventSummary>

    @Query("select * from events where eventUid = :eventUid limit 1")
    internal abstract suspend fun getEvent(eventUid: String): EventSummary?

    @Query("delete from events")
    internal abstract suspend fun clear()
}