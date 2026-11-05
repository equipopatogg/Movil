# Checklist — MO-CON-01

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-CON-01-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Contexto de grupo válido, fianza recibida y formulario válido producen un POST y resumen del contrato real open.

**Tareas:** `MO-CON-01-T4`, `MO-CON-01-T5` y `MO-CON-01-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Grupo/target/fianza se envían en fixtures; la creación requiere revisión y muestra estado de contrato confirmado en el ViewModel.

- [CreateChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/create/CreateChallengeApiTest.kt) · `CON_01_sendsGroupAndTargetForChallengeCreation` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `directedChallengeSerializesRealTarget` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `registrationGroupContractEvidenceTournamentsAndLogoutUseRealWireContract` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `challengeCreationRequiresReviewAndResetsDraftAfterConfirmation` · PASS JVM

**Pendiente/límite:** Falta recorrido UI del formulario completo con settings reales y asserts completos del resumen open del candidato actual.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-CON-01-AC2

**Estado técnico: PARCIAL.**
**Condición original:** UTC, fecha vencida, límites, fianza inválida y moderación rechazada se muestran sin éxito ficticio.

**Tareas:** `MO-CON-01-T4`, `MO-CON-01-T5` y `MO-CON-01-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Se rechazan fecha pasada/inválida, recompensa monetaria y fianza no devuelta por el grupo. Ampliación actual: CON_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity, validationAndModerationErrorsKeepDraftsEditableWithoutSuccessOrBalanceChanges.

- [CreateChallengeValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/create/CreateChallengeValidationTest.kt) · `CON_01_rejectsPastDeadlineAndMonetaryReward` · PASS JVM
- [InputRulesTest](../../../app/src/test/java/com/pactogg/app/integration/InputRulesTest.kt) · `challengeRejectsUnreturnedBondsPastDatesAndCurrency` · PASS JVM
- [CreateChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/create/CreateChallengeApiTest.kt) · `CON_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `validationAndModerationErrorsKeepDraftsEditableWithoutSuccessOrBalanceChanges` · PASS JVM

**Pendiente/límite:** CONTENT_REJECTED/MODERATION_UNAVAILABLE conservan borrador sin éxito. Faltan conversión local→UTC/DST y fronteras completas.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-CON-01-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Sin grupo/settings no se envía; member exige selección real y excluye usuario propio; voz sigue oculta y aceptar corresponde a MO-CON-06.

**Tareas:** `MO-CON-01-T4`, `MO-CON-01-T5` y `MO-CON-01-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Rival debe estar en la lista visible y no ser el propio usuario; target se serializa; revisión previa requerida.

- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `rivalMustBeVisibleAndCannotBeSelf` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `directedChallengeSerializesRealTarget` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `challengeCreationRequiresReviewAndResetsDraftAfterConfirmation` · PASS JVM

**Pendiente/límite:** Faltan casos sin grupo y sin settings, bloqueo de voz y separación de aceptar frente a crear en UI; inspección de código no sustituye esos casos.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-CON-01-AC4

**Estado técnico: PARCIAL.**
**Condición original:** Respuesta perdida no provoca reintento automático ni duplicación deliberada.

**Tareas:** `MO-CON-01-T4`, `MO-CON-01-T5` y `MO-CON-01-T4-AC4` en [tasks.md](../tasks.md).

**Lo comprobado:** 503 no reintenta la creación; guard busy evita repetir escritura; se prueba no replay de POST tras 401 en escritura compartida. Ampliación actual: CON_01_lostResponseIsNotReplayedOrReportedAsSuccess.

- [CreateChallengeSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/create/CreateChallengeSecurityTest.kt) · `CON_01_serviceFailureDoesNotRetryCreateChallenge` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `unauthorizedWriteRenewsButNeverReplaysPost` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `failedWriteRetainsInputAndNeverRetries` · PASS JVM
- [CreateChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/create/CreateChallengeApiTest.kt) · `CON_01_lostResponseIsNotReplayedOrReportedAsSuccess` · PASS JVM

**Pendiente/límite:** Desconexión tras recepción de POST no repite creación ni confirma éxito; falta recorrido de recuperación en Compose.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-CON-01-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente

**Tareas:** `MO-CON-01-T4`, `MO-CON-01-T5` y `MO-CON-01-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [CreateChallengeValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/create/CreateChallengeValidationTest.kt) · `CON_01_rejectsPastDeadlineAndMonetaryReward` · PASS JVM
- [CreateChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/create/CreateChallengeApiTest.kt) · `CON_01_sendsGroupAndTargetForChallengeCreation` · PASS JVM
- [CreateChallengeSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/create/CreateChallengeSecurityTest.kt) · `CON_01_forbiddenCreationPreservesExistingSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-CON-01

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-CON-01-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Contexto de grupo válido, fianza recibida y formulario válido producen un POST y resumen del contrato real open.

**Tareas:** `MO-CON-01-T4`, `MO-CON-01-T5` y `MO-CON-01-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Grupo/target/fianza se envían en fixtures; la creación requiere revisión y muestra estado de contrato confirmado en el ViewModel.

- [CreateChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/create/CreateChallengeApiTest.kt) · `CON_01_sendsGroupAndTargetForChallengeCreation` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `directedChallengeSerializesRealTarget` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `registrationGroupContractEvidenceTournamentsAndLogoutUseRealWireContract` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `challengeCreationRequiresReviewAndResetsDraftAfterConfirmation` · PASS JVM

**Falta:** Falta recorrido UI del formulario completo con settings reales y asserts completos del resumen open del candidato actual.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-CON-01-AC2

**Estado técnico: PARCIAL.**
**Condición original:** UTC, fecha vencida, límites, fianza inválida y moderación rechazada se muestran sin éxito ficticio.

**Tareas:** `MO-CON-01-T4`, `MO-CON-01-T5` y `MO-CON-01-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Se rechazan fecha pasada/inválida, recompensa monetaria y fianza no devuelta por el grupo.

