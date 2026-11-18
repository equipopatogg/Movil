# Plan

- `feature/auth/data/PasswordDtos.kt` y dos métodos en `core/network/AccountApi.kt`.
- `core/session/PasswordRecovery.kt`: `SessionManager.forgotPassword` y `resetPassword` con validación antes de la red.
- `feature/connected/presentation/PasswordRecovery.kt`: acciones del ViewModel y tarjetas en el acceso.
- `MainActivity` lee el enlace en `onCreate` y `onNewIntent`; el manifiesto declara solo `pactogg://auth/reset-password`.
