package com.pactogg.app.core.security

import com.pactogg.app.testing.SecurityChecks
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category

/** MASVS-RESILIENCE: aviso de root por binario `su`, Magisk o compilación con llaves de prueba. */
@Category(SecurityChecks::class)
class DeviceIntegrityTest {
    @Test fun PL_09_releaseBuildWithoutSuIsNotFlagged() {
        assertFalse(DeviceIntegrity.looksRooted("release-keys") { false })
        assertFalse(DeviceIntegrity.looksRooted(null) { false })
    }

    @Test fun PL_09_suBinaryMagiskOrTestKeysAreFlagged() {
        assertTrue(DeviceIntegrity.looksRooted("release-keys") { it == "/system/xbin/su" })
        assertTrue(DeviceIntegrity.looksRooted("release-keys") { it == "/data/adb/magisk" })
        assertTrue(DeviceIntegrity.looksRooted("test-keys") { false })
    }

    @Test fun PL_09_unreadablePathDoesNotCrashOrFlag() {
        assertFalse(DeviceIntegrity.looksRooted("release-keys") { throw SecurityException("denegado") })
    }
}
