# Tareas — <ID>

Estado: pendientes. [Spec](spec.md) y [plan](plan.md). No marcar trabajo ejecutado por copiar esta plantilla.

| ID de tarea | Cambio/entregable concreto | Ruta afectada | Criterio | Dependencia | Evidencia esperada |
|---|---|---|---|---|---|
| <ID> | <qué crear o modificar> | <ruta del alcance> | <ID> | <entrada> | <reporte/prueba> |

- [ ] Revisar entradas/decisiones necesarias y la instrucción aplicable al cambio.
- [ ] Implementar únicamente los cambios delimitados en el plan cuando se abra esa fase.
- [ ] Ejecutar las verificaciones pertinentes y registrar resultados/omisiones.
- [ ] Conciliar el expediente, aceptación y memoria.

Dividir tareas según entregables reales, sin un test por clase o carpetas vacías por plantilla. Para cierre utilizar [checklists/checklist.md](checklists/checklist.md). Firma, cuentas, Git o publicación necesitan la instrucción aplicable; no se deducen de esta lista.

## Calidad por operación

Para funcionalidades con API confirmada, definir antes de implementar casos unitarios, de integración y de seguridad pertinentes, vinculados a IDs y criterios. Actualizar docs/pruebas/operaciones.json y su matriz con identidades completas de clase/método/parámetro. No exigir exactamente tres casos ni añadir operaciones futuras. Registrar entorno, pruebas ejecutadas/fallidas/omitidas y evidencia del mismo candidato; cero pruebas o informe ausente no cierra una tarea. La integración con fixtures valida al consumidor; la autorización real del proveedor y aceptación humana se acreditan por separado. Ver docs/pruebas/pipeline-movil.md para gates y cobertura medida antes de ratificar umbrales.
