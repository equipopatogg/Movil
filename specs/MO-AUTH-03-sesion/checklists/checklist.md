# Checklist — MO-AUTH-03

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-AUTH-03-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Dos 401 concurrentes originan una renovación y ambas observan el par de tokens vigente sin carreras.

**Tareas:** `MO-AUTH-03-T4`, `MO-AUTH-03-T5` y `MO-AUTH-03-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Dos solicitudes con sesión ya expirada comparten renovación; un GET con 401 reintenta una sola vez. Ampliación actual: AUTH_03_twoConcurrent401ResponsesShareOneRotationAndRetryReadsOnce.

- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `concurrentExpiredRequestsRotateOnlyOnceAndUseFreshBearer` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `unauthorizedReadRefreshesAndRetriesOnlyOnce` · PASS JVM
- [RefreshSessionApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/session/RefreshSessionApiTest.kt) · `AUTH_03_twoConcurrent401ResponsesShareOneRotationAndRetryReadsOnce` · PASS JVM

**Pendiente/límite:** Dos respuestas 401 simultáneas y una sola rotación comprobadas en SessionManager con barrera y endpoint refresh HTTPS. Falta recorrido completo desde Compose.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-AUTH-03-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Refresh 401, 403 protegido, red caída y 5xx producen estados diferentes; no hay recursión o bucles.

**Tareas:** `MO-AUTH-03-T4`, `MO-AUTH-03-T5` y `MO-AUTH-03-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Refresh 401/503 limpia custodia sin bucle; 403 protegido conserva sesión y no renueva. Ampliación actual: AUTH_03_lostResponseIsNotReplayedOrReportedAsSuccess.

- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `failedRotationClearsTokensAndDoesNotLoop` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `forbiddenDoesNotRefreshOrEraseSessionAndHandlesArrayDetails` · PASS JVM
- [RefreshSessionSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/session/RefreshSessionSecurityTest.kt) · `AUTH_03_serviceFailureDoesNotRetryRefreshSession` · PASS JVM
- [RefreshSessionSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/session/RefreshSessionSecurityTest.kt) · `AUTH_03_rejectedRotationClearsPreviousSession` · PASS JVM
- [RefreshSessionApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/session/RefreshSessionApiTest.kt) · `AUTH_03_lostResponseIsNotReplayedOrReportedAsSuccess` · PASS JVM

**Pendiente/límite:** Desconexión real de refresh borra custodia sin repetir; controles de 401/403/5xx previos conservados. Falta timeout y comparación de mensajes de UI.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-AUTH-03-AC3

**Estado técnico: PARCIAL.**
**Condición original:** En dispositivo se verifica cifrado, backup, reinicio, corrupción y clave invalidada sin tokens en logs.

**Tareas:** `MO-AUTH-03-T4`, `MO-AUTH-03-T5` y `MO-AUTH-03-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Cifrado y reapertura de custodia en Android real; corrupción o clave ausente no restaura cuenta; reemplazo conserva B.

- [EncryptedSessionStoreTest](../../../app/src/androidTest/java/com/pactogg/app/security/EncryptedSessionStoreTest.kt) · `roundTripIsEncryptedAndClearRemovesTheFile` · PASS ANDROID
- [EncryptedSessionStoreTest](../../../app/src/androidTest/java/com/pactogg/app/security/EncryptedSessionStoreTest.kt) · `corruptedOrMissingKeyDoesNotRestoreAnAccount` · PASS ANDROID
- [EncryptedSessionStoreTest](../../../app/src/androidTest/java/com/pactogg/app/security/EncryptedSessionStoreTest.kt) · `replacingAccountPersistsOnlyTheNewIdentity` · PASS ANDROID

**Pendiente/límite:** Faltan ensayo de backup/restauración, muerte/reinicio del proceso y ventana de logs Android con marcadores. Reabrir un objeto no equivale a reiniciar el proceso.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-AUTH-03-AC4

**Estado técnico: PARCIAL.**
**Condición original:** Logout durante refresh impide restauración tardía; fallo de escritura atómica no mezcla tokens de generaciones distintas.

**Tareas:** `MO-AUTH-03-T4`, `MO-AUTH-03-T5` y `MO-AUTH-03-T4-AC4` en [tasks.md](../tasks.md).

**Lo comprobado:** Un fallo de escritura de custodia durante autenticación impide abrir sesión. Ampliación actual: AUTH_03_logoutDuringPendingRefreshEndsWithNoRestoredSession, rotatingTokenPersistenceFailureClearsOldAndFreshIdentityAtomically.

- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `incompleteSessionAndStorageFailureNeverAuthenticate` · PASS JVM
- [RefreshSessionApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/session/RefreshSessionApiTest.kt) · `AUTH_03_logoutDuringPendingRefreshEndsWithNoRestoredSession` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `rotatingTokenPersistenceFailureClearsOldAndFreshIdentityAtomically` · PASS JVM

Apoyo indirecto (no sustituye la condición): `com.pactogg.app.integration.ApiIntegrationTest#pendingLoginCannotUndoLogoutOrOverwriteNextAccount`, `com.pactogg.app.integration.ApiIntegrationTest#lateBusinessResponseAfterLogoutCannotBeAccepted`.

