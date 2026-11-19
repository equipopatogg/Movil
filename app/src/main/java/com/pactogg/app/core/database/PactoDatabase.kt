package com.pactogg.app.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CacheMetadataEntity::class, SnapshotEntity::class], version = 2, exportSchema = true,
    autoMigrations = [AutoMigration(from = 1, to = 2)])
abstract class PactoDatabase : RoomDatabase() {
    abstract fun cacheMetadataDao(): CacheMetadataDao
    abstract fun snapshotDao(): SnapshotDao
}
