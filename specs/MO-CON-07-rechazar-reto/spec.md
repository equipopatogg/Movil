# MO-CON-07 — POST /api/v1/contracts/{id}/reject

**Fecha:** 2026-11-04. **Estado móvil:** **Implementación local iniciada; verificación/aceptación integral pendientes.**
**Confirmación de alcance:** operación CON-07 incluida expresamente en Sistema.docx enviado por backend; también presente en su spec/código/OpenAPI al commit `8baa9e6d04038c1d6cf9588c5526ad63b94c3f87`. Confirmado aquí significa incluido en el documento recibido, no despliegue ni aceptación Android.
**Fuentes:** [Spec backend CON-07](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/specs/CON-07-reject-contract/spec.md) · [Contrato OpenAPI](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/api/openapi.yaml) · [Matriz de recepción](../../docs/integracion/specs-confirmadas-sistema-2026-11-04.md).

## 1. Objetivo

Permitir al retado rechazar un reto dirigido a él mientras está open. Adaptar la pantalla nativa existente y su recorrido real, conservando diseño. Una operación por expediente y mismo ID base que backend; MO identifica al consumidor móvil.

## 2. Requisitos funcionales

- RF1: Habilitar solo target_type=member y target_id del usuario autenticado; no ofrecer rechazo genérico a retos de grupo.
- RF2: Solicitar confirmación y enviar {version} de detalle observado; mantener bloqueado durante envío y no repetir POST incierto.
- RF3: 200 actualiza cancelled y closed_reason=rejected; no crear estado rejected ni mover puntos; refrescar detalle/tablero.
- RF4: 403 conserva contexto autorizado sin fingir cierre; 409 obliga recargar y decidir de nuevo, sin reintento automático.

## 3. Errores

- 401 UNAUTHORIZED; 403 NOT_CONTRACT_TARGET (y pertenencia según código); 404 NOT_FOUND.
- 409 INVALID_CONTRACT_STATE por estado/versión; red con resultado incierto.

Errores se deciden por HTTP/code, con request_id sanitizado cuando exista; no copiar message arbitrario ni registrar cuerpos o tokens. El formato data/meta y sus nulos deben validarse; campo desconocido de respuesta se tolera sin inventar datos ausentes.

## 4. Implementación prevista en Android

| Área | Cambio concreto |
|---|---|
| app/src/main/java/com/pactogg/app/core/network/PactoApi.kt | Firma Retrofit de la operación y serialización según contrato; reutilizar si ya existe |
| app/src/main/java/com/pactogg/app/feature/connected/data/ | DTO/mappers y métodos en ApiProductRepository existentes; sin duplicar custodio de sesión |
| app/src/main/java/com/pactogg/app/feature/connected/domain/ | Modelo e interfaz ProductRepository sin Android/Retrofit |
| app/src/main/java/com/pactogg/app/feature/connected/presentation/ | Estado/eventos y pantalla Compose pertinente, carga/error/resultado real y limpieza por cuenta |
| app/src/test/ y app/src/androidTest/ | Casos HTTP/estado y recorrido UI/dispositivo trazables por criterio |

Dependencias: [MO-CON-03-detalle-reto](../MO-CON-03-detalle-reto/spec.md) · [MO-CON-02-listar-retos](../MO-CON-02-listar-retos/spec.md). Estas áreas son previsión; no se crean módulos vacíos ni se asegura que ya existan sus métodos. Sin Mongo/Redis/AWS, índices, roles IAM o transacciones del servidor dentro de Android.

## 5. Criterios de aceptación

