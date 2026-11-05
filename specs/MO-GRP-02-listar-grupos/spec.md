# MO-GRP-02 — GET /api/v1/groups

**Fecha:** 2026-11-04. **Estado móvil:** **Implementación local iniciada; verificación/aceptación integral pendientes.**
**Confirmación de alcance:** operación GRP-02 incluida expresamente en Sistema.docx enviado por backend; también presente en su spec/código/OpenAPI al commit `8baa9e6d04038c1d6cf9588c5526ad63b94c3f87`. Confirmado aquí significa incluido en el documento recibido, no despliegue ni aceptación Android.
**Fuentes:** [Spec backend GRP-02](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/specs/GRP-02-list-groups/spec.md) · [Contrato OpenAPI](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/api/openapi.yaml) · [Matriz de recepción](../../docs/integracion/specs-confirmadas-sistema-2026-11-04.md).

## 1. Objetivo

Mostrar los grupos reales del usuario y permitir seleccionar el contexto activo. Adaptar la pantalla nativa existente y su recorrido real, conservando diseño. Una operación por expediente y mismo ID base que backend; MO identifica al consumidor móvil.

## 2. Requisitos funcionales

- RF1: Consumir solo grupos donde el usuario es miembro activo; incluir my_role y my_points devueltos.
- RF2: Enviar q opcional de hasta 40 caracteres, limit 1–50 (20 inicial) y cursor opaco; mapear meta.has_more y next_cursor.
- RF3: Mostrar lista/vacío/carga/error; reiniciar cursor al cambiar búsqueda, descartar respuestas anteriores y evitar duplicados.
- RF4: Refrescar tras crear/unirse/aceptar; recuperar grupos después de reiniciar sin inventar historial ni conservar grupo de otra cuenta.

## 3. Errores

- 401 UNAUTHORIZED: sesión compartida.
- 422 VALIDATION_ERROR: q/limit/cursor inválidos.
- Red/cuerpo incoherente: error recuperable, sin reemplazarlo por lista vacía confirmada.

Errores se deciden por HTTP/code, con request_id sanitizado cuando exista; no copiar message arbitrario ni registrar cuerpos o tokens. El formato data/meta y sus nulos deben validarse; campo desconocido de respuesta se tolera sin inventar datos ausentes.

## 4. Implementación prevista en Android

| Área | Cambio concreto |
|---|---|
| app/src/main/java/com/pactogg/app/core/network/PactoApi.kt | Firma Retrofit de la operación y serialización según contrato; reutilizar si ya existe |
| app/src/main/java/com/pactogg/app/feature/connected/data/ | DTO/mappers y métodos en ApiProductRepository existentes; sin duplicar custodio de sesión |
| app/src/main/java/com/pactogg/app/feature/connected/domain/ | Modelo e interfaz ProductRepository sin Android/Retrofit |
| app/src/main/java/com/pactogg/app/feature/connected/presentation/ | Estado/eventos y pantalla Compose pertinente, carga/error/resultado real y limpieza por cuenta |
| app/src/test/ y app/src/androidTest/ | Casos HTTP/estado y recorrido UI/dispositivo trazables por criterio |

Dependencias: [MO-AUTH-03-sesion](../MO-AUTH-03-sesion/spec.md) · [MO-GRP-01-crear-grupo](../MO-GRP-01-crear-grupo/spec.md). Estas áreas son previsión; no se crean módulos vacíos ni se asegura que ya existan sus métodos. Sin Mongo/Redis/AWS, índices, roles IAM o transacciones del servidor dentro de Android.

## 5. Criterios de aceptación

| ID móvil | Correspondencia backend | Resultado esperado | Evidencia actual |
|---|---|---|---|
| MO-GRP-02-AC1 | GRP02-AC1 | Tras unirse, el grupo recibido aparece al refrescar y puede seleccionarse. | Pendiente: JVM/HTTP, UI y proveedor según corresponda |
| MO-GRP-02-AC2 | GRP02-AC2 | Búsqueda filtra por nombre; paginación no duplica y cambio de filtro reinicia cursor. | Pendiente: JVM/HTTP, UI y proveedor según corresponda |
| MO-GRP-02-AC3 | GRP02-AC3 | limit inválido se maneja como 422; validar límites del cliente y meta inconsistente. | Pendiente: JVM/HTTP, UI y proveedor según corresponda |
| MO-GRP-02-ACQ | Control móvil | Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas | Pendiente del candidato |

Los IDs de backend sirven para trazabilidad, no para heredar PASS de sus pruebas. Casos de base de datos y auditoría del backend se verifican allí; Android comprueba peticiones, respuestas y resultado visible.

## 6. Plan de pruebas y restricciones móviles

Preparar fixtures HTTPS MockWebServer para contrato exacto, errores, campos faltantes/nulos y respuesta perdida; comprobar estado, doble toque y respuestas fuera de orden. UI/dispositivo para interacción, ciclo de vida y escalado de texto. Operaciones protegidas reutilizan MO-AUTH-03, HTTPS sin bypass y custodia cifrada; solo USR-06 es público. GET admite la política compartida de renovación; escrituras nunca se repiten automáticamente. Usar cuentas QA autorizadas para proveedor y dos cuentas cuando haya autorización entre usuarios; no saturar cupos o rate limits. No atribuir tests previos al nuevo candidato.

## 7. Riesgos, límites y pendientes

Caché proveedor de hasta 60 s para cambios ajenos. No añadir grupos públicos, invitaciones por enlace o Offline-First.

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


## Pruebas consumidor por tipo — actualización 2026-11-04

[Escenarios y límites](../../docs/pruebas/matriz-operaciones.md) · [Manifestación canónica](../../docs/pruebas/operaciones.json). Las instancias parametrizadas conservan el ID exacto en JUnit:

- unit: `com.pactogg.app.testing.EndpointUnitTest.enforcesConsumerBoundaryWithoutNetwork[GRP-02]`.
- integration: `com.pactogg.app.testing.EndpointContractTest.consumesConfirmedWireContract[GRP-02]`.
- security: `com.pactogg.app.testing.EndpointSecurityTest.deniedResponseIsSanitizedAndNeverReplayed[GRP-02]`.

Se reutilizan además casos compartidos de TLS, sesión, estados, reglas y contrato. Android real prueba cifrado/Room/acceso por separado. Este apartado no convierte un caso de consumidor o fixture en aceptación de todos los AC ni prueba de autorización del servidor; controles y limitaciones finales se registran en el informe de esta fase.
