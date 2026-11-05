# Checklist — MO-MEM-01

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-MEM-01-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Código en minúsculas obtiene grupo real; mostrar saldo/rol recibidos sin fijar 100 puntos en Android.

**Tareas:** `MO-MEM-01-T4`, `MO-MEM-01-T5` y `MO-MEM-01-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Código minúsculo se normaliza; rol/saldo y grupo provienen de respuesta; campo se limpia tras confirmación.

- [JoinGroupApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/join/JoinGroupApiTest.kt) · `MEM_01_normalizesInviteCodeBeforeJoining` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `groupListDetailAndJoinUseServerRoleBalanceAndNormalizedCode` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `joinClearsInviteOnlyAfterProviderConfirmation` · PASS JVM

**Pendiente/límite:** Los fixtures de unión usan 100 puntos: falta otro saldo/rol y su visualización para demostrar ausencia de constante local.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-MEM-01-AC2

**Estado técnico: COMPROBADO_LOCAL_CONSUMIDOR.**
**Condición original:** Pertenencia previa produce 409 sin duplicar grupo ni saldo.

**Tareas:** `MO-MEM-01-T4`, `MO-MEM-01-T5` y `MO-MEM-01-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** ALREADY_MEMBER 409 no repite POST ni sustituye sesión. Estado conserva grupo/saldo y permite corregir invitación sin duplicación local.

- [JoinGroupApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/join/JoinGroupApiTest.kt) · `MEM_01_existingMembershipConflictDoesNotRetryOrChangeSession` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `existingMembershipConflictKeepsGroupBalanceAndAllowsCorrection` · PASS JVM

**Pendiente/límite:** Aceptación humana y del despliegue real separadas; no ejecutadas por esta fase local.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-MEM-01-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Código inexistente/formato incorrecto muestra 404/422 y permite corregir.

**Tareas:** `MO-MEM-01-T4`, `MO-MEM-01-T5` y `MO-MEM-01-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Código de cinco caracteres/formato incorrecto se bloquea antes de red. Ampliación actual: MEM_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity.

- [JoinGroupValidationTest](../../../app/src/test/java/com/pactogg/app/feature/groups/join/JoinGroupValidationTest.kt) · `MEM_01_rejectsIncompleteInviteCode` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `invalidFormsNeverCallRepositories` · PASS JVM
- [JoinGroupApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/join/JoinGroupApiTest.kt) · `MEM_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM

**Pendiente/límite:** 404/422 no repiten ni modifican sesión. Corrección probada tras ALREADY_MEMBER; falta interacción específica tras estos dos errores.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-MEM-01-AC4

**Estado técnico: PARCIAL.**
**Condición original:** Grupo lleno/cupo propio o rate limit se distinguen sin reintentos automáticos.

**Tareas:** `MO-MEM-01-T4`, `MO-MEM-01-T5` y `MO-MEM-01-T4-AC4` en [tasks.md](../tasks.md).

**Lo comprobado:** 429 no reintenta; rechazo conserva invitación y cuenta. Ampliación actual: MEM_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity.

- [JoinGroupSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/groups/join/JoinGroupSecurityTest.kt) · `MEM_01_rateLimitDoesNotRetryJoinGroup` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `deniedJoinPreservesInviteAndAccount` · PASS JVM
- [JoinGroupApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/join/JoinGroupApiTest.kt) · `MEM_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM

**Pendiente/límite:** GROUP_FULL/GROUP_LIMIT_REACHED preservan sesión sin éxito/reintento; falta distinguir mensajes de ambos en Compose.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-MEM-01-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-MEM-01-T4`, `MO-MEM-01-T5` y `MO-MEM-01-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [JoinGroupValidationTest](../../../app/src/test/java/com/pactogg/app/feature/groups/join/JoinGroupValidationTest.kt) · `MEM_01_rejectsIncompleteInviteCode` · PASS JVM
- [JoinGroupApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/join/JoinGroupApiTest.kt) · `MEM_01_normalizesInviteCodeBeforeJoining` · PASS JVM
- [JoinGroupSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/groups/join/JoinGroupSecurityTest.kt) · `MEM_01_forbiddenJoinPreservesExistingSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-MEM-01

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-MEM-01-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Código en minúsculas obtiene grupo real; mostrar saldo/rol recibidos sin fijar 100 puntos en Android.

