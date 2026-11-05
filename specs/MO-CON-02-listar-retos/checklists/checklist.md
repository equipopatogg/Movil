# Checklist — MO-CON-02

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-CON-02-AC1

**Estado técnico: PARCIAL.**
**Condición original:** status/game/q/mine se serializan y muestran resultados reales correctos.

**Tareas:** `MO-CON-02-T4`, `MO-CON-02-T5` y `MO-CON-02-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Los cuatro filtros status/game/q/mine se verifican en la URL y la respuesta conserva versión/retador.

- [ListChallengesApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/list/ListChallengesApiTest.kt) · `CON_02_sendsOwnChallengesAndStatusFilters` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `membersPrefixAndBoardFiltersFollowCursorContract` · PASS JVM

**Pendiente/límite:** Falta comprobar representación UI y filtrado con datos reales suficientes; fixture valida consumidor, no filtrado del proveedor.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-CON-02-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Tras aceptar, refresco refleja in_progress; las acciones locales invalidan estado anterior.

**Tareas:** `MO-CON-02-T4`, `MO-CON-02-T5` y `MO-CON-02-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Aceptar requiere confirmación, envía versión y consume in_progress/my_points; estado deja de estar marcado como incierto.

- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `acceptRefreshesThenRequiresExplicitConfirmationAndServerBalance` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `acceptSendsObservedVersionAndUsesConfirmedBalance` · PASS JVM

**Pendiente/límite:** Falta assert explícito de invalidación/recarga del tablero y su fila in_progress después de aceptar; no deducirlo del saldo.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-CON-02-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Filtro inválido/usuario ajeno presenta 422/403; cambios de filtro reinician cursor.

**Tareas:** `MO-CON-02-T4`, `MO-CON-02-T5` y `MO-CON-02-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Filtro inválido falla antes de red; 403 se rechaza; cambiar filtros vacía estado de lista. Ampliación actual: CON_02_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity.

- [ListChallengesValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/list/ListChallengesValidationTest.kt) · `CON_02_rejectsUnknownBoardStatus` · PASS JVM
- [ListChallengesSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/list/ListChallengesSecurityTest.kt) · `CON_02_forbiddenBoardReadPreservesExistingSession` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `membersBoardAndFiltersDoNotMixPages` · PASS JVM
- [ListChallengesApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/list/ListChallengesApiTest.kt) · `CON_02_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM

Apoyo indirecto (no sustituye la condición): `com.pactogg.app.feature.connected.presentation.ContractPolicyTest#pagesMergeByRealIdentityAndResetForNewFilter`.

**Pendiente/límite:** 422 de tablero comprobado; falta filtros rápidos, cursor y filas en Compose; evidencia live anterior solo acredita lectura sin pertenencia.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-CON-02-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-CON-02-T4`, `MO-CON-02-T5` y `MO-CON-02-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [ListChallengesValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/list/ListChallengesValidationTest.kt) · `CON_02_rejectsUnknownBoardStatus` · PASS JVM
- [ListChallengesApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/list/ListChallengesApiTest.kt) · `CON_02_sendsOwnChallengesAndStatusFilters` · PASS JVM
- [ListChallengesSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/list/ListChallengesSecurityTest.kt) · `CON_02_forbiddenBoardReadPreservesExistingSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-CON-02

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-CON-02-AC1

**Estado técnico: PARCIAL.**
**Condición original:** status/game/q/mine se serializan y muestran resultados reales correctos.

**Tareas:** `MO-CON-02-T4`, `MO-CON-02-T5` y `MO-CON-02-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Los cuatro filtros status/game/q/mine se verifican en la URL y la respuesta conserva versión/retador.

- [ListChallengesApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/list/ListChallengesApiTest.kt) · `CON_02_sendsOwnChallengesAndStatusFilters` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `membersPrefixAndBoardFiltersFollowCursorContract` · PASS JVM

**Falta:** Falta comprobar representación UI y filtrado con datos reales suficientes; fixture valida consumidor, no filtrado del proveedor.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-CON-02-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Tras aceptar, refresco refleja in_progress; las acciones locales invalidan estado anterior.

**Tareas:** `MO-CON-02-T4`, `MO-CON-02-T5` y `MO-CON-02-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Aceptar requiere confirmación, envía versión y consume in_progress/my_points; estado deja de estar marcado como incierto.

- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `acceptRefreshesThenRequiresExplicitConfirmationAndServerBalance` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `acceptSendsObservedVersionAndUsesConfirmedBalance` · PASS JVM

**Falta:** Falta assert explícito de invalidación/recarga del tablero y su fila in_progress después de aceptar; no deducirlo del saldo.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-CON-02-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Filtro inválido/usuario ajeno presenta 422/403; cambios de filtro reinician cursor.

**Tareas:** `MO-CON-02-T4`, `MO-CON-02-T5` y `MO-CON-02-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Filtro inválido falla antes de red; 403 se rechaza; cambiar filtros vacía estado de lista.

- [ListChallengesValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/list/ListChallengesValidationTest.kt) · `CON_02_rejectsUnknownBoardStatus` · PASS JVM
- [ListChallengesSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/list/ListChallengesSecurityTest.kt) · `CON_02_forbiddenBoardReadPreservesExistingSession` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `membersBoardAndFiltersDoNotMixPages` · PASS JVM

**Apoyo indirecto; no demuestra por sí solo el criterio:**

- [ContractPolicyTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ContractPolicyTest.kt) · `pagesMergeByRealIdentityAndResetForNewFilter` · PASS JVM

**Proveedor, registro previo:** No miembro: tablero 403; no cubre escrituras, otros roles o UI. Ver catálogo de evidencia del informe global.

**Falta:** Faltan fixture 422 del tablero, cursor/filas visibles y filtros rápidos; live solo prueba no miembro para lectura.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-CON-02-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-CON-02-T4`, `MO-CON-02-T5` y `MO-CON-02-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [ListChallengesValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/list/ListChallengesValidationTest.kt) · `CON_02_rejectsUnknownBoardStatus` · PASS JVM
- [ListChallengesApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/list/ListChallengesApiTest.kt) · `CON_02_sendsOwnChallengesAndStatusFilters` · PASS JVM
- [ListChallengesSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/list/ListChallengesSecurityTest.kt) · `CON_02_forbiddenBoardReadPreservesExistingSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla. Falta accesibilidad del recorrido específico.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado: implementación local presente; recorrido positivo real y controles locales correctos; aceptación integral pendiente. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-CON-02-AC1: status/game/q/mine se serializan y muestran resultados reales correctos.
- [ ] MO-CON-02-AC2: Tras aceptar, refresco refleja in_progress; las acciones locales invalidan estado anterior.
- [ ] MO-CON-02-AC3: Filtro inválido/usuario ajeno presenta 422/403; cambios de filtro reinician cursor.
- [ ] MO-CON-02-ACQ: controles y evidencia del mismo candidato verificados.
- [ ] Bloqueantes específicos de sección 7 resueltos para las tareas afectadas.
- [ ] Prueba real del proveedor distinguida de fixtures; no se hereda PASS del backend.
- [ ] Revisión humana y aceptación de implementación registradas por separado.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación solicitada | Usuario | 2026-11-04 | Operación incluida en Sistema.docx; no prueba funcional |
| Aceptación móvil | Pendiente | Pendiente | No inferir por existencia de documentos |


[Resultado real y candidato](../../../docs/integracion/integracion-social-2026-11-04.md): caso positivo comprobado; no se cierran casillas integrales que contienen escenarios restantes. Datos y límites de la cuenta auxiliar/recorrido UI distinguidos de fixtures.
