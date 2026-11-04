# Checklist — MO-EVI-02

Estado vigente: **Bloqueada: sin contrato real confirmado para verificar envío**. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-EVI-02-AC1: Enlace y foto válidos usan serialización/content-type correctos y muestran solo evidencia confirmada por 201.
- [ ] MO-EVI-02-AC2: Selector cancelado, archivo vacío/grande/tipo inválido no envían; no existen cámara ni carga de video.
- [ ] MO-EVI-02-AC3: Moderación rechazada, límite 409, autorización 403 y servicio 503 presentan acciones correctas sin falsos aprobados.
- [ ] MO-EVI-02-AC4: Sin contrato autorizado no se abre envío; pérdida de respuesta no repite la subida.
- [ ] MO-EVI-02-ACQ: controles del candidato y evidencia válidos.
- [ ] Entradas/bloqueantes de sección 7 resueltos para las tareas afectadas.
- [ ] Revisión humana de spec y aceptación funcional diferenciadas.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Pendiente | Por designar | Pendiente | Documentación preparada, sin pruebas de integración ejecutadas |


## Avance verificado local — 2026-11-04

Implementación de consumidor presente. [Evidencia del candidato](../../../docs/integracion/verificacion-consumidor-2026-11-04.md): debug, 24 JVM, Lint/Detekt y Gitleaks correctos; androidTest compilado, no ejecutado. No marcar los AC integrales como PASS: faltan proveedor/cuentas autorizadas, ejecución Android y aceptación aplicable. La tabla histórica NO_EJECUTADO no significa ausencia de código actual.


## Verificación física posterior — 2026-11-04

No ejecutado contra proveedor: creación del contrato bloqueada por moderación; no se prueban fotos/enlaces con IDs inventados. [Informe real](../../../docs/integracion/verificacion-telefono-2026-11-04.md). No cerrar casillas integrales por un caso positivo ni transferir el PASS de fixtures a proveedor.


## Preparación para commits — 2026-11-04

Se conserva el resultado real del candidato físico y se prepara incorporación por ramas dependientes. Commit y compilación no sustituyen aceptación de todos los AC; las casillas sin evidencia completa siguen abiertas. El historial inferior no significa que la implementación actual sea solo documental. Registro de ramas y comprobaciones en docs/preparacion/entrega-por-ramas.md.
