# Checklist — MO-GRP-01

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-GRP-01-AC1

**Estado técnico: PARCIAL.**
**Condición original:** 201 muestra grupo, rol, puntos y código devueltos, sin valores fabricados.

**Tareas:** `MO-GRP-01-T4`, `MO-GRP-01-T5` y `MO-GRP-01-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Nombre, 201 y valores de fixture de puntos/grupo se consumen; estado resetea contexto solo tras éxito.

- [CreateGroupApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/create/CreateGroupApiTest.kt) · `GRP_01_sendsGroupNameAndConsumesCreatedGroup` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `registrationGroupContractEvidenceTournamentsAndLogoutUseRealWireContract` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `groupCreationResetsDependentDataOnlyAfterSuccess` · PASS JVM

**Pendiente/límite:** Faltan asserts completos de nombre/rol/código/saldo mostrados, con valores alternativos del proveedor, en UI actual.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-GRP-01-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Nombre límite, 409 y 422 conservan formulario recuperable y no producen éxito local.

**Tareas:** `MO-GRP-01-T4`, `MO-GRP-01-T5` y `MO-GRP-01-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Longitud excesiva/nombre vacío no envían; un fallo genérico preserva nombre y no repite. Ampliación actual: GRP_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity, validationAndModerationErrorsKeepDraftsEditableWithoutSuccessOrBalanceChanges.

- [CreateGroupValidationTest](../../../app/src/test/java/com/pactogg/app/feature/groups/create/CreateGroupValidationTest.kt) · `GRP_01_rejectsOversizedGroupNameBeforeRequest` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `invalidFormsNeverCallRepositories` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `failedWriteRetainsInputAndNeverRetries` · PASS JVM
- [CreateGroupApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/create/CreateGroupApiTest.kt) · `GRP_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `validationAndModerationErrorsKeepDraftsEditableWithoutSuccessOrBalanceChanges` · PASS JVM

**Pendiente/límite:** 409/422 conservan formulario y grupo en ViewModel; faltan fronteras exactas válidas y representación Compose.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-GRP-01-AC3

**Estado técnico: PARCIAL.**
**Condición original:** No existe lista ficticia; unión se resuelve por MO-MEM-01 y sesión/cuenta distinta elimina el contexto anterior.

**Tareas:** `MO-GRP-01-T4`, `MO-GRP-01-T5` y `MO-GRP-01-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Cambio de cuenta borra grupo/borrador/listas; unión exige respuesta confirmada. La matriz mantiene MO-MEM-01 separado.

- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `changingAccountClearsGroupDraftFiltersMessagesAndCredentials` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `joinClearsInviteOnlyAfterProviderConfirmation` · PASS JVM

**Pendiente/límite:** Falta revisar UI vacía y lista recién cargada sin datos de muestra como escenario explícito del recorrido de creación.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-GRP-01-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente

**Tareas:** `MO-GRP-01-T4`, `MO-GRP-01-T5` y `MO-GRP-01-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [CreateGroupValidationTest](../../../app/src/test/java/com/pactogg/app/feature/groups/create/CreateGroupValidationTest.kt) · `GRP_01_rejectsOversizedGroupNameBeforeRequest` · PASS JVM
- [CreateGroupApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/create/CreateGroupApiTest.kt) · `GRP_01_sendsGroupNameAndConsumesCreatedGroup` · PASS JVM
- [CreateGroupSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/groups/create/CreateGroupSecurityTest.kt) · `GRP_01_forbiddenCreationPreservesExistingSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-GRP-01

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-GRP-01-AC1

**Estado técnico: PARCIAL.**
**Condición original:** 201 muestra grupo, rol, puntos y código devueltos, sin valores fabricados.

**Tareas:** `MO-GRP-01-T4`, `MO-GRP-01-T5` y `MO-GRP-01-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Nombre, 201 y valores de fixture de puntos/grupo se consumen; estado resetea contexto solo tras éxito.

- [CreateGroupApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/create/CreateGroupApiTest.kt) · `GRP_01_sendsGroupNameAndConsumesCreatedGroup` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `registrationGroupContractEvidenceTournamentsAndLogoutUseRealWireContract` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `groupCreationResetsDependentDataOnlyAfterSuccess` · PASS JVM

**Falta:** Faltan asserts completos de nombre/rol/código/saldo mostrados, con valores alternativos del proveedor, en UI actual.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-GRP-01-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Nombre límite, 409 y 422 conservan formulario recuperable y no producen éxito local.

**Tareas:** `MO-GRP-01-T4`, `MO-GRP-01-T5` y `MO-GRP-01-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Longitud excesiva/nombre vacío no envían; un fallo genérico preserva nombre y no repite.

