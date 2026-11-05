# Checklist — MO-USR-03

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-USR-03-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Cambio válido recibe 200 y GET lo refleja; sesión continúa con los mismos tokens vigentes.

**Tareas:** `MO-USR-03-T4`, `MO-USR-03-T5` y `MO-USR-03-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** PATCH envía solo apodo, guarda identidad y conserva par de tokens; estado refleja perfil confirmado. Ampliación actual: USR_03_occupiedNicknamePreservesIdentityAndCanBeCorrected.

- [EditProfileApiTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileApiTest.kt) · `USR_03_sendsOnlyNicknameAndUpdatesVisibleIdentity` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `ownProfileAndPatchPreserveCredentialsAndPersistIdentity` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `profileReadAndUpdateUseServerIdentity` · PASS JVM
- [EditProfileApiTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileApiTest.kt) · `USR_03_occupiedNicknamePreservesIdentityAndCanBeCorrected` · PASS JVM

**Pendiente/límite:** PATCH→GET y tokens conservados comprobados; falta recorrido Compose completo. Avatar sigue diferido.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-USR-03-AC2

**Estado técnico: COMPROBADO_LOCAL_CONSUMIDOR.**
**Condición original:** Apodo ocupado devuelve 409 y permite corregir sin perder formulario.

**Tareas:** `MO-USR-03-T4`, `MO-USR-03-T5` y `MO-USR-03-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** PATCH 409 conserva identidad/tokens; formulario mantiene apodo editable y permite corregir; PATCH confirmado y GET posterior reflejan identidad nueva.

- [EditProfileApiTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileApiTest.kt) · `USR_03_occupiedNicknamePreservesIdentityAndCanBeCorrected` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `occupiedNicknameKeepsFormAndIdentityUntilCorrectedSuccessfully` · PASS JVM

Apoyo indirecto (no sustituye la condición): `com.pactogg.app.core.network.ApiErrorMapperTest#mapsSecurityAndRetryStatesWithoutUsingServerText`.

**Pendiente/límite:** Aceptación humana y del despliegue real separadas; no ejecutadas por esta fase local.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-USR-03-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Cuerpo vacío/campo desconocido no se presenta como éxito; verificar 422/400 del proveedor con fixture.

**Tareas:** `MO-USR-03-T4`, `MO-USR-03-T5` y `MO-USR-03-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** El cliente serializa solo nickname, bloquea longitud excesiva y rechaza respuesta de éxito malformada. Ampliación actual: USR_03_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity.

- [EditProfileApiTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileApiTest.kt) · `USR_03_sendsOnlyNicknameAndUpdatesVisibleIdentity` · PASS JVM
- [EditProfileSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileSecurityTest.kt) · `USR_03_malformedEditProfileSuccessIsRejected` · PASS JVM
- [EditProfileValidationTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileValidationTest.kt) · `USR_03_rejectsOversizedNicknameBeforeUpdate` · PASS JVM
- [EditProfileApiTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileApiTest.kt) · `USR_03_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM

**Pendiente/límite:** Fixtures 400/422 no generan éxito ni sustituyen identidad; no prueban validación del servidor con cuerpos vacío/desconocido.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-USR-03-AC4

**Estado técnico: PARCIAL.**
**Condición original:** Moderación rechazada o no disponible conserva perfil anterior confirmado y muestra error seguro.

**Tareas:** `MO-USR-03-T4`, `MO-USR-03-T5` y `MO-USR-03-T4-AC4` en [tasks.md](../tasks.md).

**Lo comprobado:** 503 del consumidor no reintenta ni elimina la custodia. Ampliación actual: USR_03_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity.

- [EditProfileSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileSecurityTest.kt) · `USR_03_serviceFailureDoesNotRetryEditProfile` · PASS JVM
- [EditProfileApiTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileApiTest.kt) · `USR_03_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM

**Pendiente/límite:** CONTENT_REJECTED/MODERATION_UNAVAILABLE de PATCH conservan identidad en repositorio; falta formulario Compose para ambos errores.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-USR-03-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-USR-03-T4`, `MO-USR-03-T5` y `MO-USR-03-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [EditProfileValidationTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileValidationTest.kt) · `USR_03_rejectsOversizedNicknameBeforeUpdate` · PASS JVM
- [EditProfileApiTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileApiTest.kt) · `USR_03_sendsOnlyNicknameAndUpdatesVisibleIdentity` · PASS JVM
- [EditProfileSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileSecurityTest.kt) · `USR_03_forbiddenUpdatePreservesExistingSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-USR-03

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-USR-03-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Cambio válido recibe 200 y GET lo refleja; sesión continúa con los mismos tokens vigentes.

