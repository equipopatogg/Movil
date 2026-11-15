# Checklist — PL-05

## Estado al 2026-11-14

Pantallas escritas y compiladas; detekt y lint en verde. Las pruebas JVM listadas pasaron en local el 2026-11-14. Ningún criterio se ha comprobado en un teléfono (T6).

### PL-05-AC1

**Condición:** Con la IA conectada, una frase llena el formulario y el reto se crea con `source=voice`.

**Estado técnico: PARCIAL (prueba de red en verde; falta teléfono).**

- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_05_parseMapsDraftMissingFieldsAndMembers` · PASS JVM local 2026-11-14
- [PlayValidationTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayValidationTest.kt) · `PL_05_emptyOrTooLongPhraseIsRejected` · PASS JVM local 2026-11-14

- [ ] Comprobado en teléfono contra la API 0.12.x

### PL-05-AC2

**Condición:** Sin IA, la app ofrece escribir a mano sin perder la frase.

**Estado técnico: PARCIAL (prueba de red en verde; falta teléfono).**

- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_05_unavailableAiIsReportedAs503WithoutRetry` · PASS JVM local 2026-11-14

- [ ] Comprobado en teléfono contra la API 0.12.x
