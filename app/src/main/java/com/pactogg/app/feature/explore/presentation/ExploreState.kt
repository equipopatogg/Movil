package com.pactogg.app.feature.explore.presentation

enum class IdeaCategory { ALL, SKILL, COOPERATIVE, CONSISTENCY }
enum class ExploreStatus { READY, LOADING, ERROR }

data class IdeaPresentation(
    val id: String,
    val game: String,
    val title: String,
    val description: String,
    val category: IdeaCategory,
)

data class ExploreState(
    val ideas: List<IdeaPresentation> = emptyList(),
    val query: String = "",
    val category: IdeaCategory = IdeaCategory.ALL,
    val status: ExploreStatus = ExploreStatus.READY,
)

sealed interface ExploreEvent {
    data class Query(val text: String) : ExploreEvent
    data class Category(val category: IdeaCategory) : ExploreEvent
    data class Details(val idea: IdeaPresentation) : ExploreEvent
    data class UseIdea(val idea: IdeaPresentation) : ExploreEvent
    data object Clear : ExploreEvent
}

/** Local presentation filter, not a query or contract for a backend. */
fun ExploreState.visibleIdeas(): List<IdeaPresentation> = ideas.filter { idea ->
    val matchesCategory = category == IdeaCategory.ALL || idea.category == category
    val term = query.trim()
    matchesCategory && (term.isEmpty() || listOf(idea.game, idea.title, idea.description)
        .any { it.contains(term, ignoreCase = true) })
}
