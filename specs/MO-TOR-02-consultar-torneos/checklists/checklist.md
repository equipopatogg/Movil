# Checklist — MO-TOR-02

## Resultado local actual — pruebas pendientes, 2026-11-05

239 JVM y 14 Android correctos del candidato UX. Cierre técnico de consumidor separado de aceptación humana/proveedor; sin publicación. [Informe actual](../../../docs/pruebas/pruebas-pendientes-2026-11-05.md).

### MO-TOR-02-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Lista vacía/llena, filtros y páginas respetan meta y no inventan torneos ni nombres.

**Tareas:** `MO-TOR-02-T4`, `MO-TOR-02-T5` y `MO-TOR-02-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Listado vacío, serialización de filtros y deduplicación en estado se prueban; cursor final impide más carga.

- [ListTournamentsApiTest](../../../app/src/test/java/com/pactogg/app/feature/tournaments/list/ListTournamentsApiTest.kt) · `TOR_02_sendsGameFilterForTournamentListing` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `registrationGroupContractEvidenceTournamentsAndLogoutUseRealWireContract` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `tournamentsAreDeduplicatedAndNoMoreMeansNoRequest` · PASS JVM

**Pendiente/límite:** Faltan lista llena con varios datos, has_more=true/siguiente página y correspondencia visual con proveedor real; no usar pruebas del prototipo Explorar como sustituto.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-TOR-02-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Cambio rápido de filtro y cuenta no mezcla páginas ni datos previos; cursor final detiene carga.

**Tareas:** `MO-TOR-02-T4`, `MO-TOR-02-T5` y `MO-TOR-02-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Cambio de cuenta reinicia estado y no se solicita más al llegar al final.

- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `changingAccountClearsGroupDraftFiltersMessagesAndCredentials` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `tournamentsAreDeduplicatedAndNoMoreMeansNoRequest` · PASS JVM

**Pendiente/límite:** Falta dos filtros con respuestas fuera de orden y cambio de cuenta con página en vuelo; no deducirlo de deduplicación.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-TOR-02-AC3

**Estado técnico: COMPROBADO_LOCAL_CONSUMIDOR.**
**Condición original:** Bracket y partido muestran datos reales solo lectura; no hay acceso a crear/inscribir/reportar.

**Tareas:** `MO-TOR-02-T4`, `MO-TOR-02-T5` y `MO-TOR-02-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** Fixture no vacía mapea ronda, participantes y puntuación; componente Compose muestra ronda/partido/marcadores y ausencia de acciones de escritura. No acredita datos del despliegue real.

- [ListTournamentsApiTest](../../../app/src/test/java/com/pactogg/app/feature/tournaments/list/ListTournamentsApiTest.kt) · `TOR_02_nonemptyBracketPreservesRoundsParticipantsAndScoresWithoutWrites` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `nonemptyBracketSelectionIsReadOnlyAndCanReturnToList` · PASS JVM
- [TournamentBracketTest](../../../app/src/androidTest/java/com/pactogg/app/feature/connected/presentation/TournamentBracketTest.kt) · `nonemptyBracketShowsRoundParticipantsScoresAndNoWriteActions` · PASS ANDROID

**Pendiente/límite:** Aceptación humana y del despliegue real separadas; no ejecutadas por esta fase local.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-TOR-02-AC4

**Estado técnico: PARCIAL.**
**Condición original:** 401, 403, 422 y red caída muestran estados diferenciados; 403 no borra sesión.

**Tareas:** `MO-TOR-02-T4`, `MO-TOR-02-T5` y `MO-TOR-02-T4-AC4` en [tasks.md](../tasks.md).

**Lo comprobado:** 401 de torneos sigue renovación; 403 conserva sesión; status inválido se bloquea antes de red. Ampliación actual: TOR_02_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity, TOR_02_lostResponseIsNotReplayedOrReportedAsSuccess.

- [ListTournamentsSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/tournaments/list/ListTournamentsSecurityTest.kt) · `TOR_02_forbiddenListingPreservesExistingSession` · PASS JVM
- [ListTournamentsValidationTest](../../../app/src/test/java/com/pactogg/app/feature/tournaments/list/ListTournamentsValidationTest.kt) · `TOR_02_rejectsUnknownTournamentStatus` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `unauthorizedReadRefreshesAndRetriesOnlyOnce` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `forbiddenDoesNotRefreshOrEraseSessionAndHandlesArrayDetails` · PASS JVM
- [ListTournamentsApiTest](../../../app/src/test/java/com/pactogg/app/feature/tournaments/list/ListTournamentsApiTest.kt) · `TOR_02_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` · PASS JVM
- [ListTournamentsApiTest](../../../app/src/test/java/com/pactogg/app/feature/tournaments/list/ListTournamentsApiTest.kt) · `TOR_02_lostResponseIsNotReplayedOrReportedAsSuccess` · PASS JVM

**Pendiente/límite:** 422/desconexión no se convierten en lista vacía exitosa ni reintentan; falta comparación visual de estados de error.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

### MO-TOR-02-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente

**Tareas:** `MO-TOR-02-T4`, `MO-TOR-02-T5` y `MO-TOR-02-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [ListTournamentsValidationTest](../../../app/src/test/java/com/pactogg/app/feature/tournaments/list/ListTournamentsValidationTest.kt) · `TOR_02_rejectsUnknownTournamentStatus` · PASS JVM
- [ListTournamentsApiTest](../../../app/src/test/java/com/pactogg/app/feature/tournaments/list/ListTournamentsApiTest.kt) · `TOR_02_sendsGameFilterForTournamentListing` · PASS JVM
- [ListTournamentsSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/tournaments/list/ListTournamentsSecurityTest.kt) · `TOR_02_forbiddenListingPreservesExistingSession` · PASS JVM

