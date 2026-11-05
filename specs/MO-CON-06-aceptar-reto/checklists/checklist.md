# Checklist — MO-CON-06

Estado: implementación local presente; recorrido positivo real y controles locales correctos; aceptación integral pendiente. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-CON-06-AC1: Miembro distinto del retador acepta reto de grupo, muestra in_progress/my_points recibidos y refresca contexto.
- [ ] MO-CON-06-AC2: Retador no puede aceptar su propio reto y se maneja 409 sin descontar puntos.
- [ ] MO-CON-06-AC3: Segunda acción o versión vieja muestra 409 y no repite aceptación automáticamente.
- [ ] MO-CON-06-AC4: Saldo insuficiente produce 402 sin modificar saldo confirmado ni mostrar deuda.
- [ ] MO-CON-06-AC5: Reto dirigido a otro usuario produce 403 sin habilitar acción ni revelar datos adicionales.
- [ ] MO-CON-06-ACQ: controles y evidencia del mismo candidato verificados.
- [ ] Bloqueantes específicos de sección 7 resueltos para las tareas afectadas.
- [ ] Prueba real del proveedor distinguida de fixtures; no se hereda PASS del backend.
- [ ] Revisión humana y aceptación de implementación registradas por separado.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación solicitada | Usuario | 2026-11-04 | Operación incluida en Sistema.docx; no prueba funcional |
| Aceptación móvil | Pendiente | Pendiente | No inferir por existencia de documentos |


[Resultado real y candidato](../../../docs/integracion/integracion-social-2026-11-04.md): caso positivo comprobado; no se cierran casillas integrales que contienen escenarios restantes. Datos y límites de la cuenta auxiliar/recorrido UI distinguidos de fixtures.
