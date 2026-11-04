# Tasks — PG-03

Estado: todas pendientes; implementación condicionada a T03.1 y apertura de fase. [Spec](spec.md) · [plan](plan.md).

- [ ] T03.1 Recibir contrato, conciliar boceto disponible y cerrar campos, reglas, consentimiento, apodo, sesión y destino; actualizar ejemplos de aceptación sin inventarlos — PG03-AC1; evidencia: versiones y decisiones identificadas.
- [ ] T03.2 Definir modelos/interfaces de dominio y DTO/mappers/servicio en feature/auth según contrato — PG03-AC2/AC3; evidencia: pruebas de mapeo y fixtures acordadas.
- [ ] T03.3 Implementar repositorio y custodia de sesión/limpieza con política ratificada — PG03-AC3/AC4; evidencia: pruebas de errores/expiración/logout y mecanismo Android real.
- [ ] T03.4 Implementar Register/Login Screen y ViewModel, validación y estados accesibles en presentation/strings — PG03-AC2/AC3/AC5; evidencia: JVM/Compose y referencia visual.
- [ ] T03.5 Conectar Hilt, arranque con/sin sesión, destino acordado y salida — PG03-AC4/AC5; evidencia: navegación/ciclo de vida sin secretos ni sesión falsa.
- [ ] T03.6 Implementar/ejecutar PF-C1–PF-C6 con HTTP/MockWebServer, UI y almacenamiento pertinente — PG03-AC2–AC5; evidencia: resultados por caso, entorno y omisiones.
- [ ] T03.7 Verificar integración con API autorizada y build/estático/seguridad aplicables — PG03-AC6; evidencia: informes sin PII y separación fixture/proveedor.
- [ ] T03.8 Conciliar historias/contrato, aceptación por criterio, checklist y memoria — todos; evidencia: alcance real entregado y deuda visible.

T03.1 puede avanzar documentalmente cuando lleguen entradas. Las tareas técnicas no se ejecutan ahora. Cierre en [checklists/checklist.md](checklists/checklist.md).

## Precisión de T03.1/T03.5

T03.1 ya dispone de boceto; faltan contrato y decisiones de primer ingreso. T03.5 integra sesión y transición; el destino se verifica en el mismo alcance de acceso. Las casillas permanecen pendientes; redactar no implementa.

## Tareas de etapa visual, separadas del cierre funcional

- [x] T03.UI0 Preparar inventario, composición, estados/eventos y frontera de integración — preparación documental, no aceptación visual o técnica.
- [ ] T03.UI1 Implementar pantallas/componentes y strings del diseño cuando se autorice código — PG03-UI1.
- [ ] T03.UI2 Preparar variantes aisladas y estados de presentación, sin credenciales persistidas — PG03-UI2/UI3/UI4.
- [ ] T03.UI3 Verificar UI/accesibilidad/teclado/navegación y separación debug/release — PG03-UI1–UI4.
- [ ] T03.UI4 Registrar evidencia visual y pendientes de integración sin cerrar PG03-AC2–AC6 por las pantallas.

T03.1–T03.8 conservan la integración funcional. El contrato no bloquea diseñar la UI; sí bloquea inventar operaciones o comportamiento de sesión.

## Ejecución UI autorizada posterior

T03.UI1 y T03.UI2: código creado en AccessScreen/AccessEntry/AccessState y catálogo debug, recursos y estado efímero. T03.UI3/UI4: comprobaciones locales documentadas; dispositivo/accesibilidad/revisión visual pendientes, por lo que no se marcan completas. No hay DTO/servicio/sesión real.


## Actualización vigente — contrato recibido, 2026-11-04

Backend externo fijado al commit f6c37d8a70de43765f870b78b452832a2c0842b9; ver [alineación canónica](../../docs/integracion/alineacion-backend-2026-11-04.md). Ya no falta recibir el contrato: falta conciliar las entradas específicas y verificar integración. El texto anterior conserva etapas históricas, no un estado vigente de ausencia total de contrato.

PG-03 mantiene sus IDs PG03-AC1–AC6 y coordinación, sin duplicar implementación. El detalle se divide en [MO-USR-01](../MO-USR-01-registro/spec.md) (AC2), [MO-AUTH-01](../MO-AUTH-01-login/spec.md) (AC3), [MO-AUTH-03](../MO-AUTH-03-sesion/spec.md) y [MO-AUTH-04](../MO-AUTH-04-logout/spec.md) (AC4); el destino se define en las operaciones de acceso (AC5). AC1 queda PARCIAL documental por recepción y dudas abiertas; AC2–AC6 no están verificados. No marcar aceptación de integración por apariencia o pruebas del prototipo. T03.1 recibe fuente pero conserva conciliación pendiente; T03.2–T03.8 se desglosan en MO-* sin duplicar servicios/custodia. Sin código nuevo por esta actualización.
