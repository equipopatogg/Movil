# Checklist — MO-AUTH-01

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-AUTH-01-AC1

**Estado técnico: PARCIAL.**
**Condición original:** 200 válido almacena sesión y abre inicio; credenciales inválidas 401 permanecen en acceso sin refresh recursivo.

**Tareas:** `MO-AUTH-01-T4`, `MO-AUTH-01-T5` y `MO-AUTH-01-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Petición de login, rechazo 401 sin sesión y formulario vacío sin acceso; rechazo de sesión incompleta/custodia fallida.

- [LoginApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginApiTest.kt) · `AUTH_01_sendsCredentialsAndConsumesLoginResponse` · PASS JVM
- [LoginSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginSecurityTest.kt) · `AUTH_01_invalidCredentialsNeverCreateSession` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `incompleteSessionAndStorageFailureNeverAuthenticate` · PASS JVM
- [AccessNavigationTest](../../../app/src/androidTest/java/com/pactogg/app/navigation/AccessNavigationTest.kt) · `emptyLoginCannotBypassAuthentication` · PASS ANDROID

**Pendiente/límite:** Completar interacción UI de login 200 y 401 del candidato actual, incluyendo destino de éxito; el recorrido real anterior queda como histórico.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-AUTH-01-AC2

**Estado técnico: PARCIAL.**
**Condición original:** 429, 422, timeout y respuesta malformada tienen estado recuperable y no inician sesión.

**Tareas:** `MO-AUTH-01-T4`, `MO-AUTH-01-T5` y `MO-AUTH-01-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** 429, éxito malformado, correo inválido y longitud máxima de contraseña no confirman una sesión. Ampliación actual: AUTH_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity, AUTH_01_lostResponseIsNotReplayedOrReportedAsSuccess.

- [LoginSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginSecurityTest.kt) · `AUTH_01_rateLimitDoesNotRetryLogin` · PASS JVM
- [LoginSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginSecurityTest.kt) · `AUTH_01_malformedLoginSuccessIsRejected` · PASS JVM
- [LoginValidationTest](../../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginValidationTest.kt) · `AUTH_01_rejectsInvalidEmailAndOversizedPassword` · PASS JVM
- [LoginApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginApiTest.kt) · `AUTH_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM
- [LoginApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginApiTest.kt) · `AUTH_01_lostResponseIsNotReplayedOrReportedAsSuccess` · PASS JVM

**Pendiente/límite:** 422 y desconexión de login no autentican ni repiten. Falta timeout con plazo agotado y recuperación mediante Compose.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-AUTH-01-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Doble toque, rotación y recreación no duplican login ni persisten contraseña en SavedState/Bundle.

**Tareas:** `MO-AUTH-01-T4`, `MO-AUTH-01-T5` y `MO-AUTH-01-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Giro horizontal y recreación reales preservan borrador sin autenticar; Bundle real no contiene contraseña sintética; busy bloquea navegación/edición. Ampliación actual: validAccessDoubleSubmitWaitsForCustodyAndNavigatesOnlyAfterSuccess.

- [AccessNavigationTest](../../../app/src/androidTest/java/com/pactogg/app/navigation/AccessNavigationTest.kt) · `rotationAndRecreationKeepDraftWithoutSavingPasswordOrAuthenticating` · PASS ANDROID
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `busyStateBlocksNavigationAndEditing` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `validAccessDoubleSubmitWaitsForCustodyAndNavigatesOnlyAfterSuccess` · PASS JVM

**Pendiente/límite:** Doble Submit válido en vuelo comprobado en ViewModel. Falta recreación de Activity durante envío válido; no atribuirlo al giro del formulario sin envío.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-AUTH-01-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente

**Tareas:** `MO-AUTH-01-T4`, `MO-AUTH-01-T5` y `MO-AUTH-01-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [LoginValidationTest](../../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginValidationTest.kt) · `AUTH_01_rejectsInvalidEmailAndOversizedPassword` · PASS JVM
- [LoginApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginApiTest.kt) · `AUTH_01_sendsCredentialsAndConsumesLoginResponse` · PASS JVM
- [LoginSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginSecurityTest.kt) · `AUTH_01_invalidCredentialsNeverCreateSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-AUTH-01

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-AUTH-01-AC1

**Estado técnico: PARCIAL.**
**Condición original:** 200 válido almacena sesión y abre inicio; credenciales inválidas 401 permanecen en acceso sin refresh recursivo.

