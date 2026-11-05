# Checklist — MO-GRP-03

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-GRP-03-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Miembro recibe y visualiza 200 con settings, rol y saldo reales.

**Tareas:** `MO-GRP-03-T4`, `MO-GRP-03-T5` y `MO-GRP-03-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** ID y puntos recibidos en detalle se verifican; prueba live del miembro confirma lectura 200.

- [GroupDetailsApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/detail/GroupDetailsApiTest.kt) · `GRP_03_mapsRequestedGroupIdentity` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `groupListDetailAndJoinUseServerRoleBalanceAndNormalizedCode` · PASS JVM

**Pendiente/límite:** Faltan asserts de settings/rol/saldo en la pantalla, además de solo recibir y mapear respuesta.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-GRP-03-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Ajeno/inexistente muestra 403/404 sin filtrar datos; respuesta de otro ID se rechaza.

**Tareas:** `MO-GRP-03-T4`, `MO-GRP-03-T5` y `MO-GRP-03-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** ID de grupo discordante se rechaza; 403 de fixture y live no miembro comprobados sin cuerpo privado retenido. Ampliación actual: GRP_03_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity.

- [GroupDetailsSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/groups/detail/GroupDetailsSecurityTest.kt) · `GRP_03_forbiddenGroupReadPreservesExistingSession` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `malformedPageAndWrongGroupAreRejected` · PASS JVM
- [GroupDetailsApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/detail/GroupDetailsApiTest.kt) · `GRP_03_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM

**Pendiente/límite:** 404 NOT_FOUND comprobado en repositorio; falta estado Compose del recurso inexistente.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-GRP-03-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-GRP-03-T4`, `MO-GRP-03-T5` y `MO-GRP-03-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [GroupDetailsValidationTest](../../../app/src/test/java/com/pactogg/app/feature/groups/detail/GroupDetailsValidationTest.kt) · `GRP_03_rejectsMalformedGroupIdentity` · PASS JVM
- [GroupDetailsApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/detail/GroupDetailsApiTest.kt) · `GRP_03_mapsRequestedGroupIdentity` · PASS JVM
- [GroupDetailsSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/groups/detail/GroupDetailsSecurityTest.kt) · `GRP_03_forbiddenGroupReadPreservesExistingSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-GRP-03

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-GRP-03-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Miembro recibe y visualiza 200 con settings, rol y saldo reales.

**Tareas:** `MO-GRP-03-T4`, `MO-GRP-03-T5` y `MO-GRP-03-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** ID y puntos recibidos en detalle se verifican; prueba live del miembro confirma lectura 200.

- [GroupDetailsApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/detail/GroupDetailsApiTest.kt) · `GRP_03_mapsRequestedGroupIdentity` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `groupListDetailAndJoinUseServerRoleBalanceAndNormalizedCode` · PASS JVM

**Proveedor, registro previo:** Miembro: detalle 200; no cubre escrituras, otros roles o UI. Ver catálogo de evidencia del informe global.

**Falta:** Faltan asserts de settings/rol/saldo en la pantalla, además de solo recibir y mapear respuesta.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-GRP-03-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Ajeno/inexistente muestra 403/404 sin filtrar datos; respuesta de otro ID se rechaza.

**Tareas:** `MO-GRP-03-T4`, `MO-GRP-03-T5` y `MO-GRP-03-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** ID de grupo discordante se rechaza; 403 de fixture y live no miembro comprobados sin cuerpo privado retenido.

- [GroupDetailsSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/groups/detail/GroupDetailsSecurityTest.kt) · `GRP_03_forbiddenGroupReadPreservesExistingSession` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `malformedPageAndWrongGroupAreRejected` · PASS JVM

**Proveedor, registro previo:** No miembro: detalle 403; no cubre escrituras, otros roles o UI. Ver catálogo de evidencia del informe global.

**Falta:** Falta 404 de grupo inexistente y su estado UI; no miembro y recurso inexistente son casos diferentes.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-GRP-03-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-GRP-03-T4`, `MO-GRP-03-T5` y `MO-GRP-03-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [GroupDetailsValidationTest](../../../app/src/test/java/com/pactogg/app/feature/groups/detail/GroupDetailsValidationTest.kt) · `GRP_03_rejectsMalformedGroupIdentity` · PASS JVM
- [GroupDetailsApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/detail/GroupDetailsApiTest.kt) · `GRP_03_mapsRequestedGroupIdentity` · PASS JVM
- [GroupDetailsSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/groups/detail/GroupDetailsSecurityTest.kt) · `GRP_03_forbiddenGroupReadPreservesExistingSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla. Falta accesibilidad del recorrido específico.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado: implementación local presente; recorrido positivo real y controles locales correctos; aceptación integral pendiente. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-GRP-03-AC1: Miembro recibe y visualiza 200 con settings, rol y saldo reales.
- [ ] MO-GRP-03-AC2: Ajeno/inexistente muestra 403/404 sin filtrar datos; respuesta de otro ID se rechaza.
- [ ] MO-GRP-03-ACQ: controles y evidencia del mismo candidato verificados.
- [ ] Bloqueantes específicos de sección 7 resueltos para las tareas afectadas.
- [ ] Prueba real del proveedor distinguida de fixtures; no se hereda PASS del backend.
- [ ] Revisión humana y aceptación de implementación registradas por separado.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación solicitada | Usuario | 2026-11-04 | Operación incluida en Sistema.docx; no prueba funcional |
| Aceptación móvil | Pendiente | Pendiente | No inferir por existencia de documentos |


[Resultado real y candidato](../../../docs/integracion/integracion-social-2026-11-04.md): caso positivo comprobado; no se cierran casillas integrales que contienen escenarios restantes. Datos y límites de la cuenta auxiliar/recorrido UI distinguidos de fixtures.
