# Tareas — MO-CON-03

Estado: implementación local autorizada; verificación integral pendiente. Crear código no cierra aceptación. [Spec](spec.md) · [Plan](plan.md).

- [ ] MO-CON-03-T1: Conciliar campos/nulos/errores y bloqueantes específicos de `GET /api/v1/contracts/{id}`; fijar evidencia de despliegue antes de probar proveedor.
- [x] MO-CON-03-T2: Adaptar DTO/mappers/interfaz/repositorio en las áreas definidas, reutilizando sesión y transporte.
- [x] MO-CON-03-T3: Conectar pantalla/estado/navegación correspondientes, sin muestras ni acciones fuera del contrato.
- [ ] MO-CON-03-T4: Implementar y ejecutar pruebas por AC y límites de estado/sesión, con evidencia del candidato.
- [ ] MO-CON-03-T5: Comprobar integración real autorizada, errores y autorización aplicable; registrar omisiones.
- [ ] MO-CON-03-T6: Conciliar checklist, memoria y aceptación sin trasladar PASS backend/prototipo.

Avance anterior a este expediente: GET auxiliar y comprobación ID/grupo presentes por MO-EVI-02. T2 sigue parcial: faltan modelo completo y recorrido de detalle; no se reinventa secuencia specs/código. [Evidencia previa](../../docs/integracion/ajuste-estado-evidencia-2026-11-04.md).


Implementación solicitada 2026-11-04: código/recorrido incorporados localmente; completar verificación antes de actualizar tareas integrales. USR-03 conserva avatar pendiente de catálogo. CON-03 reutiliza GET auxiliar previo y amplía modelo/detalle en esta etapa sin alterar cronología.


T2/T3 describen incorporación de código/recorrido, no aceptación institucional. T4/T5 siguen parciales por escenarios restantes, aunque existe recorrido positivo real. [Evidencia](../../docs/integracion/integracion-social-2026-11-04.md). USR-03: apodo integrado y probado, avatar bloqueado; sus T2/T3 permanecen parciales.


- [x] MO-CON-03-TQ: Comprobar instancias unit/integration/security exactas de esta operación en informes JUnit y registrar alcance/pedientes; no sustituye T4/T5 integrales.

TQ verificada localmente: [informe de calidad](../../docs/pruebas/verificacion-calidad-2026-11-04.md). No equivale a aceptación integral.


## Seguimiento por criterio — revisión 2026-11-05

T4 y T5 siguen abiertos donde faltan escenarios. Separar los archivos o aprobar una prueba parcial no cierra toda la tarea. Cada tarea de implementación autorizada debe incluir sus pruebas y evidencia del mismo cambio antes de marcarse terminada.

- [ ] MO-CON-03-T4-AC1: Faltan asserts de campos completos en UI y ausencia de galería inventada; el recorrido positivo real anterior no cierra todos esos puntos. [Criterio y pruebas](checklists/checklist.md#mo-con-03-ac1).
- [ ] MO-CON-03-T4-AC2: 404 de contrato comprobado; falta group_id discordante con id correcto y estado Compose. [Criterio y pruebas](checklists/checklist.md#mo-con-03-ac2).
- [ ] MO-CON-03-T4-AC3: Falta recorrido de refresco de detalle tras aceptar/rechazar y logout desde esa pantalla; el caso de evidencia tiene alcance propio. [Criterio y pruebas](checklists/checklist.md#mo-con-03-ac3).
- [ ] MO-CON-03-T5-ACQ: Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes. [Criterio y pruebas](checklists/checklist.md#mo-con-03-acq).
