# Plan

- `feature/assist/domain/ScoreParser.kt` y `ChallengeIdeas.kt` (`IdeaPrompt`, `ChallengeIdeaGenerator`): lógica pura, probada sin Android.
- `feature/assist/data/MlKitScoreReader.kt`: ML Kit `text-recognition` 16.0.1 (modelo incluido, sin descarga).
- `feature/assist/data/FirebaseIdeaGenerator.kt`: `firebase-ai` 17.11.0, `Firebase.ai(GenerativeBackend.googleAI())`, tiempo máximo 20 s.
- `feature/assist/presentation/AiIdeasViewModel.kt` (Hilt) y tarjeta en `ExploreScreens.kt`; lectura del marcador en `MatchScreen.kt`.
- `AssistModule` liga la interfaz con Firebase; las pruebas usan un generador falso.
