# Tareas — MO-CON-06

Estado: implementación local autorizada; verificación integral pendiente. Crear código no cierra aceptación. [Spec](spec.md) · [Plan](plan.md).

- [ ] MO-CON-06-T1: Conciliar campos/nulos/errores y bloqueantes específicos de `POST /api/v1/contracts/{id}/accept`; fijar evidencia de despliegue antes de probar proveedor.
- [x] MO-CON-06-T2: Adaptar DTO/mappers/interfaz/repositorio en las áreas definidas, reutilizando sesión y transporte.
- [x] MO-CON-06-T3: Conectar pantalla/estado/navegación correspondientes, sin muestras ni acciones fuera del contrato.
- [ ] MO-CON-06-T4: Implementar y ejecutar pruebas por AC y límites de estado/sesión, con evidencia del candidato.
- [ ] MO-CON-06-T5: Comprobar integración real autorizada, errores y autorización aplicable; registrar omisiones.
- [ ] MO-CON-06-T6: Conciliar checklist, memoria y aceptación sin trasladar PASS backend/prototipo.



Implementación solicitada 2026-11-04: código/recorrido incorporados localmente; completar verificación antes de actualizar tareas integrales. USR-03 conserva avatar pendiente de catálogo. CON-03 reutiliza GET auxiliar previo y amplía modelo/detalle en esta etapa sin alterar cronología.


T2/T3 describen incorporación de código/recorrido, no aceptación institucional. T4/T5 siguen parciales por escenarios restantes, aunque existe recorrido positivo real. [Evidencia](../../docs/integracion/integracion-social-2026-11-04.md). USR-03: apodo integrado y probado, avatar bloqueado; sus T2/T3 permanecen parciales.


- [x] MO-CON-06-TQ: Comprobar instancias unit/integration/security exactas de esta operación en informes JUnit y registrar alcance/pedientes; no sustituye T4/T5 integrales.

TQ verificada localmente: [informe de calidad](../../docs/pruebas/verificacion-calidad-2026-11-04.md). No equivale a aceptación integral.


## Seguimiento por criterio — revisión 2026-11-05

T4 y T5 siguen abiertos donde faltan escenarios. Separar los archivos o aprobar una prueba parcial no cierra toda la tarea. Cada tarea de implementación autorizada debe incluir sus pruebas y evidencia del mismo cambio antes de marcarse terminada.

- [ ] MO-CON-06-T4-AC1: El recorrido positivo histórico fue un reto dirigido; falta aceptación real de reto de grupo y refresco visual completo del contexto. [Criterio y pruebas](checklists/checklist.md#mo-con-06-ac1).
- [ ] MO-CON-06-T4-AC2: CANNOT_ACCEPT_OWN_CONTRACT 409 y error de estado conservan saldo sin éxito en consumidor; falta intento real del retador y Compose. [Criterio y pruebas](checklists/checklist.md#mo-con-06-ac2).
- [ ] MO-CON-06-T4-AC3: 409 de estado conserva saldo y exige detalle fresco; no se ejecutaron dos aceptaciones reales concurrentes contra proveedor. [Criterio y pruebas](checklists/checklist.md#mo-con-06-ac3).
- [ ] MO-CON-06-T4-AC4: 402 conserva saldo de grupo y contrato, sin éxito y con acción invalidada en ViewModel; falta recorrido Compose. [Criterio y pruebas](checklists/checklist.md#mo-con-06-ac4).
- [ ] MO-CON-06-T4-AC5: 403 NOT_CONTRACT_TARGET conserva contexto/saldo en consumidor; falta intento real de tercero y su pantalla. [Criterio y pruebas](checklists/checklist.md#mo-con-06-ac5).
- [ ] MO-CON-06-T5-ACQ: Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes. [Criterio y pruebas](checklists/checklist.md#mo-con-06-acq).


## Actualizacion de contrato recibida - 2026-11-06

La lista de 54 rutas del usuario exige Idempotency-Key en esta operacion. El consumidor genera un UUID por invocacion antes de SessionManager y lo envia como cabecera obligatoria. No activa reintentos automaticos ni persiste claves en logs. La prueba HTTPS existente verifica presencia y formato UUID junto con autorizacion, ruta y numero de peticiones. Implementacion local; ejecucion del candidato pendiente. La semantica de reenvio manual despues de respuesta perdida y la vigencia del registro del servidor requieren el contrato actualizado.
