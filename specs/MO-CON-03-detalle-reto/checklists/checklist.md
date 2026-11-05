# Checklist — MO-CON-03

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-CON-03-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Miembro abre reto 200 con datos, estado, contador y versión del proveedor; no inventa galería.

**Tareas:** `MO-CON-03-T4`, `MO-CON-03-T5` y `MO-CON-03-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** GET de detalle conserva versión y contador; abre grupo y detalle en estado y borra evidencia anterior.

- [ChallengeDetailsApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/detail/ChallengeDetailsApiTest.kt) · `CON_03_mapsChallengeVersion` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `membersPrefixAndBoardFiltersFollowCursorContract` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `refreshDetailOpensCorrectGroupAndClearsEvidence` · PASS JVM

**Pendiente/límite:** Faltan asserts de campos completos en UI y ausencia de galería inventada; el recorrido positivo real anterior no cierra todos esos puntos.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-CON-03-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Ajeno/inexistente produce 403/404; IDs o grupo discordantes se rechazan.

**Tareas:** `MO-CON-03-T4`, `MO-CON-03-T5` y `MO-CON-03-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** 403 y un ID de contrato discordante se rechazan; los validadores rechazan IDs malformados. La discordancia de grupo probada es de grupo, no de contrato. Ampliación actual: CON_03_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity.

- [ChallengeDetailsSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/detail/ChallengeDetailsSecurityTest.kt) · `CON_03_forbiddenDetailReadPreservesExistingSession` · PASS JVM
- [ChallengeDetailsValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/detail/ChallengeDetailsValidationTest.kt) · `CON_03_rejectsMalformedChallengeIdentity` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `challengeRefreshUsesAuthenticatedGetAndRejectsAnotherContract` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `malformedPageAndWrongGroupAreRejected` · PASS JVM
- [ChallengeDetailsApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/detail/ChallengeDetailsApiTest.kt) · `CON_03_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM

**Pendiente/límite:** 404 de contrato comprobado; falta group_id discordante con id correcto y estado Compose.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-CON-03-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Detalle se refresca tras acciones y logout evita restauración tardía; reutiliza ayuda de evidencias sin duplicar POST.

**Tareas:** `MO-CON-03-T4`, `MO-CON-03-T5` y `MO-CON-03-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Lectura posterior a evidencia confirma estado; una lectura tardía no repone identidad tras limpieza; se reutiliza GET sin segunda subida.

- [EvidenceConfirmationTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/EvidenceConfirmationTest.kt) · `confirmedUploadRefreshesActualStatusWithoutInventingIt` · PASS JVM
- [EvidenceConfirmationTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/EvidenceConfirmationTest.kt) · `lateReadCannotRestoreDataAfterSessionWasCleared` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `refreshDetailOpensCorrectGroupAndClearsEvidence` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `lateBusinessResponseAfterLogoutCannotBeAccepted` · PASS JVM

**Pendiente/límite:** Falta recorrido de refresco de detalle tras aceptar/rechazar y logout desde esa pantalla; el caso de evidencia tiene alcance propio.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-CON-03-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-CON-03-T4`, `MO-CON-03-T5` y `MO-CON-03-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [ChallengeDetailsValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/detail/ChallengeDetailsValidationTest.kt) · `CON_03_rejectsMalformedChallengeIdentity` · PASS JVM
- [ChallengeDetailsApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/detail/ChallengeDetailsApiTest.kt) · `CON_03_mapsChallengeVersion` · PASS JVM
- [ChallengeDetailsSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/detail/ChallengeDetailsSecurityTest.kt) · `CON_03_forbiddenDetailReadPreservesExistingSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-CON-03

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-CON-03-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Miembro abre reto 200 con datos, estado, contador y versión del proveedor; no inventa galería.

**Tareas:** `MO-CON-03-T4`, `MO-CON-03-T5` y `MO-CON-03-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** GET de detalle conserva versión y contador; abre grupo y detalle en estado y borra evidencia anterior.

- [ChallengeDetailsApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/detail/ChallengeDetailsApiTest.kt) · `CON_03_mapsChallengeVersion` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `membersPrefixAndBoardFiltersFollowCursorContract` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `refreshDetailOpensCorrectGroupAndClearsEvidence` · PASS JVM

