# MO-COM-01 — Integración Android

Estado: implementación local ampliada; ejecución JVM/Android y aceptación pendientes.
Revisores: pendiente. Fecha: 2026-11-05.
Detalle: ampliación solicitada expresamente por el usuario; backend sin cambios.

## Contrato

GET `/api/v1/groups/{gid}/commitments`. Fuente fijada: [spec backend](https://github.com/equipopatogg/backend/blob/451c8ae1bfae3df64cf95f5a26ace7b1eacb3b1e/specs/COM-01-list-commitments/spec.md) y OpenAPI del mismo commit.

## Criterios móviles

- C1: acceso desde navegación conectada con sesión real y contexto de grupo/reto correspondiente.
- C2: representar respuesta tipada, carga, vacío, error y paginación cuando exista en el contrato.
- C3: validar entradas y conservar autorización HTTPS con SessionManager; rechazar respuestas de otro recurso.
- C4: acciones confirmadas por servidor; errores 401/403/404/409/422/429/503 no producen éxito ficticio. Escrituras sin reintento automático.
- C5: cambio de cuenta/grupo cancela o invalida respuestas previas; no filtrar datos entre cuentas.
- C6: pruebas unitarias, integración HTTPS con fixtures, seguridad y recorrido Android con evidencia individual.

## Reglas de proveedor de referencia

# COM-01 — GET /api/v1/groups/{gid}/commitments

**Fecha:** 2026-11-04
**Estado:** Implementado y verificado localmente (pruebas en verde).
**Contrato:** `api/openapi.yaml` (v0.6.0)

## 1. Objetivo

ID 124/125 · Compromisos por cumplir y a mi favor.

## 2. Requisitos funcionales

- RF1: role=debo (soy deudor, por defecto) o me-deben (soy acreedor); otro → 422.
- RF2: status pending|completion_requested|completed; cursor.
- RF3: Cada compromiso con deudor y acreedor (apodo), descripción (la recompensa) y juego. Se crean solos al cumplirse un reto (uno por reto, índice único).

## 3. Errores

| HTTP | `code` | Causa |
|---|---|---|
| 401 | `UNAUTHORIZED` | Sin token |
| 403 | `NOT_GROUP_MEMBER` | No pertenece |
| 422 | `VALIDATION_ERROR` | role/status |

## 4. Implementación

`Pactogg.Api/Modules/Commitments/CommitmentService.cs`

## 5. Criterios de aceptación

| Escenario | Evidencia |
|---|---|
| Retador ve 1 en debo; ejecutor 1 en me-deben | IntegrationTest/JuryFlowTests.cs |

401 sin token: `SecurityTest/AuthorizationTests.cs`.


## Revisión local 2026-11-06

Pantalla y consumidor presentes. Ver [evidencia y límites](../../docs/pruebas/ampliacion-local-2026-11-06.md). No se acredita C6 ni aceptación completa por controles estáticos.
