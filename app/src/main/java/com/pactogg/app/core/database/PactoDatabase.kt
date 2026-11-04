package com.pactogg.app.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CacheMetadataEntity::class], version = 1, exportSchema = true)
abstract class PactoDatabase : RoomDatabase() {
    abstract fun cacheMetadataDao(): CacheMetadataDao
}
