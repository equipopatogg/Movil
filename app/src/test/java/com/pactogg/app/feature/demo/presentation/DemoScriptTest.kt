package com.pactogg.app.feature.demo.presentation

import com.pactogg.app.testing.UnitChecks
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category

/** PL-07: la animación del reto por voz de la demo es determinista y no depende de micrófono ni red. */
@Category(UnitChecks::class)
class DemoScriptTest {
    @Test fun PL_07_startsEmptyAndListening() {
        val frame = demoFrame(0)
        assertEquals("", frame.phrase); assertTrue(frame.fields.all { it.isEmpty() })
        assertTrue(frame.listening); assertFalse(frame.done)
        assertEquals(frame, demoFrame(-5))
    }

    @Test fun PL_07_phraseFinishesBeforeAnyFieldStarts() {
        val frame = demoFrame(DemoPhrase.length)
        assertEquals(DemoPhrase, frame.phrase); assertFalse(frame.listening)
        assertTrue(frame.fields.all { it.isEmpty() }); assertFalse(frame.done)
    }

    @Test fun PL_07_fieldsFillInOrderAndNeverShrink() {
        var previous = demoFrame(0)
        for (tick in 1..DemoTicks) {
            val frame = demoFrame(tick)
            assertTrue(frame.phrase.length >= previous.phrase.length)
            frame.fields.forEachIndexed { index, value ->
                assertTrue(value.length >= previous.fields[index].length)
                if (value.isNotEmpty() && index > 0) assertEquals(DemoFields[index - 1].second, frame.fields[index - 1])
            }
            previous = frame
        }
    }

    @Test fun PL_07_lastFrameShowsEverythingAndStaysDone() {
        val frame = demoFrame(DemoTicks)
        assertTrue(frame.done); assertEquals(DemoPhrase, frame.phrase)
        assertEquals(DemoFields.map { it.second }, frame.fields)
        assertEquals(frame, demoFrame(DemoTicks + 100))
    }
}
