# Plan

- Dependencia `firebase-messaging` 25.0.1 (la misma de la rama de origen). Sin el plugin de Google Services: `app/build.gradle.kts` genera `google_app_id`, `gcm_defaultSenderId`, `project_id` y `google_api_key` como `resValue`.
- `core/network/DeviceApi.kt` (lo hereda `PactoApi`) y `core/push/PushDtos.kt`.
- `core/push/PushRegistration.kt`: serializa registro, rotación y baja con un `Mutex`; el token solo en memoria.
- `core/push/PushRuntime.kt`: inicia Firebase si hay configuración, escucha la sesión y se engancha a `SessionManager.beforeLogout`.
- `core/push/PushNotifications.kt`, `PactoMessagingService.kt`, `PushEvents.kt` (abrir Notificaciones al tocar).
- `core/push/PushCard.kt`: estado y permiso en el perfil. `feature/connected/presentation/PushEffects.kt`: pide el permiso una vez y navega al tocar.
