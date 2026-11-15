# Checklist — PL-03

## Estado al 2026-11-14

Pantallas escritas y compiladas; detekt y lint en verde. Las pruebas JVM listadas son del 2026-11-14 y **aún no se han ejecutado**: cada una pasa a PASS solo cuando corra en local o en CI. Ningún criterio se ha comprobado en un teléfono (T6).

### PL-03-AC1

**Condición:** Cada pestaña muestra los datos del servidor o su estado vacío, nunca datos de ejemplo.

**Estado técnico: PARCIAL (prueba de red escrita, sin ejecutar; falta teléfono).**

- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_03_commitmentsSendServerRoleAndMapBothParties` · ESCRITA, SIN EJECUTAR

- [ ] Comprobado en teléfono contra la API 0.12.x

### PL-03-AC2

**Condición:** Marcar un compromiso cumplido lo saca de pendientes al recargar.

**Estado técnico: PENDIENTE (sin prueba automática; se comprueba en teléfono).**

- [ ] Comprobado en teléfono contra la API 0.12.x