**Tareas:** `MO-AUTH-01-T4`, `MO-AUTH-01-T5` y `MO-AUTH-01-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Petición de login, rechazo 401 sin sesión y formulario vacío sin acceso; rechazo de sesión incompleta/custodia fallida.

- [LoginApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginApiTest.kt) · `AUTH_01_sendsCredentialsAndConsumesLoginResponse` · PASS JVM
- [LoginSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginSecurityTest.kt) · `AUTH_01_invalidCredentialsNeverCreateSession` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `incompleteSessionAndStorageFailureNeverAuthenticate` · PASS JVM
- [AccessNavigationTest](../../../app/src/androidTest/java/com/pactogg/app/navigation/AccessNavigationTest.kt) · `emptyLoginCannotBypassAuthentication` · PASS ANDROID

**Falta:** Completar interacción UI de login 200 y 401 del candidato actual, incluyendo destino de éxito; el recorrido real anterior queda como histórico.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-AUTH-01-AC2

**Estado técnico: PARCIAL.**
**Condición original:** 429, 422, timeout y respuesta malformada tienen estado recuperable y no inician sesión.

**Tareas:** `MO-AUTH-01-T4`, `MO-AUTH-01-T5` y `MO-AUTH-01-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** 429, éxito malformado, correo inválido y longitud máxima de contraseña no confirman una sesión.

- [LoginSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginSecurityTest.kt) · `AUTH_01_rateLimitDoesNotRetryLogin` · PASS JVM
- [LoginSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginSecurityTest.kt) · `AUTH_01_malformedLoginSuccessIsRejected` · PASS JVM
- [LoginValidationTest](../../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginValidationTest.kt) · `AUTH_01_rejectsInvalidEmailAndOversizedPassword` · PASS JVM

**Falta:** Faltan fixtures 422 y timeout real de login, más recuperación del formulario después de esos errores.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-AUTH-01-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Doble toque, rotación y recreación no duplican login ni persisten contraseña en SavedState/Bundle.

**Tareas:** `MO-AUTH-01-T4`, `MO-AUTH-01-T5` y `MO-AUTH-01-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Giro horizontal y recreación reales preservan borrador sin autenticar; Bundle real no contiene contraseña sintética; busy bloquea navegación/edición.

- [AccessNavigationTest](../../../app/src/androidTest/java/com/pactogg/app/navigation/AccessNavigationTest.kt) · `rotationAndRecreationKeepDraftWithoutSavingPasswordOrAuthenticating` · PASS ANDROID
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `busyStateBlocksNavigationAndEditing` · PASS JVM

**Falta:** Falta doble pulsación de login válido con respuesta en vuelo y recreación durante ese envío; giro sin Submit no prueba ausencia de POST duplicado.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-AUTH-01-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente

**Tareas:** `MO-AUTH-01-T4`, `MO-AUTH-01-T5` y `MO-AUTH-01-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [LoginValidationTest](../../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginValidationTest.kt) · `AUTH_01_rejectsInvalidEmailAndOversizedPassword` · PASS JVM
- [LoginApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginApiTest.kt) · `AUTH_01_sendsCredentialsAndConsumesLoginResponse` · PASS JVM
- [LoginSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginSecurityTest.kt) · `AUTH_01_invalidCredentialsNeverCreateSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado vigente: **Implementación verificada en flujo positivo y rechazo 401; cierre integral pendiente**. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-AUTH-01-AC1: 200 válido almacena sesión y abre inicio; credenciales inválidas 401 permanecen en acceso sin refresh recursivo.
- [ ] MO-AUTH-01-AC2: 429, 422, timeout y respuesta malformada tienen estado recuperable y no inician sesión.
- [ ] MO-AUTH-01-AC3: Doble toque, rotación y recreación no duplican login ni persisten contraseña en SavedState/Bundle.
- [ ] MO-AUTH-01-ACQ: controles del candidato y evidencia válidos.
- [ ] Entradas/bloqueantes de sección 7 resueltos para las tareas afectadas.
- [ ] Revisión humana de spec y aceptación funcional diferenciadas.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Pendiente | Por designar | Pendiente | Documentación preparada, sin pruebas de integración ejecutadas |


## Avance verificado local — 2026-11-04

Implementación de consumidor presente. [Evidencia del candidato](../../../docs/integracion/verificacion-consumidor-2026-11-04.md): debug, 24 JVM, Lint/Detekt y Gitleaks correctos; androidTest compilado, no ejecutado. No marcar los AC integrales como PASS: faltan proveedor/cuentas autorizadas, ejecución Android y aceptación aplicable. La tabla histórica NO_EJECUTADO no significa ausencia de código actual.


## Verificación física posterior — 2026-11-04

Login válido e inválido 401 comprobados en teléfono; sin bypass. UI y API reales, aceptación integral pendiente. [Informe real](../../../docs/integracion/verificacion-telefono-2026-11-04.md). No cerrar casillas integrales por un caso positivo ni transferir el PASS de fixtures a proveedor.


## Preparación para commits — 2026-11-04

Se conserva el resultado real del candidato físico y se prepara incorporación por ramas dependientes. Commit y compilación no sustituyen aceptación de todos los AC; las casillas sin evidencia completa siguen abiertas. El historial inferior no significa que la implementación actual sea solo documental. Registro de ramas y comprobaciones en docs/preparacion/entrega-por-ramas.md.
