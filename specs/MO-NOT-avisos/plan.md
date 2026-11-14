# Plan — MO-NOT-avisos

- Datos: `feature/play/data/PlayApi.kt` (Retrofit), `PlayDtos.kt` (snake_case, mapeo explícito), `ApiPlayRepository.kt`; dominio en `feature/play/domain/PlayModels.kt` sin Android ni Retrofit.
- Estado: `PlayState` dentro de `ConnectedState`; acciones como extensiones de `ConnectedViewModel` en `feature/play/presentation/PlayActions.kt`, reutilizando `perform {}` (un solo indicador de ocupado y manejo de errores común).
- UI: pantallas Compose en `feature/play/presentation/` con los componentes ya usados (`AppPage`, `PactCard`, `Action`, `Field`, `Choices`); rutas nuevas en `ConnectedRoute`.
- Cabecera `Idempotency-Key` para las operaciones que el backend la exige.
- Pruebas JVM con MockWebServer en `src/test/.../play/`.
