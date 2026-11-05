# Checklist — PG-00

Estado técnico: verificado en el alcance técnico documentado, actualización 2026-11-05. Aceptación humana/producción separadas.

- [x] [Spec](../spec.md), [plan](../plan.md) y [tareas](../tasks.md) conciliados con autorización acotada y pendientes.
- [x] Referencia setup-gradle oficial resuelta/fijada; alcance integrado del JAR y orden comprobados en fuente; checksum de distribución conservado. Acción válida/alterada ejecutada: ver cierre posterior.
- [x] Caso negativo aislado bloquea Gradle sin alterar archivos de trabajo.
- [x] Informes de escáneres y SBOM legibles, redactados e identificados.
- [x] Hallazgo/error/ausencia mantienen estado correcto después de recoger artefactos.
- [x] Fixtures positivas/negativas y límites de SAST registrados.
- [x] Permisos, filtros y retención revisados localmente; sin publicación ni credenciales añadidas.
- [x] Ejecución real diferenciada de revisión de YAML; aceptación y memoria actualizadas.

| Criterio | Estado | Evidencia | Pendiente |
|---|---|---|---|
| PG00-AC1 | PASS técnico acotado | Regresiones/motores y gate: run41977160594 | Aceptación humana; producción/fuentes de producto aparte |
| PG00-AC2 | PASS técnico acotado | Regresiones/motores y gate: run41977160594 | Aceptación humana; producción/fuentes de producto aparte |
| PG00-AC3 | PASS técnico acotado | Regresiones/motores y gate: run41977160594 | Aceptación humana; producción/fuentes de producto aparte |
| PG00-AC4 | PASS técnico acotado | Regresiones/motores y gate: run41977160594 | Aceptación humana; producción/fuentes de producto aparte |
| PG00-AC5 | PASS técnico acotado | Regresiones/motores y gate: run41977160594 | Aceptación humana; producción/fuentes de producto aparte |

Estados futuros: PASS, FAIL, ERROR, NO_EJECUTADO o NO_APLICA justificado. Preparar documentos no satisface criterios técnicos.

## Evidencia posterior — 2026-11-03

[CI remoto verificado](../../../docs/preparacion/ci-remoto-android.md): calidad/seguridad PASS para 2a9fb5e, artefactos descargados y resúmenes vinculados. Se acreditan ejecución ordinaria, informes y permisos operativos de esas ejecuciones. No marcar PG-00 entero PASS: siguen pendientes fixtures de reglas Semgrep y negativo de Wrapper alterado en runner, revisión completa de criterios y aceptación independiente.


## Actualización de calidad — 2026-11-04

Comparación móvil y grafo propios implementados localmente. Se conserva aceptación integral abierta hasta comprobar todos los criterios pertinentes, ejecución remota y revisión humana. No usar omisiones ni número total de pruebas para cerrar AC de funcionalidad. Ver docs/pruebas/pipeline-movil.md y matriz-operaciones.md.

Cierre técnico vigente: [evidencia y límites](../../../docs/pruebas/cierre-pendientes-2026-11-04.md). Los pendientes de fases anteriores conservados debajo/arriba son antecedentes; no anulan resultados posteriores. Los recorridos autenticados completos y Play no se acreditan por este cierre.
