# Checklist — MO-CON-07

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-CON-07-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Retador/tercero no puede rechazar; 403 no modifica estado ni saldo.

**Tareas:** `MO-CON-07-T4`, `MO-CON-07-T5` y `MO-CON-07-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Retador/tercero no cumplen reglas locales de rechazo; 403 se rechaza conservando sesión. Ampliación actual: CON_07_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity.

- [RejectChallengeValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/reject/RejectChallengeValidationTest.kt) · `CON_07_rejectsGroupTargetAndAnotherTargetUser` · PASS JVM
- [ContractPolicyTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ContractPolicyTest.kt) · `directedActionsAreExclusiveAndRejectionDoesNotNeedFunds` · PASS JVM
- [RejectChallengeSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/reject/RejectChallengeSecurityTest.kt) · `CON_07_forbiddenRejectPreservesExistingSession` · PASS JVM
- [RejectChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/reject/RejectChallengeApiTest.kt) · `CON_07_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM

**Pendiente/límite:** 403 NOT_CONTRACT_TARGET conserva identidad en repositorio; falta proveedor real con ambos roles y sus estados de pantalla.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-CON-07-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Versión vieja produce 409; versión válida obtiene cancelled con motivo rejected y puntos intactos.

**Tareas:** `MO-CON-07-T4`, `MO-CON-07-T5` y `MO-CON-07-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Versión se envía; respuesta válida cancelled/rejected y saldo anterior se conservan en consumidor. Ampliación actual: CON_07_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity.

- [RejectChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/reject/RejectChallengeApiTest.kt) · `CON_07_sendsVersionWhenRejectingChallenge` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `rejectionUsesCancelledReasonAndVersionWithoutInventingPoints` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `rejectDoesNotInventPointChangesAndCancelDoesNotWrite` · PASS JVM
- [RejectChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/reject/RejectChallengeApiTest.kt) · `CON_07_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM

**Pendiente/límite:** 409 INVALID_CONTRACT_STATE específico de reject comprobado sin repetición; falta recuperación Compose con detalle actualizado.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-CON-07-AC3

**Estado técnico: COMPROBADO_LOCAL_CONSUMIDOR.**
**Condición original:** Aceptar después de rechazo no produce éxito y maneja 409 tras refrescar.

**Tareas:** `MO-CON-07-T4`, `MO-CON-07-T5` y `MO-CON-07-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Secuencia HTTPS reject→GET→accept termina en 409, sin éxito ni cambio de identidad; estado cancelado bloquea aceptar y conserva saldo.

- [RejectChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/reject/RejectChallengeApiTest.kt) · `CON_07_rejectReadThenAcceptConflictNeverProducesSuccess` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `rejectedChallengeCannotBeConfirmedAsAcceptedAndKeepsBalance` · PASS JVM

Apoyo indirecto (no sustituye la condición): `com.pactogg.app.integration.SocialApiIntegrationTest#rejectionUsesCancelledReasonAndVersionWithoutInventingPoints`, `com.pactogg.app.integration.SocialApiIntegrationTest#acceptErrorsNeverRetryOrEraseSession`.

**Pendiente/límite:** Aceptación humana y del despliegue real separadas; no ejecutadas por esta fase local.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-CON-07-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-CON-07-T4`, `MO-CON-07-T5` y `MO-CON-07-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [RejectChallengeValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/reject/RejectChallengeValidationTest.kt) · `CON_07_rejectsGroupTargetAndAnotherTargetUser` · PASS JVM
- [RejectChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/reject/RejectChallengeApiTest.kt) · `CON_07_sendsVersionWhenRejectingChallenge` · PASS JVM
- [RejectChallengeSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/reject/RejectChallengeSecurityTest.kt) · `CON_07_forbiddenRejectPreservesExistingSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-CON-07

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-CON-07-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Retador/tercero no puede rechazar; 403 no modifica estado ni saldo.

**Tareas:** `MO-CON-07-T4`, `MO-CON-07-T5` y `MO-CON-07-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Retador/tercero no cumplen reglas locales de rechazo; 403 se rechaza conservando sesión.

