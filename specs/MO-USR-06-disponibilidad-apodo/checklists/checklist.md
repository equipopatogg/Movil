# Checklist — MO-USR-06

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-USR-06-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Apodo existente con otra capitalización se presenta como ocupado sin identificar al dueño.

**Tareas:** `MO-USR-06-T4`, `MO-USR-06-T5` y `MO-USR-06-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Respuesta occupied/available=false se procesa sin Bearer; estado muestra ocupado para un resultado falso. Ampliación actual: USR_06_existingNameWithDifferentCaseIsShownOccupiedWithoutAuthentication.

- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `availabilityIsPublicAndRateLimitDoesNotLoop` · PASS JVM
- [NicknameQueriesTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/NicknameQueriesTest.kt) · `cancelledSlowResponseCannotOverwriteNewNameResult` · PASS JVM
- [CheckNicknameApiTest](../../../app/src/test/java/com/pactogg/app/feature/profile/nickname/CheckNicknameApiTest.kt) · `USR_06_existingNameWithDifferentCaseIsShownOccupiedWithoutAuthentication` · PASS JVM

**Pendiente/límite:** Fixture de nombre con distinta capitalización informa ocupado sin autenticación ni datos del dueño; política case-insensitive del proveedor real no se acredita.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-USR-06-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Apodo libre muestra disponible; formato inválido no provoca consultas innecesarias.

**Tareas:** `MO-USR-06-T4`, `MO-USR-06-T5` y `MO-USR-06-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Apodo inválido no consulta y uno válido espera antes de consultar; resultado libre se refleja como disponible en estado.

- [CheckNicknameValidationTest](../../../app/src/test/java/com/pactogg/app/feature/profile/nickname/CheckNicknameValidationTest.kt) · `USR_06_rejectsNicknameWithSpacesBeforeRequest` · PASS JVM
- [NicknameQueriesTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/NicknameQueriesTest.kt) · `typingWaitsAndInvalidNameNeverQueries` · PASS JVM

**Pendiente/límite:** Falta assert de representación real en Compose para esos estados y disponibilidad libre contra proveedor en el candidato actual.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-USR-06-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Respuestas fuera de orden no cambian disponibilidad del texto actual; 429 no inicia bucle ni bloquea permanentemente edición.

**Tareas:** `MO-USR-06-T4`, `MO-USR-06-T5` y `MO-USR-06-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Respuesta cancelada/tardía no sobreescribe consulta nueva; 429 añade espera sin polling ni bucle. Ampliación actual: elapsedCooldownAllowsNewQueryAndEditingWithoutAutomaticPolling.

- [NicknameQueriesTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/NicknameQueriesTest.kt) · `cancelledSlowResponseCannotOverwriteNewNameResult` · PASS JVM
- [NicknameQueriesTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/NicknameQueriesTest.kt) · `rateLimitAddsCooldownWithoutPolling` · PASS JVM
- [CheckNicknameSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/profile/nickname/CheckNicknameSecurityTest.kt) · `USR_06_rateLimitDoesNotRetryCheckNickname` · PASS JVM
- [NicknameQueriesTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/NicknameQueriesTest.kt) · `elapsedCooldownAllowsNewQueryAndEditingWithoutAutomaticPolling` · PASS JVM

**Pendiente/límite:** Se comprueban bloqueo con deadline futuro, edición y nueva consulta con deadline pasado, sin polling. Se simula deadline vencido; no se esperaron 60 segundos reales ni se ensayó Compose.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-USR-06-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-USR-06-T4`, `MO-USR-06-T5` y `MO-USR-06-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [CheckNicknameValidationTest](../../../app/src/test/java/com/pactogg/app/feature/profile/nickname/CheckNicknameValidationTest.kt) · `USR_06_rejectsNicknameWithSpacesBeforeRequest` · PASS JVM
- [CheckNicknameApiTest](../../../app/src/test/java/com/pactogg/app/feature/profile/nickname/CheckNicknameApiTest.kt) · `USR_06_sendsNicknameAvailabilityQuery` · PASS JVM
- [CheckNicknameSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/profile/nickname/CheckNicknameSecurityTest.kt) · `USR_06_publicRateLimitDoesNotEraseExistingSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-USR-06

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-USR-06-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Apodo existente con otra capitalización se presenta como ocupado sin identificar al dueño.

