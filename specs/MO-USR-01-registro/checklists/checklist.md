# Checklist — MO-USR-01

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-USR-01-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Registro válido produce una sola petición con campos exactos y lleva al inicio solo después de custodiar sesión.

**Tareas:** `MO-USR-01-T4`, `MO-USR-01-T5` y `MO-USR-01-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Campos/consentimiento/version de registro y 201/custodia se prueban mediante peticiones y uso posterior de la sesión. Ampliación actual: validAccessDoubleSubmitWaitsForCustodyAndNavigatesOnlyAfterSuccess.

- [RegisterApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterApiTest.kt) · `USR_01_sendsRegistrationAndAcceptsInitialSession` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `registrationGroupContractEvidenceTournamentsAndLogoutUseRealWireContract` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `validAccessDoubleSubmitWaitsForCustodyAndNavigatesOnlyAfterSuccess` · PASS JVM

**Pendiente/límite:** Doble Submit y publicación de identidad tras custodia comprobados en ViewModel; falta recorrido Compose exitoso de registro instalado.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-USR-01-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Límites de email/apodo/contraseña, consentimiento ausente y versión incorrecta muestran validación; duplicados 409 y 429 no navegan.

**Tareas:** `MO-USR-01-T4`, `MO-USR-01-T5` y `MO-USR-01-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Contraseña/apodo excesivo, consentimiento ausente y 429 se rechazan; cambio de modo borra consentimiento. Ampliación actual: USR_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity.

- [RegisterValidationTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterValidationTest.kt) · `USR_01_rejectsWeakPasswordMissingConsentAndOversizedNickname` · PASS JVM
- [RegisterSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterSecurityTest.kt) · `USR_01_rateLimitDoesNotRetryRegister` · PASS JVM
- [InputRulesTest](../../../app/src/test/java/com/pactogg/app/integration/InputRulesTest.kt) · `loginDoesNotApplyRegistrationMinimumAndConsentStartsFalse` · PASS JVM
- [AccessNavigationTest](../../../app/src/androidTest/java/com/pactogg/app/navigation/AccessNavigationTest.kt) · `switchingRegistrationAndLoginClearsCredentialsAndConsent` · PASS ANDROID
- [RegisterApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterApiTest.kt) · `USR_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM

**Pendiente/límite:** 409/422 de registro comprobados sin sesión/reintentos. Faltan fronteras completas y versión de privacidad rechazada; aviso definitivo diferido.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-USR-01-AC3

**Estado técnico: PARCIAL.**
**Condición original:** 201 incompleto, timeout y fallo de custodia no crean sesión ni repiten automáticamente el registro.

**Tareas:** `MO-USR-01-T4`, `MO-USR-01-T5` y `MO-USR-01-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** 201 malformado y 503 de register no crean sesión ni reintentan. Ampliación actual: USR_01_failedCustodyCannotPublishRegisteredIdentity, USR_01_lostResponseIsNotReplayedOrReportedAsSuccess.

- [RegisterSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterSecurityTest.kt) · `USR_01_malformedRegisterSuccessIsRejected` · PASS JVM
- [RegisterSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterSecurityTest.kt) · `USR_01_serviceFailureDoesNotRetryRegister` · PASS JVM
- [RegisterApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterApiTest.kt) · `USR_01_failedCustodyCannotPublishRegisteredIdentity` · PASS JVM
- [RegisterApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterApiTest.kt) · `USR_01_lostResponseIsNotReplayedOrReportedAsSuccess` · PASS JVM

Apoyo indirecto (no sustituye la condición): `com.pactogg.app.integration.ApiIntegrationTest#incompleteSessionAndStorageFailureNeverAuthenticate`.

**Pendiente/límite:** Fallo inducido de custodia y desconexión después de recibir POST comprobados específicamente para registro. Falta timeout con plazo agotado y su recorrido Compose.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-USR-01-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente

**Tareas:** `MO-USR-01-T4`, `MO-USR-01-T5` y `MO-USR-01-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [RegisterValidationTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterValidationTest.kt) · `USR_01_rejectsWeakPasswordMissingConsentAndOversizedNickname` · PASS JVM
- [RegisterApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterApiTest.kt) · `USR_01_sendsRegistrationAndAcceptsInitialSession` · PASS JVM
- [RegisterSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterSecurityTest.kt) · `USR_01_rejectedRegistrationNeverCreatesSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-USR-01

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-USR-01-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Registro válido produce una sola petición con campos exactos y lleva al inicio solo después de custodiar sesión.

