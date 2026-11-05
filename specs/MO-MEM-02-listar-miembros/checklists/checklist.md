# Checklist — MO-MEM-02

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-MEM-02-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Lista muestra miembros/rol/saldo reales en orden del servidor.

**Tareas:** `MO-MEM-02-T4`, `MO-MEM-02-T5` y `MO-MEM-02-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Miembro recibido conserva saldo y se coloca en estado; lectura live miembro 200.

- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `membersPrefixAndBoardFiltersFollowCursorContract` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `membersBoardAndFiltersDoNotMixPages` · PASS JVM

**Pendiente/límite:** Faltan varios miembros/roles/saldos para comprobar orden real y representación UI sin constantes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-MEM-02-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Prefijo encuentra el miembro esperado y respuesta tardía no contamina otro grupo.

**Tareas:** `MO-MEM-02-T4`, `MO-MEM-02-T5` y `MO-MEM-02-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Prefijo q se envía y cambiar grupo borra miembros/rival anteriores.

- [ListMembersApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/members/ListMembersApiTest.kt) · `MEM_02_sendsMemberSearchWithinSelectedGroup` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `membersPrefixAndBoardFiltersFollowCursorContract` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `anotherGroupClearsOldContractAndRival` · PASS JVM

**Pendiente/límite:** Faltan respuesta de miembros retenida durante cambio de grupo y resultado real de búsqueda por prefijo; reset previo no prueba carrera en vuelo.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-MEM-02-AC3

**Estado técnico: COMPROBADO_LOCAL_CONSUMIDOR.**
**Condición original:** limit=1 y cursor cargan siguiente página sin duplicados.

**Tareas:** `MO-MEM-02-T4`, `MO-MEM-02-T5` y `MO-MEM-02-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Repositorio sigue cursor; frontera HTTP envía limit=1 en dos páginas. Estado agrega sin duplicados y deja de consultar al final; la pantalla normal conserva limit=20.

- [ListMembersApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/members/ListMembersApiTest.kt) · `MEM_02_followsNextCursorAndSupportsLimitOneAtWireBoundary` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `memberPagesAppendDeduplicateAndStopAfterLastPage` · PASS JVM

Apoyo indirecto (no sustituye la condición): `com.pactogg.app.feature.connected.presentation.ContractPolicyTest#pagesMergeByRealIdentityAndResetForNewFilter`.

**Pendiente/límite:** Aceptación humana y del despliegue real separadas; no ejecutadas por esta fase local.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-MEM-02-AC4

**Estado técnico: PARCIAL.**
**Condición original:** Cuenta ajena recibe 403 sin exponer miembros ni permitir elegir rival.

**Tareas:** `MO-MEM-02-T4`, `MO-MEM-02-T5` y `MO-MEM-02-T4-AC4` en [tasks.md](../tasks.md).

**Lo comprobado:** 403 de lectura live no miembro y consumidor; sin sesión no hay tráfico; rival exige miembro visible y otro usuario.

- [ListMembersSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/groups/members/ListMembersSecurityTest.kt) · `MEM_02_forbiddenMemberReadPreservesExistingSession` · PASS JVM
- [ListMembersSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/groups/members/ListMembersSecurityTest.kt) · `MEM_02_missingSessionPreventsListMembersRequest` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `rivalMustBeVisibleAndCannotBeSelf` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `changingAccountClearsGroupDraftFiltersMessagesAndCredentials` · PASS JVM

**Pendiente/límite:** Falta escenario UI autenticado con cuenta ajena que compruebe lista vacía y selección de rival bloqueada después del 403.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-MEM-02-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-MEM-02-T4`, `MO-MEM-02-T5` y `MO-MEM-02-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [ListMembersValidationTest](../../../app/src/test/java/com/pactogg/app/feature/groups/members/ListMembersValidationTest.kt) · `MEM_02_rejectsOversizedMemberSearch` · PASS JVM
- [ListMembersApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/members/ListMembersApiTest.kt) · `MEM_02_sendsMemberSearchWithinSelectedGroup` · PASS JVM
- [ListMembersSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/groups/members/ListMembersSecurityTest.kt) · `MEM_02_forbiddenMemberReadPreservesExistingSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-MEM-02

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-MEM-02-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Lista muestra miembros/rol/saldo reales en orden del servidor.

**Tareas:** `MO-MEM-02-T4`, `MO-MEM-02-T5` y `MO-MEM-02-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Miembro recibido conserva saldo y se coloca en estado; lectura live miembro 200.

- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `membersPrefixAndBoardFiltersFollowCursorContract` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `membersBoardAndFiltersDoNotMixPages` · PASS JVM

**Proveedor, registro previo:** Miembro: miembros 200; no cubre escrituras, otros roles o UI. Ver catálogo de evidencia del informe global.