- [CreateGroupValidationTest](../../../app/src/test/java/com/pactogg/app/feature/groups/create/CreateGroupValidationTest.kt) · `GRP_01_rejectsOversizedGroupNameBeforeRequest` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `invalidFormsNeverCallRepositories` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `failedWriteRetainsInputAndNeverRetries` · PASS JVM

**Falta:** Faltan fronteras exactas válidas y fixtures 409/422 de creación con recuperación del formulario; 503 no los sustituye.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-GRP-01-AC3

**Estado técnico: PARCIAL.**
**Condición original:** No existe lista ficticia; unión se resuelve por MO-MEM-01 y sesión/cuenta distinta elimina el contexto anterior.

**Tareas:** `MO-GRP-01-T4`, `MO-GRP-01-T5` y `MO-GRP-01-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Cambio de cuenta borra grupo/borrador/listas; unión exige respuesta confirmada. La matriz mantiene MO-MEM-01 separado.

- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `changingAccountClearsGroupDraftFiltersMessagesAndCredentials` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `joinClearsInviteOnlyAfterProviderConfirmation` · PASS JVM

**Falta:** Falta revisar UI vacía y lista recién cargada sin datos de muestra como escenario explícito del recorrido de creación.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-GRP-01-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente

**Tareas:** `MO-GRP-01-T4`, `MO-GRP-01-T5` y `MO-GRP-01-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [CreateGroupValidationTest](../../../app/src/test/java/com/pactogg/app/feature/groups/create/CreateGroupValidationTest.kt) · `GRP_01_rejectsOversizedGroupNameBeforeRequest` · PASS JVM
- [CreateGroupApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/create/CreateGroupApiTest.kt) · `GRP_01_sendsGroupNameAndConsumesCreatedGroup` · PASS JVM
- [CreateGroupSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/groups/create/CreateGroupSecurityTest.kt) · `GRP_01_forbiddenCreationPreservesExistingSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado vigente: **Creación real verificada; negativos y recuperación de listado pendientes**. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-GRP-01-AC1: 201 muestra grupo, rol, puntos y código devueltos, sin valores fabricados.
- [ ] MO-GRP-01-AC2: Nombre límite, 409 y 422 conservan formulario recuperable y no producen éxito local.
- [ ] MO-GRP-01-AC3: No existe lista ficticia; unión se resuelve por MO-MEM-01 y sesión/cuenta distinta elimina el contexto anterior.
- [ ] MO-GRP-01-ACQ: controles del candidato y evidencia válidos.
- [ ] Entradas/bloqueantes de sección 7 resueltos para las tareas afectadas.
- [ ] Revisión humana de spec y aceptación funcional diferenciadas.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Pendiente | Por designar | Pendiente | Documentación preparada, sin pruebas de integración ejecutadas |


## Avance verificado local — 2026-11-04

Implementación de consumidor presente. [Evidencia del candidato](../../../docs/integracion/verificacion-consumidor-2026-11-04.md): debug, 24 JVM, Lint/Detekt y Gitleaks correctos; androidTest compilado, no ejecutado. No marcar los AC integrales como PASS: faltan proveedor/cuentas autorizadas, ejecución Android y aceptación aplicable. La tabla histórica NO_EJECUTADO no significa ausencia de código actual.


## Verificación física posterior — 2026-11-04

Creación de grupo real comprobada en teléfono; configuración recibida, sin lista ficticia. Casos negativos/aceptación mantienen pendientes. [Informe real](../../../docs/integracion/verificacion-telefono-2026-11-04.md). No cerrar casillas integrales por un caso positivo ni transferir el PASS de fixtures a proveedor.


## Preparación para commits — 2026-11-04

Se conserva el resultado real del candidato físico y se prepara incorporación por ramas dependientes. Commit y compilación no sustituyen aceptación de todos los AC; las casillas sin evidencia completa siguen abiertas. El historial inferior no significa que la implementación actual sea solo documental. Registro de ramas y comprobaciones en docs/preparacion/entrega-por-ramas.md.
