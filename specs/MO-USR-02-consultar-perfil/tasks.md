# Tareas — MO-USR-02

Estado: implementación local autorizada; verificación integral pendiente. Crear código no cierra aceptación. [Spec](spec.md) · [Plan](plan.md).

- [ ] MO-USR-02-T1: Conciliar campos/nulos/errores y bloqueantes específicos de `GET /api/v1/users/me`; fijar evidencia de despliegue antes de probar proveedor.
- [x] MO-USR-02-T2: Adaptar DTO/mappers/interfaz/repositorio en las áreas definidas, reutilizando sesión y transporte.
- [x] MO-USR-02-T3: Conectar pantalla/estado/navegación correspondientes, sin muestras ni acciones fuera del contrato.
- [ ] MO-USR-02-T4: Implementar y ejecutar pruebas por AC y límites de estado/sesión, con evidencia del candidato.
- [ ] MO-USR-02-T5: Comprobar integración real autorizada, errores y autorización aplicable; registrar omisiones.
- [ ] MO-USR-02-T6: Conciliar checklist, memoria y aceptación sin trasladar PASS backend/prototipo.



Implementación solicitada 2026-11-04: código/recorrido incorporados localmente; completar verificación antes de actualizar tareas integrales. USR-03 conserva avatar pendiente de catálogo. CON-03 reutiliza GET auxiliar previo y amplía modelo/detalle en esta etapa sin alterar cronología.


T2/T3 describen incorporación de código/recorrido, no aceptación institucional. T4/T5 siguen parciales por escenarios restantes, aunque existe recorrido positivo real. [Evidencia](../../docs/integracion/integracion-social-2026-11-04.md). USR-03: apodo integrado y probado, avatar bloqueado; sus T2/T3 permanecen parciales.


- [x] MO-USR-02-TQ: Comprobar instancias unit/integration/security exactas de esta operación en informes JUnit y registrar alcance/pedientes; no sustituye T4/T5 integrales.

TQ verificada localmente: [informe de calidad](../../docs/pruebas/verificacion-calidad-2026-11-04.md). No equivale a aceptación integral.


## Seguimiento por criterio — revisión 2026-11-05

T4 y T5 siguen abiertos donde faltan escenarios. Separar los archivos o aprobar una prueba parcial no cierra toda la tarea. Cada tarea de implementación autorizada debe incluir sus pruebas y evidencia del mismo cambio antes de marcarse terminada.

- [ ] MO-USR-02-T4-AC1: Faltan varios perfiles HTTP con estadísticas/groups_count distintos y asserts Compose de esos valores; caso de recién registrado con datos reales sigue histórico. [Criterio y pruebas](checklists/checklist.md#mo-usr-02-ac1).
- [ ] MO-USR-02-T4-AC2: Secuencia PATCH→GET comprobada en consumidor; falta respuesta tardía de perfil anterior en la misma cuenta. [Criterio y pruebas](checklists/checklist.md#mo-usr-02-ac2).
- [ ] MO-USR-02-T4-AC3: Falta 401 del endpoint perfil a través de su UI y prueba que datos anteriores no se muestren al entrar sin sesión. [Criterio y pruebas](checklists/checklist.md#mo-usr-02-ac3).
- [ ] MO-USR-02-T5-ACQ: Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes. [Criterio y pruebas](checklists/checklist.md#mo-usr-02-acq).
