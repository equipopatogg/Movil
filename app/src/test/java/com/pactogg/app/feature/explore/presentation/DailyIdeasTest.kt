package com.pactogg.app.feature.explore.presentation

import com.pactogg.app.testing.UnitChecks
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category

/** PL-06: catálogo local de ideas y selección diaria de Explorar. */
@Category(UnitChecks::class)
class DailyIdeasTest {
    private val catalog = parseIdeas(File("src/main/assets/explorar/retos.json").readText())

    @Test fun PL_06_bundledCatalogIsCompleteAndFitsTheChallengeForm() {
        assertEquals(500, catalog.size)
        assertEquals(200, catalog.map { it.game }.distinct().size)
        assertEquals(catalog.size, catalog.map { it.game to it.description }.distinct().size)
        assertTrue(catalog.all { it.game.length in 1..MaxIdeaGame && it.description.length in 1..MaxIdeaRule })
        assertTrue(catalog.none { it.category == IdeaCategory.ALL })
    }

    @Test fun PL_06_invalidRowsAreSkippedAndCategoriesAreMapped() {
        val ideas = parseIdeas("""[
            {"juego":"Halo","titulo":"Uno","reto":"Gana","categoria":"Cooperativo"},
            {"juego":"Halo","titulo":"Dos","reto":"Gana","categoria":"Desconocida"},
            {"juego":"","titulo":"Tres","reto":"Gana","categoria":"Habilidad"},
            {"juego":"Halo","titulo":"","reto":"${"x".repeat(281)}","categoria":"Constancia"}]""")
        assertEquals(listOf("Uno"), ideas.map { it.title })
        assertEquals(IdeaCategory.COOPERATIVE, ideas.single().category)
    }

    @Test fun PL_06_sameDayShowsSameFourAndAnotherDayChanges() {
        val state = ExploreState(catalog)
        val today = state.dailyIdeas(2026318, emptyList())
        assertEquals(DailyIdeaCount, today.size)
        assertEquals(today, state.dailyIdeas(2026318, emptyList()))
        assertNotEquals(today, state.dailyIdeas(2026319, emptyList()))
    }

    @Test fun PL_06_discardReplacesOnlyThatIdeaAndFiltersApply() {
        val state = ExploreState(catalog)
        val before = state.dailyIdeas(2026318, emptyList())
        val after = state.dailyIdeas(2026318, listOf(before.first().id))
        assertFalse(after.contains(before.first()))
        assertEquals(before.drop(1), after.take(3))
        assertEquals(DailyIdeaCount, after.size)
        val halo = state.copy(query = "minecraft", category = IdeaCategory.SKILL).dailyIdeas(2026318, emptyList())
        assertTrue(halo.isNotEmpty() && halo.all { it.game == "Minecraft" && it.category == IdeaCategory.SKILL })
        assertTrue(state.copy(query = "juego que no existe").dailyIdeas(2026318, emptyList()).isEmpty())
    }
}
