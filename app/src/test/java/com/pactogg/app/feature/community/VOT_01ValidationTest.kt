package com.pactogg.app.feature.community

import com.pactogg.app.feature.community.data.PendingVoteResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.SerializationException
import org.junit.Test
import org.junit.Assert.assertTrue

@org.junit.experimental.categories.Category(com.pactogg.app.testing.UnitChecks::class)
class VOT_01ValidationTest {
    @Test fun VOT_01_missingRequiredResponseFieldsAreRejected() {
        val error = runCatching { Json.decodeFromString<PendingVoteResponse>("{}") }.exceptionOrNull()
        assertTrue(error is SerializationException)
    }
}
