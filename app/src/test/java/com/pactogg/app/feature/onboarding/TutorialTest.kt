package com.pactogg.app.feature.onboarding

import com.pactogg.app.core.preferences.TutorialStore
import com.pactogg.app.testing.UnitChecks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.experimental.categories.Category

/** PL-15: tutorial de primer uso. */
@OptIn(ExperimentalCoroutinesApi::class)
@Category(UnitChecks::class)
class TutorialTest {
    private val dispatcher = StandardTestDispatcher()
    private val store = object : TutorialStore {
        val flag = MutableStateFlow(false)
        override val seen = flag
        override suspend fun markSeen() { flag.value = true }
    }

    @Before fun setup() = Dispatchers.setMain(dispatcher)
    @After fun cleanup() = Dispatchers.resetMain()

    @Test fun PL_15_stepsExplainEveryMainButtonBriefly() {
        val steps = TutorialSteps.all
        assertTrue(steps.size in 6..12)
        assertTrue(steps.all { it.title.isNotBlank() && it.where.isNotBlank() && it.body.length <= TutorialSteps.MaxBody })
        val text = steps.joinToString(" ") { it.title + " " + it.where + " " + it.body }
        for (button in listOf("Grupos", "Crear", "Explorar", "Torneos", "Jurado", "evidencia", "Por voz", "perfil")) {
            assertTrue("falta explicar $button", text.contains(button, ignoreCase = true))
        }
        assertTrue("debe aclarar que no hay dinero real", text.contains("dinero real"))
    }

    @Test fun PL_15_pagerStaysInsideTheSteps() {
        var pager = TutorialPager(total = 3)
        assertTrue(pager.isFirst); pager = pager.back(); assertEquals(0, pager.index)
        pager = pager.next().next().next()
        assertEquals(2, pager.index); assertTrue(pager.isLast)
        assertEquals(1, pager.back().index)
    }

    @Test fun PL_15_opensOnceOnFirstUseAndAgainOnlyWhenAsked() = runTest(dispatcher) {
        val first = TutorialViewModel(store); runCurrent()
        assertTrue(first.visible.value)
        first.next(); first.finish(); runCurrent()
        assertFalse(first.visible.value); assertTrue(store.flag.value)

        val later = TutorialViewModel(store); runCurrent()
        assertFalse(later.visible.value)
        later.open()
        assertTrue(later.visible.value); assertEquals(0, later.pager.value.index)
    }
}
