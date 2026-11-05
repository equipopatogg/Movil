# Checklist — MO-EVI-02

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-EVI-02-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Enlace y foto válidos usan serialización/content-type correctos y muestran solo evidencia confirmada por 201.

**Tareas:** `MO-EVI-02-T4`, `MO-EVI-02-T5` y `MO-EVI-02-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Enlace y multipart foto/type se envían a evidencia; 201 approved y refresco de estado se comprueban con fixtures.

- [SendEvidenceApiTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceApiTest.kt) · `EVI_02_sendsAllowedEvidenceLink` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `registrationGroupContractEvidenceTournamentsAndLogoutUseRealWireContract` · PASS JVM
- [EvidenceConfirmationTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/EvidenceConfirmationTest.kt) · `confirmedUploadRefreshesActualStatusWithoutInventingIt` · PASS JVM

**Pendiente/límite:** Faltan asserts de Content-Type interno de foto y UI completa de ambos tipos sobre el candidato; evidencia real anterior se conserva como histórica.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-EVI-02-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Selector cancelado, archivo vacío/grande/tipo inválido no envían; no existen cámara ni carga de video.

**Tareas:** `MO-EVI-02-T4`, `MO-EVI-02-T5` y `MO-EVI-02-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Hosts suplantados/credenciales/HTTP/puertos ajenos y foto vacía se rechazan.

- [SendEvidenceValidationTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceValidationTest.kt) · `EVI_02_rejectsLookalikeDomainAndEmptyPhoto` · PASS JVM
- [InputRulesTest](../../../app/src/test/java/com/pactogg/app/integration/InputRulesTest.kt) · `evidenceRejectsSpoofedHostsCredentialsAndInsecureUrls` · PASS JVM

**Pendiente/límite:** Faltan cancelación del selector, archivo grande, MIME inválido y ausencia de cámara/video en la UI instalada.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-EVI-02-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Moderación rechazada, límite 409, autorización 403 y servicio 503 presentan acciones correctas sin falsos aprobados.

**Tareas:** `MO-EVI-02-T4`, `MO-EVI-02-T5` y `MO-EVI-02-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** 403/503 y éxito malformado no confirman evidencia; bytes de foto se borran incluso con error. Ampliación actual: EVI_02_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity, evidenceModerationAndLimitErrorsKeepLinkWithoutInventingApprovedEvidence.

- [SendEvidenceSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceSecurityTest.kt) · `EVI_02_forbiddenEvidencePreservesExistingSession` · PASS JVM
- [SendEvidenceSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceSecurityTest.kt) · `EVI_02_serviceFailureDoesNotRetrySendEvidence` · PASS JVM
- [SendEvidenceSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceSecurityTest.kt) · `EVI_02_malformedSendEvidenceSuccessIsRejected` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `photoBytesAreErasedEvenWhenUploadFails` · PASS JVM
- [SendEvidenceApiTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceApiTest.kt) · `EVI_02_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `evidenceModerationAndLimitErrorsKeepLinkWithoutInventingApprovedEvidence` · PASS JVM

**Pendiente/límite:** CONTENT_REJECTED/EVIDENCE_LIMIT_REACHED preservan enlace y no inventan evidencia; falta recorrido Compose y moderación de foto.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-EVI-02-AC4

**Estado técnico: PARCIAL.**
**Condición original:** Sin contrato autorizado no se abre envío; pérdida de respuesta no repite la subida.

**Tareas:** `MO-EVI-02-T4`, `MO-EVI-02-T5` y `MO-EVI-02-T4-AC4` en [tasks.md](../tasks.md).

**Lo comprobado:** Sin sesión no hay tráfico; si falla GET después de una subida confirmada se conserva evidencia y no se vuelve a subir. Ampliación actual: EVI_02_lostResponseIsNotReplayedOrReportedAsSuccess.

- [SendEvidenceSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceSecurityTest.kt) · `EVI_02_missingSessionPreventsSendEvidenceRequest` · PASS JVM
- [EvidenceConfirmationTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/EvidenceConfirmationTest.kt) · `failedReadKeepsConfirmedEvidenceAndMarksStatusStaleWithoutAnotherUpload` · PASS JVM
- [EvidenceConfirmationTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/EvidenceConfirmationTest.kt) · `lateReadCannotRestoreDataAfterSessionWasCleared` · PASS JVM
- [SendEvidenceApiTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceApiTest.kt) · `EVI_02_lostResponseIsNotReplayedOrReportedAsSuccess` · PASS JVM

