package com.pactogg.app.feature.evidence.send

import com.pactogg.app.feature.connected.domain.EvidenceRules
import com.pactogg.app.feature.connected.domain.InputRules
import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.ConsumerBoundaryFixture
import com.pactogg.app.testing.shared.ContractFixture
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** EVI-02: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class SendEvidenceValidationTest {
    @Test fun EVI_02_rejectsLookalikeDomainAndEmptyPhoto() = runBlocking {
        val boundary = ConsumerBoundaryFixture()
        assertFalse(InputRules.evidenceLink("https://youtube.com.attacker.example/a"))
        assertTrue(runCatching { boundary.products.sendPhoto(ContractFixture, byteArrayOf(), "image/png") }.exceptionOrNull() is IllegalArgumentException)
    }

    @Test fun EVI_02_onlyExecutorInActiveStateBeforeDeadlineCanUpload() {
        val active = ContractFixture.copy(status = "in_progress", acceptedBy = ContractFixture.targetId)
        val user = ContractFixture.targetId
        assertTrue(EvidenceRules.canUpload(active, user, 1000))
        assertTrue(EvidenceRules.canUpload(active.copy(status = "voting", evidenceCount = 2), user, 1000))
        for (invalid in listOf(active.copy(status = "open"), active.copy(status = "cancelled"),
            active.copy(evidenceCount = 3), active.copy(deadline = "1970-01-01T00:00:01Z"),
            active.copy(deadline = "bad"), active.copy(acceptedBy = "other"))) {
            assertFalse(EvidenceRules.canUpload(invalid, user, 1000))
        }
        assertFalse(EvidenceRules.canUpload(active, null, 1000))
        assertFalse(EvidenceRules.canUpload(active, "other", 1000))
        val group = active.copy(targetType = "group", targetId = null)
        assertTrue(EvidenceRules.canUpload(group, user, 1000))
        assertFalse(EvidenceRules.canUpload(group.copy(acceptedBy = null), user, 1000))
        assertTrue(EvidenceRules.canUpload(group.copy(acceptedBy = null, executorId = user), user, 1000))
    }

    @Test fun EVI_02_onlyHttpsImagesAndUnexpiredDatesAreDisplayable() {
        assertEquals("https://cdn.example/photo.jpg", EvidenceRules.imageUrl("https://cdn.example/photo.jpg"))
        for (value in listOf("http://cdn.example/a", "https://user:pass@cdn.example/a", "file:///photo", "bad")) {
            assertNull(EvidenceRules.imageUrl(value))
        }
        assertTrue(EvidenceRules.expired("1970-01-01T00:00:01.000Z", 1001))
        assertTrue(EvidenceRules.expired("invalid", 1000))
        assertFalse(EvidenceRules.expired("2099-01-01T00:00:00Z", 1000))
        assertFalse(EvidenceRules.expired(null, 1000))
    }

    @Test fun EVI_02_oversizedAndInvalidMimePhotosStopBeforeHttp() = runBlocking {
        val products = ConsumerBoundaryFixture().products
        val max = com.pactogg.app.feature.connected.data.ApiProductRepository.MaxPhotoBytes
        assertTrue(runCatching { products.sendPhoto(ContractFixture, ByteArray(max + 1), "image/jpeg") }
            .exceptionOrNull() is IllegalArgumentException)
        assertTrue(runCatching { products.sendPhoto(ContractFixture, byteArrayOf(1), "video/mp4") }
            .exceptionOrNull() is IllegalArgumentException)
    }
}
