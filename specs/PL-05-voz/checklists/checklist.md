# Checklist — PL-05

## Estado al 2026-11-14

Pantallas escritas y compiladas; detekt y lint en verde. Las pruebas JVM listadas son del 2026-11-14 y **aún no se han ejecutado**: cada una pasa a PASS solo cuando corra en local o en CI. Ningún criterio se ha comprobado en un teléfono (T6).

### PL-05-AC1

**Condición:** Con la IA conectada, una frase llena el formulario y el reto se crea con `source=voice`.

**Estado técnico: PARCIAL (prueba de red escrita, sin ejecutar; falta teléfono).**

- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_05_parseMapsDraftMissingFieldsAndMembers` · ESCRITA, SIN EJECUTAR
- [PlayValidationTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayValidationTest.kt) · `PL_05_emptyOrTooLongPhraseIsRejected` · ESCRITA, SIN EJECUTAR

- [ ] Comprobado en teléfono contra la API 0.12.x

### PL-05-AC2

**Condición:** Sin IA, la app ofrece escribir a mano sin perder la frase.

**Estado técnico: PARCIAL (prueba de red escrita, sin ejecutar; falta teléfono).**

- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_05_unavailableAiIsReportedAs503WithoutRetry` · ESCRITA, SIN EJECUTAR

- [ ] Comprobado en teléfono contra la API 0.12.x