**Pendiente/límite:** POST con respuesta perdida no se repite ni confirma evidencia; falta apertura bloqueada por contrato no autorizado en Compose.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-EVI-02-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente

**Tareas:** `MO-EVI-02-T4`, `MO-EVI-02-T5` y `MO-EVI-02-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [SendEvidenceValidationTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceValidationTest.kt) · `EVI_02_rejectsLookalikeDomainAndEmptyPhoto` · PASS JVM
- [SendEvidenceApiTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceApiTest.kt) · `EVI_02_sendsAllowedEvidenceLink` · PASS JVM
- [SendEvidenceSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceSecurityTest.kt) · `EVI_02_forbiddenEvidencePreservesExistingSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-EVI-02

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-EVI-02-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Enlace y foto válidos usan serialización/content-type correctos y muestran solo evidencia confirmada por 201.

**Tareas:** `MO-EVI-02-T4`, `MO-EVI-02-T5` y `MO-EVI-02-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Enlace y multipart foto/type se envían a evidencia; 201 approved y refresco de estado se comprueban con fixtures.

- [SendEvidenceApiTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceApiTest.kt) · `EVI_02_sendsAllowedEvidenceLink` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `registrationGroupContractEvidenceTournamentsAndLogoutUseRealWireContract` · PASS JVM
- [EvidenceConfirmationTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/EvidenceConfirmationTest.kt) · `confirmedUploadRefreshesActualStatusWithoutInventingIt` · PASS JVM

**Falta:** Faltan asserts de Content-Type interno de foto y UI completa de ambos tipos sobre el candidato; evidencia real anterior se conserva como histórica.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-EVI-02-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Selector cancelado, archivo vacío/grande/tipo inválido no envían; no existen cámara ni carga de video.

**Tareas:** `MO-EVI-02-T4`, `MO-EVI-02-T5` y `MO-EVI-02-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Hosts suplantados/credenciales/HTTP/puertos ajenos y foto vacía se rechazan.

- [SendEvidenceValidationTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceValidationTest.kt) · `EVI_02_rejectsLookalikeDomainAndEmptyPhoto` · PASS JVM
- [InputRulesTest](../../../app/src/test/java/com/pactogg/app/integration/InputRulesTest.kt) · `evidenceRejectsSpoofedHostsCredentialsAndInsecureUrls` · PASS JVM

**Falta:** Faltan cancelación del selector, archivo grande, MIME inválido y ausencia de cámara/video en la UI instalada.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-EVI-02-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Moderación rechazada, límite 409, autorización 403 y servicio 503 presentan acciones correctas sin falsos aprobados.

