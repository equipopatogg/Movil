# Checklist — <ID>

Estado: sin ejecutar. Revisor, fecha, versión e instrucción aplicable por registrar.

## Antes de implementar

- [ ] [Spec](../spec.md) describe un cambio concreto respecto al estado actual.
- [ ] [Plan](../plan.md) delimita archivos/artefactos, orden y decisiones.
- [ ] [Tareas](../tasks.md) enlazan entregables con criterios verificables.
- [ ] Contratos/maqueta/entradas que condicionan el cambio están disponibles.
- [ ] Reglas generales referenciadas, sin convertir guías o corroboración en specs.
- [ ] Fuente y alcance de aprobación registrados; fase respetada.

## Verificación y cierre

- [ ] Cambios y criterios comprobados mediante evidencia pertinente real.
- [ ] Resultados ejecutados/omitidos/fallidos y entorno/variante identificados.
- [ ] Hallazgos, excepciones y pendientes visibles con su alcance.
- [ ] Spec/plan/tareas conciliados; aceptación diferenciada de aprobación.
- [ ] Memoria actualizada con el patrón de fase.

| Criterio/control | Estado | Evidencia | Límite o pendiente |
|---|---|---|---|
| <criterio aplicable> | NO_EJECUTADO | Pendiente | Pendiente |

Estados: PASS, FAIL, ERROR, NO_EJECUTADO o NO_APLICA justificado. Los checks de documentación no demuestran build, pruebas o seguridad. Mantener este archivo dentro de checklists/ de su expediente; no trasladarlo a la raíz de la spec.

## Calidad por operación

Para funcionalidades con API confirmada, definir antes de implementar casos unitarios, de integración y de seguridad pertinentes, vinculados a IDs y criterios. Actualizar docs/pruebas/operaciones.json y su matriz con identidades completas de clase/método/parámetro. No exigir exactamente tres casos ni añadir operaciones futuras. Registrar entorno, pruebas ejecutadas/fallidas/omitidas y evidencia del mismo candidato; cero pruebas o informe ausente no cierra una tarea. La integración con fixtures valida al consumidor; la autorización real del proveedor y aceptación humana se acreditan por separado. Ver docs/pruebas/pipeline-movil.md para gates y cobertura medida antes de ratificar umbrales.
