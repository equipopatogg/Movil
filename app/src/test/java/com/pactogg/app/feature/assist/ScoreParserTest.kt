package com.pactogg.app.feature.assist

import com.pactogg.app.feature.assist.domain.ScoreParser
import com.pactogg.app.testing.UnitChecks
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.experimental.categories.Category

/** PL-14 (ML): lectura del marcador del texto que reconoce ML Kit. */
@Category(UnitChecks::class)
class ScoreParserTest {
    @Test fun PL_14_readsCommonScoreboardFormats() {
        assertEquals(3 to 1, ScoreParser.parse("VICTORIA\nEquipo A 3 - 1 Equipo B"))
        assertEquals(2 to 0, ScoreParser.parse("Final 2:0"))
        assertEquals(10 to 8, ScoreParser.parse("Ronda 10 x 8"))
        assertEquals(4 to 4, ScoreParser.parse("4 a 4"))
        assertEquals(1 to 2, ScoreParser.parse("Marcador 1–2"))
    }

    @Test fun PL_14_ambiguousOrMissingScoreIsLeftToThePerson() {
        assertNull(ScoreParser.parse("Sin marcador en esta foto"))
        assertNull(ScoreParser.parse("Ronda 1: 3 - 1, ronda 2: 0 - 2"))
        assertNull(ScoreParser.parse("Partida a las 18:30"))
        assertNull(ScoreParser.parse("1234 - 1"))
        assertNull(ScoreParser.parse("versión 1.2 - 3"))
    }

    @Test fun PL_14_sameScoreRepeatedIsStillOneScore() {
        assertEquals(5 to 3, ScoreParser.parse("5 - 3\nRESULTADO 5-3"))
    }
}