**Tareas:** `MO-EVI-02-T4`, `MO-EVI-02-T5` y `MO-EVI-02-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** 403/503 y éxito malformado no confirman evidencia; bytes de foto se borran incluso con error.

- [SendEvidenceSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceSecurityTest.kt) · `EVI_02_forbiddenEvidencePreservesExistingSession` · PASS JVM
- [SendEvidenceSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceSecurityTest.kt) · `EVI_02_serviceFailureDoesNotRetrySendEvidence` · PASS JVM
- [SendEvidenceSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceSecurityTest.kt) · `EVI_02_malformedSendEvidenceSuccessIsRejected` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `photoBytesAreErasedEvenWhenUploadFails` · PASS JVM

**Falta:** Faltan CONTENT_REJECTED y EVIDENCE_LIMIT_REACHED 409 específicos y acciones/mensajes UI; STORAGE_UNAVAILABLE no sustituye moderación rechazada.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-EVI-02-AC4

**Estado técnico: PARCIAL.**
**Condición original:** Sin contrato autorizado no se abre envío; pérdida de respuesta no repite la subida.

**Tareas:** `MO-EVI-02-T4`, `MO-EVI-02-T5` y `MO-EVI-02-T4-AC4` en [tasks.md](../tasks.md).

**Lo comprobado:** Sin sesión no hay tráfico; si falla GET después de una subida confirmada se conserva evidencia y no se vuelve a subir.

- [SendEvidenceSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceSecurityTest.kt) · `EVI_02_missingSessionPreventsSendEvidenceRequest` · PASS JVM
- [EvidenceConfirmationTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/EvidenceConfirmationTest.kt) · `failedReadKeepsConfirmedEvidenceAndMarksStatusStaleWithoutAnotherUpload` · PASS JVM
- [EvidenceConfirmationTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/EvidenceConfirmationTest.kt) · `lateReadCannotRestoreDataAfterSessionWasCleared` · PASS JVM

**Falta:** Falta bloquear apertura/envío con contrato no autorizado en UI y pérdida de respuesta del POST; fallo del GET posterior no equivale a subida incierta.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-EVI-02-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente

**Tareas:** `MO-EVI-02-T4`, `MO-EVI-02-T5` y `MO-EVI-02-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [SendEvidenceValidationTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceValidationTest.kt) · `EVI_02_rejectsLookalikeDomainAndEmptyPhoto` · PASS JVM
- [SendEvidenceApiTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceApiTest.kt) · `EVI_02_sendsAllowedEvidenceLink` · PASS JVM
- [SendEvidenceSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceSecurityTest.kt) · `EVI_02_forbiddenEvidencePreservesExistingSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado vigente: **Parcial: envío real de enlace/foto probado; criterios restantes y ajuste de estado pendientes de verificación**. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-EVI-02-AC1: Enlace y foto válidos usan serialización/content-type correctos y muestran solo evidencia confirmada por 201.
- [ ] MO-EVI-02-AC2: Selector cancelado, archivo vacío/grande/tipo inválido no envían; no existen cámara ni carga de video.
- [ ] MO-EVI-02-AC3: Moderación rechazada, límite 409, autorización 403 y servicio 503 presentan acciones correctas sin falsos aprobados.
- [ ] MO-EVI-02-AC4: Sin contrato autorizado no se abre envío; pérdida de respuesta no repite la subida.
- [ ] MO-EVI-02-ACQ: controles del candidato y evidencia válidos.
- [ ] Entradas/bloqueantes de sección 7 resueltos para las tareas afectadas.
- [ ] Revisión humana de spec y aceptación funcional diferenciadas.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Pendiente | Por designar | Pendiente | Documentación preparada, sin pruebas de integración ejecutadas |


## Avance verificado local — 2026-11-04

Implementación de consumidor presente. [Evidencia del candidato](../../../docs/integracion/verificacion-consumidor-2026-11-04.md): debug, 24 JVM, Lint/Detekt y Gitleaks correctos; androidTest compilado, no ejecutado. No marcar los AC integrales como PASS: faltan proveedor/cuentas autorizadas, ejecución Android y aceptación aplicable. La tabla histórica NO_EJECUTADO no significa ausencia de código actual.


## Verificación física posterior — 2026-11-04

No ejecutado contra proveedor: creación del contrato bloqueada por moderación; no se prueban fotos/enlaces con IDs inventados. [Informe real](../../../docs/integracion/verificacion-telefono-2026-11-04.md). No cerrar casillas integrales por un caso positivo ni transferir el PASS de fixtures a proveedor.


## Preparación para commits — 2026-11-04

Se conserva el resultado real del candidato físico y se prepara incorporación por ramas dependientes. Commit y compilación no sustituyen aceptación de todos los AC; las casillas sin evidencia completa siguen abiertas. El historial inferior no significa que la implementación actual sea solo documental. Registro de ramas y comprobaciones en docs/preparacion/entrega-por-ramas.md.


Actualización posterior: la reprueba física de reto/enlace/foto fue positiva tras corrección AWS; el bloqueo 503 inferior es histórico. [Informe](../../../docs/integracion/verificacion-telefono-2026-11-04.md). Ajuste actual: lectura auxiliar de estado posterior a evidencia; no implica aceptación del expediente completo.

- [ ] MO-EVI-02-AC5: actualización real posterior al envío, fallo de lectura sin duplicar subida y sin restaurar datos tras logout; verificar candidato afectado.


AC5: verificación local positiva (HTTP/estado) y controles del candidato correctos; recorrido real posterior a subida/aceptación pendiente. [Informe](../../../docs/integracion/ajuste-estado-evidencia-2026-11-04.md). Se conserva casilla integral abierta.
