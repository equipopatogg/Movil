package com.pactogg.app.feature.community

import com.pactogg.app.feature.community.data.RankingEntryResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.SerializationException
import org.junit.Test
import org.junit.Assert.assertTrue

@org.junit.experimental.categories.Category(com.pactogg.app.testing.UnitChecks::class)
class PTS_02ValidationTest {
    @Test fun PTS_02_missingRequiredResponseFieldsAreRejected() {
        val error = runCatching { Json.decodeFromString<RankingEntryResponse>("{}") }.exceptionOrNull()
        assertTrue(error is SerializationException)
    }
}
