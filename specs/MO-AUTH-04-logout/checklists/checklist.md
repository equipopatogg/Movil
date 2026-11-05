# Checklist — MO-AUTH-04

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-AUTH-04-AC1

**Estado técnico: PARCIAL.**
**Condición original:** 204 vacía custodia y estado por cuenta; atrás/reinicio no reabren pantallas protegidas.

**Tareas:** `MO-AUTH-04-T4`, `MO-AUTH-04-T5` y `MO-AUTH-04-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** 204 vacía sesión/custodia; cambio de identidad elimina grupo, contrato, borrador y mensajes.

- [LogoutApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/logout/LogoutApiTest.kt) · `AUTH_04_revokesSessionAndClearsLocalCustody` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `registrationGroupContractEvidenceTournamentsAndLogoutUseRealWireContract` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `changingAccountClearsGroupDraftFiltersMessagesAndCredentials` · PASS JVM
- [EncryptedSessionStoreTest](../../../app/src/androidTest/java/com/pactogg/app/security/EncryptedSessionStoreTest.kt) · `roundTripIsEncryptedAndClearRemovesTheFile` · PASS ANDROID

**Pendiente/límite:** Falta recorrido Android de logout seguido de Atrás y reinicio/muerte del proceso para comprobar pantallas protegidas.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-AUTH-04-AC2

**Estado técnico: PARCIAL.**
**Condición original:** 401, timeout o desconexión limpian localmente y distinguen revocación remota pendiente.

**Tareas:** `MO-AUTH-04-T4`, `MO-AUTH-04-T5` y `MO-AUTH-04-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Logout con 403/503 limpia localmente. El método llamado logoutNetworkFailure usa un HTTP 503, no una desconexión real. Ampliación actual: AUTH_04_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity, AUTH_04_transportDisconnectStillErasesLocalTokensWithoutRetry.

- [LogoutSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/logout/LogoutSecurityTest.kt) · `AUTH_04_rejectedLogoutStillClearsLocalSession` · PASS JVM
- [LogoutSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/logout/LogoutSecurityTest.kt) · `AUTH_04_serviceFailureDoesNotRetryLogout` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `logoutNetworkFailureStillClearsLocalSession` · PASS JVM
- [LogoutApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/logout/LogoutApiTest.kt) · `AUTH_04_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM
- [LogoutApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/logout/LogoutApiTest.kt) · `AUTH_04_transportDisconnectStillErasesLocalTokensWithoutRetry` · PASS JVM

**Pendiente/límite:** 401 y desconexión real cierran localmente sin repetir. Falta timeout y aviso de revocación pendiente comprobado en Compose.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-AUTH-04-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Cambio A→B y refresh concurrente no filtran ni reponen identidad o datos de A.

**Tareas:** `MO-AUTH-04-T4`, `MO-AUTH-04-T5` y `MO-AUTH-04-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Respuesta/errores de A y login pendiente no reemplazan B; cambio de identidad limpia contexto y custodia reabierta conserva B. Ampliación actual: AUTH_03_logoutDuringPendingRefreshEndsWithNoRestoredSession.

- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `lateResponseFromAccountACannotReplaceAccountB` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `pendingLoginCannotUndoLogoutOrOverwriteNextAccount` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `changingAccountClearsGroupDraftFiltersMessagesAndCredentials` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `delayedErrorFromPreviousAccountCannotAppearInNextAccount` · PASS JVM
- [EncryptedSessionStoreTest](../../../app/src/androidTest/java/com/pactogg/app/security/EncryptedSessionStoreTest.kt) · `replacingAccountPersistsOnlyTheNewIdentity` · PASS ANDROID
- [RefreshSessionApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/session/RefreshSessionApiTest.kt) · `AUTH_03_logoutDuringPendingRefreshEndsWithNoRestoredSession` · PASS JVM

**Pendiente/límite:** Refresh pendiente y logout sin resurrección comprobados; falta combinación con login B y navegación Android de ambas cuentas.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-AUTH-04-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente

**Tareas:** `MO-AUTH-04-T4`, `MO-AUTH-04-T5` y `MO-AUTH-04-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [LogoutValidationTest](../../../app/src/test/java/com/pactogg/app/feature/auth/logout/LogoutValidationTest.kt) · `AUTH_04_serializesRefreshTokenWithoutPassword` · PASS JVM
- [LogoutApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/logout/LogoutApiTest.kt) · `AUTH_04_revokesSessionAndClearsLocalCustody` · PASS JVM
- [LogoutSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/logout/LogoutSecurityTest.kt) · `AUTH_04_rejectedLogoutStillClearsLocalSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-AUTH-04

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-AUTH-04-AC1

**Estado técnico: PARCIAL.**
**Condición original:** 204 vacía custodia y estado por cuenta; atrás/reinicio no reabren pantallas protegidas.

