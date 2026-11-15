# Checklist — PL-02

## Estado al 2026-11-14

Pantallas escritas y compiladas; detekt y lint en verde. Las pruebas JVM listadas son del 2026-11-14 y **aún no se han ejecutado**: cada una pasa a PASS solo cuando corra en local o en CI. Ningún criterio se ha comprobado en un teléfono (T6).

### PL-02-AC1

**Condición:** La campana muestra el contador real de no leídos.

**Estado técnico: PARCIAL (prueba de red escrita, sin ejecutar; falta teléfono).**

- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_02_unreadNoticesUseFilterAndServerCounter` · ESCRITA, SIN EJECUTAR

- [ ] Comprobado en teléfono contra la API 0.12.x

### PL-02-AC2

**Condición:** Abrir un aviso de reto lleva al reto y lo deja leído.

**Estado técnico: PENDIENTE (sin prueba automática; se comprueba en teléfono).**

- [ ] Comprobado en teléfono contra la API 0.12.x

### PL-02-AC3

**Condición:** "Marcar todo" deja el contador en 0.

**Estado técnico: PENDIENTE (sin prueba automática; se comprueba en teléfono).**

- [ ] Comprobado en teléfono contra la API 0.12.x
