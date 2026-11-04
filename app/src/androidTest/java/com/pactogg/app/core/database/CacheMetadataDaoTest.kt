package com.pactogg.app.core.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CacheMetadataDaoTest {
    private lateinit var database: PactoDatabase

    @Before
    fun createDatabase() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
            PactoDatabase::class.java).build()
    }

    @After
    fun closeDatabase() { database.close() }

    @Test
    fun upsertReplacesTimestampAndClearRemovesData() = runTest {
        val dao = database.cacheMetadataDao()
        assertNull(dao.find("technical_probe"))
        dao.upsert(CacheMetadataEntity("technical_probe", 1))
        dao.upsert(CacheMetadataEntity("technical_probe", 2))
        assertEquals(2L, dao.find("technical_probe")?.updatedAtEpochMillis)
        dao.clear()
        assertNull(dao.find("technical_probe"))
    }
}
