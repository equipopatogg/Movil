# Tareas — MO-CON-01

Estado: implementación local iniciada; ver actualización inferior. [Spec](spec.md) · [Plan](plan.md).

- [ ] MO-CON-01-T1: Conciliar contrato, reglas y bloqueantes específicos con evidencia de la fuente.
- [x] MO-CON-01-T2: Definir DTO/mappers y repositorio de contracts, reutilizando transporte/sesión compartidos.
- [x] MO-CON-01-T3: Adaptar pantalla y estado real, carga/error/éxito y navegación de la operación contratada.
- [ ] MO-CON-01-T4: Implementar pruebas por cada AC: JVM/HTTP y Compose/dispositivo aplicables.
- [ ] MO-CON-01-T5: Verificar compatibilidad del proveedor y controles del candidato; registrar fallos y omisiones.
- [ ] MO-CON-01-T6: Conciliar checklist, memoria y aceptación sin transferir PASS del prototipo.


T2/T3 acreditan creación de código local, no aceptación integral. T1 conserva conciliaciones pendientes; T4 requiere también ejecución Android; T5/T6 conservan pruebas de proveedor y aceptación. Resultados por candidato en docs/integracion/verificacion-consumidor-2026-11-04.md.


Reprueba física posterior (2026-11-04): creación real de reto y envíos de enlace/foto sintética PASS tras corrección AWS informada por backend. Ver [informe](../../docs/integracion/verificacion-telefono-2026-11-04.md). T4–T6 siguen parciales: faltan casos negativos/límites, selector nativo para foto y aceptación; no se cierran por un caso positivo.


Ajuste acotado autorizado: estado del reto después de evidencia en MO-EVI-02; consulta auxiliar de detalle, confirmación preservada si falla lectura y mensajes sin atribuir ausencia de endpoints a backend. Ver AC5 de MO-EVI-02. Cierre de las specs sigue pendiente por los otros criterios.


- [x] MO-CON-01-TQ: Comprobar instancias unit/integration/security exactas de esta operación en informes JUnit y registrar alcance/pedientes; no sustituye T4/T5 integrales.

TQ verificada localmente: [informe de calidad](../../docs/pruebas/verificacion-calidad-2026-11-04.md). No equivale a aceptación integral.


## Seguimiento por criterio — revisión 2026-11-05

T4 y T5 siguen abiertos donde faltan escenarios. Separar los archivos o aprobar una prueba parcial no cierra toda la tarea. Cada tarea de implementación autorizada debe incluir sus pruebas y evidencia del mismo cambio antes de marcarse terminada.

- [ ] MO-CON-01-T4-AC1: Falta recorrido UI del formulario completo con settings reales y asserts completos del resumen open del candidato actual. [Criterio y pruebas](checklists/checklist.md#mo-con-01-ac1).
- [ ] MO-CON-01-T4-AC2: CONTENT_REJECTED/MODERATION_UNAVAILABLE conservan borrador sin éxito. Faltan conversión local→UTC/DST y fronteras completas. [Criterio y pruebas](checklists/checklist.md#mo-con-01-ac2).
- [ ] MO-CON-01-T4-AC3: Faltan casos sin grupo y sin settings, bloqueo de voz y separación de aceptar frente a crear en UI; inspección de código no sustituye esos casos. [Criterio y pruebas](checklists/checklist.md#mo-con-01-ac3).
- [ ] MO-CON-01-T4-AC4: Desconexión tras recepción de POST no repite creación ni confirma éxito; falta recorrido de recuperación en Compose. [Criterio y pruebas](checklists/checklist.md#mo-con-01-ac4).
- [ ] MO-CON-01-T5-ACQ: Pruebas locales actuales y controles acotados registrados; CI remoto diferido por el usuario. Accesibilidad integral/aceptación humana siguen pendientes. [Criterio y pruebas](checklists/checklist.md#mo-con-01-acq).
# Correccion de mensaje GROUP_TOO_SMALL - 2026-11-08

Implementado mensaje por codigo conforme al contrato local v0.8 (al menos tres integrantes). No cambia la autorizacion del servidor ni supone que todos los conflictos 409 tengan esa causa. Regresiones especificas en ApiErrorMapperTest: CON_01_smallGroupConflictExplainsMinimumWithoutExposingServerText y CON_01_unknownConflictDoesNotClaimGroupIsTooSmall; ejecucion y limites en Memoria.md. La captura del usuario no revela el codigo real y no acredita esta causa ni creacion exitosa.
