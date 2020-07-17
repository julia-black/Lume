package com.singlelab.data.repositories.events

import com.singlelab.data.net.ApiUnit
import com.singlelab.data.repositories.BaseRepository

class EventsRepositoryImpl(private val apiUnit: ApiUnit) : EventsRepository, BaseRepository() {
}