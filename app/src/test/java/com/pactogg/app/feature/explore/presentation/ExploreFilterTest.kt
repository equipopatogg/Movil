package com.pactogg.app.feature.explore.presentation

import org.junit.experimental.categories.Category
import com.pactogg.app.testing.UnitChecks
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@Category(UnitChecks::class)
class ExploreFilterTest {
    private val ideas = listOf(
        IdeaPresentation("one", "Game A", "Precision", "Train control", IdeaCategory.SKILL),
        IdeaPresentation("two", "Game B", "Team", "Train communication", IdeaCategory.COOPERATIVE),
    )

    @Test fun combinesCategoryAndTrimmedCaseInsensitiveSearch() {
        val state = ExploreState(ideas = ideas, query = "  TRAIN  ", category = IdeaCategory.SKILL)
        assertEquals(listOf("one"), state.visibleIdeas().map { it.id })
        assertTrue(state.copy(query = "communication").visibleIdeas().isEmpty())
    }

    @Test fun emptyQueryPreservesOrderAndSourceAndMissingQueryReturnsEmpty() {
        assertEquals(ideas, ExploreState(ideas).visibleIdeas())
        assertTrue(ExploreState(ideas, query = "missing").visibleIdeas().isEmpty())
        assertEquals(2, ideas.size)
        assertTrue(ExploreState().visibleIdeas().isEmpty())
    }
}
