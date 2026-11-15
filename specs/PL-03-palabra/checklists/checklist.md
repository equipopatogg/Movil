# Checklist — PL-03

## Estado al 2026-11-14

Pantallas escritas y compiladas; detekt y lint en verde. Las pruebas JVM listadas pasaron en local el 2026-11-14. Ningún criterio se ha comprobado en un teléfono (T6).

### PL-03-AC1

**Condición:** Cada pestaña muestra los datos del servidor o su estado vacío, nunca datos de ejemplo.

**Estado técnico: PARCIAL (prueba de red en verde; falta teléfono).**

- [PlayApiTest](../../../app/src/test/java/com/pactogg/app/feature/play/PlayApiTest.kt) · `PL_03_commitmentsSendServerRoleAndMapBothParties` · PASS JVM local 2026-11-14

- [ ] Comprobado en teléfono contra la API 0.12.x

### PL-03-AC2

**Condición:** Marcar un compromiso cumplido lo saca de pendientes al recargar.

**Estado técnico: PENDIENTE (sin prueba automática; se comprueba en teléfono).**

- [ ] Comprobado en teléfono contra la API 0.12.x