**Tareas:** `MO-AUTH-04-T4`, `MO-AUTH-04-T5` y `MO-AUTH-04-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** 204 vacía sesión/custodia; cambio de identidad elimina grupo, contrato, borrador y mensajes.

- [LogoutApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/logout/LogoutApiTest.kt) · `AUTH_04_revokesSessionAndClearsLocalCustody` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `registrationGroupContractEvidenceTournamentsAndLogoutUseRealWireContract` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `changingAccountClearsGroupDraftFiltersMessagesAndCredentials` · PASS JVM
- [EncryptedSessionStoreTest](../../../app/src/androidTest/java/com/pactogg/app/security/EncryptedSessionStoreTest.kt) · `roundTripIsEncryptedAndClearRemovesTheFile` · PASS ANDROID

**Falta:** Falta recorrido Android de logout seguido de Atrás y reinicio/muerte del proceso para comprobar pantallas protegidas.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-AUTH-04-AC2

**Estado técnico: PARCIAL.**
**Condición original:** 401, timeout o desconexión limpian localmente y distinguen revocación remota pendiente.

**Tareas:** `MO-AUTH-04-T4`, `MO-AUTH-04-T5` y `MO-AUTH-04-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Logout con 403/503 limpia localmente. El método llamado logoutNetworkFailure usa un HTTP 503, no una desconexión real.

- [LogoutSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/logout/LogoutSecurityTest.kt) · `AUTH_04_rejectedLogoutStillClearsLocalSession` · PASS JVM
- [LogoutSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/logout/LogoutSecurityTest.kt) · `AUTH_04_serviceFailureDoesNotRetryLogout` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `logoutNetworkFailureStillClearsLocalSession` · PASS JVM

**Falta:** Faltan logout 401, timeout/desconexión de transporte y aviso UI de revocación pendiente.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-AUTH-04-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Cambio A→B y refresh concurrente no filtran ni reponen identidad o datos de A.

**Tareas:** `MO-AUTH-04-T4`, `MO-AUTH-04-T5` y `MO-AUTH-04-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Respuesta/errores de A y login pendiente no reemplazan B; cambio de identidad limpia contexto y custodia reabierta conserva B.

- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `lateResponseFromAccountACannotReplaceAccountB` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `pendingLoginCannotUndoLogoutOrOverwriteNextAccount` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `changingAccountClearsGroupDraftFiltersMessagesAndCredentials` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `delayedErrorFromPreviousAccountCannotAppearInNextAccount` · PASS JVM
- [EncryptedSessionStoreTest](../../../app/src/androidTest/java/com/pactogg/app/security/EncryptedSessionStoreTest.kt) · `replacingAccountPersistsOnlyTheNewIdentity` · PASS ANDROID

**Falta:** Falta combinación de refresh concurrente con logout/login B y recorrido UI autenticado entre ambas cuentas.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-AUTH-04-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente

**Tareas:** `MO-AUTH-04-T4`, `MO-AUTH-04-T5` y `MO-AUTH-04-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [LogoutValidationTest](../../../app/src/test/java/com/pactogg/app/feature/auth/logout/LogoutValidationTest.kt) · `AUTH_04_serializesRefreshTokenWithoutPassword` · PASS JVM
- [LogoutApiTest](../../../app/src/test/java/com/pactogg/app/feature/auth/logout/LogoutApiTest.kt) · `AUTH_04_revokesSessionAndClearsLocalCustody` · PASS JVM
- [LogoutSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/auth/logout/LogoutSecurityTest.kt) · `AUTH_04_rejectedLogoutStillClearsLocalSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado vigente: **Logout y nuevo ingreso verificados; escenarios restantes pendientes**. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-AUTH-04-AC1: 204 vacía custodia y estado por cuenta; atrás/reinicio no reabren pantallas protegidas.
- [ ] MO-AUTH-04-AC2: 401, timeout o desconexión limpian localmente y distinguen revocación remota pendiente.
- [ ] MO-AUTH-04-AC3: Cambio A→B y refresh concurrente no filtran ni reponen identidad o datos de A.
- [ ] MO-AUTH-04-ACQ: controles del candidato y evidencia válidos.
- [ ] Entradas/bloqueantes de sección 7 resueltos para las tareas afectadas.
- [ ] Revisión humana de spec y aceptación funcional diferenciadas.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Pendiente | Por designar | Pendiente | Documentación preparada, sin pruebas de integración ejecutadas |


## Avance verificado local — 2026-11-04

Implementación de consumidor presente. [Evidencia del candidato](../../../docs/integracion/verificacion-consumidor-2026-11-04.md): debug, 24 JVM, Lint/Detekt y Gitleaks correctos; androidTest compilado, no ejecutado. No marcar los AC integrales como PASS: faltan proveedor/cuentas autorizadas, ejecución Android y aceptación aplicable. La tabla histórica NO_EJECUTADO no significa ausencia de código actual.


## Verificación física posterior — 2026-11-04

Logout desde interfaz y nuevo login comprobados con API real, manteniendo busy hasta finalizar. Logout global y fallos de conectividad reales mantienen pendientes. [Informe real](../../../docs/integracion/verificacion-telefono-2026-11-04.md). No cerrar casillas integrales por un caso positivo ni transferir el PASS de fixtures a proveedor.


## Preparación para commits — 2026-11-04

Se conserva el resultado real del candidato físico y se prepara incorporación por ramas dependientes. Commit y compilación no sustituyen aceptación de todos los AC; las casillas sin evidencia completa siguen abiertas. El historial inferior no significa que la implementación actual sea solo documental. Registro de ramas y comprobaciones en docs/preparacion/entrega-por-ramas.md.
