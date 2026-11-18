# PL-11 — Recuperar contraseña

Fecha: 2026-11-16. Estado: **APROBADA POR EL PO; ESCRITA, FALTA COMPILAR Y PROBAR EN EL CELULAR**. Backend: AUTH-06 `POST /api/v1/auth/forgot-password` (202) y AUTH-07 `POST /api/v1/auth/reset-password` (ya desplegados). Sprint planning: ID 16.

## Requisitos
- RF1: En iniciar sesión hay "¿Olvidaste tu contraseña?". Pide el correo y llama a `forgot-password`. El aviso es el mismo exista o no la cuenta, para no revelar qué correos están registrados.
- RF2: El enlace del correo `pactogg://auth/reset-password?token=…` abre la app en "Contraseña nueva". Solo se acepta ese esquema, host y ruta; el token debe ser base64url de 16 a 128 caracteres y vive solo en memoria.
- RF3: La contraseña nueva tiene de 12 a 128 caracteres y se escribe dos veces. `reset-password` cierra todas las sesiones de la cuenta y la persona entra con la nueva.
- RF4: `INVALID_OR_EXPIRED_TOKEN` muestra "El enlace no es válido o ya caducó. Pide uno nuevo."
- RF5: Si el equipo publica una página web de restablecer (igual que la de verificar correo), el backend cambia `Email__ResetPasswordLink` con `scripts/configure-web.sh` y la app solo usa RF1.

## Criterios
| ID | Resultado |
|---|---|
| PL-11-AC1 | Pedir el enlace manda solo el correo y no crea sesión. |
| PL-11-AC2 | Abrir el enlace y guardar la contraseña nueva permite entrar con ella. |
| PL-11-AC3 | Un enlace usado o caducado muestra su mensaje. |
| PL-11-AC4 | Correo, token o contraseña inválidos no llegan a la red. |
