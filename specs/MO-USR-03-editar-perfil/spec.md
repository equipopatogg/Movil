# MO-USR-03 — PATCH /api/v1/users/me

**Fecha:** 2026-11-04. **Estado móvil:** **Implementación local iniciada; verificación/aceptación integral pendientes.**
**Confirmación de alcance:** operación USR-03 incluida expresamente en Sistema.docx enviado por backend; también presente en su spec/código/OpenAPI al commit `8baa9e6d04038c1d6cf9588c5526ad63b94c3f87`. Confirmado aquí significa incluido en el documento recibido, no despliegue ni aceptación Android.
**Fuentes:** [Spec backend USR-03](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/specs/USR-03-update-profile/spec.md) · [Contrato OpenAPI](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/api/openapi.yaml) · [Matriz de recepción](../../docs/integracion/specs-confirmadas-sistema-2026-11-04.md).

## 1. Objetivo

Editar apodo y/o avatar predefinido del usuario autenticado. Adaptar la pantalla nativa existente y su recorrido real, conservando diseño. Una operación por expediente y mismo ID base que backend; MO identifica al consumidor móvil.

## 2. Requisitos funcionales

- RF1: Enviar solo nickname y/o avatar_id; exigir al menos un campo, sin email u otros campos desconocidos.
- RF2: Validar nickname con ^[A-Za-z0-9_.-]{3,24}$ y tratar unicidad sin distinguir mayúsculas según servidor; moderación se ejecuta solo en backend.
- RF3: avatar_id sigue ^[a-z0-9_-]{1,32}$; habilitar selección únicamente al acordar catálogo, sin subir fotografía de perfil.
- RF4: Guardar identidad visible confirmada sin rotar ni perder tokens; conservar formulario ante rechazo y actualizar perfil/miembros después de éxito. No repetir PATCH incierto automáticamente.

## 3. Errores

- 400 INVALID_INPUT: cuerpo/campos inválidos.
- 401 UNAUTHORIZED; 409 NICKNAME_ALREADY_EXISTS.
- 422 VALIDATION_ERROR / CONTENT_REJECTED; 503 MODERATION_UNAVAILABLE; no simular éxito.

Errores se deciden por HTTP/code, con request_id sanitizado cuando exista; no copiar message arbitrario ni registrar cuerpos o tokens. El formato data/meta y sus nulos deben validarse; campo desconocido de respuesta se tolera sin inventar datos ausentes.

## 4. Implementación prevista en Android

| Área | Cambio concreto |
|---|---|
| app/src/main/java/com/pactogg/app/core/network/PactoApi.kt | Firma Retrofit de la operación y serialización según contrato; reutilizar si ya existe |
| app/src/main/java/com/pactogg/app/feature/connected/data/ | DTO/mappers y métodos en ApiProductRepository existentes; sin duplicar custodio de sesión |
| app/src/main/java/com/pactogg/app/feature/connected/domain/ | Modelo e interfaz ProductRepository sin Android/Retrofit |
| app/src/main/java/com/pactogg/app/feature/connected/presentation/ | Estado/eventos y pantalla Compose pertinente, carga/error/resultado real y limpieza por cuenta |
| app/src/test/ y app/src/androidTest/ | Casos HTTP/estado y recorrido UI/dispositivo trazables por criterio |

Dependencias: [MO-AUTH-03-sesion](../MO-AUTH-03-sesion/spec.md) · [MO-USR-02-consultar-perfil](../MO-USR-02-consultar-perfil/spec.md) · [MO-USR-06-disponibilidad-apodo](../MO-USR-06-disponibilidad-apodo/spec.md). Estas áreas son previsión; no se crean módulos vacíos ni se asegura que ya existan sus métodos. Sin Mongo/Redis/AWS, índices, roles IAM o transacciones del servidor dentro de Android.

## 5. Criterios de aceptación

