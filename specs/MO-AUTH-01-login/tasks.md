# Tareas — MO-AUTH-01

Estado: implementación local iniciada; ver actualización inferior. [Spec](spec.md) · [Plan](plan.md).

- [ ] MO-AUTH-01-T1: Conciliar contrato, reglas y bloqueantes específicos con evidencia de la fuente.
- [x] MO-AUTH-01-T2: Definir DTO/mappers y repositorio de auth, reutilizando transporte/sesión compartidos.
- [x] MO-AUTH-01-T3: Adaptar pantalla y estado real, carga/error/éxito y navegación de la operación contratada.
- [ ] MO-AUTH-01-T4: Implementar pruebas por cada AC: JVM/HTTP y Compose/dispositivo aplicables.
- [ ] MO-AUTH-01-T5: Verificar compatibilidad del proveedor y controles del candidato; registrar fallos y omisiones.
- [ ] MO-AUTH-01-T6: Conciliar checklist, memoria y aceptación sin transferir PASS del prototipo.


T2/T3 acreditan creación de código local, no aceptación integral. T1 conserva conciliaciones pendientes; T4 requiere también ejecución Android; T5/T6 conservan pruebas de proveedor y aceptación. Resultados por candidato en docs/integracion/verificacion-consumidor-2026-11-04.md.


- [x] MO-AUTH-01-TQ: Comprobar instancias unit/integration/security exactas de esta operación en informes JUnit y registrar alcance/pedientes; no sustituye T4/T5 integrales.

TQ verificada localmente: [informe de calidad](../../docs/pruebas/verificacion-calidad-2026-11-04.md). No equivale a aceptación integral.


Actualización 2026-11-05: giro/recreación, reemplazo de cuenta y permisos comprobados localmente en su alcance; [evidencia](../../docs/pruebas/rotacion-cuentas-permisos-2026-11-05.md). No cierra criterios compuestos de proveedor/roles/aceptación humana sin el resto de sus pruebas.


## Seguimiento por criterio — revisión 2026-11-05

T4 y T5 siguen abiertos donde faltan escenarios. Separar los archivos o aprobar una prueba parcial no cierra toda la tarea. Cada tarea de implementación autorizada debe incluir sus pruebas y evidencia del mismo cambio antes de marcarse terminada.

- [ ] MO-AUTH-01-T4-AC1: Completar interacción UI de login 200 y 401 del candidato actual, incluyendo destino de éxito; el recorrido real anterior queda como histórico. [Criterio y pruebas](checklists/checklist.md#mo-auth-01-ac1).
- [ ] MO-AUTH-01-T4-AC2: 422 y desconexión de login no autentican ni repiten. Falta timeout con plazo agotado y recuperación mediante Compose. [Criterio y pruebas](checklists/checklist.md#mo-auth-01-ac2).
- [ ] MO-AUTH-01-T4-AC3: Doble Submit válido en vuelo comprobado en ViewModel. Falta recreación de Activity durante envío válido; no atribuirlo al giro del formulario sin envío. [Criterio y pruebas](checklists/checklist.md#mo-auth-01-ac3).
- [ ] MO-AUTH-01-T5-ACQ: Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes. [Criterio y pruebas](checklists/checklist.md#mo-auth-01-acq).
