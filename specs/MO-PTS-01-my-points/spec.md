# MO-PTS-01 — Integración Android

Estado: implementación local ampliada; ejecución JVM/Android y aceptación pendientes.
Revisores: pendiente. Fecha: 2026-11-05.
Detalle: ampliación solicitada expresamente por el usuario; backend sin cambios.

## Contrato

GET `/api/v1/groups/{gid}/points/me`. Fuente fijada: [spec backend](https://github.com/equipopatogg/backend/blob/451c8ae1bfae3df64cf95f5a26ace7b1eacb3b1e/specs/PTS-01-my-points/spec.md) y OpenAPI del mismo commit.

## Criterios móviles

- C1: acceso desde navegación conectada con sesión real y contexto de grupo/reto correspondiente.
- C2: representar respuesta tipada, carga, vacío, error y paginación cuando exista en el contrato.
- C3: validar entradas y conservar autorización HTTPS con SessionManager; rechazar respuestas de otro recurso.
- C4: acciones confirmadas por servidor; errores 401/403/404/409/422/429/503 no producen éxito ficticio. Escrituras sin reintento automático.
- C5: cambio de cuenta/grupo cancela o invalida respuestas previas; no filtrar datos entre cuentas.
- C6: pruebas unitarias, integración HTTPS con fixtures, seguridad y recorrido Android con evidencia individual.

## Reglas de proveedor de referencia

# PTS-01 — GET /api/v1/groups/{gid}/points/me

**Fecha:** 2026-11-04
**Estado:** Implementado y verificado localmente (pruebas en verde).
**Contrato:** `api/openapi.yaml` (v0.6.0)

## 1. Objetivo

ID 159 · Puntos sin valor monetario: saldo y movimientos propios.

## 2. Requisitos funcionales

- RF1: Miembros (403).
- RF2: balance actual + movimientos del libro (points_ledger) del más reciente al más antiguo; filtro type; cursor.
- RF3: No existe ningún endpoint de compra; los movimientos solo los crea el backend en transacciones.

## 3. Errores

| HTTP | `code` | Causa |
|---|---|---|
| 401 | `UNAUTHORIZED` | Sin token |
| 403 | `NOT_GROUP_MEMBER` | No pertenece |
| 422 | `VALIDATION_ERROR` | type inválido |

## 4. Implementación

`Pactogg.Api/Modules/Points/PointsService.cs`

## 5. Criterios de aceptación

| Escenario | Evidencia |
|---|---|
| Tras cumplir: bond_release y bond_lock, saldo 100 | IntegrationTest/JuryFlowTests.cs |
| type=compra → 422 | IntegrationTest/JuryFlowTests.cs |

401 sin token: `SecurityTest/AuthorizationTests.cs`.


## Revisión local 2026-11-06

Pantalla y consumidor presentes. Ver [evidencia y límites](../../docs/pruebas/ampliacion-local-2026-11-06.md). No se acredita C6 ni aceptación completa por controles estáticos.
