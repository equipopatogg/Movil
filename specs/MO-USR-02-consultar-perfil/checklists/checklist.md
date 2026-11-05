# Checklist — MO-USR-02

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-USR-02-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Perfil recién registrado muestra estadísticas y cantidad de grupos recibidas; no fijar word_index=50 ni groups_count=0 para todas las cuentas.

**Tareas:** `MO-USR-02-T4`, `MO-USR-02-T5` y `MO-USR-02-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Identidad, estadística fuera de rango y perfil completo en estado se prueban; el perfil fake de estado contiene valores no predeterminados.

- [ProfileDetailsApiTest](../../../app/src/test/java/com/pactogg/app/feature/profile/detail/ProfileDetailsApiTest.kt) · `USR_02_mapsOwnProfileIdentity` · PASS JVM
- [ProfileDetailsValidationTest](../../../app/src/test/java/com/pactogg/app/feature/profile/detail/ProfileDetailsValidationTest.kt) · `USR_02_rejectsOutOfRangeProfileStatistics` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `profileReadAndUpdateUseServerIdentity` · PASS JVM

**Pendiente/límite:** Faltan varios perfiles HTTP con estadísticas/groups_count distintos y asserts Compose de esos valores; caso de recién registrado con datos reales sigue histórico.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-USR-02-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Después de PATCH, la consulta refleja el perfil confirmado sin restaurar datos de sesión antiguos.

**Tareas:** `MO-USR-02-T4`, `MO-USR-02-T5` y `MO-USR-02-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** PATCH confirma apodo/custodia preservando tokens; perfil de otro ID no sustituye identidad. Ampliación actual: USR_03_occupiedNicknamePreservesIdentityAndCanBeCorrected.

- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `ownProfileAndPatchPreserveCredentialsAndPersistIdentity` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `profileReadAndUpdateUseServerIdentity` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `anotherProfileCannotOverwriteSession` · PASS JVM
- [EditProfileApiTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileApiTest.kt) · `USR_03_occupiedNicknamePreservesIdentityAndCanBeCorrected` · PASS JVM

**Pendiente/límite:** Secuencia PATCH→GET comprobada en consumidor; falta respuesta tardía de perfil anterior en la misma cuenta.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-USR-02-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Sin sesión no se muestran datos; un 401 sigue el flujo compartido de sesión.

**Tareas:** `MO-USR-02-T4`, `MO-USR-02-T5` y `MO-USR-02-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Sin sesión no se envía GET de perfil; limpieza de cuenta elimina contexto; tratamiento compartido de 401 se prueba en torneos.

- [ProfileDetailsSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/profile/detail/ProfileDetailsSecurityTest.kt) · `USR_02_missingSessionPreventsProfileDetailsRequest` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `unauthorizedReadRefreshesAndRetriesOnlyOnce` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `changingAccountClearsGroupDraftFiltersMessagesAndCredentials` · PASS JVM

**Pendiente/límite:** Falta 401 del endpoint perfil a través de su UI y prueba que datos anteriores no se muestren al entrar sin sesión.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-USR-02-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-USR-02-T4`, `MO-USR-02-T5` y `MO-USR-02-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [ProfileDetailsValidationTest](../../../app/src/test/java/com/pactogg/app/feature/profile/detail/ProfileDetailsValidationTest.kt) · `USR_02_rejectsOutOfRangeProfileStatistics` · PASS JVM
- [ProfileDetailsApiTest](../../../app/src/test/java/com/pactogg/app/feature/profile/detail/ProfileDetailsApiTest.kt) · `USR_02_mapsOwnProfileIdentity` · PASS JVM
- [ProfileDetailsSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/profile/detail/ProfileDetailsSecurityTest.kt) · `USR_02_forbiddenProfileReadPreservesExistingSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-USR-02

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-USR-02-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Perfil recién registrado muestra estadísticas y cantidad de grupos recibidas; no fijar word_index=50 ni groups_count=0 para todas las cuentas.