- [CreateChallengeValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/create/CreateChallengeValidationTest.kt) · `CON_01_rejectsPastDeadlineAndMonetaryReward` · PASS JVM
- [InputRulesTest](../../../app/src/test/java/com/pactogg/app/integration/InputRulesTest.kt) · `challengeRejectsUnreturnedBondsPastDatesAndCurrency` · PASS JVM

**Falta:** Faltan conversión local→UTC/DST, fronteras completas de campos y CONTENT_REJECTED/errores UI del servicio de moderación.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-CON-01-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Sin grupo/settings no se envía; member exige selección real y excluye usuario propio; voz sigue oculta y aceptar corresponde a MO-CON-06.

**Tareas:** `MO-CON-01-T4`, `MO-CON-01-T5` y `MO-CON-01-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Rival debe estar en la lista visible y no ser el propio usuario; target se serializa; revisión previa requerida.

- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `rivalMustBeVisibleAndCannotBeSelf` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `directedChallengeSerializesRealTarget` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `challengeCreationRequiresReviewAndResetsDraftAfterConfirmation` · PASS JVM

**Falta:** Faltan casos sin grupo y sin settings, bloqueo de voz y separación de aceptar frente a crear en UI; inspección de código no sustituye esos casos.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-CON-01-AC4

**Estado técnico: PARCIAL.**
**Condición original:** Respuesta perdida no provoca reintento automático ni duplicación deliberada.

**Tareas:** `MO-CON-01-T4`, `MO-CON-01-T5` y `MO-CON-01-T4-AC4` en [tasks.md](../tasks.md).

**Lo comprobado:** 503 no reintenta la creación; guard busy evita repetir escritura; se prueba no replay de POST tras 401 en escritura compartida.

- [CreateChallengeSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/create/CreateChallengeSecurityTest.kt) · `CON_01_serviceFailureDoesNotRetryCreateChallenge` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `unauthorizedWriteRenewsButNeverReplaysPost` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `failedWriteRetainsInputAndNeverRetries` · PASS JVM

**Falta:** Falta pérdida de respuesta después de que el servidor recibió el POST de creación; 503 y 401 no equivalen a respuesta perdida.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-CON-01-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente

**Tareas:** `MO-CON-01-T4`, `MO-CON-01-T5` y `MO-CON-01-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [CreateChallengeValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/create/CreateChallengeValidationTest.kt) · `CON_01_rejectsPastDeadlineAndMonetaryReward` · PASS JVM
- [CreateChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/create/CreateChallengeApiTest.kt) · `CON_01_sendsGroupAndTargetForChallengeCreation` · PASS JVM
- [CreateChallengeSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/create/CreateChallengeSecurityTest.kt) · `CON_01_forbiddenCreationPreservesExistingSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado vigente: **Parcial: creación real comprobada tras corrección AWS; criterios restantes pendientes**. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-CON-01-AC1: Contexto de grupo válido, fianza recibida y formulario válido producen un POST y resumen del contrato real open.
- [ ] MO-CON-01-AC2: UTC, fecha vencida, límites, fianza inválida y moderación rechazada se muestran sin éxito ficticio.
- [ ] MO-CON-01-AC3: Sin grupo/settings no se envía; member exige selección real y excluye usuario propio; voz sigue oculta y aceptar corresponde a MO-CON-06.
- [ ] MO-CON-01-AC4: Respuesta perdida no provoca reintento automático ni duplicación deliberada.
- [ ] MO-CON-01-ACQ: controles del candidato y evidencia válidos.
- [ ] Entradas/bloqueantes de sección 7 resueltos para las tareas afectadas.
- [ ] Revisión humana de spec y aceptación funcional diferenciadas.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Pendiente | Por designar | Pendiente | Documentación preparada, sin pruebas de integración ejecutadas |


## Avance verificado local — 2026-11-04

Implementación de consumidor presente. [Evidencia del candidato](../../../docs/integracion/verificacion-consumidor-2026-11-04.md): debug, 24 JVM, Lint/Detekt y Gitleaks correctos; androidTest compilado, no ejecutado. No marcar los AC integrales como PASS: faltan proveedor/cuentas autorizadas, ejecución Android y aceptación aplicable. La tabla histórica NO_EJECUTADO no significa ausencia de código actual.


## Verificación física posterior — 2026-11-04

Bloqueado por proveedor: 503 MODERATION_UNAVAILABLE al crear contrato; request_id y datos QA en informe. No aceptar ni simular éxito. [Informe real](../../../docs/integracion/verificacion-telefono-2026-11-04.md). No cerrar casillas integrales por un caso positivo ni transferir el PASS de fixtures a proveedor.


## Preparación para commits — 2026-11-04

Se conserva el resultado real del candidato físico y se prepara incorporación por ramas dependientes. Commit y compilación no sustituyen aceptación de todos los AC; las casillas sin evidencia completa siguen abiertas. El historial inferior no significa que la implementación actual sea solo documental. Registro de ramas y comprobaciones en docs/preparacion/entrega-por-ramas.md.


Actualización posterior: la reprueba física de reto/enlace/foto fue positiva tras corrección AWS; el bloqueo 503 inferior es histórico. [Informe](../../../docs/integracion/verificacion-telefono-2026-11-04.md). Ajuste actual: lectura auxiliar de estado posterior a evidencia; no implica aceptación del expediente completo.
