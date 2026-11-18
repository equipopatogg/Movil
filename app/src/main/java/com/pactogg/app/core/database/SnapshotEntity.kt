package com.pactogg.app.core.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert

/** Última respuesta JSON de una consulta, por cuenta (PL-12). Nunca guarda tokens ni encabezados. */
@Entity(tableName = "snapshots")
data class SnapshotEntity(@PrimaryKey val cacheKey: String, val ownerId: String, val body: String,
    val savedAtEpochMillis: Long)

/** Sin suspend: lo usa el interceptor de OkHttp, que ya corre fuera del hilo principal. */
@Dao
interface SnapshotDao {
    @Upsert
    fun save(snapshot: SnapshotEntity)

    @Query("SELECT * FROM snapshots WHERE cacheKey = :key AND ownerId = :owner LIMIT 1")
    fun find(key: String, owner: String): SnapshotEntity?

    @Query("DELETE FROM snapshots")
    fun clear()
}
