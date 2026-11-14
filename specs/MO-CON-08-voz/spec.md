# MO-CON-08 — Crear reto por voz

Fecha: 2026-11-14. Estado: **APROBADA POR EL PO E IMPLEMENTADA EN LOCAL**. Backend: CON-08 `POST /groups/{gid}/contracts/parse` y CON-01 con `source=voice` + `original_phrase` (API 0.12.0, spec backend 005). Maqueta: `voz` y la opción "Crear por voz" de `crear`.

## Requisitos
- RF1: Dictado con el reconocedor del sistema (`RecognizerIntent.ACTION_RECOGNIZE_SPEECH`, idioma es-MX). No se graba ni se manda audio; solo el texto. Si el teléfono no tiene reconocedor, se puede escribir la frase.
- RF2: La frase (1–300) va a CON-08. La respuesta llena el borrador manual: juego, condición, recompensa y rival (`target_id`). `missing_fields` se muestran como "Completa:" y `rival_unmatched` abre la lista de miembros para elegir.
- RF3: Al lanzar, CON-01 envía `source: "voice"` y `original_phrase`.
- RF4: `503 AI_UNAVAILABLE` → aviso "La IA no está disponible; escríbelo a mano" y botón al formulario manual. `422 AI_PARSE_FAILED` → pedir decirlo de otra forma. `429` → respetar la espera.

## Criterios
| ID | Resultado |
|---|---|
| MO-VOZ-AC1 | Con la IA conectada, una frase llena el formulario y el reto se crea con `source=voice`. |
| MO-VOZ-AC2 | Sin IA, la app ofrece escribir a mano sin perder la frase. |