- [RejectChallengeValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/reject/RejectChallengeValidationTest.kt) · `CON_07_rejectsGroupTargetAndAnotherTargetUser` · PASS JVM
- [ContractPolicyTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ContractPolicyTest.kt) · `directedActionsAreExclusiveAndRejectionDoesNotNeedFunds` · PASS JVM
- [RejectChallengeSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/reject/RejectChallengeSecurityTest.kt) · `CON_07_forbiddenRejectPreservesExistingSession` · PASS JVM

**Falta:** Falta rechazo por esos roles en proveedor y verificar contrato/saldo UI invariantes después del 403.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-CON-07-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Versión vieja produce 409; versión válida obtiene cancelled con motivo rejected y puntos intactos.

**Tareas:** `MO-CON-07-T4`, `MO-CON-07-T5` y `MO-CON-07-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Versión se envía; respuesta válida cancelled/rejected y saldo anterior se conservan en consumidor.

- [RejectChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/reject/RejectChallengeApiTest.kt) · `CON_07_sendsVersionWhenRejectingChallenge` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `rejectionUsesCancelledReasonAndVersionWithoutInventingPoints` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `rejectDoesNotInventPointChangesAndCancelDoesNotWrite` · PASS JVM

**Falta:** Falta fixture de versión vieja 409 para reject y su recuperación UI; un 409 de accept no cubre reject.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-CON-07-AC3

**Estado técnico: SIN_CASO_ESPECIFICO.**
**Condición original:** Aceptar después de rechazo no produce éxito y maneja 409 tras refrescar.

**Tareas:** `MO-CON-07-T4`, `MO-CON-07-T5` y `MO-CON-07-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** No se encontró un caso que encadene rechazo confirmado, refresco y posterior intento de aceptación.

No se encontró un caso específico que ejercite la secuencia completa indicada.

**Apoyo indirecto; no demuestra por sí solo el criterio:**

- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `rejectionUsesCancelledReasonAndVersionWithoutInventingPoints` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `acceptErrorsNeverRetryOrEraseSession` · PASS JVM

**Falta:** Crear el escenario secuencial reject→GET→accept 409 y verificar que no confirme éxito ni cambie saldo.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-CON-07-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-CON-07-T4`, `MO-CON-07-T5` y `MO-CON-07-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [RejectChallengeValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/reject/RejectChallengeValidationTest.kt) · `CON_07_rejectsGroupTargetAndAnotherTargetUser` · PASS JVM
- [RejectChallengeApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/reject/RejectChallengeApiTest.kt) · `CON_07_sendsVersionWhenRejectingChallenge` · PASS JVM
- [RejectChallengeSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/reject/RejectChallengeSecurityTest.kt) · `CON_07_forbiddenRejectPreservesExistingSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla. Falta accesibilidad del recorrido específico.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado: implementación local presente; recorrido positivo real y controles locales correctos; aceptación integral pendiente. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-CON-07-AC1: Retador/tercero no puede rechazar; 403 no modifica estado ni saldo.
- [ ] MO-CON-07-AC2: Versión vieja produce 409; versión válida obtiene cancelled con motivo rejected y puntos intactos.
- [ ] MO-CON-07-AC3: Aceptar después de rechazo no produce éxito y maneja 409 tras refrescar.
- [ ] MO-CON-07-ACQ: controles y evidencia del mismo candidato verificados.
- [ ] Bloqueantes específicos de sección 7 resueltos para las tareas afectadas.
- [ ] Prueba real del proveedor distinguida de fixtures; no se hereda PASS del backend.
- [ ] Revisión humana y aceptación de implementación registradas por separado.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación solicitada | Usuario | 2026-11-04 | Operación incluida en Sistema.docx; no prueba funcional |
| Aceptación móvil | Pendiente | Pendiente | No inferir por existencia de documentos |


[Resultado real y candidato](../../../docs/integracion/integracion-social-2026-11-04.md): caso positivo comprobado; no se cierran casillas integrales que contienen escenarios restantes. Datos y límites de la cuenta auxiliar/recorrido UI distinguidos de fixtures.
