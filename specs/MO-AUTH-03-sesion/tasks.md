# Tareas — MO-AUTH-03

Estado: implementación local iniciada; ver actualización inferior. [Spec](spec.md) · [Plan](plan.md).

- [ ] MO-AUTH-03-T1: Conciliar contrato, reglas y bloqueantes específicos con evidencia de la fuente.
- [x] MO-AUTH-03-T2: Definir DTO/mappers y repositorio de session, reutilizando transporte/sesión compartidos.
- [x] MO-AUTH-03-T3: Adaptar pantalla y estado real, carga/error/éxito y navegación de la operación contratada.
- [ ] MO-AUTH-03-T4: Implementar pruebas por cada AC: JVM/HTTP y Compose/dispositivo aplicables.
- [ ] MO-AUTH-03-T5: Verificar compatibilidad del proveedor y controles del candidato; registrar fallos y omisiones.
- [ ] MO-AUTH-03-T6: Conciliar checklist, memoria y aceptación sin transferir PASS del prototipo.


T2/T3 acreditan creación de código local, no aceptación integral. T1 conserva conciliaciones pendientes; T4 requiere también ejecución Android; T5/T6 conservan pruebas de proveedor y aceptación. Resultados por candidato en docs/integracion/verificacion-consumidor-2026-11-04.md.


- [x] MO-AUTH-03-TQ: Comprobar instancias unit/integration/security exactas de esta operación en informes JUnit y registrar alcance/pedientes; no sustituye T4/T5 integrales.

TQ verificada localmente: [informe de calidad](../../docs/pruebas/verificacion-calidad-2026-11-04.md). No equivale a aceptación integral.


## Seguimiento por criterio — revisión 2026-11-05

T4 y T5 siguen abiertos donde faltan escenarios. Separar los archivos o aprobar una prueba parcial no cierra toda la tarea. Cada tarea de implementación autorizada debe incluir sus pruebas y evidencia del mismo cambio antes de marcarse terminada.

- [ ] MO-AUTH-03-T4-AC1: Dos respuestas 401 simultáneas y una sola rotación comprobadas en SessionManager con barrera y endpoint refresh HTTPS. Falta recorrido completo desde Compose. [Criterio y pruebas](checklists/checklist.md#mo-auth-03-ac1).
- [ ] MO-AUTH-03-T4-AC2: Desconexión real de refresh borra custodia sin repetir; controles de 401/403/5xx previos conservados. Falta timeout y comparación de mensajes de UI. [Criterio y pruebas](checklists/checklist.md#mo-auth-03-ac2).
- [ ] MO-AUTH-03-T4-AC3: Faltan ensayo de backup/restauración, muerte/reinicio del proceso y ventana de logs Android con marcadores. Reabrir un objeto no equivale a reiniciar el proceso. [Criterio y pruebas](checklists/checklist.md#mo-auth-03-ac3).
- [ ] MO-AUTH-03-T4-AC4: Logout solicitado durante refresh pendiente acaba sin sesión; fallo al persistir tokens rotados borra identidad anterior/nueva. Falta combinación con login B en esa misma ventana y recorrido Android. [Criterio y pruebas](checklists/checklist.md#mo-auth-03-ac4).
- [ ] MO-AUTH-03-T5-ACQ: Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes. [Criterio y pruebas](checklists/checklist.md#mo-auth-03-acq).
