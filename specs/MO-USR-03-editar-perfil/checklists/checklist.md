# Checklist — MO-USR-03

Estado: implementación local presente; recorrido positivo real y controles locales correctos; aceptación integral pendiente. [Spec](../spec.md) · [Plan](../plan.md) · [Tareas](../tasks.md).

- [ ] MO-USR-03-AC1: Cambio válido recibe 200 y GET lo refleja; sesión continúa con los mismos tokens vigentes.
- [ ] MO-USR-03-AC2: Apodo ocupado devuelve 409 y permite corregir sin perder formulario.
- [ ] MO-USR-03-AC3: Cuerpo vacío/campo desconocido no se presenta como éxito; verificar 422/400 del proveedor con fixture.
- [ ] MO-USR-03-AC4: Moderación rechazada o no disponible conserva perfil anterior confirmado y muestra error seguro.
- [ ] MO-USR-03-ACQ: controles y evidencia del mismo candidato verificados.
- [ ] Bloqueantes específicos de sección 7 resueltos para las tareas afectadas.
- [ ] Prueba real del proveedor distinguida de fixtures; no se hereda PASS del backend.
- [ ] Revisión humana y aceptación de implementación registradas por separado.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación solicitada | Usuario | 2026-11-04 | Operación incluida en Sistema.docx; no prueba funcional |
| Aceptación móvil | Pendiente | Pendiente | No inferir por existencia de documentos |


[Resultado real y candidato](../../../docs/integracion/integracion-social-2026-11-04.md): caso positivo comprobado; no se cierran casillas integrales que contienen escenarios restantes. Datos y límites de la cuenta auxiliar/recorrido UI distinguidos de fixtures.
