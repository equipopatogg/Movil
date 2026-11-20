# PL-08 — Continuar con Google

Fecha: 2026-11-14. Estado: **APROBADA POR EL PO E IMPLEMENTADA. Compila con Detekt y Lint sin advertencias; pruebas JVM (unitarias, integración y seguridad) en verde el 2026-11-19 (344 casos); en el teléfono falta registrar la huella SHA-1 en Google Cloud**. Backend: AUTH-02 `POST /api/v1/auth/google` (ya desplegado). Origen: el botón existía en la rama sin fusionar `codex/push-google-notificaciones-20261109`; aquí se trae solo el acceso con Google, adaptado a la app actual.

## Requisitos
- RF1: En iniciar sesión y en crear cuenta hay un botón "Continuar con Google". Usa Credential Manager con selección explícita de cuenta.
- RF2: La app solo manda el ID token a `auth/google`; la sesión de PactoGG la crea el backend y se guarda cifrada igual que la de correo. No se usa Firebase Auth ni se registra el token.
- RF3: `200` cuenta existente y `201` cuenta nueva inician sesión. `422 NICKNAME_REQUIRED` muestra el formulario de apodo y aviso de privacidad y reintenta con `nickname`, `accept_privacy` y `privacy_version`. El token queda solo en memoria y se descarta al cancelar o al entrar.
- RF4: Cancelar el selector de Google no envía nada. Sin cuenta de Google en el teléfono o sin conexión se muestra un mensaje y se puede seguir con correo.
- RF5: `INVALID_GOOGLE_TOKEN` y `GOOGLE_AUTH_UNAVAILABLE` tienen mensaje propio en español.
- RF6: El Client ID web es público y se configura con `pactogg.googleWebClientId` (por defecto, el del proyecto `pactogg`).

## Dependencia externa
Google solo entrega el token si la huella SHA-1 de la firma del APK está registrada en el cliente OAuth Android del proyecto. Cada firma (debug de cada computadora, release, Google Play) necesita su huella.

## Criterios
| ID | Resultado |
|---|---|
| PL-08-AC1 | Una cuenta existente entra con Google y llega al inicio. |
| PL-08-AC2 | Una cuenta nueva pide apodo y aviso de privacidad y queda creada. |
| PL-08-AC3 | Cancelar o fallar en Google no crea sesión ni manda solicitudes repetidas. |
| PL-08-AC4 | Token mal formado o apodo sin consentimiento no llegan a la red. |