**Falta:** Faltan asserts de campos completos en UI y ausencia de galería inventada; el recorrido positivo real anterior no cierra todos esos puntos.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-CON-03-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Ajeno/inexistente produce 403/404; IDs o grupo discordantes se rechazan.

**Tareas:** `MO-CON-03-T4`, `MO-CON-03-T5` y `MO-CON-03-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** 403 y un ID de contrato discordante se rechazan; los validadores rechazan IDs malformados. La discordancia de grupo probada es de grupo, no de contrato.

- [ChallengeDetailsSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/detail/ChallengeDetailsSecurityTest.kt) · `CON_03_forbiddenDetailReadPreservesExistingSession` · PASS JVM
- [ChallengeDetailsValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/detail/ChallengeDetailsValidationTest.kt) · `CON_03_rejectsMalformedChallengeIdentity` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `challengeRefreshUsesAuthenticatedGetAndRejectsAnotherContract` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `malformedPageAndWrongGroupAreRejected` · PASS JVM

**Falta:** Faltan 404 del detalle y contrato correcto con group_id discordante. No transferir la prueba de identidad de grupo a la del contrato.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-CON-03-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Detalle se refresca tras acciones y logout evita restauración tardía; reutiliza ayuda de evidencias sin duplicar POST.

**Tareas:** `MO-CON-03-T4`, `MO-CON-03-T5` y `MO-CON-03-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Lectura posterior a evidencia confirma estado; una lectura tardía no repone identidad tras limpieza; se reutiliza GET sin segunda subida.

- [EvidenceConfirmationTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/EvidenceConfirmationTest.kt) · `confirmedUploadRefreshesActualStatusWithoutInventingIt` · PASS JVM
- [EvidenceConfirmationTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/EvidenceConfirmationTest.kt) · `lateReadCannotRestoreDataAfterSessionWasCleared` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `refreshDetailOpensCorrectGroupAndClearsEvidence` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `lateBusinessResponseAfterLogoutCannotBeAccepted` · PASS JVM

**Falta:** Falta recorrido de refresco de detalle tras aceptar/rechazar y logout desde esa pantalla; el caso de evidencia tiene alcance propio.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-CON-03-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-CON-03-T4`, `MO-CON-03-T5` y `MO-CON-03-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [ChallengeDetailsValidationTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/detail/ChallengeDetailsValidationTest.kt) · `CON_03_rejectsMalformedChallengeIdentity` · PASS JVM
- [ChallengeDetailsApiTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/detail/ChallengeDetailsApiTest.kt) · `CON_03_mapsChallengeVersion` · PASS JVM
- [ChallengeDetailsSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/contracts/detail/ChallengeDetailsSecurityTest.kt) · `CON_03_forbiddenDetailReadPreservesExistingSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla. Falta accesibilidad del recorrido específico.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado: implementación local presente; recorrido positivo real y controles locales correctos; aceptación integral pendiente. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-CON-03-AC1: Miembro abre reto 200 con datos, estado, contador y versión del proveedor; no inventa galería.
- [ ] MO-CON-03-AC2: Ajeno/inexistente produce 403/404; IDs o grupo discordantes se rechazan.
- [ ] MO-CON-03-AC3: Detalle se refresca tras acciones y logout evita restauración tardía; reutiliza ayuda de evidencias sin duplicar POST.
- [ ] MO-CON-03-ACQ: controles y evidencia del mismo candidato verificados.
- [ ] Bloqueantes específicos de sección 7 resueltos para las tareas afectadas.
- [ ] Prueba real del proveedor distinguida de fixtures; no se hereda PASS del backend.
- [ ] Revisión humana y aceptación de implementación registradas por separado.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación solicitada | Usuario | 2026-11-04 | Operación incluida en Sistema.docx; no prueba funcional |
| Aceptación móvil | Pendiente | Pendiente | No inferir por existencia de documentos |


[Resultado real y candidato](../../../docs/integracion/integracion-social-2026-11-04.md): caso positivo comprobado; no se cierran casillas integrales que contienen escenarios restantes. Datos y límites de la cuenta auxiliar/recorrido UI distinguidos de fixtures.
