# Checklist — PL-04

## Estado al 2026-11-14

Pantallas escritas y compiladas; detekt y lint en verde. Las pruebas JVM listadas pasaron en local el 2026-11-14. Ningún criterio se ha comprobado en un teléfono (T6).

### PL-04-AC1

**Condición:** Crear torneo muestra su código y aparece en la lista.

**Estado técnico: PARCIAL (prueba de red en verde; falta teléfono).**

- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_04_createTournamentSendsSingleEliminationAndReturnsCode` · PASS JVM local 2026-11-14

- [ ] Comprobado en teléfono contra la API 0.12.x

### PL-04-AC2

**Condición:** Otro grupo se inscribe con el código y el organizador lo inicia; aparece el bracket.

**Estado técnico: PARCIAL (prueba de red en verde; falta teléfono).**

- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_04_joinByCodeSendsKeyGroupAndCode` · PASS JVM local 2026-11-14

- [ ] Comprobado en teléfono contra la API 0.12.x

### PL-04-AC3

**Condición:** Reportar con foto, confirmar por el rival y ver al ganador avanzar.

**Estado técnico: PARCIAL (prueba de red en verde; falta teléfono).**

- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_04_reportIsMultipartWithScoresPhotoAndGroup` · PASS JVM local 2026-11-14
- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_04_nullNextMatchMeansNoMatch` · PASS JVM local 2026-11-14
- [PlayValidationTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayValidationTest.kt) · `PL_04_tiesOversizedPhotosAndBadTournamentDataAreRejected` · PASS JVM local 2026-11-14

- [ ] Comprobado en teléfono contra la API 0.12.x

### PL-04-AC4

**Condición:** Disputar y que el organizador resuelva; al terminar la final el torneo queda `finished`.

**Estado técnico: PARCIAL (prueba de red en verde; falta teléfono).**

- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_04_matchErrorKeepsCodeWithoutRetryOrServerText` · PASS JVM local 2026-11-14

- [ ] Comprobado en teléfono contra la API 0.12.x
