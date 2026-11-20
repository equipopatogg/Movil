# PL-01 — Jurado: votar retos de otros

Fecha: 2026-11-14. Estado: **APROBADA POR EL PO E IMPLEMENTADA. Compila con Detekt y Lint sin advertencias; pruebas JVM (unitarias, integración y seguridad) en verde el 2026-11-19 (344 casos); probada en el teléfono (votación y explicación de por qué no puedes votar)**. Backend: VOT-01..05 (API 0.12.x, `docs/API-movil.md` §VOT). Maqueta: pantalla `jurado` y filtro "Jurado" del inicio (`MaquetaFigmaMake/src/App.tsx`, función `Jury`).

## 1. Objetivo
Que el miembro asignado como jurado vea los retos que le toca revisar, consulte la evidencia, vote **Legítimo** o **Hizo trampa** (con comentario opcional) y vea el avance y el resultado final. Sin esto ningún reto llega a `fulfilled` o `cheating`.

## 2. Requisitos funcionales
- RF1: `GET /api/v1/me/votes/pending` lista los retos por votar (contrato + `voting_ends_at`). Lista vacía es válida.
- RF2: Al abrir uno, cargar el detalle del reto (CON-03), sus evidencias (EVI-03) y `GET /contracts/{id}/votes/summary` (VOT-03).
- RF3: `PUT /contracts/{id}/votes` con `{vote: legit|cheat, comment?≤140}` y cabecera `Idempotency-Key` (UUID por intento del usuario; se reutiliza si se reintenta el mismo voto). Confirmación previa en diálogo.
- RF4: Errores con mensaje propio: `NOT_JUROR` (403), `ALREADY_VOTED`, `VOTING_CLOSED` (409).
- RF5: Si el reto ya cerró, `GET /contracts/{id}/result` (VOT-05) muestra el resultado y los votos revelados.
- RF6: Un administrador neutral ve "Decidir" cuando `admin_can_resolve` es verdadero: `POST /contracts/{id}/votes/resolve` con `outcome` e `Idempotency-Key`.
- RF7 (2026-11-17, prueba del PO): si no aparece el formulario de voto, la pantalla dice por qué: eres parte del reto (decide el jurado), no te tocó ser jurado, no hay jurado en el grupo o la votación aún no empieza. "Actualizar" recarga la votación sin salir. En el detalle del reto el botón dice "Ver votación y votar".
- RF8 (2026-11-17): la foto de una evidencia se reintenta sola dos veces si la primera descarga falla, y después muestra "Toca para reintentar" en la misma tarjeta.

## 3. No funcionales
Mismas reglas del repo: HTTPS, sin registrar tokens ni cuerpos, mensajes por `code`, botones deshabilitados mientras hay una petición, nada inventado si el servidor no lo manda.

## 4. Criterios de aceptación
| ID | Resultado observable |
|---|---|
| PL-01-AC1 | El inicio muestra cuántos retos esperan mi voto y abre la lista. |
| PL-01-AC2 | Votar envía `Idempotency-Key` y, al volver, el reto ya no aparece como pendiente. |
| PL-01-AC3 | Un segundo voto o fuera de plazo muestra el mensaje del código 409 sin cerrar la pantalla. |
| PL-01-AC4 | Un reto cerrado muestra resultado, conteo y movimiento de fianza. |
| PL-01-AC5 | Un admin neutral puede decidir cuando el servidor lo permite; los demás no ven el botón. |
| PL-01-AC6 | Quien creó o aceptó el reto ve "Eres parte de este reto…" y no el formulario (`VoteBlockedReasonTest`). |
