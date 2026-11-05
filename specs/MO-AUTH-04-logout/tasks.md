# Tareas — MO-AUTH-04

Estado: implementación local iniciada; ver actualización inferior. [Spec](spec.md) · [Plan](plan.md).

- [ ] MO-AUTH-04-T1: Conciliar contrato, reglas y bloqueantes específicos con evidencia de la fuente.
- [x] MO-AUTH-04-T2: Definir DTO/mappers y repositorio de session, reutilizando transporte/sesión compartidos.
- [x] MO-AUTH-04-T3: Adaptar pantalla y estado real, carga/error/éxito y navegación de la operación contratada.
- [ ] MO-AUTH-04-T4: Implementar pruebas por cada AC: JVM/HTTP y Compose/dispositivo aplicables.
- [ ] MO-AUTH-04-T5: Verificar compatibilidad del proveedor y controles del candidato; registrar fallos y omisiones.
- [ ] MO-AUTH-04-T6: Conciliar checklist, memoria y aceptación sin transferir PASS del prototipo.


T2/T3 acreditan creación de código local, no aceptación integral. T1 conserva conciliaciones pendientes; T4 requiere también ejecución Android; T5/T6 conservan pruebas de proveedor y aceptación. Resultados por candidato en docs/integracion/verificacion-consumidor-2026-11-04.md.


- [x] MO-AUTH-04-TQ: Comprobar instancias unit/integration/security exactas de esta operación en informes JUnit y registrar alcance/pedientes; no sustituye T4/T5 integrales.

TQ verificada localmente: [informe de calidad](../../docs/pruebas/verificacion-calidad-2026-11-04.md). No equivale a aceptación integral.


Actualización 2026-11-05: giro/recreación, reemplazo de cuenta y permisos comprobados localmente en su alcance; [evidencia](../../docs/pruebas/rotacion-cuentas-permisos-2026-11-05.md). No cierra criterios compuestos de proveedor/roles/aceptación humana sin el resto de sus pruebas.


## Seguimiento por criterio — revisión 2026-11-05

T4 y T5 siguen abiertos donde faltan escenarios. Separar los archivos o aprobar una prueba parcial no cierra toda la tarea. Cada tarea de implementación autorizada debe incluir sus pruebas y evidencia del mismo cambio antes de marcarse terminada.

- [ ] MO-AUTH-04-T4-AC1: Falta recorrido Android de logout seguido de Atrás y reinicio/muerte del proceso para comprobar pantallas protegidas. [Criterio y pruebas](checklists/checklist.md#mo-auth-04-ac1).
- [ ] MO-AUTH-04-T4-AC2: 401 y desconexión real cierran localmente sin repetir. Falta timeout y aviso de revocación pendiente comprobado en Compose. [Criterio y pruebas](checklists/checklist.md#mo-auth-04-ac2).
- [ ] MO-AUTH-04-T4-AC3: Refresh pendiente y logout sin resurrección comprobados; falta combinación con login B y navegación Android de ambas cuentas. [Criterio y pruebas](checklists/checklist.md#mo-auth-04-ac3).
- [ ] MO-AUTH-04-T5-ACQ: Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes. [Criterio y pruebas](checklists/checklist.md#mo-auth-04-acq).
