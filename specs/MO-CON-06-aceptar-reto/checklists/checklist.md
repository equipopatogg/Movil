# Checklist — MO-CON-06

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-CON-06-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Miembro distinto del retador acepta reto de grupo, muestra in_progress/my_points recibidos y refresca contexto.

**Tareas:** `MO-CON-06-T4`, `MO-CON-06-T5` y `MO-CON-06-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Elegibilidad de reto de grupo, confirmación y versión/saldo de respuesta comprobados en reglas/HTTP/estado.

- [ContractPolicyTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ContractPolicyTest.kt) · `onlyEligibleMemberWithKnownBalanceAndVersionCanAccept` · PASS JVM
- [AcceptChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeApiTest.kt) · `CON_06_sendsVersionWhenAcceptingChallenge` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `acceptSendsObservedVersionAndUsesConfirmedBalance` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `acceptRefreshesThenRequiresExplicitConfirmationAndServerBalance` · PASS JVM

**Pendiente/límite:** El recorrido positivo histórico fue un reto dirigido; falta aceptación real de reto de grupo y refresco visual completo del contexto.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-CON-06-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Retador no puede aceptar su propio reto y se maneja 409 sin descontar puntos.

**Tareas:** `MO-CON-06-T4`, `MO-CON-06-T5` y `MO-CON-06-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Retador no es elegible; el consumidor procesa 409 sin replay y conserva sesión. Ampliación actual: CON_06_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity, insufficientPointsOrForbiddenActionNeverDeductsBalanceOrConfirmsSuccess.

- [AcceptChallengeValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeValidationTest.kt) · `CON_06_rejectsOwnChallengeAndInsufficientBalance` · PASS JVM
- [ContractPolicyTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ContractPolicyTest.kt) · `onlyEligibleMemberWithKnownBalanceAndVersionCanAccept` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `acceptErrorsNeverRetryOrEraseSession` · PASS JVM
- [AcceptChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeApiTest.kt) · `CON_06_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `insufficientPointsOrForbiddenActionNeverDeductsBalanceOrConfirmsSuccess` · PASS JVM

**Pendiente/límite:** CANNOT_ACCEPT_OWN_CONTRACT 409 y error de estado conservan saldo sin éxito en consumidor; falta intento real del retador y Compose.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-CON-06-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Segunda acción o versión vieja muestra 409 y no repite aceptación automáticamente.

**Tareas:** `MO-CON-06-T4`, `MO-CON-06-T5` y `MO-CON-06-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Se envía versión observada; 409 no dispara reintento; acción incierta exige refresco/nueva decisión. Ampliación actual: insufficientPointsOrForbiddenActionNeverDeductsBalanceOrConfirmsSuccess.

- [AcceptChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeApiTest.kt) · `CON_06_sendsVersionWhenAcceptingChallenge` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `acceptErrorsNeverRetryOrEraseSession` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `uncertainAcceptDisablesReplayUntilFreshDetail` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `insufficientPointsOrForbiddenActionNeverDeductsBalanceOrConfirmsSuccess` · PASS JVM

**Pendiente/límite:** 409 de estado conserva saldo y exige detalle fresco; no se ejecutaron dos aceptaciones reales concurrentes contra proveedor.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-CON-06-AC4

**Estado técnico: PARCIAL.**
**Condición original:** Saldo insuficiente produce 402 sin modificar saldo confirmado ni mostrar deuda.

**Tareas:** `MO-CON-06-T4`, `MO-CON-06-T5` y `MO-CON-06-T4-AC4` en [tasks.md](../tasks.md).

**Lo comprobado:** Saldo insuficiente impide elegibilidad y fixture 402 se procesa como INSUFFICIENT_POINTS sin replay. Ampliación actual: CON_06_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity, insufficientPointsOrForbiddenActionNeverDeductsBalanceOrConfirmsSuccess.

- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `acceptErrorsNeverRetryOrEraseSession` · PASS JVM
- [AcceptChallengeValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeValidationTest.kt) · `CON_06_rejectsOwnChallengeAndInsufficientBalance` · PASS JVM
- [AcceptChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeApiTest.kt) · `CON_06_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `insufficientPointsOrForbiddenActionNeverDeductsBalanceOrConfirmsSuccess` · PASS JVM

**Pendiente/límite:** 402 conserva saldo de grupo y contrato, sin éxito y con acción invalidada en ViewModel; falta recorrido Compose.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-CON-06-AC5

**Estado técnico: PARCIAL.**
**Condición original:** Reto dirigido a otro usuario produce 403 sin habilitar acción ni revelar datos adicionales.

**Tareas:** `MO-CON-06-T4`, `MO-CON-06-T5` y `MO-CON-06-T4-AC5` en [tasks.md](../tasks.md).

**Lo comprobado:** Otro destinatario no es elegible; respuesta 403 no expone mensaje privado ni reintenta. Ampliación actual: CON_06_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity, insufficientPointsOrForbiddenActionNeverDeductsBalanceOrConfirmsSuccess.

- [ContractPolicyTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ContractPolicyTest.kt) · `directedActionsAreExclusiveAndRejectionDoesNotNeedFunds` · PASS JVM
- [AcceptChallengeSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeSecurityTest.kt) · `CON_06_forbiddenAcceptPreservesExistingSession` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `acceptErrorsNeverRetryOrEraseSession` · PASS JVM
- [AcceptChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeApiTest.kt) · `CON_06_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `insufficientPointsOrForbiddenActionNeverDeductsBalanceOrConfirmsSuccess` · PASS JVM

**Pendiente/límite:** 403 NOT_CONTRACT_TARGET conserva contexto/saldo en consumidor; falta intento real de tercero y su pantalla.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-CON-06-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-CON-06-T4`, `MO-CON-06-T5` y `MO-CON-06-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [AcceptChallengeValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeValidationTest.kt) · `CON_06_rejectsOwnChallengeAndInsufficientBalance` · PASS JVM
- [AcceptChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeApiTest.kt) · `CON_06_sendsVersionWhenAcceptingChallenge` · PASS JVM
- [AcceptChallengeSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeSecurityTest.kt) · `CON_06_forbiddenAcceptPreservesExistingSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-CON-06

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-CON-06-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Miembro distinto del retador acepta reto de grupo, muestra in_progress/my_points recibidos y refresca contexto.

**Tareas:** `MO-CON-06-T4`, `MO-CON-06-T5` y `MO-CON-06-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Elegibilidad de reto de grupo, confirmación y versión/saldo de respuesta comprobados en reglas/HTTP/estado.

- [ContractPolicyTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ContractPolicyTest.kt) · `onlyEligibleMemberWithKnownBalanceAndVersionCanAccept` · PASS JVM
- [AcceptChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeApiTest.kt) · `CON_06_sendsVersionWhenAcceptingChallenge` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `acceptSendsObservedVersionAndUsesConfirmedBalance` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `acceptRefreshesThenRequiresExplicitConfirmationAndServerBalance` · PASS JVM

**Falta:** El recorrido positivo histórico fue un reto dirigido; falta aceptación real de reto de grupo y refresco visual completo del contexto.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-CON-06-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Retador no puede aceptar su propio reto y se maneja 409 sin descontar puntos.

**Tareas:** `MO-CON-06-T4`, `MO-CON-06-T5` y `MO-CON-06-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Retador no es elegible; el consumidor procesa 409 sin replay y conserva sesión.

- [AcceptChallengeValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeValidationTest.kt) · `CON_06_rejectsOwnChallengeAndInsufficientBalance` · PASS JVM
- [ContractPolicyTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ContractPolicyTest.kt) · `onlyEligibleMemberWithKnownBalanceAndVersionCanAccept` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `acceptErrorsNeverRetryOrEraseSession` · PASS JVM

**Falta:** Falta 409 específico por reto propio y assert del saldo/UI tras ese error; no inferirlo del rechazo genérico de transporte.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-CON-06-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Segunda acción o versión vieja muestra 409 y no repite aceptación automáticamente.

**Tareas:** `MO-CON-06-T4`, `MO-CON-06-T5` y `MO-CON-06-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Se envía versión observada; 409 no dispara reintento; acción incierta exige refresco/nueva decisión.

- [AcceptChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeApiTest.kt) · `CON_06_sendsVersionWhenAcceptingChallenge` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `acceptErrorsNeverRetryOrEraseSession` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `uncertainAcceptDisablesReplayUntilFreshDetail` · PASS JVM