**Tareas:** `MO-USR-01-T4`, `MO-USR-01-T5` y `MO-USR-01-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Campos/consentimiento/version de registro y 201/custodia se prueban mediante peticiones y uso posterior de la sesión.

- [RegisterApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterApiTest.kt) · `USR_01_sendsRegistrationAndAcceptsInitialSession` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `registrationGroupContractEvidenceTournamentsAndLogoutUseRealWireContract` · PASS JVM

**Falta:** Faltan doble pulsación de registro y navegación UI después de custodia; no transferir la prueba de login como si ejercitara register.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-USR-01-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Límites de email/apodo/contraseña, consentimiento ausente y versión incorrecta muestran validación; duplicados 409 y 429 no navegan.

**Tareas:** `MO-USR-01-T4`, `MO-USR-01-T5` y `MO-USR-01-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Contraseña/apodo excesivo, consentimiento ausente y 429 se rechazan; cambio de modo borra consentimiento.

- [RegisterValidationTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterValidationTest.kt) · `USR_01_rejectsWeakPasswordMissingConsentAndOversizedNickname` · PASS JVM
- [RegisterSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterSecurityTest.kt) · `USR_01_rateLimitDoesNotRetryRegister` · PASS JVM
- [InputRulesTest](../../../app/src/test/java/com/pactogg/app/integration/InputRulesTest.kt) · `loginDoesNotApplyRegistrationMinimumAndConsentStartsFalse` · PASS JVM
- [AccessNavigationTest](../../../app/src/androidTest/java/com/pactogg/app/navigation/AccessNavigationTest.kt) · `switchingRegistrationAndLoginClearsCredentialsAndConsent` · PASS ANDROID

**Falta:** Faltan fronteras de email y máximos/mínimos completos, versión rechazada y duplicados 409 de registro con no navegación. Aviso definitivo queda diferido por backend.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-USR-01-AC3

**Estado técnico: PARCIAL.**
**Condición original:** 201 incompleto, timeout y fallo de custodia no crean sesión ni repiten automáticamente el registro.

**Tareas:** `MO-USR-01-T4`, `MO-USR-01-T5` y `MO-USR-01-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** 201 malformado y 503 de register no crean sesión ni reintentan.

- [RegisterSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterSecurityTest.kt) · `USR_01_malformedRegisterSuccessIsRejected` · PASS JVM
- [RegisterSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterSecurityTest.kt) · `USR_01_serviceFailureDoesNotRetryRegister` · PASS JVM

**Apoyo indirecto; no demuestra por sí solo el criterio:**

- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `incompleteSessionAndStorageFailureNeverAuthenticate` · PASS JVM

**Falta:** Faltan timeout y fallo de custodia inducidos específicamente en register; el test de fallo de escritura existente llama login.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-USR-01-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente

**Tareas:** `MO-USR-01-T4`, `MO-USR-01-T5` y `MO-USR-01-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [RegisterValidationTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterValidationTest.kt) · `USR_01_rejectsWeakPasswordMissingConsentAndOversizedNickname` · PASS JVM
- [RegisterApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterApiTest.kt) · `USR_01_sendsRegistrationAndAcceptsInitialSession` · PASS JVM
- [RegisterSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/register/RegisterSecurityTest.kt) · `USR_01_rejectedRegistrationNeverCreatesSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado vigente: **Registro real verificado; negativos completos y aviso definitivo pendientes**. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-USR-01-AC1: Registro válido produce una sola petición con campos exactos y lleva al inicio solo después de custodiar sesión.
- [ ] MO-USR-01-AC2: Límites de email/apodo/contraseña, consentimiento ausente y versión incorrecta muestran validación; duplicados 409 y 429 no navegan.
- [ ] MO-USR-01-AC3: 201 incompleto, timeout y fallo de custodia no crean sesión ni repiten automáticamente el registro.
- [ ] MO-USR-01-ACQ: controles del candidato y evidencia válidos.
- [ ] Entradas/bloqueantes de sección 7 resueltos para las tareas afectadas.
- [ ] Revisión humana de spec y aceptación funcional diferenciadas.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Pendiente | Por designar | Pendiente | Documentación preparada, sin pruebas de integración ejecutadas |


## Avance verificado local — 2026-11-04

Implementación de consumidor presente. [Evidencia del candidato](../../../docs/integracion/verificacion-consumidor-2026-11-04.md): debug, 24 JVM, Lint/Detekt y Gitleaks correctos; androidTest compilado, no ejecutado. No marcar los AC integrales como PASS: faltan proveedor/cuentas autorizadas, ejecución Android y aceptación aplicable. La tabla histórica NO_EJECUTADO no significa ausencia de código actual.


## Verificación física posterior — 2026-11-04

Registro real confirmado con cuenta QA; acceso/consentimiento probados. Aceptación integral y aviso definitivo pendientes. [Informe real](../../../docs/integracion/verificacion-telefono-2026-11-04.md). No cerrar casillas integrales por un caso positivo ni transferir el PASS de fixtures a proveedor.


## Preparación para commits — 2026-11-04

Se conserva el resultado real del candidato físico y se prepara incorporación por ramas dependientes. Commit y compilación no sustituyen aceptación de todos los AC; las casillas sin evidencia completa siguen abiertas. El historial inferior no significa que la implementación actual sea solo documental. Registro de ramas y comprobaciones en docs/preparacion/entrega-por-ramas.md.
