# MO-VOT — Jurado: votar retos de otros

Fecha: 2026-11-14. Estado: **APROBADA POR EL PO E IMPLEMENTADA EN LOCAL / aceptación en dispositivo pendiente**. Backend: VOT-01..05 (API 0.12.x, `docs/API-movil.md` §VOT). Maqueta: pantalla `jurado` y filtro "Jurado" del inicio (`MaquetaFigmaMake/src/App.tsx`, función `Jury`).

## 1. Objetivo
Que el miembro asignado como jurado vea los retos que le toca revisar, consulte la evidencia, vote **Legítimo** o **Hizo trampa** (con comentario opcional) y vea el avance y el resultado final. Sin esto ningún reto llega a `fulfilled` o `cheating`.

## 2. Requisitos funcionales
- RF1: `GET /api/v1/me/votes/pending` lista los retos por votar (contrato + `voting_ends_at`). Lista vacía es válida.
- RF2: Al abrir uno, cargar el detalle del reto (CON-03), sus evidencias (EVI-03) y `GET /contracts/{id}/votes/summary` (VOT-03).
- RF3: `PUT /contracts/{id}/votes` con `{vote: legit|cheat, comment?≤140}` y cabecera `Idempotency-Key` (UUID por intento del usuario; se reutiliza si se reintenta el mismo voto). Confirmación previa en diálogo.
- RF4: Errores con mensaje propio: `NOT_JUROR` (403), `ALREADY_VOTED`, `VOTING_CLOSED` (409).
- RF5: Si el reto ya cerró, `GET /contracts/{id}/result` (VOT-05) muestra el resultado y los votos revelados.
- RF6: Un administrador neutral ve "Decidir" cuando `admin_can_resolve` es verdadero: `POST /contracts/{id}/votes/resolve` con `outcome` e `Idempotency-Key`.

## 3. No funcionales
Mismas reglas del repo: HTTPS, sin registrar tokens ni cuerpos, mensajes por `code`, botones deshabilitados mientras hay una petición, nada inventado si el servidor no lo manda.

## 4. Criterios de aceptación
| ID | Resultado observable |
|---|---|
| MO-VOT-AC1 | El inicio muestra cuántos retos esperan mi voto y abre la lista. |
| MO-VOT-AC2 | Votar envía `Idempotency-Key` y, al volver, el reto ya no aparece como pendiente. |
| MO-VOT-AC3 | Un segundo voto o fuera de plazo muestra el mensaje del código 409 sin cerrar la pantalla. |
| MO-VOT-AC4 | Un reto cerrado muestra resultado, conteo y movimiento de fianza. |
| MO-VOT-AC5 | Un admin neutral puede decidir cuando el servidor lo permite; los demás no ven el botón. |
