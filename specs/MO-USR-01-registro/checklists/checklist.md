# Checklist — MO-USR-01

Estado vigente: **Registro real verificado; negativos completos y aviso definitivo pendientes**. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-USR-01-AC1: Registro válido produce una sola petición con campos exactos y lleva al inicio solo después de custodiar sesión.
- [ ] MO-USR-01-AC2: Límites de email/apodo/contraseña, consentimiento ausente y versión incorrecta muestran validación; duplicados 409 y 429 no navegan.
- [ ] MO-USR-01-AC3: 201 incompleto, timeout y fallo de custodia no crean sesión ni repiten automáticamente el registro.
- [ ] MO-USR-01-ACQ: controles del candidato y evidencia válidos.
- [ ] Entradas/bloqueantes de sección 7 resueltos para las tareas afectadas.
- [ ] Revisión humana de spec y aceptación funcional diferenciadas.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Pendiente | Por designar | Pendiente | Documentación preparada, sin pruebas de integración ejecutadas |


## Avance verificado local — 2026-11-04

Implementación de consumidor presente. [Evidencia del candidato](../../../docs/integracion/verificacion-consumidor-2026-11-04.md): debug, 24 JVM, Lint/Detekt y Gitleaks correctos; androidTest compilado, no ejecutado. No marcar los AC integrales como PASS: faltan proveedor/cuentas autorizadas, ejecución Android y aceptación aplicable. La tabla histórica NO_EJECUTADO no significa ausencia de código actual.


## Verificación física posterior — 2026-11-04

Registro real confirmado con cuenta QA; acceso/consentimiento probados. Aceptación integral y aviso definitivo pendientes. [Informe real](../../../docs/integracion/verificacion-telefono-2026-11-04.md). No cerrar casillas integrales por un caso positivo ni transferir el PASS de fixtures a proveedor.


## Preparación para commits — 2026-11-04

Se conserva el resultado real del candidato físico y se prepara incorporación por ramas dependientes. Commit y compilación no sustituyen aceptación de todos los AC; las casillas sin evidencia completa siguen abiertas. El historial inferior no significa que la implementación actual sea solo documental. Registro de ramas y comprobaciones en docs/preparacion/entrega-por-ramas.md.
