# Checklist — PG-03

Estado técnico: NO_EJECUTADO. Preparación condicionada; responsables y fecha de aceptación pendientes.

- [ ] [Spec](../spec.md), [plan](../plan.md) y [tareas](../tasks.md) actualizados con contrato/maqueta versionados.
- [ ] Campos, errores, reglas, consentimiento/apodo, sesión y destino ratificados; PG03-AC1 satisfecho antes de implementar.
- [ ] DTO/mappers en datos; dominio independiente; casos de uso justificados; Hilt conservado.
- [ ] Registro/login con validación, carga, error, éxito y bloqueo de doble envío comprobados.
- [ ] Sesión, expiración/revocación/logout y fallos tratados según contrato; almacenamiento sensible real verificado.
- [ ] Destino y ciclo de vida accesibles, sin contenido protegido transitorio ni credenciales persistidas indebidamente.
- [ ] JVM/HTTP/UI/integración y controles pertinentes con evidencia real, sin confundir fixtures con proveedor.
- [ ] Historias aceptadas solo en su alcance demostrado; dudas, aprobación, checklist y memoria conciliadas.

| Criterio | Estado | Evidencia | Pendiente |
|---|---|---|---|
| PG03-AC1 | NO_EJECUTADO | Tabla de recepción sin completar | Contrato/maqueta/decisiones |
| PG03-AC2 | NO_EJECUTADO | Ninguna nueva | Registro y reglas |
| PG03-AC3 | NO_EJECUTADO | Ninguna nueva | Login y errores |
| PG03-AC4 | NO_EJECUTADO | Ninguna nueva | Sesión y custodia |
| PG03-AC5 | NO_EJECUTADO | Ninguna nueva | Destino/UI/ciclo de vida |
| PG03-AC6 | NO_EJECUTADO | Ninguna nueva | Integración y controles |

Estados futuros: PASS, FAIL, ERROR, NO_EJECUTADO o NO_APLICA justificado. El requisito de entrada es documental; las restantes verificaciones son técnicas. Redactar el expediente no satisface ninguno por sí solo.
