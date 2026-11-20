package com.pactogg.app.core.network

import com.pactogg.app.BuildConfig
import com.pactogg.app.testing.SecurityChecks
import java.io.File
import java.net.URI
import java.time.LocalDate
import javax.xml.parsers.DocumentBuilderFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category
import org.w3c.dom.Element

/** MASVS-NETWORK: sin tráfico en claro y certificados fijados para la API de producción. */
@Category(SecurityChecks::class)
class NetworkSecurityConfigTest {
    private val document = DocumentBuilderFactory.newInstance().apply {
        setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
    }.newDocumentBuilder().parse(File("src/main/res/xml/network_security_config.xml"))

    private fun elements(name: String) = document.getElementsByTagName(name).let { list ->
        (0 until list.length).map { list.item(it) as Element }
    }

    @Test fun NET_SEC_cleartextIsForbiddenEverywhere() {
        val configs = elements("base-config") + elements("domain-config")
        assertTrue(configs.isNotEmpty())
        configs.forEach { assertEquals("false", it.getAttribute("cleartextTrafficPermitted")) }
    }

    @Test fun NET_SEC_apiHostIsPinnedWithBackupPinsThatOutliveTheEvaluation() {
        val host = URI(BuildConfig.API_BASE_URL).host
        assertEquals("https", URI(BuildConfig.API_BASE_URL).scheme)
        assertTrue("el dominio de la API debe estar fijado", elements("domain").any { it.textContent.trim() == host })
        val pinSet = elements("pin-set").single()
        val pins = elements("pin").map { it.textContent.trim() }
        assertTrue("se necesitan al menos 2 pines (principal y respaldo)", pins.size >= 2)
        assertTrue(pins.all { it.length == 44 && it.endsWith("=") })
        assertEquals(pins.size, pins.toSet().size)
        assertTrue(LocalDate.parse(pinSet.getAttribute("expiration")).isAfter(LocalDate.of(2026, 12, 31)))
    }
}