**Falta:** Faltan varios miembros/roles/saldos para comprobar orden real y representación UI sin constantes.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-MEM-02-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Prefijo encuentra el miembro esperado y respuesta tardía no contamina otro grupo.

**Tareas:** `MO-MEM-02-T4`, `MO-MEM-02-T5` y `MO-MEM-02-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Prefijo q se envía y cambiar grupo borra miembros/rival anteriores.

- [ListMembersApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/members/ListMembersApiTest.kt) · `MEM_02_sendsMemberSearchWithinSelectedGroup` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `membersPrefixAndBoardFiltersFollowCursorContract` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `anotherGroupClearsOldContractAndRival` · PASS JVM

**Falta:** Faltan respuesta de miembros retenida durante cambio de grupo y resultado real de búsqueda por prefijo; reset previo no prueba carrera en vuelo.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-MEM-02-AC3

**Estado técnico: SIN_CASO_ESPECIFICO.**
**Condición original:** limit=1 y cursor cargan siguiente página sin duplicados.

**Tareas:** `MO-MEM-02-T4`, `MO-MEM-02-T5` y `MO-MEM-02-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** No hay caso específico que solicite miembros con limit=1 y siga un cursor. El nombre FollowCursorContract del test compartido solo usa limit=20 y cursor inicial.

No se encontró un caso específico que ejercite la secuencia completa indicada.

**Apoyo indirecto; no demuestra por sí solo el criterio:**

- [ContractPolicyTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ContractPolicyTest.kt) · `pagesMergeByRealIdentityAndResetForNewFilter` · PASS JVM

**Falta:** Añadir primera/siguiente página de miembros, cursor enviado, orden/deduplicación y final de lista.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-MEM-02-AC4

**Estado técnico: PARCIAL.**
**Condición original:** Cuenta ajena recibe 403 sin exponer miembros ni permitir elegir rival.

**Tareas:** `MO-MEM-02-T4`, `MO-MEM-02-T5` y `MO-MEM-02-T4-AC4` en [tasks.md](../tasks.md).

**Lo comprobado:** 403 de lectura live no miembro y consumidor; sin sesión no hay tráfico; rival exige miembro visible y otro usuario.

- [ListMembersSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/groups/members/ListMembersSecurityTest.kt) · `MEM_02_forbiddenMemberReadPreservesExistingSession` · PASS JVM
- [ListMembersSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/groups/members/ListMembersSecurityTest.kt) · `MEM_02_missingSessionPreventsListMembersRequest` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `rivalMustBeVisibleAndCannotBeSelf` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `changingAccountClearsGroupDraftFiltersMessagesAndCredentials` · PASS JVM

**Proveedor, registro previo:** No miembro: miembros 403; no cubre escrituras, otros roles o UI. Ver catálogo de evidencia del informe global.

**Falta:** Falta escenario UI autenticado con cuenta ajena que compruebe lista vacía y selección de rival bloqueada después del 403.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-MEM-02-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-MEM-02-T4`, `MO-MEM-02-T5` y `MO-MEM-02-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [ListMembersValidationTest](../../../app/src/test/java/com/pactogg/app/feature/groups/members/ListMembersValidationTest.kt) · `MEM_02_rejectsOversizedMemberSearch` · PASS JVM
- [ListMembersApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/members/ListMembersApiTest.kt) · `MEM_02_sendsMemberSearchWithinSelectedGroup` · PASS JVM
- [ListMembersSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/groups/members/ListMembersSecurityTest.kt) · `MEM_02_forbiddenMemberReadPreservesExistingSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla. Falta accesibilidad del recorrido específico.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado: implementación local presente; recorrido positivo real y controles locales correctos; aceptación integral pendiente. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-MEM-02-AC1: Lista muestra miembros/rol/saldo reales en orden del servidor.
- [ ] MO-MEM-02-AC2: Prefijo encuentra el miembro esperado y respuesta tardía no contamina otro grupo.
- [ ] MO-MEM-02-AC3: limit=1 y cursor cargan siguiente página sin duplicados.
- [ ] MO-MEM-02-AC4: Cuenta ajena recibe 403 sin exponer miembros ni permitir elegir rival.
- [ ] MO-MEM-02-ACQ: controles y evidencia del mismo candidato verificados.
- [ ] Bloqueantes específicos de sección 7 resueltos para las tareas afectadas.
- [ ] Prueba real del proveedor distinguida de fixtures; no se hereda PASS del backend.
- [ ] Revisión humana y aceptación de implementación registradas por separado.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación solicitada | Usuario | 2026-11-04 | Operación incluida en Sistema.docx; no prueba funcional |
| Aceptación móvil | Pendiente | Pendiente | No inferir por existencia de documentos |


[Resultado real y candidato](../../../docs/integracion/integracion-social-2026-11-04.md): caso positivo comprobado; no se cierran casillas integrales que contienen escenarios restantes. Datos y límites de la cuenta auxiliar/recorrido UI distinguidos de fixtures.
