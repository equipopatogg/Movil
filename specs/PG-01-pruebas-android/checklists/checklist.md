# Checklist — PG-01

Estado técnico: verificado localmente el 2026-11-04; CI remoto y aceptación humana pendientes.

- [x] [Spec](../spec.md), [plan](../plan.md), [tareas](../tasks.md), entorno y fase revisados.
- [x] Motor de medición compatible y cambios de dependencias autorizados.
- [x] Room y navegación ejecutados en dispositivo identificado; casos y omisiones visibles.
- [x] CI ejecuta instrumentación real, también disponible por vía manual; compilar no satisface ese criterio.
- [x] Cobertura JVM real, exclusiones justificadas y rechazo de ausencia/cero pruebas.
- [x] Línea base registrada; propuesta y ratificación de umbral diferenciadas.
- [x] FAIL de pruebas y ERROR de entorno diferenciados, con diagnóstico conservado.
- [x] Documentación/aceptación/memoria conciliadas, sin atribuir pruebas de login al cascarón.

| Criterio | Estado | Evidencia | Pendiente |
|---|---|---|---|
| PG01-AC1 | PASS local | Cinco métodos Android API35, cero omisiones | Alcance del informe; no aceptación integral |
| PG01-AC2 | PASS remoto | PR8/run41969860447: cinco Android y gate PASS | Sin aceptación humana integral |
| PG01-AC3 | PASS local | 116 JVM y 537/2756 líneas medidas | Alcance del informe; no aceptación integral |
| PG01-AC4 | PASS local | Línea base y propuesta sin ratificación | Alcance del informe; no aceptación integral |
| PG01-AC5 | PASS local | 19 verificaciones negativas; arranque fallido diferenciado de ejecución | Alcance del informe; no aceptación integral |

Estados futuros: PASS, FAIL, ERROR, NO_EJECUTADO o NO_APLICA justificado. Los resultados históricos se enlazan con sus límites, no se transfieren.


## Actualización de calidad — 2026-11-04

Comparación móvil y grafo propios implementados localmente. Se conserva aceptación integral abierta hasta comprobar todos los criterios pertinentes, ejecución remota y revisión humana. No usar omisiones ni número total de pruebas para cerrar AC de funcionalidad. Ver docs/pruebas/pipeline-movil.md y matriz-operaciones.md.

[Informe verificable](../../../docs/pruebas/verificacion-calidad-2026-11-04.md). PG01-AC6: PASS local; PG01-AC2: NO_EJECUTADO remoto. Las casillas de CI conservan pendiente.

Actualización posterior: PG01-AC2 PASS remoto, [entrega](../../../docs/pruebas/entrega-github-2026-11-04.md); las notas anteriores de NO_EJECUTADO son historia del cierre local.
