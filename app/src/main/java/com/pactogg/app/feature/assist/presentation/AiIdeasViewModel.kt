package com.pactogg.app.feature.assist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pactogg.app.feature.assist.domain.AiIdea
import com.pactogg.app.feature.assist.domain.AssistUnavailable
import com.pactogg.app.feature.assist.domain.ChallengeIdeaGenerator
import com.pactogg.app.feature.assist.domain.IdeaPrompt
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AiIdeasState(
    val game: String = "",
    val ideas: List<AiIdea> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
    val used: Int = 0,
)

/** "Ideas con IA" de Explorar (PL-14). Máximo [MaxPerSession] consultas por sesión para cuidar la cuota gratis. */
@HiltViewModel
class AiIdeasViewModel @Inject constructor(private val generator: ChallengeIdeaGenerator) : ViewModel() {
    private val mutable = MutableStateFlow(AiIdeasState())
    val state = mutable.asStateFlow()

    fun game(value: String) = mutable.update { it.copy(game = value.take(IdeaPrompt.MaxGame), error = null) }

    fun generate() {
        val current = mutable.value
        if (current.loading) return
        val game = IdeaPrompt.cleanGame(current.game)
        when {
            game == null -> mutable.update { it.copy(error = InvalidGame) }
            current.used >= MaxPerSession -> mutable.update { it.copy(error = TooMany) }
            else -> {
                mutable.update { it.copy(loading = true, error = null, ideas = emptyList(), used = it.used + 1) }
                viewModelScope.launch {
                    val result = try {
                        Result.success(generator.ideas(game))
                    } catch (error: AssistUnavailable) {
                        Result.failure(error)
                    }
                    mutable.update {
                        it.copy(loading = false, ideas = result.getOrDefault(emptyList()),
                            error = if (result.isFailure) Unavailable else null)
                    }
                }
            }
        }
    }

    companion object {
        const val MaxPerSession = 5
        const val InvalidGame = "Escribe solo el nombre del juego (letras y números)."
        const val TooMany = "Ya pediste varias ideas. Usa una o vuelve más tarde."
        const val Unavailable = "La IA no está disponible ahora. Usa las ideas de abajo."
    }
}
