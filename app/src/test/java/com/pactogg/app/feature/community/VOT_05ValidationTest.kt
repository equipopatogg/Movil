package com.pactogg.app.feature.community

import com.pactogg.app.feature.community.data.ContractResultResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.SerializationException
import org.junit.Test
import org.junit.Assert.assertTrue

@org.junit.experimental.categories.Category(com.pactogg.app.testing.UnitChecks::class)
class VOT_05ValidationTest {
    @Test fun VOT_05_missingRequiredResponseFieldsAreRejected() {
        val error = runCatching { Json.decodeFromString<ContractResultResponse>("{}") }.exceptionOrNull()
        assertTrue(error is SerializationException)
    }
}
