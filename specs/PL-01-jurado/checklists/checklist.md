# Checklist — PL-01

## Estado al 2026-11-14

Pantallas escritas y compiladas; detekt y lint en verde. Las pruebas JVM listadas son del 2026-11-14 y **aún no se han ejecutado**: cada una pasa a PASS solo cuando corra en local o en CI. Ningún criterio se ha comprobado en un teléfono (T6).

### PL-01-AC1

**Condición:** El inicio muestra cuántos retos esperan mi voto y abre la lista.

**Estado técnico: PARCIAL (prueba de red escrita, sin ejecutar; falta teléfono).**

- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_01_pendingVotesMapContractAndDeadline` · ESCRITA, SIN EJECUTAR

- [ ] Comprobado en teléfono contra la API 0.12.x

### PL-01-AC2

**Condición:** Votar envía `Idempotency-Key` y, al volver, el reto ya no aparece como pendiente.

**Estado técnico: PARCIAL (prueba de red escrita, sin ejecutar; falta teléfono).**

- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_01_voteSendsIdempotencyKeyThenReadsSummary` · ESCRITA, SIN EJECUTAR
- [PlayValidationTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayValidationTest.kt) · `PL_01_unknownVoteOrOutcomeAndLongCommentAreRejected` · ESCRITA, SIN EJECUTAR

- [ ] Comprobado en teléfono contra la API 0.12.x

### PL-01-AC3

**Condición:** Un segundo voto o fuera de plazo muestra el mensaje del código 409 sin cerrar la pantalla.

**Estado técnico: PENDIENTE (sin prueba automática; se comprueba en teléfono).**

- [ ] Comprobado en teléfono contra la API 0.12.x

### PL-01-AC4

**Condición:** Un reto cerrado muestra resultado, conteo y movimiento de fianza.

**Estado técnico: PENDIENTE (sin prueba automática; se comprueba en teléfono).**

- [ ] Comprobado en teléfono contra la API 0.12.x

### PL-01-AC5

**Condición:** Un admin neutral puede decidir cuando el servidor lo permite; los demás no ven el botón.

**Estado técnico: PENDIENTE (sin prueba automática; se comprueba en teléfono).**

- [ ] Comprobado en teléfono contra la API 0.12.x
