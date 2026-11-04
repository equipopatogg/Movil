package com.pactogg.app.core.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface CacheMetadataDao {
    @Upsert
    suspend fun upsert(metadata: CacheMetadataEntity)

    @Query("SELECT * FROM cache_metadata WHERE cacheKey = :key LIMIT 1")
    suspend fun find(key: String): CacheMetadataEntity?

    @Query("DELETE FROM cache_metadata")
    suspend fun clear()
}