**Tareas:** `MO-USR-02-T4`, `MO-USR-02-T5` y `MO-USR-02-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Identidad, estadística fuera de rango y perfil completo en estado se prueban; el perfil fake de estado contiene valores no predeterminados.

- [ProfileDetailsApiTest](../../../app/src/test/java/com/pactogg/app/feature/profile/detail/ProfileDetailsApiTest.kt) · `USR_02_mapsOwnProfileIdentity` · PASS JVM
- [ProfileDetailsValidationTest](../../../app/src/test/java/com/pactogg/app/feature/profile/detail/ProfileDetailsValidationTest.kt) · `USR_02_rejectsOutOfRangeProfileStatistics` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `profileReadAndUpdateUseServerIdentity` · PASS JVM

**Falta:** Faltan varios perfiles HTTP con estadísticas/groups_count distintos y asserts Compose de esos valores; caso de recién registrado con datos reales sigue histórico.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-USR-02-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Después de PATCH, la consulta refleja el perfil confirmado sin restaurar datos de sesión antiguos.

**Tareas:** `MO-USR-02-T4`, `MO-USR-02-T5` y `MO-USR-02-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** PATCH confirma apodo/custodia preservando tokens; perfil de otro ID no sustituye identidad.

- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `ownProfileAndPatchPreserveCredentialsAndPersistIdentity` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `profileReadAndUpdateUseServerIdentity` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `anotherProfileCannotOverwriteSession` · PASS JVM

**Falta:** El test compartido hace GET antes de PATCH, no GET posterior. Falta secuencia PATCH→GET y respuesta tardía de perfil antiguo en esa misma cuenta.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-USR-02-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Sin sesión no se muestran datos; un 401 sigue el flujo compartido de sesión.

**Tareas:** `MO-USR-02-T4`, `MO-USR-02-T5` y `MO-USR-02-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Sin sesión no se envía GET de perfil; limpieza de cuenta elimina contexto; tratamiento compartido de 401 se prueba en torneos.

- [ProfileDetailsSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/profile/detail/ProfileDetailsSecurityTest.kt) · `USR_02_missingSessionPreventsProfileDetailsRequest` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `unauthorizedReadRefreshesAndRetriesOnlyOnce` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `changingAccountClearsGroupDraftFiltersMessagesAndCredentials` · PASS JVM

**Falta:** Falta 401 del endpoint perfil a través de su UI y prueba que datos anteriores no se muestren al entrar sin sesión.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-USR-02-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-USR-02-T4`, `MO-USR-02-T5` y `MO-USR-02-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [ProfileDetailsValidationTest](../../../app/src/test/java/com/pactogg/app/feature/profile/detail/ProfileDetailsValidationTest.kt) · `USR_02_rejectsOutOfRangeProfileStatistics` · PASS JVM
- [ProfileDetailsApiTest](../../../app/src/test/java/com/pactogg/app/feature/profile/detail/ProfileDetailsApiTest.kt) · `USR_02_mapsOwnProfileIdentity` · PASS JVM
- [ProfileDetailsSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/profile/detail/ProfileDetailsSecurityTest.kt) · `USR_02_forbiddenProfileReadPreservesExistingSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla. Falta accesibilidad del recorrido específico.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado: implementación local presente; recorrido positivo real y controles locales correctos; aceptación integral pendiente. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-USR-02-AC1: Perfil recién registrado muestra estadísticas y cantidad de grupos recibidas; no fijar word_index=50 ni groups_count=0 para todas las cuentas.
- [ ] MO-USR-02-AC2: Después de PATCH, la consulta refleja el perfil confirmado sin restaurar datos de sesión antiguos.
- [ ] MO-USR-02-AC3: Sin sesión no se muestran datos; un 401 sigue el flujo compartido de sesión.
- [ ] MO-USR-02-ACQ: controles y evidencia del mismo candidato verificados.
- [ ] Bloqueantes específicos de sección 7 resueltos para las tareas afectadas.
- [ ] Prueba real del proveedor distinguida de fixtures; no se hereda PASS del backend.
- [ ] Revisión humana y aceptación de implementación registradas por separado.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación solicitada | Usuario | 2026-11-04 | Operación incluida en Sistema.docx; no prueba funcional |
| Aceptación móvil | Pendiente | Pendiente | No inferir por existencia de documentos |


[Resultado real y candidato](../../../docs/integracion/integracion-social-2026-11-04.md): caso positivo comprobado; no se cierran casillas integrales que contienen escenarios restantes. Datos y límites de la cuenta auxiliar/recorrido UI distinguidos de fixtures.
