# Plan

- `feature/explore/presentation/DailyIdeas.kt`: lectura del JSON (`parseIdeas`) y selección diaria (`dailyIdeas`), puras y probadas en JVM. Reutiliza `ExploreState.visibleIdeas()`.
- `feature/play/presentation/ExploreScreens.kt`: pantalla con MakeKit; estado local (búsqueda, categoría, descartadas) con `rememberSaveable`; catálogo con `produceState` en `Dispatchers.IO`.
- `PlayActions.useIdea`: copia la idea al borrador y navega a Crear.
- `ConnectedNavigation`: Explorar navega a `ConnectedRoute.EXPLORE`; se retiran el diálogo y los textos `section_upcoming` y `understood`.
- Prueba de dispositivo renombrada a `navigationKeepsFiveMakeEntriesAndExploreOpensIdeas`. **Requiere actualizar el nombre en `EXPECTED_DEVICE` de `.github/scripts/android-ci-checks.py`.**
