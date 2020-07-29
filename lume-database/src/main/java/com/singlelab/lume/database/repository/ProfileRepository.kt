package com.singlelab.lume.database.repository

import com.singlelab.lume.database.LumeDatabase
import com.singlelab.lume.database.entity.ProfileEntity

interface ProfileRepository {
    suspend fun insert(profile: ProfileEntity)
    suspend fun update(profile: ProfileEntity)
    suspend fun getProfile(): ProfileEntity?
    suspend fun clear()
}

class RoomProfileRepository(db: LumeDatabase) : ProfileRepository {
    private val dao = db.profileDao()

    override suspend fun insert(profile: ProfileEntity) {
        dao.insertOrReplace(profile)
    }

    override suspend fun update(profile: ProfileEntity) {
        dao.update(profile)
    }

    override suspend fun getProfile() = dao.getProfile()

    override suspend fun clear() {
        dao.clear()
    }
}