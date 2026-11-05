# Tareas — MO-TOR-02

Estado: implementación local iniciada; ver actualización inferior. [Spec](spec.md) · [Plan](plan.md).

- [ ] MO-TOR-02-T1: Conciliar contrato, reglas y bloqueantes específicos con evidencia de la fuente.
- [x] MO-TOR-02-T2: Definir DTO/mappers y repositorio de tournaments, reutilizando transporte/sesión compartidos.
- [x] MO-TOR-02-T3: Adaptar pantalla y estado real, carga/error/éxito y navegación de la operación contratada.
- [ ] MO-TOR-02-T4: Implementar pruebas por cada AC: JVM/HTTP y Compose/dispositivo aplicables.
- [ ] MO-TOR-02-T5: Verificar compatibilidad del proveedor y controles del candidato; registrar fallos y omisiones.
- [ ] MO-TOR-02-T6: Conciliar checklist, memoria y aceptación sin transferir PASS del prototipo.


T2/T3 acreditan creación de código local, no aceptación integral. T1 conserva conciliaciones pendientes; T4 requiere también ejecución Android; T5/T6 conservan pruebas de proveedor y aceptación. Resultados por candidato en docs/integracion/verificacion-consumidor-2026-11-04.md.


- [x] MO-TOR-02-TQ: Comprobar instancias unit/integration/security exactas de esta operación en informes JUnit y registrar alcance/pedientes; no sustituye T4/T5 integrales.

TQ verificada localmente: [informe de calidad](../../docs/pruebas/verificacion-calidad-2026-11-04.md). No equivale a aceptación integral.


## Seguimiento por criterio — revisión 2026-11-05

T4 y T5 siguen abiertos donde faltan escenarios. Separar los archivos o aprobar una prueba parcial no cierra toda la tarea. Cada tarea de implementación autorizada debe incluir sus pruebas y evidencia del mismo cambio antes de marcarse terminada.

- [ ] MO-TOR-02-T4-AC1: Faltan lista llena con varios datos, has_more=true/siguiente página y correspondencia visual con proveedor real; no usar pruebas del prototipo Explorar como sustituto. [Criterio y pruebas](checklists/checklist.md#mo-tor-02-ac1).
- [ ] MO-TOR-02-T4-AC2: Falta dos filtros con respuestas fuera de orden y cambio de cuenta con página en vuelo; no deducirlo de deduplicación. [Criterio y pruebas](checklists/checklist.md#mo-tor-02-ac2).
- [x] MO-TOR-02-T4-AC3: Comprobado localmente; aceptación humana/proveedor separadas. [Criterio y pruebas](checklists/checklist.md#mo-tor-02-ac3).
- [ ] MO-TOR-02-T4-AC4: 422/desconexión no se convierten en lista vacía exitosa ni reintentan; falta comparación visual de estados de error. [Criterio y pruebas](checklists/checklist.md#mo-tor-02-ac4).
- [ ] MO-TOR-02-T5-ACQ: Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes. [Criterio y pruebas](checklists/checklist.md#mo-tor-02-acq).