**Pendiente/límite:** Logout solicitado durante refresh pendiente acaba sin sesión; fallo al persistir tokens rotados borra identidad anterior/nueva. Falta combinación con login B en esa misma ventana y recorrido Android.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-AUTH-03-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente

**Tareas:** `MO-AUTH-03-T4`, `MO-AUTH-03-T5` y `MO-AUTH-03-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [RefreshSessionValidationTest](../../../app/src/test/java/com/pactogg/app/feature/auth/session/RefreshSessionValidationTest.kt) · `AUTH_03_rejectsInvalidSessionLifetime` · PASS JVM
- [RefreshSessionApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/session/RefreshSessionApiTest.kt) · `AUTH_03_sendsRefreshTokenAndPersistsRotatedToken` · PASS JVM
- [RefreshSessionSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/session/RefreshSessionSecurityTest.kt) · `AUTH_03_rejectedRotationClearsPreviousSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-AUTH-03

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-AUTH-03-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Dos 401 concurrentes originan una renovación y ambas observan el par de tokens vigente sin carreras.

**Tareas:** `MO-AUTH-03-T4`, `MO-AUTH-03-T5` y `MO-AUTH-03-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Dos solicitudes con sesión ya expirada comparten renovación; un GET con 401 reintenta una sola vez.

- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `concurrentExpiredRequestsRotateOnlyOnceAndUseFreshBearer` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `unauthorizedReadRefreshesAndRetriesOnlyOnce` · PASS JVM

**Falta:** El caso concurrente existente fuerza expiración previa, no dos respuestas 401 simultáneas. Falta ese escenario exacto.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-AUTH-03-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Refresh 401, 403 protegido, red caída y 5xx producen estados diferentes; no hay recursión o bucles.

**Tareas:** `MO-AUTH-03-T4`, `MO-AUTH-03-T5` y `MO-AUTH-03-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Refresh 401/503 limpia custodia sin bucle; 403 protegido conserva sesión y no renueva.

- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `failedRotationClearsTokensAndDoesNotLoop` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `forbiddenDoesNotRefreshOrEraseSessionAndHandlesArrayDetails` · PASS JVM
- [RefreshSessionSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/session/RefreshSessionSecurityTest.kt) · `AUTH_03_serviceFailureDoesNotRetryRefreshSession` · PASS JVM
- [RefreshSessionSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/session/RefreshSessionSecurityTest.kt) · `AUTH_03_rejectedRotationClearsPreviousSession` · PASS JVM

**Falta:** Falta caída de red/timeout real durante refresh y comprobación diferenciada de mensajes/recuperación de cada estado.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-AUTH-03-AC3

**Estado técnico: PARCIAL.**
**Condición original:** En dispositivo se verifica cifrado, backup, reinicio, corrupción y clave invalidada sin tokens en logs.

