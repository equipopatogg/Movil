# MO-VOT-04 — Integración Android

Estado: implementación local ampliada; ejecución JVM/Android y aceptación pendientes.
Revisores: pendiente. Fecha: 2026-11-05.
Detalle: ampliación solicitada expresamente por el usuario; backend sin cambios.

## Contrato

POST `/api/v1/contracts/{id}/votes/resolve`. Fuente fijada: [spec backend](https://github.com/equipopatogg/backend/blob/451c8ae1bfae3df64cf95f5a26ace7b1eacb3b1e/specs/VOT-04-resolve-voting/spec.md) y OpenAPI del mismo commit.

## Criterios móviles

- C1: acceso desde navegación conectada con sesión real y contexto de grupo/reto correspondiente.
- C2: representar respuesta tipada, carga, vacío, error y paginación cuando exista en el contrato.
- C3: validar entradas y conservar autorización HTTPS con SessionManager; rechazar respuestas de otro recurso.
- C4: acciones confirmadas por servidor; errores 401/403/404/409/422/429/503 no producen éxito ficticio. Escrituras sin reintento automático.
- C5: cambio de cuenta/grupo cancela o invalida respuestas previas; no filtrar datos entre cuentas.
- C6: pruebas unitarias, integración HTTPS con fixtures, seguridad y recorrido Android con evidencia individual.

## Reglas de proveedor de referencia

# VOT-04 — POST /api/v1/contracts/{id}/votes/resolve

**Fecha:** 2026-11-04
**Estado:** Implementado y verificado localmente (pruebas en verde).
**Contrato:** `api/openapi.yaml` (v0.6.0)

## 1. Objetivo

El administrador resuelve un empate, una votación vencida o un reto sin jurados.

## 2. Requisitos funcionales

- RF1: Solo rol admin del grupo (403 NOT_GROUP_ADMIN).
- RF2: Solo en voting y si: no hay jurados, todos votaron sin consenso o pasaron las 48 h; si no → 409 VOTING_NOT_FINISHED.
- RF3: outcome fulfilled|cheating; misma transacción de cierre que VOT-02 con resolved_by=admin.

## 3. Errores

| HTTP | `code` | Causa |
|---|---|---|
| 401 | `UNAUTHORIZED` | Sin token |
| 403 | `NOT_GROUP_ADMIN` | No es admin |
| 409 | `VOTING_NOT_FINISHED / INVALID_CONTRACT_STATE` | Votación en curso o ya cerrada |
| 422 | `VALIDATION_ERROR` | outcome inválido |

## 4. Implementación

`Pactogg.Api/Modules/Votes/VoteService.cs (AdminResolveAsync)`

## 5. Criterios de aceptación

| Escenario | Evidencia |
|---|---|
| Antes de tiempo → 409 | IntegrationTest/JuryFlowTests.cs |
| Miembro no admin → 403; admin tras empate → 200 | IntegrationTest/JuryFlowTests.cs |

401 sin token: `SecurityTest/AuthorizationTests.cs`.


## Revisión local 2026-11-06

Pantalla y consumidor presentes. Ver [evidencia y límites](../../docs/pruebas/ampliacion-local-2026-11-06.md). No se acredita C6 ni aceptación completa por controles estáticos.


## Actualizacion de contrato recibida - 2026-11-06

La lista de 54 rutas del usuario exige Idempotency-Key en esta operacion. El consumidor genera un UUID por invocacion antes de SessionManager y lo envia como cabecera obligatoria. No activa reintentos automaticos ni persiste claves en logs. La prueba HTTPS existente verifica presencia y formato UUID junto con autorizacion, ruta y numero de peticiones. Implementacion local; ejecucion del candidato pendiente. La semantica de reenvio manual despues de respuesta perdida y la vigencia del registro del servidor requieren el contrato actualizado.
