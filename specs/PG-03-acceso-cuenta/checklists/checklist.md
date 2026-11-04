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

## Entrada visual disponible

Boceto Figma recorrido el 2026-11-03. PG03-AC1 sigue sin cerrar por contrato/conciliación y alcance; sustituir «maqueta no recibida» por «boceto disponible, diseño aplicable pendiente» al evaluar entradas. El destino se verifica en los expedientes de acceso.

## Cierre independiente de etapa visual

- [x] Diseño documental UI-A01/A02 preparado, sin implementación.
- [ ] PG03-UI1 pantalla/recursos verificados.
- [ ] PG03-UI2 estados/accesibilidad verificados.
- [ ] PG03-UI3 intenciones sin autenticación/persistencia ficticias verificado.
- [ ] PG03-UI4 variantes aisladas verificadas.

Estado PG03-UI1–UI4: NO_EJECUTADO. PG03-AC1–AC6 conservan sus pendientes; no se marcan completos al cerrar UI.

## Implementación UI posterior autorizada — 2026-11-03

Fuentes UI implementadas y debug/release compilados; [evidencia local](../../../docs/preparacion/verificacion-ui-local.md). Catálogo exclusivo de debug, sin API ni sesión simulada. Criterios UI: PARCIAL por revisión visual e instrumentación ejecutada pendientes; no convertir en PASS completo. Criterios AC de integración sin cerrar. La instrucción posterior de implementación prevalece sobre el antecedente «solo diseño».

## Revisión humana de apariencia — 2026-11-03

Usuario conforme con apariencia del inicio de sesión: «se ve igualito». Evidencia: revisión comunicada en sesión; no inferir equipo, versión exacta ni ejecución automatizada. PG03-UI1 tiene conformidad visual de acceso en el alcance observado; registro, accesibilidad/ciclo de vida y criterios de integración no quedan aceptados por extensión.


## Recepción posterior — 2026-11-04

Contrato recibido; PG03-AC1 pasa a PARCIAL documental, no PASS: privacidad/compatibilidad/reglas pendientes en [matriz](../../../docs/integracion/alineacion-backend-2026-11-04.md). PG03-AC2–AC6 continúan sin verificación funcional. Operaciones desglosadas en MO-USR-01, MO-AUTH-01/03/04 y destino posterior al ingreso. Las casillas anteriores conservan sus pendientes; NO_EJECUTADO anterior no significa ausencia actual de fuente. Esta recepción no acepta implementación.
