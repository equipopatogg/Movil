package com.pactogg.app.feature.assist.data

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.FirebaseAIException
import com.google.firebase.ai.type.GenerativeBackend
import com.pactogg.app.BuildConfig
import com.pactogg.app.feature.assist.domain.AiIdea
import com.pactogg.app.feature.assist.domain.AssistUnavailable
import com.pactogg.app.feature.assist.domain.ChallengeIdeaGenerator
import com.pactogg.app.feature.assist.domain.IdeaPrompt
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.withTimeoutOrNull

/**
 * PL-14 (IA generativa): Gemini desde la app con Firebase AI Logic (Gemini Developer API, plan sin costo).
 * No hay llave en el código: Firebase usa la configuración pública de `google-services.json`.
 * Sin esa configuración (por ejemplo en CI) la función queda como "no disponible".
 */
class FirebaseIdeaGenerator @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : ChallengeIdeaGenerator {
    override suspend fun ideas(game: String): List<AiIdea> {
        if (FirebaseApp.getApps(context).isEmpty()) throw AssistUnavailable("Firebase no configurado")
        val text = try {
            withTimeoutOrNull(TimeoutMillis) {
                Firebase.ai(backend = GenerativeBackend.googleAI())
                    .generativeModel(BuildConfig.GEMINI_MODEL)
                    .generateContent(IdeaPrompt.build(game))
                    .text
            }
        } catch (error: FirebaseAIException) {
            throw AssistUnavailable("Gemini respondió con error", error)
        } catch (error: IOException) {
            throw AssistUnavailable("Sin conexión con Gemini", error)
        } catch (error: IllegalStateException) {
            throw AssistUnavailable("Firebase AI Logic no está listo", error)
        }
        return IdeaPrompt.parse(text.orEmpty()).ifEmpty { throw AssistUnavailable("Sin ideas válidas") }
    }

    private companion object {
        const val TimeoutMillis = 20_000L
    }
}