**Tareas:** `MO-AUTH-03-T4`, `MO-AUTH-03-T5` y `MO-AUTH-03-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Cifrado y reapertura de custodia en Android real; corrupción o clave ausente no restaura cuenta; reemplazo conserva B.

- [EncryptedSessionStoreTest](../../../app/src/androidTest/java/com/pactogg/app/security/EncryptedSessionStoreTest.kt) · `roundTripIsEncryptedAndClearRemovesTheFile` · PASS ANDROID
- [EncryptedSessionStoreTest](../../../app/src/androidTest/java/com/pactogg/app/security/EncryptedSessionStoreTest.kt) · `corruptedOrMissingKeyDoesNotRestoreAnAccount` · PASS ANDROID
- [EncryptedSessionStoreTest](../../../app/src/androidTest/java/com/pactogg/app/security/EncryptedSessionStoreTest.kt) · `replacingAccountPersistsOnlyTheNewIdentity` · PASS ANDROID

**Falta:** Faltan ensayo de backup/restauración, muerte/reinicio del proceso y ventana de logs Android con marcadores. Reabrir un objeto no equivale a reiniciar el proceso.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-AUTH-03-AC4

**Estado técnico: PARCIAL.**
**Condición original:** Logout durante refresh impide restauración tardía; fallo de escritura atómica no mezcla tokens de generaciones distintas.

**Tareas:** `MO-AUTH-03-T4`, `MO-AUTH-03-T5` y `MO-AUTH-03-T4-AC4` en [tasks.md](../tasks.md).

**Lo comprobado:** Un fallo de escritura de custodia durante autenticación impide abrir sesión.

- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `incompleteSessionAndStorageFailureNeverAuthenticate` · PASS JVM

**Apoyo indirecto; no demuestra por sí solo el criterio:**

- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `pendingLoginCannotUndoLogoutOrOverwriteNextAccount` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `lateBusinessResponseAfterLogoutCannotBeAccepted` · PASS JVM

**Falta:** No hay prueba de logout durante refresh en vuelo ni fallo atómico/mezcla de generaciones durante rotación. Login pendiente no equivale a refresh pendiente.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-AUTH-03-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente

**Tareas:** `MO-AUTH-03-T4`, `MO-AUTH-03-T5` y `MO-AUTH-03-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [RefreshSessionValidationTest](../../../app/src/test/java/com/pactogg/app/feature/auth/session/RefreshSessionValidationTest.kt) · `AUTH_03_rejectsInvalidSessionLifetime` · PASS JVM
- [RefreshSessionApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/session/RefreshSessionApiTest.kt) · `AUTH_03_sendsRefreshTokenAndPersistsRotatedToken` · PASS JVM
- [RefreshSessionSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/session/RefreshSessionSecurityTest.kt) · `AUTH_03_rejectedRotationClearsPreviousSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado vigente: **Cifrado, rotación y arranque verificados; escenarios restantes pendientes**. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-AUTH-03-AC1: Dos 401 concurrentes originan una renovación y ambas observan el par de tokens vigente sin carreras.
- [ ] MO-AUTH-03-AC2: Refresh 401, 403 protegido, red caída y 5xx producen estados diferentes; no hay recursión o bucles.
- [ ] MO-AUTH-03-AC3: En dispositivo se verifica cifrado, backup, reinicio, corrupción y clave invalidada sin tokens en logs.
- [ ] MO-AUTH-03-AC4: Logout durante refresh impide restauración tardía; fallo de escritura atómica no mezcla tokens de generaciones distintas.
- [ ] MO-AUTH-03-ACQ: controles del candidato y evidencia válidos.
- [ ] Entradas/bloqueantes de sección 7 resueltos para las tareas afectadas.
- [ ] Revisión humana de spec y aceptación funcional diferenciadas.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Pendiente | Por designar | Pendiente | Documentación preparada, sin pruebas de integración ejecutadas |


## Avance verificado local — 2026-11-04

Implementación de consumidor presente. [Evidencia del candidato](../../../docs/integracion/verificacion-consumidor-2026-11-04.md): debug, 24 JVM, Lint/Detekt y Gitleaks correctos; androidTest compilado, no ejecutado. No marcar los AC integrales como PASS: faltan proveedor/cuentas autorizadas, ejecución Android y aceptación aplicable. La tabla histórica NO_EJECUTADO no significa ausencia de código actual.


## Verificación física posterior — 2026-11-04

Refresh real con rotación y rechazo del token anterior comprobados; arranque de proceso con expiración y cifrado Android comprobados. Escenarios restantes y aceptación integral pendientes. [Informe real](../../../docs/integracion/verificacion-telefono-2026-11-04.md). No cerrar casillas integrales por un caso positivo ni transferir el PASS de fixtures a proveedor.


## Preparación para commits — 2026-11-04

Se conserva el resultado real del candidato físico y se prepara incorporación por ramas dependientes. Commit y compilación no sustituyen aceptación de todos los AC; las casillas sin evidencia completa siguen abiertas. El historial inferior no significa que la implementación actual sea solo documental. Registro de ramas y comprobaciones en docs/preparacion/entrega-por-ramas.md.
