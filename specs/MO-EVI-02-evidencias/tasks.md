# Tareas — MO-EVI-02

Estado: implementación local iniciada; ver actualización inferior. [Spec](spec.md) · [Plan](plan.md).

- [ ] MO-EVI-02-T1: Conciliar contrato, reglas y bloqueantes específicos con evidencia de la fuente.
- [x] MO-EVI-02-T2: Definir DTO/mappers y repositorio de evidence, reutilizando transporte/sesión compartidos.
- [x] MO-EVI-02-T3: Adaptar pantalla y estado real, carga/error/éxito y navegación de la operación contratada.
- [ ] MO-EVI-02-T4: Implementar pruebas por cada AC: JVM/HTTP y Compose/dispositivo aplicables.
- [ ] MO-EVI-02-T5: Verificar compatibilidad del proveedor y controles del candidato; registrar fallos y omisiones.
- [ ] MO-EVI-02-T6: Conciliar checklist, memoria y aceptación sin transferir PASS del prototipo.


T2/T3 acreditan creación de código local, no aceptación integral. T1 conserva conciliaciones pendientes; T4 requiere también ejecución Android; T5/T6 conservan pruebas de proveedor y aceptación. Resultados por candidato en docs/integracion/verificacion-consumidor-2026-11-04.md.


Reprueba física posterior (2026-11-04): creación real de reto y envíos de enlace/foto sintética PASS tras corrección AWS informada por backend. Ver [informe](../../docs/integracion/verificacion-telefono-2026-11-04.md). T4–T6 siguen parciales: faltan casos negativos/límites, selector nativo para foto y aceptación; no se cierran por un caso positivo.


Ajuste acotado autorizado: estado del reto después de evidencia en MO-EVI-02; consulta auxiliar de detalle, confirmación preservada si falla lectura y mensajes sin atribuir ausencia de endpoints a backend. Ver AC5 de MO-EVI-02. Cierre de las specs sigue pendiente por los otros criterios.


- [x] MO-EVI-02-TQ: Comprobar instancias unit/integration/security exactas de esta operación en informes JUnit y registrar alcance/pedientes; no sustituye T4/T5 integrales.

TQ verificada localmente: [informe de calidad](../../docs/pruebas/verificacion-calidad-2026-11-04.md). No equivale a aceptación integral.


## Seguimiento por criterio — revisión 2026-11-05

T4 y T5 siguen abiertos donde faltan escenarios. Separar los archivos o aprobar una prueba parcial no cierra toda la tarea. Cada tarea de implementación autorizada debe incluir sus pruebas y evidencia del mismo cambio antes de marcarse terminada.

- [ ] MO-EVI-02-T4-AC1: Faltan asserts de Content-Type interno de foto y UI completa de ambos tipos sobre el candidato; evidencia real anterior se conserva como histórica. [Criterio y pruebas](checklists/checklist.md#mo-evi-02-ac1).
- [ ] MO-EVI-02-T4-AC2: Faltan cancelación del selector, archivo grande, MIME inválido y ausencia de cámara/video en la UI instalada. [Criterio y pruebas](checklists/checklist.md#mo-evi-02-ac2).
- [ ] MO-EVI-02-T4-AC3: CONTENT_REJECTED/EVIDENCE_LIMIT_REACHED preservan enlace y no inventan evidencia; falta recorrido Compose y moderación de foto. [Criterio y pruebas](checklists/checklist.md#mo-evi-02-ac3).
- [ ] MO-EVI-02-T4-AC4: POST con respuesta perdida no se repite ni confirma evidencia; falta apertura bloqueada por contrato no autorizado en Compose. [Criterio y pruebas](checklists/checklist.md#mo-evi-02-ac4).
- [ ] MO-EVI-02-T5-ACQ: Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes. [Criterio y pruebas](checklists/checklist.md#mo-evi-02-acq).