| ID móvil | Correspondencia backend | Resultado esperado | Evidencia actual |
|---|---|---|---|
| MO-CON-07-AC1 | CON07-AC1 | Retador/tercero no puede rechazar; 403 no modifica estado ni saldo. | [PARCIAL](checklists/checklist.md#mo-con-07-ac1) · ejecución local 2026-11-05 |
| MO-CON-07-AC2 | CON07-AC2 | Versión vieja produce 409; versión válida obtiene cancelled con motivo rejected y puntos intactos. | [PARCIAL](checklists/checklist.md#mo-con-07-ac2) · ejecución local 2026-11-05 |
| MO-CON-07-AC3 | CON07-AC3 | Aceptar después de rechazo no produce éxito y maneja 409 tras refrescar. | [COMPROBADO_LOCAL_CONSUMIDOR](checklists/checklist.md#mo-con-07-ac3) · ejecución local 2026-11-05 |
| MO-CON-07-ACQ | Control móvil | Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas | [CONTROLES_LOCALES_PARCIALES](checklists/checklist.md#mo-con-07-acq) · ejecución local 2026-11-05 |

Los IDs de backend sirven para trazabilidad, no para heredar PASS de sus pruebas. Casos de base de datos y auditoría del backend se verifican allí; Android comprueba peticiones, respuestas y resultado visible.

## 6. Plan de pruebas y restricciones móviles

Preparar fixtures HTTPS MockWebServer para contrato exacto, errores, campos faltantes/nulos y respuesta perdida; comprobar estado, doble toque y respuestas fuera de orden. UI/dispositivo para interacción, ciclo de vida y escalado de texto. Operaciones protegidas reutilizan MO-AUTH-03, HTTPS sin bypass y custodia cifrada; solo USR-06 es público. GET admite la política compartida de renovación; escrituras nunca se repiten automáticamente. Usar cuentas QA autorizadas para proveedor y dos cuentas cuando haya autorización entre usuarios; no saturar cupos o rate limits. No atribuir tests previos al nuevo candidato.

## 7. Riesgos, límites y pendientes

Backend spec menciona rejected_by pero DTO/OpenAPI no lo devuelven: no mostrar autor del rechazo sin dato acordado. Rechazar no equivale a cancelar cualquier reto o editarlo.

Fuente de discrepancias: [revisión de contratos](../../docs/integracion/revision-contratos-nuevos-2026-11-04.md). Catálogo, nulabilidad y reglas discrepantes se resuelven antes de la tarea afectada. No agregar funciones ausentes en Sistema.docx ni datos falsos al recorrido conectado. Implementación/despliegue anunciado por backend no equivale a verificación de consumidor.

## Aprobación y control de cambios

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación solicitada | Usuario | 2026-11-04 | Crear equivalentes solo de operaciones del documento backend |
| Alcance corroborado | Revisión documental del agente | 2026-11-04 | Sistema.docx + spec/OpenAPI/código fijados; no aceptación humana |
| Implementación / aceptación móvil | Pendiente | Pendiente | Verificar todos los criterios del candidato; no se programa por redactar |

[Plan](plan.md) · [Tareas](tasks.md) · [Checklist](checklists/checklist.md).


## Implementación autorizada posterior — 2026-11-04

Usuario solicita integrar las once specs en la app. Se amplía el consumidor conectado existente, sin datos de muestra ni dependencias nuevas. PactoApi hereda firmas agrupadas de ProfileApi, GroupReadApi y ContractActionsApi; se reutiliza GET de detalle anterior. ApiSocialRepository/SocialRepository agrupan estas operaciones y ApiProductRepository mantiene creación/evidencia/torneos; ambos usan SessionManager único. Perfil confirmado actualiza identidad cifrada conservando tokens. Pantallas usan SocialState y acciones/consultas del mismo ViewModel, con paginación, contexto por cuenta y confirmación versionada de aceptar/rechazar. Ninguna transacción Mongo o IAM se copia al móvil.

Disponibilidad del apodo espera al escribir, cancela/descarta consultas previas y respeta enfriamiento tras 429. Avatar conserva bloqueo explícito por catálogo pendiente. Se mantiene el diseño y las funciones futuras ocultas. La frase documental anterior «no se programa por redactar» describe aquella preparación, superada únicamente por esta autorización expresa. Verificación del candidato y proveedor se registrará en informe; ninguna casilla integral se cierra por crear código.


## Verificación posterior del consumidor

[Informe del candidato y límites](../../docs/integracion/integracion-social-2026-11-04.md): 44 JVM, build/Lint/Detekt, dos métodos instrumentados y recorrido positivo real correctos. Operación integrada; no aceptación integral. Avatar sigue pendiente en USR-03; los AC negativos/completos y revisión humana conservan estado abierto. No atribuir el estado inicial NO_EJECUTADO a ausencia de código actual ni reemplazar evidencia esperada por PASS global.


## Pruebas específicas por operación — actualización 2026-11-05

[Escenarios y límites](../../docs/pruebas/matriz-operaciones.md) · [Manifestación canónica](../../docs/pruebas/operaciones.json). Cada archivo muestra sus escenarios y llama directamente al consumidor correspondiente. El ID de operación se conserva como prefijo del método (por ejemplo, AUTH_01_); la matriz valida la identidad completa:

- unit: `com.pactogg.app.feature.contracts.reject.RejectChallengeValidationTest.CON_07_rejectsGroupTargetAndAnotherTargetUser`.
- integration: `com.pactogg.app.feature.contracts.reject.RejectChallengeApiTest.CON_07_sendsVersionWhenRejectingChallenge`.
- security: `com.pactogg.app.feature.contracts.reject.RejectChallengeSecurityTest.CON_07_forbiddenRejectPreservesExistingSession`.

Se reutilizan además casos compartidos de TLS, sesión, estados, reglas y contrato. Android real prueba cifrado/Room/acceso por separado. Este apartado no convierte un caso de consumidor o fixture en aceptación de todos los AC ni prueba de autorización del servidor; controles y limitaciones finales se registran en el informe de esta fase.

### Archivos de prueba de esta operación

- Validación: [RejectChallengeValidationTest.kt](../../app/src/test/java/com/pactogg/app/feature/contracts/reject/RejectChallengeValidationTest.kt).
- Comunicación con API: [RejectChallengeApiTest.kt](../../app/src/test/java/com/pactogg/app/feature/contracts/reject/RejectChallengeApiTest.kt).
- Seguridad y errores: [RejectChallengeSecurityTest.kt](../../app/src/test/java/com/pactogg/app/feature/contracts/reject/RejectChallengeSecurityTest.kt).

Reorganización local 2026-11-05: se conservan los escenarios y las pruebas compartidas de sesión/recorridos. Sin aceptación funcional nueva ni publicación por mover pruebas.

## Relación criterio → tareas → pruebas → resultado — 2026-11-05

La tabla de aceptación enlaza la revisión detallada en el checklist. Se identifican casos ejecutados, alcance y escenarios faltantes sin cerrar criterios compuestos por un PASS parcial. [Resumen de las 19 specs](../../docs/pruebas/revision-specs-2026-11-05.md). Cambios exclusivamente locales; privacidad/avatares esperan al backend y Google Play queda fuera.
