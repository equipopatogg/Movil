# Tareas — MO-GRP-01

Estado: implementación local iniciada; ver actualización inferior. [Spec](spec.md) · [Plan](plan.md).

- [ ] MO-GRP-01-T1: Conciliar contrato, reglas y bloqueantes específicos con evidencia de la fuente.
- [x] MO-GRP-01-T2: Definir DTO/mappers y repositorio de groups, reutilizando transporte/sesión compartidos.
- [x] MO-GRP-01-T3: Adaptar pantalla y estado real, carga/error/éxito y navegación de la operación contratada.
- [ ] MO-GRP-01-T4: Implementar pruebas por cada AC: JVM/HTTP y Compose/dispositivo aplicables.
- [ ] MO-GRP-01-T5: Verificar compatibilidad del proveedor y controles del candidato; registrar fallos y omisiones.
- [ ] MO-GRP-01-T6: Conciliar checklist, memoria y aceptación sin transferir PASS del prototipo.


T2/T3 acreditan creación de código local, no aceptación integral. T1 conserva conciliaciones pendientes; T4 requiere también ejecución Android; T5/T6 conservan pruebas de proveedor y aceptación. Resultados por candidato en docs/integracion/verificacion-consumidor-2026-11-04.md.


- [x] MO-GRP-01-TQ: Comprobar instancias unit/integration/security exactas de esta operación en informes JUnit y registrar alcance/pedientes; no sustituye T4/T5 integrales.

TQ verificada localmente: [informe de calidad](../../docs/pruebas/verificacion-calidad-2026-11-04.md). No equivale a aceptación integral.


## Seguimiento por criterio — revisión 2026-11-05

T4 y T5 siguen abiertos donde faltan escenarios. Separar los archivos o aprobar una prueba parcial no cierra toda la tarea. Cada tarea de implementación autorizada debe incluir sus pruebas y evidencia del mismo cambio antes de marcarse terminada.

- [ ] MO-GRP-01-T4-AC1: Faltan asserts completos de nombre/rol/código/saldo mostrados, con valores alternativos del proveedor, en UI actual. [Criterio y pruebas](checklists/checklist.md#mo-grp-01-ac1).
- [ ] MO-GRP-01-T4-AC2: 409/422 conservan formulario y grupo en ViewModel; faltan fronteras exactas válidas y representación Compose. [Criterio y pruebas](checklists/checklist.md#mo-grp-01-ac2).
- [ ] MO-GRP-01-T4-AC3: Falta revisar UI vacía y lista recién cargada sin datos de muestra como escenario explícito del recorrido de creación. [Criterio y pruebas](checklists/checklist.md#mo-grp-01-ac3).
- [ ] MO-GRP-01-T5-ACQ: Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes. [Criterio y pruebas](checklists/checklist.md#mo-grp-01-acq).
