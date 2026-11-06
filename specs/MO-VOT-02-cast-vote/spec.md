# MO-VOT-02 — Integración Android

Estado: implementación local ampliada; ejecución JVM/Android y aceptación pendientes.
Revisores: pendiente. Fecha: 2026-11-05.
Detalle: ampliación solicitada expresamente por el usuario; backend sin cambios.

## Contrato

PUT `/api/v1/contracts/{id}/votes`. Fuente fijada: [spec backend](https://github.com/equipopatogg/backend/blob/451c8ae1bfae3df64cf95f5a26ace7b1eacb3b1e/specs/VOT-02-cast-vote/spec.md) y OpenAPI del mismo commit.

## Criterios móviles

- C1: acceso desde navegación conectada con sesión real y contexto de grupo/reto correspondiente.
- C2: representar respuesta tipada, carga, vacío, error y paginación cuando exista en el contrato.
- C3: validar entradas y conservar autorización HTTPS con SessionManager; rechazar respuestas de otro recurso.
- C4: acciones confirmadas por servidor; errores 401/403/404/409/422/429/503 no producen éxito ficticio. Escrituras sin reintento automático.
- C5: cambio de cuenta/grupo cancela o invalida respuestas previas; no filtrar datos entre cuentas.
- C6: pruebas unitarias, integración HTTPS con fixtures, seguridad y recorrido Android con evidencia individual.

## Reglas de proveedor de referencia

# VOT-02 — PUT /api/v1/contracts/{id}/votes

**Fecha:** 2026-11-04
**Estado:** Implementado y verificado localmente (pruebas en verde).
**Contrato:** `api/openapi.yaml` (v0.6.0)

## 1. Objetivo

ID 88 · Votación legítimo o trampa; ID 99/100 · entrega o pérdida de fianza.

## 2. Requisitos funcionales

- RF1: Jurados: al subir la primera evidencia se eligen al azar tantos como `settings.jurors` (3 por defecto) entre los miembros, nunca el retador ni quien cumple el reto; 48 h para votar; se les avisa.
- RF2: Solo jurados (403 NOT_JUROR), solo en voting (409), un voto por jurado e inmutable (409 ALREADY_VOTED; índice único contract_id+juror_id).
- RF3: Comentario opcional ≤140, moderado (falla cerrada).
- RF4: Consenso: un lado gana cuando sus votos / jurados asignados ≥ consenso del grupo (0.66 por defecto). Con 3 jurados bastan 2 votos iguales.
- RF5: Cierre en una transacción: fulfilled → bond_release (la fianza regresa a quien aceptó), compromiso retador→ejecutor por la recompensa, completed+1 y racha+1; cheating → bond_forfeit (la fianza pasa al retador), cheated+1 y racha a 0.
- RF6: Si todos votan sin consenso → needs_admin=true y aviso a los administradores.

## 3. Errores

| HTTP | `code` | Causa |
|---|---|---|
| 401 | `UNAUTHORIZED` | Sin token |
| 403 | `NOT_JUROR / NOT_GROUP_MEMBER` | No es jurado |
| 409 | `ALREADY_VOTED / INVALID_CONTRACT_STATE` | Ya votó o no está en voting |
| 422 | `VALIDATION_ERROR / CONTENT_REJECTED` | vote inválido o comentario moderado |
| 503 | `MODERATION_UNAVAILABLE` | Moderación caída |

## 4. Implementación

`Pactogg.Api/Modules/Votes/VoteService.cs, MongoContractRepository (TryRegisterVoteAsync, ResolveAsync)`

## 5. Criterios de aceptación

| Escenario | Evidencia |
|---|---|
| 2 de 3 legit → fulfilled, saldo 100, compromiso creado | IntegrationTest/JuryFlowTests.cs |
| 1 de 1 cheat → cheating, retador 125, ejecutor 75 | IntegrationTest/JuryFlowTests.cs |
| Votar dos veces → 409; no jurado → 403 | IntegrationTest/JuryFlowTests.cs |

401 sin token: `SecurityTest/AuthorizationTests.cs`.


## Revisión local 2026-11-06

Pantalla y consumidor presentes. Ver [evidencia y límites](../../docs/pruebas/ampliacion-local-2026-11-06.md). No se acredita C6 ni aceptación completa por controles estáticos.
