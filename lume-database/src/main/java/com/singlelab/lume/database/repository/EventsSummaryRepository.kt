package com.singlelab.lume.database.repository

import com.singlelab.lume.database.LumeDatabase
import com.singlelab.lume.database.entity.EventSummary

interface EventsSummaryRepository {
    suspend fun insert(events: List<EventSummary>)
    suspend fun getEvents(): List<EventSummary>?
    suspend fun clear()
}

class RoomEventsSummaryRepository(db: LumeDatabase) : EventsSummaryRepository {
    private val dao = db.eventsSummaryDao()

    override suspend fun insert(events: List<EventSummary>) =
        dao.insertOrReplace(events)

    override suspend fun getEvents(): List<EventSummary> = dao.all()

    override suspend fun clear() {
        dao.clear()
    }
}