**Tareas:** `MO-USR-06-T4`, `MO-USR-06-T5` y `MO-USR-06-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Respuesta occupied/available=false se procesa sin Bearer; estado muestra ocupado para un resultado falso.

- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `availabilityIsPublicAndRateLimitDoesNotLoop` · PASS JVM
- [NicknameQueriesTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/NicknameQueriesTest.kt) · `cancelledSlowResponseCannotOverwriteNewNameResult` · PASS JVM

**Falta:** Faltan nombre existente con distinta capitalización y prueba de no exponer al dueño. No asumir case-insensitive a partir de mismo nombre exacto.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-USR-06-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Apodo libre muestra disponible; formato inválido no provoca consultas innecesarias.

**Tareas:** `MO-USR-06-T4`, `MO-USR-06-T5` y `MO-USR-06-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Apodo inválido no consulta y uno válido espera antes de consultar; resultado libre se refleja como disponible en estado.

- [CheckNicknameValidationTest](../../../app/src/test/java/com/pactogg/app/feature/profile/nickname/CheckNicknameValidationTest.kt) · `USR_06_rejectsNicknameWithSpacesBeforeRequest` · PASS JVM
- [NicknameQueriesTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/NicknameQueriesTest.kt) · `typingWaitsAndInvalidNameNeverQueries` · PASS JVM

**Falta:** Falta assert de representación real en Compose para esos estados y disponibilidad libre contra proveedor en el candidato actual.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-USR-06-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Respuestas fuera de orden no cambian disponibilidad del texto actual; 429 no inicia bucle ni bloquea permanentemente edición.

**Tareas:** `MO-USR-06-T4`, `MO-USR-06-T5` y `MO-USR-06-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Respuesta cancelada/tardía no sobreescribe consulta nueva; 429 añade espera sin polling ni bucle.

- [NicknameQueriesTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/NicknameQueriesTest.kt) · `cancelledSlowResponseCannotOverwriteNewNameResult` · PASS JVM
- [NicknameQueriesTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/NicknameQueriesTest.kt) · `rateLimitAddsCooldownWithoutPolling` · PASS JVM
- [CheckNicknameSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/profile/nickname/CheckNicknameSecurityTest.kt) · `USR_06_rateLimitDoesNotRetryCheckNickname` · PASS JVM

**Falta:** Falta recuperar consulta al acabar cooldown y comprobar que edición sigue operativa; avanzar tiempo virtual no demuestra fin de espera basada en reloj real.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-USR-06-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-USR-06-T4`, `MO-USR-06-T5` y `MO-USR-06-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [CheckNicknameValidationTest](../../../app/src/test/java/com/pactogg/app/feature/profile/nickname/CheckNicknameValidationTest.kt) · `USR_06_rejectsNicknameWithSpacesBeforeRequest` · PASS JVM
- [CheckNicknameApiTest](../../../app/src/test/java/com/pactogg/app/feature/profile/nickname/CheckNicknameApiTest.kt) · `USR_06_sendsNicknameAvailabilityQuery` · PASS JVM
- [CheckNicknameSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/profile/nickname/CheckNicknameSecurityTest.kt) · `USR_06_publicRateLimitDoesNotEraseExistingSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla. Falta accesibilidad del recorrido específico.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado: implementación local presente; recorrido positivo real y controles locales correctos; aceptación integral pendiente. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-USR-06-AC1: Apodo existente con otra capitalización se presenta como ocupado sin identificar al dueño.
- [ ] MO-USR-06-AC2: Apodo libre muestra disponible; formato inválido no provoca consultas innecesarias.
- [ ] MO-USR-06-AC3: Respuestas fuera de orden no cambian disponibilidad del texto actual; 429 no inicia bucle ni bloquea permanentemente edición.
- [ ] MO-USR-06-ACQ: controles y evidencia del mismo candidato verificados.
- [ ] Bloqueantes específicos de sección 7 resueltos para las tareas afectadas.
- [ ] Prueba real del proveedor distinguida de fixtures; no se hereda PASS del backend.
- [ ] Revisión humana y aceptación de implementación registradas por separado.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación solicitada | Usuario | 2026-11-04 | Operación incluida en Sistema.docx; no prueba funcional |
| Aceptación móvil | Pendiente | Pendiente | No inferir por existencia de documentos |


[Resultado real y candidato](../../../docs/integracion/integracion-social-2026-11-04.md): caso positivo comprobado; no se cierran casillas integrales que contienen escenarios restantes. Datos y límites de la cuenta auxiliar/recorrido UI distinguidos de fixtures.
