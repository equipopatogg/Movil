# Checklist — MO-CON-02

Estado: implementación local presente; recorrido positivo real y controles locales correctos; aceptación integral pendiente. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-CON-02-AC1: status/game/q/mine se serializan y muestran resultados reales correctos.
- [ ] MO-CON-02-AC2: Tras aceptar, refresco refleja in_progress; las acciones locales invalidan estado anterior.
- [ ] MO-CON-02-AC3: Filtro inválido/usuario ajeno presenta 422/403; cambios de filtro reinician cursor.
- [ ] MO-CON-02-ACQ: controles y evidencia del mismo candidato verificados.
- [ ] Bloqueantes específicos de sección 7 resueltos para las tareas afectadas.
- [ ] Prueba real del proveedor distinguida de fixtures; no se hereda PASS del backend.
- [ ] Revisión humana y aceptación de implementación registradas por separado.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación solicitada | Usuario | 2026-11-04 | Operación incluida en Sistema.docx; no prueba funcional |
| Aceptación móvil | Pendiente | Pendiente | No inferir por existencia de documentos |


[Resultado real y candidato](../../../docs/integracion/integracion-social-2026-11-04.md): caso positivo comprobado; no se cierran casillas integrales que contienen escenarios restantes. Datos y límites de la cuenta auxiliar/recorrido UI distinguidos de fixtures.