**Tareas:** `MO-USR-03-T4`, `MO-USR-03-T5` y `MO-USR-03-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** PATCH envía solo apodo, guarda identidad y conserva par de tokens; estado refleja perfil confirmado.

- [EditProfileApiTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileApiTest.kt) · `USR_03_sendsOnlyNicknameAndUpdatesVisibleIdentity` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `ownProfileAndPatchPreserveCredentialsAndPersistIdentity` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `profileReadAndUpdateUseServerIdentity` · PASS JVM

**Falta:** Falta GET posterior a PATCH en prueba actual y su recorrido completo. Avatar queda diferido hasta catálogo backend.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-USR-03-AC2

**Estado técnico: SIN_CASO_ESPECIFICO.**
**Condición original:** Apodo ocupado devuelve 409 y permite corregir sin perder formulario.

**Tareas:** `MO-USR-03-T4`, `MO-USR-03-T5` y `MO-USR-03-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** No hay caso específico de NICKNAME_ALREADY_EXISTS/409 en PATCH ni recuperación de su formulario.

No se encontró un caso específico que ejercite la secuencia completa indicada.

**Apoyo indirecto; no demuestra por sí solo el criterio:**

- [ApiErrorMapperTest](../../../app/src/test/java/com/pactogg/app/core/network/ApiErrorMapperTest.kt) · `mapsSecurityAndRetryStatesWithoutUsingServerText` · PASS JVM

**Falta:** Añadir 409 de actualización de apodo y verificar identidad anterior, texto editable y ausencia de éxito.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-USR-03-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Cuerpo vacío/campo desconocido no se presenta como éxito; verificar 422/400 del proveedor con fixture.

**Tareas:** `MO-USR-03-T4`, `MO-USR-03-T5` y `MO-USR-03-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** El cliente serializa solo nickname, bloquea longitud excesiva y rechaza respuesta de éxito malformada.

- [EditProfileApiTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileApiTest.kt) · `USR_03_sendsOnlyNicknameAndUpdatesVisibleIdentity` · PASS JVM
- [EditProfileSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileSecurityTest.kt) · `USR_03_malformedEditProfileSuccessIsRejected` · PASS JVM
- [EditProfileValidationTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileValidationTest.kt) · `USR_03_rejectsOversizedNicknameBeforeUpdate` · PASS JVM

**Falta:** Respuesta malformada no equivale a cuerpo de solicitud vacío/campo desconocido: faltan fixtures 400/422 para esos casos.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-USR-03-AC4

**Estado técnico: PARCIAL.**
**Condición original:** Moderación rechazada o no disponible conserva perfil anterior confirmado y muestra error seguro.

**Tareas:** `MO-USR-03-T4`, `MO-USR-03-T5` y `MO-USR-03-T4-AC4` en [tasks.md](../tasks.md).

**Lo comprobado:** 503 del consumidor no reintenta ni elimina la custodia.

- [EditProfileSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileSecurityTest.kt) · `USR_03_serviceFailureDoesNotRetryEditProfile` · PASS JVM

**Falta:** La fixture usa STORAGE_UNAVAILABLE: faltan CONTENT_REJECTED/MODERATION_UNAVAILABLE de apodo y asserts de perfil/formulario anterior intactos.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-USR-03-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-USR-03-T4`, `MO-USR-03-T5` y `MO-USR-03-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [EditProfileValidationTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileValidationTest.kt) · `USR_03_rejectsOversizedNicknameBeforeUpdate` · PASS JVM
- [EditProfileApiTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileApiTest.kt) · `USR_03_sendsOnlyNicknameAndUpdatesVisibleIdentity` · PASS JVM
- [EditProfileSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/profile/edit/EditProfileSecurityTest.kt) · `USR_03_forbiddenUpdatePreservesExistingSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla. Falta accesibilidad del recorrido específico.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado: implementación local presente; recorrido positivo real y controles locales correctos; aceptación integral pendiente. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-USR-03-AC1: Cambio válido recibe 200 y GET lo refleja; sesión continúa con los mismos tokens vigentes.
- [ ] MO-USR-03-AC2: Apodo ocupado devuelve 409 y permite corregir sin perder formulario.
- [ ] MO-USR-03-AC3: Cuerpo vacío/campo desconocido no se presenta como éxito; verificar 422/400 del proveedor con fixture.
- [ ] MO-USR-03-AC4: Moderación rechazada o no disponible conserva perfil anterior confirmado y muestra error seguro.
- [ ] MO-USR-03-ACQ: controles y evidencia del mismo candidato verificados.
- [ ] Bloqueantes específicos de sección 7 resueltos para las tareas afectadas.
- [ ] Prueba real del proveedor distinguida de fixtures; no se hereda PASS del backend.
- [ ] Revisión humana y aceptación de implementación registradas por separado.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación solicitada | Usuario | 2026-11-04 | Operación incluida en Sistema.docx; no prueba funcional |
| Aceptación móvil | Pendiente | Pendiente | No inferir por existencia de documentos |


[Resultado real y candidato](../../../docs/integracion/integracion-social-2026-11-04.md): caso positivo comprobado; no se cierran casillas integrales que contienen escenarios restantes. Datos y límites de la cuenta auxiliar/recorrido UI distinguidos de fixtures.
