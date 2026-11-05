# Tasks — PG-00

Estado actualizado 2026-11-03: configuración/verificaciones locales acotadas autorizadas en preparación Git; implementación/aceptación completa parcial. [Spec](spec.md) · [plan](plan.md) · evidencia (referencia histórica externa).

- [x] T00.1 Revisar referencia no resoluble de setup-gradle, elegir commit oficial compatible y verificar validación integrada — PG00-AC1/AC5; v6.4.1/SHA oficial, action.yml/código y checksums; no ejecución de acción acreditada.
- [x] T00.2 Corregir referencia/opciones necesarias en ambos workflows y comprobar validación del Wrapper bajo Movil/ antes de Gradle, sin acción redundante — PG00-AC1; evidencia: validación positiva y negativa aislada.
- [x] T00.3 Exportar Gitleaks/Semgrep/Trivy y conservar SBOM/manifiesto con datos redactados — PG00-AC2; evidencia: artefactos legibles de la misma ejecución.
- [x] T00.4 Implementar recogida de informes y propagación de fallos/ausencias — PG00-AC3; evidencia: casos sintéticos y estado final de cada uno.
- [x] T00.5 Añadir/comprobar fixtures sintéticas .semgrep-tests/ con positivos y negativos — PG00-AC4; evidencia: reporte y límites de las reglas.
- [x] T00.6 Verificar permisos, referencias, filtros y retención; ejecutar CI solo al estar autorizado — PG00-AC5; evidencia: revisión y ejecución identificada, o NO_EJECUTADO.
- [x] T00.7 Actualizar docs/seguridad/controles.md, conciliar expediente y memoria — cierre local acotado; criterios completos y pendientes conservados.

T00.2–T00.4 y T00.6 tienen configuración/helpers/rutas/política implementados y pruebas locales; las casillas siguen abiertas por el negativo de acción, reportes de motores/runner y descarga reales. T00.5 no ejecutado. Este encargo expreso permite ajustes y herramientas locales/portables de comprobación, no Git ni instalaciones globales. Cierre en [checklists/checklist.md](checklists/checklist.md).


Actualización 2026-11-04: código/configuración de categorías, medición, runner y gate incorporados bajo autorización expresa. Las tareas iniciales que exigían no ejecutar al redactar son antecedentes; el informe de esta fase distinguirá verificación local, motor real, fixtures y CI remoto pendiente.

Verificación 2026-11-04: actionlint y 19 pruebas de helpers/política correctos; Gitleaks sobre 384 fuentes candidatas sin hallazgos. Gate con rechazo de omisión, informe faltante/alterado y APK distinto validado localmente. T00.2–T00.6 siguen abiertas en sus partes de acción/motores/CI remoto. [Informe](../../docs/pruebas/verificacion-calidad-2026-11-04.md).

## Cierre posterior 2026-11-05

Regresiones oficiales y artefactos reales correctos en run41977160594: negativo Wrapper identifica la copia alterada exacta, Semgrep verifica tres positivos/tres negativos/ámbito, scanners/gate correctos. [Evidencia](../../docs/pruebas/cierre-pendientes-2026-11-04.md). Las notas previas de pendientes de motor son historia; aceptación humana no inferida.
