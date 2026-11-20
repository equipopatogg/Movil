# PL-13 — Correo verificado obligatorio en la app

Fecha: 2026-11-18. Estado: **APROBADA POR EL PO E IMPLEMENTADA. Compila con Detekt y Lint sin advertencias; pruebas JVM (unitarias, integración y seguridad) en verde el 2026-11-19 (344 casos); la verificación obligatoria se enciende con `Auth__RequireVerifiedEmail=true` en SSM**. Backend: AUTH-05 (`verify-email`, `verify-email/resend`), `Auth__RequireVerifiedEmail=true` (CLARIFICACION #5 y #223), página web de verificación del equipo (F2-D75).

## Requisitos
- RF1: Si el perfil dice `email_verified=false` o el servidor responde `EMAIL_NOT_VERIFIED`, Grupos y Perfil muestran la tarjeta "Verifica tu correo".
- RF2: "Reenviar correo" llama a `POST auth/verify-email/resend` (202). Un 409 significa que ya estaba verificado y recarga el perfil; 429 muestra "Espera un momento".
- RF3: "Ya lo verifiqué" recarga el perfil; si ya está verificado, la tarjeta desaparece.
- RF4: El enlace del correo abre la página web del equipo, no la app.

## Criterios
| ID | Resultado |
|---|---|
| PL-13-AC1 | Una cuenta nueva sin verificar ve la tarjeta y no puede crear ni unirse a grupos. |
| PL-13-AC2 | Después de abrir el enlace y tocar "Ya lo verifiqué", la tarjeta desaparece y puede crear un grupo. |
