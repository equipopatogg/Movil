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

Para funcionalidades con API confirmada, definir antes de implementar casos unitarios, de integración y de seguridad pertinentes, vinculados a IDs y criterios. Actualizar docs/pruebas/operaciones.json y su matriz con identidades completas de clase/método y parámetro cuando exista. No exigir exactamente tres casos ni añadir operaciones futuras. Registrar entorno, pruebas ejecutadas/fallidas/omitidas y evidencia del mismo candidato; cero pruebas o informe ausente no cierra una tarea. La integración con fixtures valida al consumidor; la autorización real del proveedor y aceptación humana se acreditan por separado. Ver docs/pruebas/pipeline-movil.md para gates y cobertura medida antes de ratificar umbrales.

Organización de pruebas solicitada por el usuario 2026-11-05: cada operación referencia sus archivos específicos de validación, API y seguridad bajo feature/<función>/. Mantener infraestructura/datos y recorridos compartidos sin duplicar escenarios. El ID se codifica como AUTH_01_ en el método; operaciones.json valida nombres completos. La plantilla no autoriza Git ni publicación.


## Trazabilidad y cierre por criterio — 2026-11-05

Cada tarea de implementación autorizada incluye crear o actualizar sus pruebas pertinentes y ejecutarlas antes de marcarla terminada. Registrar condición → tarea → clase/método concreto → entorno e informe con resultado real → parte pendiente. Una prueba compartida acredita únicamente sus aserciones; el nombre, la cantidad de casos o la existencia del archivo no demuestran el criterio completo. Distinguir esperado, no ejecutado, parcial, fallido y comprobado; aceptación humana y CI remoto se registran por separado. No trasladar resultados entre operaciones ni confundir expiración con dos HTTP 401, respuesta 503 con desconexión o recreación de Activity con muerte del proceso.

Esta revisión documental no autoriza implementar pendientes, ejecutar nuevas pruebas reales ni publicar. Fuente: decisión D-SDD-36 y revisión de criterios del 2026-11-05.
