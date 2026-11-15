# Plan

- Dependencias: `androidx.credentials` 1.5.0, `credentials-play-services-auth` 1.5.0 y `googleid` 1.1.1 (las mismas versiones de la rama de origen).
- `core/network/AccountApi.kt` (lo hereda `PactoApi`) y `core/session/GoogleSession.kt` (`SessionManager.google`, reutiliza `authenticate`).
- `feature/connected/presentation/GoogleAccess.kt`: botón, formulario de apodo y acciones del ViewModel como extensiones.
- `ConnectedApp.SignedOut`: el botón entra por el espacio `tools` de `AccessScreen`, que no se modifica.
