# MO-COM-04 — Integración Android

Estado: implementación local ampliada; ejecución JVM/Android y aceptación pendientes.
Revisores: pendiente. Fecha: 2026-11-05.
Detalle: ampliación solicitada expresamente por el usuario; backend sin cambios.

## Contrato

POST `/api/v1/commitments/{id}/request-complete`. Fuente fijada: [spec backend](https://github.com/equipopatogg/backend/blob/451c8ae1bfae3df64cf95f5a26ace7b1eacb3b1e/specs/COM-04-request-complete/spec.md) y OpenAPI del mismo commit.

## Criterios móviles

- C1: acceso desde navegación conectada con sesión real y contexto de grupo/reto correspondiente.
- C2: representar respuesta tipada, carga, vacío, error y paginación cuando exista en el contrato.
- C3: validar entradas y conservar autorización HTTPS con SessionManager; rechazar respuestas de otro recurso.
- C4: acciones confirmadas por servidor; errores 401/403/404/409/422/429/503 no producen éxito ficticio. Escrituras sin reintento automático.
- C5: cambio de cuenta/grupo cancela o invalida respuestas previas; no filtrar datos entre cuentas.
- C6: pruebas unitarias, integración HTTPS con fixtures, seguridad y recorrido Android con evidencia individual.

## Reglas de proveedor de referencia

# COM-04 — POST /api/v1/commitments/{id}/request-complete

**Fecha:** 2026-11-04
**Estado:** Implementado y verificado localmente (pruebas en verde).
**Contrato:** `api/openapi.yaml` (v0.6.0)

## 1. Objetivo

El deudor avisa que ya cumplió.

## 2. Requisitos funcionales

- RF1: Solo deudor (403).
- RF2: pending → completion_requested; otro estado → 409.
- RF3: Avisa al acreedor.

## 3. Errores

| HTTP | `code` | Causa |
|---|---|---|
| 401 | `UNAUTHORIZED` | Sin token |
| 403 | `NOT_COMMITMENT_PARTY` | No es deudor |
| 409 | `INVALID_COMMITMENT_STATE` | No pendiente |

## 4. Implementación

`Pactogg.Api/Modules/Commitments/CommitmentService.cs (RequestCompleteAsync)`

## 5. Criterios de aceptación

| Escenario | Evidencia |
|---|---|
| Acreedor intenta → 403; deudor → 200 | IntegrationTest/JuryFlowTests.cs |

401 sin token: `SecurityTest/AuthorizationTests.cs`.


## Revisión local 2026-11-06

Pantalla y consumidor presentes. Ver [evidencia y límites](../../docs/pruebas/ampliacion-local-2026-11-06.md). No se acredita C6 ni aceptación completa por controles estáticos.
