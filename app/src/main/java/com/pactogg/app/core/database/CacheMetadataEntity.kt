package com.pactogg.app.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Technical cache timestamps only. Domain data and session tokens do not belong here. */
@Entity(tableName = "cache_metadata")
data class CacheMetadataEntity(@PrimaryKey val cacheKey: String, val updatedAtEpochMillis: Long)
