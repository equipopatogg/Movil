# Checklist — PL-04

## Estado al 2026-11-14

Pantallas escritas y compiladas; detekt y lint en verde. Las pruebas JVM listadas son del 2026-11-14 y **aún no se han ejecutado**: cada una pasa a PASS solo cuando corra en local o en CI. Ningún criterio se ha comprobado en un teléfono (T6).

### PL-04-AC1

**Condición:** Crear torneo muestra su código y aparece en la lista.

**Estado técnico: PARCIAL (prueba de red escrita, sin ejecutar; falta teléfono).**

- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_04_createTournamentSendsSingleEliminationAndReturnsCode` · ESCRITA, SIN EJECUTAR

- [ ] Comprobado en teléfono contra la API 0.12.x

### PL-04-AC2

**Condición:** Otro grupo se inscribe con el código y el organizador lo inicia; aparece el bracket.

**Estado técnico: PARCIAL (prueba de red escrita, sin ejecutar; falta teléfono).**

- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_04_joinByCodeSendsKeyGroupAndCode` · ESCRITA, SIN EJECUTAR

- [ ] Comprobado en teléfono contra la API 0.12.x

### PL-04-AC3

**Condición:** Reportar con foto, confirmar por el rival y ver al ganador avanzar.

**Estado técnico: PARCIAL (prueba de red escrita, sin ejecutar; falta teléfono).**

- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_04_reportIsMultipartWithScoresPhotoAndGroup` · ESCRITA, SIN EJECUTAR
- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_04_nullNextMatchMeansNoMatch` · ESCRITA, SIN EJECUTAR
- [PlayValidationTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayValidationTest.kt) · `PL_04_tiesOversizedPhotosAndBadTournamentDataAreRejected` · ESCRITA, SIN EJECUTAR

- [ ] Comprobado en teléfono contra la API 0.12.x

### PL-04-AC4

**Condición:** Disputar y que el organizador resuelva; al terminar la final el torneo queda `finished`.

**Estado técnico: PARCIAL (prueba de red escrita, sin ejecutar; falta teléfono).**

- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_04_matchErrorKeepsCodeWithoutRetryOrServerText` · ESCRITA, SIN EJECUTAR

- [ ] Comprobado en teléfono contra la API 0.12.x
