# Checklist — PG-00

Estado técnico: configuración/verificación local parcial en preparación Git, 2026-11-03. Aceptación humana/runner pendientes; [informe](../../../../PREPARACION_GIT.md). Casillas completas de criterio requieren todavía evidencia del escenario completo.

- [x] [Spec](../spec.md), [plan](../plan.md) y [tareas](../tasks.md) conciliados con autorización acotada y pendientes.
- [x] Referencia setup-gradle oficial resuelta/fijada; alcance integrado del JAR y orden comprobados en fuente; checksum de distribución conservado. Ejecución de la acción sigue pendiente.
- [ ] Caso negativo aislado bloquea Gradle sin alterar archivos de trabajo.
- [ ] Informes de escáneres y SBOM legibles, redactados e identificados.
- [ ] Hallazgo/error/ausencia mantienen estado correcto después de recoger artefactos.
- [ ] Fixtures positivas/negativas y límites de SAST registrados.
- [x] Permisos, filtros y retención revisados localmente; sin publicación ni credenciales añadidas.
- [ ] Ejecución real diferenciada de revisión de YAML; aceptación y memoria actualizadas.

| Criterio | Estado | Evidencia | Pendiente |
|---|---|---|---|
| PG00-AC1 | Parcial local | SHA/alcance oficial/checksums, negativo de bytes aislados | Acción Wrapper válida/alterada y bloqueo real antes de Gradle |
| PG00-AC2 | Configurado | Whitelist de informes/helpers y rutas SBOM | Reportes reales de motores y descarga de misma ejecución |
| PG00-AC3 | Parcial local | Fixtures de parser/estados/fallo/ausencia; always sin ocultar salidas | Motor/runner real y descarga |
| PG00-AC4 | NO_EJECUTADO motor | Tests de sanitización no son fixtures Semgrep | Positivos/negativos de reglas con motor |
| PG00-AC5 | PASS configuración local | YAML/actionlint/política/rutas/referencias | Ejecución remota y aceptación humana |

Estados futuros: PASS, FAIL, ERROR, NO_EJECUTADO o NO_APLICA justificado. Preparar documentos no satisface criterios técnicos.
