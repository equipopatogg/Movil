# Tareas — MO-USR-01

Estado: implementación local iniciada; ver actualización inferior. [Spec](spec.md) · [Plan](plan.md).

- [ ] MO-USR-01-T1: Conciliar contrato, reglas y bloqueantes específicos con evidencia de la fuente.
- [x] MO-USR-01-T2: Definir DTO/mappers y repositorio de auth, reutilizando transporte/sesión compartidos.
- [x] MO-USR-01-T3: Adaptar pantalla y estado real, carga/error/éxito y navegación de la operación contratada.
- [ ] MO-USR-01-T4: Implementar pruebas por cada AC: JVM/HTTP y Compose/dispositivo aplicables.
- [ ] MO-USR-01-T5: Verificar compatibilidad del proveedor y controles del candidato; registrar fallos y omisiones.
- [ ] MO-USR-01-T6: Conciliar checklist, memoria y aceptación sin transferir PASS del prototipo.


T2/T3 acreditan creación de código local, no aceptación integral. T1 conserva conciliaciones pendientes; T4 requiere también ejecución Android; T5/T6 conservan pruebas de proveedor y aceptación. Resultados por candidato en docs/integracion/verificacion-consumidor-2026-11-04.md.


- [x] MO-USR-01-TQ: Comprobar instancias unit/integration/security exactas de esta operación en informes JUnit y registrar alcance/pedientes; no sustituye T4/T5 integrales.

TQ verificada localmente: [informe de calidad](../../docs/pruebas/verificacion-calidad-2026-11-04.md). No equivale a aceptación integral.


## Seguimiento por criterio — revisión 2026-11-05

T4 y T5 siguen abiertos donde faltan escenarios. Separar los archivos o aprobar una prueba parcial no cierra toda la tarea. Cada tarea de implementación autorizada debe incluir sus pruebas y evidencia del mismo cambio antes de marcarse terminada.

- [ ] MO-USR-01-T4-AC1: Doble Submit y publicación de identidad tras custodia comprobados en ViewModel; falta recorrido Compose exitoso de registro instalado. [Criterio y pruebas](checklists/checklist.md#mo-usr-01-ac1).
- [ ] MO-USR-01-T4-AC2: 409/422 de registro comprobados sin sesión/reintentos. Faltan fronteras completas y versión de privacidad rechazada; aviso definitivo diferido. [Criterio y pruebas](checklists/checklist.md#mo-usr-01-ac2).
- [ ] MO-USR-01-T4-AC3: Fallo inducido de custodia y desconexión después de recibir POST comprobados específicamente para registro. Falta timeout con plazo agotado y su recorrido Compose. [Criterio y pruebas](checklists/checklist.md#mo-usr-01-ac3).
- [ ] MO-USR-01-T5-ACQ: Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes. [Criterio y pruebas](checklists/checklist.md#mo-usr-01-acq).
