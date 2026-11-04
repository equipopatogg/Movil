# Checklist — MO-AUTH-03

Estado vigente: **Cifrado, rotación y arranque verificados; escenarios restantes pendientes**. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-AUTH-03-AC1: Dos 401 concurrentes originan una renovación y ambas observan el par de tokens vigente sin carreras.
- [ ] MO-AUTH-03-AC2: Refresh 401, 403 protegido, red caída y 5xx producen estados diferentes; no hay recursión o bucles.
- [ ] MO-AUTH-03-AC3: En dispositivo se verifica cifrado, backup, reinicio, corrupción y clave invalidada sin tokens en logs.
- [ ] MO-AUTH-03-AC4: Logout durante refresh impide restauración tardía; fallo de escritura atómica no mezcla tokens de generaciones distintas.
- [ ] MO-AUTH-03-ACQ: controles del candidato y evidencia válidos.
- [ ] Entradas/bloqueantes de sección 7 resueltos para las tareas afectadas.
- [ ] Revisión humana de spec y aceptación funcional diferenciadas.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Pendiente | Por designar | Pendiente | Documentación preparada, sin pruebas de integración ejecutadas |


## Avance verificado local — 2026-11-04

Implementación de consumidor presente. [Evidencia del candidato](../../../docs/integracion/verificacion-consumidor-2026-11-04.md): debug, 24 JVM, Lint/Detekt y Gitleaks correctos; androidTest compilado, no ejecutado. No marcar los AC integrales como PASS: faltan proveedor/cuentas autorizadas, ejecución Android y aceptación aplicable. La tabla histórica NO_EJECUTADO no significa ausencia de código actual.


## Verificación física posterior — 2026-11-04

Refresh real con rotación y rechazo del token anterior comprobados; arranque de proceso con expiración y cifrado Android comprobados. Escenarios restantes y aceptación integral pendientes. [Informe real](../../../docs/integracion/verificacion-telefono-2026-11-04.md). No cerrar casillas integrales por un caso positivo ni transferir el PASS de fixtures a proveedor.


## Preparación para commits — 2026-11-04

Se conserva el resultado real del candidato físico y se prepara incorporación por ramas dependientes. Commit y compilación no sustituyen aceptación de todos los AC; las casillas sin evidencia completa siguen abiertas. El historial inferior no significa que la implementación actual sea solo documental. Registro de ramas y comprobaciones en docs/preparacion/entrega-por-ramas.md.