**Falta:** Faltan dos aceptaciones concurrentes/segunda acción con versión vieja en proveedor y comprobación UI del 409.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-CON-06-AC4

**Estado técnico: PARCIAL.**
**Condición original:** Saldo insuficiente produce 402 sin modificar saldo confirmado ni mostrar deuda.

**Tareas:** `MO-CON-06-T4`, `MO-CON-06-T5` y `MO-CON-06-T4-AC4` en [tasks.md](../tasks.md).

**Lo comprobado:** Saldo insuficiente impide elegibilidad y fixture 402 se procesa como INSUFFICIENT_POINTS sin replay.

- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `acceptErrorsNeverRetryOrEraseSession` · PASS JVM
- [AcceptChallengeValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeValidationTest.kt) · `CON_06_rejectsOwnChallengeAndInsufficientBalance` · PASS JVM

**Falta:** Falta 402 a través del ViewModel/Compose con saldo confirmado intacto y sin deuda visible; timeout de aceptación es otro escenario.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-CON-06-AC5

**Estado técnico: PARCIAL.**
**Condición original:** Reto dirigido a otro usuario produce 403 sin habilitar acción ni revelar datos adicionales.

**Tareas:** `MO-CON-06-T4`, `MO-CON-06-T5` y `MO-CON-06-T4-AC5` en [tasks.md](../tasks.md).

**Lo comprobado:** Otro destinatario no es elegible; respuesta 403 no expone mensaje privado ni reintenta.

- [ContractPolicyTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ContractPolicyTest.kt) · `directedActionsAreExclusiveAndRejectionDoesNotNeedFunds` · PASS JVM
- [AcceptChallengeSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeSecurityTest.kt) · `CON_06_forbiddenAcceptPreservesExistingSession` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `acceptErrorsNeverRetryOrEraseSession` · PASS JVM

**Falta:** Falta rechazo real al intentar aceptar con un tercero y su UI, distinto de los 403 live de lectura de grupos.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-CON-06-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-CON-06-T4`, `MO-CON-06-T5` y `MO-CON-06-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [AcceptChallengeValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeValidationTest.kt) · `CON_06_rejectsOwnChallengeAndInsufficientBalance` · PASS JVM
- [AcceptChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeApiTest.kt) · `CON_06_sendsVersionWhenAcceptingChallenge` · PASS JVM
- [AcceptChallengeSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/accept/AcceptChallengeSecurityTest.kt) · `CON_06_forbiddenAcceptPreservesExistingSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla. Falta accesibilidad del recorrido específico.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado: implementación local presente; recorrido positivo real y controles locales correctos; aceptación integral pendiente. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-CON-06-AC1: Miembro distinto del retador acepta reto de grupo, muestra in_progress/my_points recibidos y refresca contexto.
- [ ] MO-CON-06-AC2: Retador no puede aceptar su propio reto y se maneja 409 sin descontar puntos.
- [ ] MO-CON-06-AC3: Segunda acción o versión vieja muestra 409 y no repite aceptación automáticamente.
- [ ] MO-CON-06-AC4: Saldo insuficiente produce 402 sin modificar saldo confirmado ni mostrar deuda.
- [ ] MO-CON-06-AC5: Reto dirigido a otro usuario produce 403 sin habilitar acción ni revelar datos adicionales.
- [ ] MO-CON-06-ACQ: controles y evidencia del mismo candidato verificados.
- [ ] Bloqueantes específicos de sección 7 resueltos para las tareas afectadas.
- [ ] Prueba real del proveedor distinguida de fixtures; no se hereda PASS del backend.
- [ ] Revisión humana y aceptación de implementación registradas por separado.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación solicitada | Usuario | 2026-11-04 | Operación incluida en Sistema.docx; no prueba funcional |
| Aceptación móvil | Pendiente | Pendiente | No inferir por existencia de documentos |


[Resultado real y candidato](../../../docs/integracion/integracion-social-2026-11-04.md): caso positivo comprobado; no se cierran casillas integrales que contienen escenarios restantes. Datos y límites de la cuenta auxiliar/recorrido UI distinguidos de fixtures.