**Tareas:** `MO-MEM-01-T4`, `MO-MEM-01-T5` y `MO-MEM-01-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Código minúsculo se normaliza; rol/saldo y grupo provienen de respuesta; campo se limpia tras confirmación.

- [JoinGroupApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/join/JoinGroupApiTest.kt) · `MEM_01_normalizesInviteCodeBeforeJoining` · PASS JVM
- [SocialApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/SocialApiIntegrationTest.kt) · `groupListDetailAndJoinUseServerRoleBalanceAndNormalizedCode` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `joinClearsInviteOnlyAfterProviderConfirmation` · PASS JVM

**Falta:** Los fixtures de unión usan 100 puntos: falta otro saldo/rol y su visualización para demostrar ausencia de constante local.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-MEM-01-AC2

**Estado técnico: SIN_CASO_ESPECIFICO.**
**Condición original:** Pertenencia previa produce 409 sin duplicar grupo ni saldo.

**Tareas:** `MO-MEM-01-T4`, `MO-MEM-01-T5` y `MO-MEM-01-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** No hay fixture de ALREADY_MEMBER/409 ni assert específico de lista/saldo tras pertenencia previa.

No se encontró un caso específico que ejercite la secuencia completa indicada.

**Falta:** Añadir respuesta 409 de unión y verificar que no duplique grupo/saldo ni borre el contexto válido.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-MEM-01-AC3

**Estado técnico: PARCIAL.**
**Condición original:** Código inexistente/formato incorrecto muestra 404/422 y permite corregir.

**Tareas:** `MO-MEM-01-T4`, `MO-MEM-01-T5` y `MO-MEM-01-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Código de cinco caracteres/formato incorrecto se bloquea antes de red.

- [JoinGroupValidationTest](../../../app/src/test/java/com/pactogg/app/feature/groups/join/JoinGroupValidationTest.kt) · `MEM_01_rejectsIncompleteInviteCode` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `invalidFormsNeverCallRepositories` · PASS JVM

**Falta:** Faltan respuestas 404 y 422 del proveedor y posibilidad real de corregir/reintentar el formulario después de cada una.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-MEM-01-AC4

**Estado técnico: PARCIAL.**
**Condición original:** Grupo lleno/cupo propio o rate limit se distinguen sin reintentos automáticos.

**Tareas:** `MO-MEM-01-T4`, `MO-MEM-01-T5` y `MO-MEM-01-T4-AC4` en [tasks.md](../tasks.md).

**Lo comprobado:** 429 no reintenta; rechazo conserva invitación y cuenta.

- [JoinGroupSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/groups/join/JoinGroupSecurityTest.kt) · `MEM_01_rateLimitDoesNotRetryJoinGroup` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `deniedJoinPreservesInviteAndAccount` · PASS JVM

**Falta:** Faltan GROUP_FULL y GROUP_LIMIT_REACHED con mensajes/estados distintos; rechazo 403 no cubre esos cupos.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-MEM-01-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build/Lint/Detekt y pruebas del candidato afectado correctos; evidencia sin secretos; accesibilidad/privacidad aplicables verificadas

**Tareas:** `MO-MEM-01-T4`, `MO-MEM-01-T5` y `MO-MEM-01-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [JoinGroupValidationTest](../../../app/src/test/java/com/pactogg/app/feature/groups/join/JoinGroupValidationTest.kt) · `MEM_01_rejectsIncompleteInviteCode` · PASS JVM
- [JoinGroupApiTest](../../../app/src/test/java/com/pactogg/app/feature/groups/join/JoinGroupApiTest.kt) · `MEM_01_normalizesInviteCodeBeforeJoining` · PASS JVM
- [JoinGroupSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/groups/join/JoinGroupSecurityTest.kt) · `MEM_01_forbiddenJoinPreservesExistingSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla. Falta accesibilidad del recorrido específico.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado: implementación local presente; recorrido positivo real y controles locales correctos; aceptación integral pendiente. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-MEM-01-AC1: Código en minúsculas obtiene grupo real; mostrar saldo/rol recibidos sin fijar 100 puntos en Android.
- [ ] MO-MEM-01-AC2: Pertenencia previa produce 409 sin duplicar grupo ni saldo.
- [ ] MO-MEM-01-AC3: Código inexistente/formato incorrecto muestra 404/422 y permite corregir.
- [ ] MO-MEM-01-AC4: Grupo lleno/cupo propio o rate limit se distinguen sin reintentos automáticos.
- [ ] MO-MEM-01-ACQ: controles y evidencia del mismo candidato verificados.
- [ ] Bloqueantes específicos de sección 7 resueltos para las tareas afectadas.
- [ ] Prueba real del proveedor distinguida de fixtures; no se hereda PASS del backend.
- [ ] Revisión humana y aceptación de implementación registradas por separado.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación solicitada | Usuario | 2026-11-04 | Operación incluida en Sistema.docx; no prueba funcional |
| Aceptación móvil | Pendiente | Pendiente | No inferir por existencia de documentos |


[Resultado real y candidato](../../../docs/integracion/integracion-social-2026-11-04.md): caso positivo comprobado; no se cierran casillas integrales que contienen escenarios restantes. Datos y límites de la cuenta auxiliar/recorrido UI distinguidos de fixtures.
