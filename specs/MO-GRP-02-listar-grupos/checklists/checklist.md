# Checklist — MO-GRP-02

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-GRP-02-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Tras unirse, el grupo recibido aparece al refrescar y puede seleccionarse.

**Tareas:** `MO-GRP-02-T4`, `MO-GRP-02-T5` y `MO-GRP-02-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Unión confirmada aplica grupo e invalida lista; listado/detalle/unión se consumen y selección cambia contexto.

- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `joinClearsInviteOnlyAfterProviderConfirmation` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `anotherGroupClearsOldContractAndRival` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `groupListDetailAndJoinUseServerRoleBalanceAndNormalizedCode` · PASS JVM

**Pendiente/límite:** Falta un caso encadenado de unirse→refrescar lista→ver nueva fila→seleccionarla; los pasos aislados no prueban todo el recorrido.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-GRP-02-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Búsqueda filtra por nombre; paginación no duplica y cambio de filtro reinicia cursor.

**Tareas:** `MO-GRP-02-T4`, `MO-GRP-02-T5` y `MO-GRP-02-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** q se serializa; segunda página usa cursor, no duplica, detiene al final y búsqueda vacía lista previa.

- [ListGroupsApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/list/ListGroupsApiTest.kt) · `GRP_02_sendsGroupSearchQuery` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `paginationAppendsWithoutDuplicatingAndStopsAtEnd` · PASS JVM
- [ContractPolicyTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ContractPolicyTest.kt) · `pagesMergeByRealIdentityAndResetForNewFilter` · PASS JVM

**Pendiente/límite:** Falta assert explícito de cursor reiniciado y filtrado real con varias páginas/nombres; no se transfiere el filtro del prototipo Explorar.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-GRP-02-AC3

**Estado técnico: PARCIAL.**
**Condición original:** limit inválido se maneja como 422; validar límites del cliente y meta inconsistente.

**Tareas:** `MO-GRP-02-T4`, `MO-GRP-02-T5` y `MO-GRP-02-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Búsqueda excesiva y meta has_more sin cursor se rechazan.

- [ListGroupsValidationTest](../../../app/src/test/java/com/pactogg/app/feature/groups/list/ListGroupsValidationTest.kt) · `GRP_02_rejectsOversizedGroupSearch` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `malformedPageAndWrongGroupAreRejected` · PASS JVM

**Pendiente/límite:** Falta fixture 422 por limit y fronteras del límite admitido; cliente fija limit=20 y los tests no envían limit inválido.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-GRP-02-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-GRP-02-T4`, `MO-GRP-02-T5` y `MO-GRP-02-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [ListGroupsValidationTest](../../../app/src/test/java/com/pactogg/app/feature/groups/list/ListGroupsValidationTest.kt) · `GRP_02_rejectsOversizedGroupSearch` · PASS JVM
- [ListGroupsApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/list/ListGroupsApiTest.kt) · `GRP_02_sendsGroupSearchQuery` · PASS JVM
- [ListGroupsSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/groups/list/ListGroupsSecurityTest.kt) · `GRP_02_forbiddenListingPreservesExistingSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-GRP-02

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-GRP-02-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Tras unirse, el grupo recibido aparece al refrescar y puede seleccionarse.

**Tareas:** `MO-GRP-02-T4`, `MO-GRP-02-T5` y `MO-GRP-02-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Unión confirmada aplica grupo e invalida lista; listado/detalle/unión se consumen y selección cambia contexto.

- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `joinClearsInviteOnlyAfterProviderConfirmation` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `anotherGroupClearsOldContractAndRival` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `groupListDetailAndJoinUseServerRoleBalanceAndNormalizedCode` · PASS JVM

**Falta:** Falta un caso encadenado de unirse→refrescar lista→ver nueva fila→seleccionarla; los pasos aislados no prueban todo el recorrido.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-GRP-02-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Búsqueda filtra por nombre; paginación no duplica y cambio de filtro reinicia cursor.

**Tareas:** `MO-GRP-02-T4`, `MO-GRP-02-T5` y `MO-GRP-02-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** q se serializa; segunda página usa cursor, no duplica, detiene al final y búsqueda vacía lista previa.

- [ListGroupsApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/list/ListGroupsApiTest.kt) · `GRP_02_sendsGroupSearchQuery` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `paginationAppendsWithoutDuplicatingAndStopsAtEnd` · PASS JVM
- [ContractPolicyTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ContractPolicyTest.kt) · `pagesMergeByRealIdentityAndResetForNewFilter` · PASS JVM

**Falta:** Falta assert explícito de cursor reiniciado y filtrado real con varias páginas/nombres; no se transfiere el filtro del prototipo Explorar.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-GRP-02-AC3

**Estado técnico: PARCIAL.**
**Condición original:** limit inválido se maneja como 422; validar límites del cliente y meta inconsistente.

**Tareas:** `MO-GRP-02-T4`, `MO-GRP-02-T5` y `MO-GRP-02-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Búsqueda excesiva y meta has_more sin cursor se rechazan.

- [ListGroupsValidationTest](../../../app/src/test/java/com/pactogg/app/feature/groups/list/ListGroupsValidationTest.kt) · `GRP_02_rejectsOversizedGroupSearch` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `malformedPageAndWrongGroupAreRejected` · PASS JVM

**Falta:** Falta fixture 422 por limit y fronteras del límite admitido; cliente fija limit=20 y los tests no envían limit inválido.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-GRP-02-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-GRP-02-T4`, `MO-GRP-02-T5` y `MO-GRP-02-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [ListGroupsValidationTest](../../../app/src/test/java/com/pactogg/app/feature/groups/list/ListGroupsValidationTest.kt) · `GRP_02_rejectsOversizedGroupSearch` · PASS JVM
- [ListGroupsApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/list/ListGroupsApiTest.kt) · `GRP_02_sendsGroupSearchQuery` · PASS JVM
- [ListGroupsSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/groups/list/ListGroupsSecurityTest.kt) · `GRP_02_forbiddenListingPreservesExistingSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla. Falta accesibilidad del recorrido específico.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado: implementación local presente; recorrido positivo real y controles locales correctos; aceptación integral pendiente. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-GRP-02-AC1: Tras unirse, el grupo recibido aparece al refrescar y puede seleccionarse.
- [ ] MO-GRP-02-AC2: Búsqueda filtra por nombre; paginación no duplica y cambio de filtro reinicia cursor.
- [ ] MO-GRP-02-AC3: limit inválido se maneja como 422; validar límites del cliente y meta inconsistente.
- [ ] MO-GRP-02-ACQ: controles y evidencia del mismo candidato verificados.
- [ ] Bloqueantes específicos de sección 7 resueltos para las tareas afectadas.
- [ ] Prueba real del proveedor distinguida de fixtures; no se hereda PASS del backend.
- [ ] Revisión humana y aceptación de implementación registradas por separado.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación solicitada | Usuario | 2026-11-04 | Operación incluida en Sistema.docx; no prueba funcional |
| Aceptación móvil | Pendiente | Pendiente | No inferir por existencia de documentos |


[Resultado real y candidato](../../../docs/integracion/integracion-social-2026-11-04.md): caso positivo comprobado; no se cierran casillas integrales que contienen escenarios restantes. Datos y límites de la cuenta auxiliar/recorrido UI distinguidos de fixtures.