**Pendiente/límite:** Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes.

**Registro:** [casos, informes y hashes](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

# Checklist — MO-TOR-02

## Revisión criterio por criterio — 2026-11-05

Resultados existentes comprobados y vinculados; no se ejecutaron nuevas pruebas por esta revisión documental. PASS de un caso respalda únicamente el alcance descrito. Aceptación funcional/humana separada.

#### Histórico MO-TOR-02-AC1

**Estado técnico: PARCIAL.**
**Condición original:** Lista vacía/llena, filtros y páginas respetan meta y no inventan torneos ni nombres.

**Tareas:** `MO-TOR-02-T4`, `MO-TOR-02-T5` y `MO-TOR-02-T4-AC1` en [tasks.md](../tasks.md).

**Lo comprobado:** Listado vacío, serialización de filtros y deduplicación en estado se prueban; cursor final impide más carga.

- [ListTournamentsApiTest](../../../app/src/test/java/com/pactogg/app/feature/tournaments/list/ListTournamentsApiTest.kt) · `TOR_02_sendsGameFilterForTournamentListing` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `registrationGroupContractEvidenceTournamentsAndLogoutUseRealWireContract` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `tournamentsAreDeduplicatedAndNoMoreMeansNoRequest` · PASS JVM

**Falta:** Faltan lista llena con varios datos, has_more=true/siguiente página y correspondencia visual con proveedor real; no usar pruebas del prototipo Explorar como sustituto.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-TOR-02-AC2

**Estado técnico: PARCIAL.**
**Condición original:** Cambio rápido de filtro y cuenta no mezcla páginas ni datos previos; cursor final detiene carga.

**Tareas:** `MO-TOR-02-T4`, `MO-TOR-02-T5` y `MO-TOR-02-T4-AC2` en [tasks.md](../tasks.md).

**Lo comprobado:** Cambio de cuenta reinicia estado y no se solicita más al llegar al final.

- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `changingAccountClearsGroupDraftFiltersMessagesAndCredentials` · PASS JVM
- [ConnectedFlowTest](../../../app/src/test/java/com/pactogg/app/feature/connected/presentation/ConnectedFlowTest.kt) · `tournamentsAreDeduplicatedAndNoMoreMeansNoRequest` · PASS JVM

**Falta:** Falta dos filtros con respuestas fuera de orden y cambio de cuenta con página en vuelo; no deducirlo de deduplicación.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-TOR-02-AC3

**Estado técnico: SIN_CASO_ESPECIFICO.**
**Condición original:** Bracket y partido muestran datos reales solo lectura; no hay acceso a crear/inscribir/reportar.

**Tareas:** `MO-TOR-02-T4`, `MO-TOR-02-T5` y `MO-TOR-02-T4-AC3` en [tasks.md](../tasks.md).

**Lo comprobado:** No se encontró prueba de visualización de bracket/partido: el fixture de listado contiene matches vacío y el test de API solo comprueba el filtro game.

No se encontró un caso específico que ejercite la secuencia completa indicada.

**Falta:** Preparar bracket no vacío con partidos/datos y comprobar UI de solo lectura, sin acciones crear/inscribir/reportar.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-TOR-02-AC4

**Estado técnico: PARCIAL.**
**Condición original:** 401, 403, 422 y red caída muestran estados diferenciados; 403 no borra sesión.

**Tareas:** `MO-TOR-02-T4`, `MO-TOR-02-T5` y `MO-TOR-02-T4-AC4` en [tasks.md](../tasks.md).

**Lo comprobado:** 401 de torneos sigue renovación; 403 conserva sesión; status inválido se bloquea antes de red.

- [ListTournamentsSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/tournaments/list/ListTournamentsSecurityTest.kt) · `TOR_02_forbiddenListingPreservesExistingSession` · PASS JVM
- [ListTournamentsValidationTest](../../../app/src/test/java/com/pactogg/app/feature/tournaments/list/ListTournamentsValidationTest.kt) · `TOR_02_rejectsUnknownTournamentStatus` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `unauthorizedReadRefreshesAndRetriesOnlyOnce` · PASS JVM
- [ApiIntegrationTest](../../../app/src/test/java/com/pactogg/app/integration/ApiIntegrationTest.kt) · `forbiddenDoesNotRefreshOrEraseSessionAndHandlesArrayDetails` · PASS JVM

**Falta:** Faltan fixture 422 y desconexión real de torneos con mensajes diferenciados; 503 no equivale a transporte caído.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

#### Histórico MO-TOR-02-ACQ

**Estado técnico: CONTROLES_LOCALES_PARCIALES.**
**Condición original:** Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente

**Tareas:** `MO-TOR-02-T4`, `MO-TOR-02-T5` y `MO-TOR-02-T5-ACQ` en [tasks.md](../tasks.md).

**Lo comprobado:** Suite local 196, categorías 56/44/96, 27 verificaciones de integridad, compilación de pruebas, contraste de producción intacta y revisión de fuentes sin secretos registrados.

- [ListTournamentsValidationTest](../../../app/src/test/java/com/pactogg/app/feature/tournaments/list/ListTournamentsValidationTest.kt) · `TOR_02_rejectsUnknownTournamentStatus` · PASS JVM
- [ListTournamentsApiTest](../../../app/src/test/java/com/pactogg/app/feature/tournaments/list/ListTournamentsApiTest.kt) · `TOR_02_sendsGameFilterForTournamentListing` · PASS JVM
- [ListTournamentsSecurityTest](../../../app/src/test/java/com/pactogg/app/feature/tournaments/list/ListTournamentsSecurityTest.kt) · `TOR_02_forbiddenListingPreservesExistingSession` · PASS JVM

**Falta:** Los resultados de CI remoto/SAST/SCA anteriores no se atribuyen a este árbol local. Entrega remota diferida por el usuario; completar controles del candidato al autorizarla.

**Resultado/registro:** [evidencia y límites](../../../docs/pruebas/revision-specs-2026-11-05.md#evidencia-comprobada). Los archivos JUnit y sus hashes se identifican en la [trazabilidad estructurada](../../../docs/pruebas/trazabilidad-criterios.json).

## Historial anterior conservado

Estado vigente: **Consulta vacía verificada; datos/bracket/paginación reales pendientes**. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-TOR-02-AC1: Lista vacía/llena, filtros y páginas respetan meta y no inventan torneos ni nombres.
- [ ] MO-TOR-02-AC2: Cambio rápido de filtro y cuenta no mezcla páginas ni datos previos; cursor final detiene carga.
- [ ] MO-TOR-02-AC3: Bracket y partido muestran datos reales solo lectura; no hay acceso a crear/inscribir/reportar.
- [ ] MO-TOR-02-AC4: 401, 403, 422 y red caída muestran estados diferenciados; 403 no borra sesión.
- [ ] MO-TOR-02-ACQ: controles del candidato y evidencia válidos.
- [ ] Entradas/bloqueantes de sección 7 resueltos para las tareas afectadas.
- [ ] Revisión humana de spec y aceptación funcional diferenciadas.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Pendiente | Por designar | Pendiente | Documentación preparada, sin pruebas de integración ejecutadas |


## Avance verificado local — 2026-11-04

Implementación de consumidor presente. [Evidencia del candidato](../../../docs/integracion/verificacion-consumidor-2026-11-04.md): debug, 24 JVM, Lint/Detekt y Gitleaks correctos; androidTest compilado, no ejecutado. No marcar los AC integrales como PASS: faltan proveedor/cuentas autorizadas, ejecución Android y aceptación aplicable. La tabla histórica NO_EJECUTADO no significa ausencia de código actual.


## Verificación física posterior — 2026-11-04

Consulta real correcta con lista vacía; bracket/paginación con datos del proveedor y aceptación permanecen pendientes. [Informe real](../../../docs/integracion/verificacion-telefono-2026-11-04.md). No cerrar casillas integrales por un caso positivo ni transferir el PASS de fixtures a proveedor.


## Preparación para commits — 2026-11-04

Se conserva el resultado real del candidato físico y se prepara incorporación por ramas dependientes. Commit y compilación no sustituyen aceptación de todos los AC; las casillas sin evidencia completa siguen abiertas. El historial inferior no significa que la implementación actual sea solo documental. Registro de ramas y comprobaciones en docs/preparacion/entrega-por-ramas.md.
