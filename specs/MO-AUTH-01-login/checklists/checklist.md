# Checklist — MO-AUTH-01

Estado vigente: **Implementación verificada en flujo positivo y rechazo 401; cierre integral pendiente**. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-AUTH-01-AC1: 200 válido almacena sesión y abre inicio; credenciales inválidas 401 permanecen en acceso sin refresh recursivo.
- [ ] MO-AUTH-01-AC2: 429, 422, timeout y respuesta malformada tienen estado recuperable y no inician sesión.
- [ ] MO-AUTH-01-AC3: Doble toque, rotación y recreación no duplican login ni persisten contraseña en SavedState/Bundle.
- [ ] MO-AUTH-01-ACQ: controles del candidato y evidencia válidos.
- [ ] Entradas/bloqueantes de sección 7 resueltos para las tareas afectadas.
- [ ] Revisión humana de spec y aceptación funcional diferenciadas.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Pendiente | Por designar | Pendiente | Documentación preparada, sin pruebas de integración ejecutadas |


## Avance verificado local — 2026-11-04

Implementación de consumidor presente. [Evidencia del candidato](../../../docs/integracion/verificacion-consumidor-2026-11-04.md): debug, 24 JVM, Lint/Detekt y Gitleaks correctos; androidTest compilado, no ejecutado. No marcar los AC integrales como PASS: faltan proveedor/cuentas autorizadas, ejecución Android y aceptación aplicable. La tabla histórica NO_EJECUTADO no significa ausencia de código actual.


## Verificación física posterior — 2026-11-04

Login válido e inválido 401 comprobados en teléfono; sin bypass. UI y API reales, aceptación integral pendiente. [Informe real](../../../docs/integracion/verificacion-telefono-2026-11-04.md). No cerrar casillas integrales por un caso positivo ni transferir el PASS de fixtures a proveedor.


## Preparación para commits — 2026-11-04

Se conserva el resultado real del candidato físico y se prepara incorporación por ramas dependientes. Commit y compilación no sustituyen aceptación de todos los AC; las casillas sin evidencia completa siguen abiertas. El historial inferior no significa que la implementación actual sea solo documental. Registro de ramas y comprobaciones en docs/preparacion/entrega-por-ramas.md.