| ID móvil | Correspondencia backend | Resultado esperado | Evidencia actual |
|---|---|---|---|
| MO-USR-03-AC1 | USR03-AC1 | Cambio válido recibe 200 y GET lo refleja; sesión continúa con los mismos tokens vigentes. | [PARCIAL](checklists/checklist.md#mo-usr-03-ac1) · ejecución local 2026-11-05 |
| MO-USR-03-AC2 | USR03-AC2 | Apodo ocupado devuelve 409 y permite corregir sin perder formulario. | [COMPROBADO_LOCAL_CONSUMIDOR](checklists/checklist.md#mo-usr-03-ac2) · ejecución local 2026-11-05 |
| MO-USR-03-AC3 | USR03-AC3 | Cuerpo vacío/campo desconocido no se presenta como éxito; verificar 422/400 del proveedor con fixture. | [PARCIAL](checklists/checklist.md#mo-usr-03-ac3) · ejecución local 2026-11-05 |
| MO-USR-03-AC4 | USR03-AC4 | Moderación rechazada o no disponible conserva perfil anterior confirmado y muestra error seguro. | [PARCIAL](checklists/checklist.md#mo-usr-03-ac4) · ejecución local 2026-11-05 |
| MO-USR-03-ACQ | Control móvil | Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas | [CONTROLES_LOCALES_PARCIALES](checklists/checklist.md#mo-usr-03-acq) · ejecución local 2026-11-05 |

Los IDs de backend sirven para trazabilidad, no para heredar PASS de sus pruebas. Casos de base de datos y auditoría del backend se verifican allí; Android comprueba peticiones, respuestas y resultado visible.

## 6. Plan de pruebas y restricciones móviles

Preparar fixtures HTTPS MockWebServer para contrato exacto, errores, campos faltantes/nulos y respuesta perdida; comprobar estado, doble toque y respuestas fuera de orden. UI/dispositivo para interacción, ciclo de vida y escalado de texto. Operaciones protegidas reutilizan MO-AUTH-03, HTTPS sin bypass y custodia cifrada; solo USR-06 es público. GET admite la política compartida de renovación; escrituras nunca se repiten automáticamente. Usar cuentas QA autorizadas para proveedor y dos cuentas cuando haya autorización entre usuarios; no saturar cupos o rate limits. No atribuir tests previos al nuevo candidato.

## 7. Riesgos, límites y pendientes

Catálogo de avatar pendiente de backend/equipo. No inventar IDs, subida de avatar, cambios de correo o eliminación de cuenta.

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

- unit: `com.pactogg.app.feature.profile.edit.EditProfileValidationTest.USR_03_rejectsOversizedNicknameBeforeUpdate`.
- integration: `com.pactogg.app.feature.profile.edit.EditProfileApiTest.USR_03_sendsOnlyNicknameAndUpdatesVisibleIdentity`.
- security: `com.pactogg.app.feature.profile.edit.EditProfileSecurityTest.USR_03_forbiddenUpdatePreservesExistingSession`.

Se reutilizan además casos compartidos de TLS, sesión, estados, reglas y contrato. Android real prueba cifrado/Room/acceso por separado. Este apartado no convierte un caso de consumidor o fixture en aceptación de todos los AC ni prueba de autorización del servidor; controles y limitaciones finales se registran en el informe de esta fase.

### Archivos de prueba de esta operación

- Validación: [EditProfileValidationTest.kt](../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileValidationTest.kt).
- Comunicación con API: [EditProfileApiTest.kt](../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileApiTest.kt).
- Seguridad y errores: [EditProfileSecurityTest.kt](../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileSecurityTest.kt).

Reorganización local 2026-11-05: se conservan los escenarios y las pruebas compartidas de sesión/recorridos. Sin aceptación funcional nueva ni publicación por mover pruebas.

## Relación criterio → tareas → pruebas → resultado — 2026-11-05

La tabla de aceptación enlaza la revisión detallada en el checklist. Se identifican casos ejecutados, alcance y escenarios faltantes sin cerrar criterios compuestos por un PASS parcial. [Resumen de las 19 specs](../../docs/pruebas/revision-specs-2026-11-05.md). Cambios exclusivamente locales; privacidad/avatares esperan al backend y Google Play queda fuera.
