package com.pactogg.app.feature.community

import com.pactogg.app.feature.community.data.FameEntryResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.SerializationException
import org.junit.Test
import org.junit.Assert.assertTrue

@org.junit.experimental.categories.Category(com.pactogg.app.testing.UnitChecks::class)
class ACH_01ValidationTest {
    @Test fun ACH_01_missingRequiredResponseFieldsAreRejected() {
        val error = runCatching { Json.decodeFromString<FameEntryResponse>("{}") }.exceptionOrNull()
        assertTrue(error is SerializationException)
    }
}
