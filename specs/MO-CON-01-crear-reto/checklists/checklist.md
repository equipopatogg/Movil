# Checklist — MO-CON-01

Estado vigente: **Parcial: creación real comprobada tras corrección AWS; criterios restantes pendientes**. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-CON-01-AC1: Contexto de grupo válido, fianza recibida y formulario válido producen un POST y resumen del contrato real open.
- [ ] MO-CON-01-AC2: UTC, fecha vencida, límites, fianza inválida y moderación rechazada se muestran sin éxito ficticio.
- [ ] MO-CON-01-AC3: Sin grupo/settings no se envía; member exige selección real y excluye usuario propio; voz sigue oculta y aceptar corresponde a MO-CON-06.
- [ ] MO-CON-01-AC4: Respuesta perdida no provoca reintento automático ni duplicación deliberada.
- [ ] MO-CON-01-ACQ: controles del candidato y evidencia válidos.
- [ ] Entradas/bloqueantes de sección 7 resueltos para las tareas afectadas.
- [ ] Revisión humana de spec y aceptación funcional diferenciadas.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Pendiente | Por designar | Pendiente | Documentación preparada, sin pruebas de integración ejecutadas |


## Avance verificado local — 2026-11-04

Implementación de consumidor presente. [Evidencia del candidato](../../../docs/integracion/verificacion-consumidor-2026-11-04.md): debug, 24 JVM, Lint/Detekt y Gitleaks correctos; androidTest compilado, no ejecutado. No marcar los AC integrales como PASS: faltan proveedor/cuentas autorizadas, ejecución Android y aceptación aplicable. La tabla histórica NO_EJECUTADO no significa ausencia de código actual.


## Verificación física posterior — 2026-11-04

Bloqueado por proveedor: 503 MODERATION_UNAVAILABLE al crear contrato; request_id y datos QA en informe. No aceptar ni simular éxito. [Informe real](../../../docs/integracion/verificacion-telefono-2026-11-04.md). No cerrar casillas integrales por un caso positivo ni transferir el PASS de fixtures a proveedor.


## Preparación para commits — 2026-11-04

Se conserva el resultado real del candidato físico y se prepara incorporación por ramas dependientes. Commit y compilación no sustituyen aceptación de todos los AC; las casillas sin evidencia completa siguen abiertas. El historial inferior no significa que la implementación actual sea solo documental. Registro de ramas y comprobaciones en docs/preparacion/entrega-por-ramas.md.


Actualización posterior: la reprueba física de reto/enlace/foto fue positiva tras corrección AWS; el bloqueo 503 inferior es histórico. [Informe](../../../docs/integracion/verificacion-telefono-2026-11-04.md). Ajuste actual: lectura auxiliar de estado posterior a evidencia; no implica aceptación del expediente completo.
