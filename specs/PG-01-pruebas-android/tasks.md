# Tasks — PG-01

Estado: implementación de calidad local autorizada; resultados por tarea al cierre, CI nuevo pendiente. [Spec](spec.md) · [plan](plan.md).

- [x] T01.1 Revisar suites/stack y elegir imagen/API/ABI/recursos y motor de cobertura compatibles — PG01-AC1/AC3; evidencia: decisión y matriz de entorno.
- [x] T01.2 Ejecutar/diagnosticar Room y navegación; corregir solo aislamiento o fallos reales del alcance — PG01-AC1; evidencia: resultados por caso, no solo compilación.
- [x] T01.3 Configurar medición JVM en Gradle/catálogo según decisión — PG01-AC3; evidencia: informe, código medido y exclusiones justificadas.
- [x] T01.4 Agregar job manual instrumentado y recogida de resultados a android-ci.yml — PG01-AC2/AC5; evidencia: ejecución real y diagnóstico.
- [x] T01.5 Comprobar estados de test fallido, entorno roto, informe ausente y cero pruebas esperadas — PG01-AC3/AC5; evidencia: casos controlados y resultado final correcto.
- [x] T01.6 Registrar línea base y propuesta de umbral separada de su aprobación — PG01-AC4; evidencia: medición reproducible y decisión pendiente/ratificada.
- [x] T01.7 Actualizar docs/pruebas.md, conciliar aceptación/checklist/memoria — todos; evidencia: vínculo de cada criterio a su resultado real.

Antecedente de preparación: no ejecutar tareas por copiar el expediente. Cierre en [checklists/checklist.md](checklists/checklist.md).


Actualización 2026-11-04: código/configuración de categorías, medición, runner y gate incorporados bajo autorización expresa. Las tareas iniciales que exigían no ejecutar al redactar son antecedentes; el informe de esta fase distinguirá verificación local, motor real, fixtures y CI remoto pendiente.

Cierre local: 116 JVM y cinco Android API35, medición real y validadores negativos correctos. [Evidencia](../../docs/pruebas/verificacion-calidad-2026-11-04.md). T01.4 conserva pendiente la ejecución remota aunque su configuración ya existe. T01.6 documenta propuesta, no ratificación de porcentaje.

T01.4 ejecutada en GitHub bajo autorización posterior: PR8/run41969860447, cinco métodos Android API35 y recogida real de artefactos. [Entrega](../../docs/pruebas/entrega-github-2026-11-04.md).

Actualización 2026-11-05: 192 JVM y 26 validadores; cobertura 27,14 % local/26,96 % CI. Cinco Android offline y gate correctos en run41977160594. Mínimo de cobertura sigue sin ratificar. [Cierre](../../docs/pruebas/cierre-pendientes-2026-11-04.md).
