# PG-00 — Integridad del Wrapper y evidencias de seguridad en CI

Tipo: configuración/pipeline. Fecha: 2026-10-30. Estado: borrador de cambio implementable; solo planificación autorizada.

Actualización acotada 2026-11-03: el usuario autorizó preparación Git/ajustes CI y comprobaciones afectadas. Workflows trasladados/configurados en raíz PactoGG; setup-gradle v6.4.1 oficial fijado a SHA, validación integrada/conservación de distribución comprobadas localmente y helpers de informes/estados probados. **Aceptación completa PG00-AC1–AC5 pendiente** de pruebas de acción/motores/runner/descarga; no se reducen criterios. Evidencia y pendientes (referencia histórica externa).
Rector: [AGENTS](../../AGENTS.md) y [constitución SDD](../../.specify/memory/constitution.md).

## 1. Contexto y objetivo

android-ci.yml configura compilación, pruebas JVM, Lint y Detekt; android-security.yml configura Gitleaks, Semgrep y Trivy/SBOM. Son archivos locales de una base previa, sin ejecuciones acreditadas en GitHub. Se necesita verificar la referencia de setup-gradle y su validación integrada del Wrapper, y conservar informes de seguridad aunque un escáner encuentre problemas.

Corrección de diagnóstico: [setup-gradle v4 ya valida el JAR](https://docs.gradle.org/current/userguide/gradle_wrapper.html#sec:verification); no falta una acción separada por definición. Sin embargo, la consulta del 2026-10-30 a la API oficial de gradle/actions no resolvió el commit configurado 48b5f213c81028ace310571dc5ec0fbbca0b2947 (HTTP 422, No commit found). Esa referencia se debe resolver/corregir y verificar antes de confiar en el control. Las otras tres referencias de acciones consultadas sí resolvieron; esto no acredita su ejecución ni todas sus opciones.

El cambio agregará esas comprobaciones y su evidencia, sin crear funcionalidades Android ni publicar artefactos. Fuente: [D-SDD-06](../../docs/decisiones/registro.md), [controles](../../docs/preparacion/controles-y-compuertas.md) y [seguridad](../../docs/seguridad/controles.md).

## 2. Requisitos funcionales

- RF1: resolver/corregir la referencia de setup-gradle a un commit oficial compatible y comprobar su validación integrada del JAR, incluido el Wrapper bajo Movil/, antes de Gradle. Conservar el SHA-256 de la distribución; no duplicar acciones si la validación integrada satisface el control.
- RF2: conservar informes estructurados de Gitleaks, Semgrep y Trivy, más el SBOM inspeccionado. Cada resultado identifica herramienta, alcance, versión, configuración y estado.
- RF3: recoger evidencias aun cuando un escáner falle. Distinguir hallazgos, error de herramienta e informe ausente; ninguno se transforma en éxito al subir artefactos.
- RF4: mantener permisos mínimos y acciones/imágenes fijadas. Revisar filtros de cambios para que los archivos afectados disparen el control correspondiente.

## 3. Requisitos no funcionales

Informes redactados y datos sintéticos; no publicar secretos ni payloads. No añadir cuentas, servicios de pago o permisos de escritura innecesarios. La política de severidades/excepciones y la retención de informes requieren revisión; hasta entonces no se relajan los fallos existentes.

## 4. Arquitectura y contratos internos

| Ruta o artefacto futuro | Cambio delimitado |
|---|---|
| .github/workflows/android-ci.yml | Referencia oficial resoluble de setup-gradle y validación integrada del Wrapper previa a Gradle |
| .github/workflows/android-security.yml | Misma corrección/verificación en jobs Gradle; exportación, recogida y subida de informes sin ocultar el estado del escáner |
| .semgrep-tests/ | Fixtures sintéticas aisladas para comprobar reglas locales, si la revisión confirma el formato compatible |
| Artefactos temporales de CI | Informes de seguridad, SBOM y manifiesto de alcance/estados; no se versionan salidas generadas |
| docs/seguridad/controles.md | Alcance y evidencia reales después de implementar/verificar |

No cambia .semgrep.yml ni .gitleaks.toml para silenciar hallazgos. Una corrección sustancial de reglas requiere delimitar su cambio y sus casos. La validación del JAR y la de la distribución son controles distintos.

Corrección delimitada pre-Git: cinco falsos positivos de SHA256 públicos en el manifiesto histórico se revisaron y se exceptuaron solo para `generic-api-key` con condición AND de archivo/valores exactos. El manifiesto no se modifica ni excluye en bloque; motor real probado con cuatro casos de ruta/valor permitido o rechazado. No se silencian secretos reales ni se cambian reglas Semgrep. Evidencia/motivo en el informe común; fixtures Semgrep y ejecución remota siguen pendientes.

## 5. Criterios de aceptación

| ID | Dado / cuando / entonces | Evidencia futura |
|---|---|---|
| PG00-AC1 | Dado un Wrapper válido, su validación precede a Gradle en ambos workflows; uno alterado en una copia aislada impide llegar a Gradle | Logs y caso negativo, sin alterar el Wrapper de trabajo |
| PG00-AC2 | Cuando hay ejecución de seguridad, quedan informes legibles y SBOM de la misma ejecución, con alcance y versiones identificados | Artefacto y manifiesto sin secretos |
| PG00-AC3 | Cuando hay hallazgo, herramienta rota o informe ausente, el resumen los distingue y el fallo aplicable se conserva tras recoger evidencias | Casos sintéticos de fallo y estados finales |
| PG00-AC4 | Dadas muestras sintéticas que activan/no activan reglas, se demuestra su comportamiento; el control no declara cobertura total de SAST | Resultado de fixtures y límites documentados |
| PG00-AC5 | Al revisar YAML, se conservan permisos mínimos, referencias fijadas, filtros pertinentes y ausencia de publicación | Revisión de configuración y ejecuciones autorizadas |

## 6. Plan de pruebas

Verificación estructural de YAML y orden de jobs/pasos; casos de Wrapper válido/alterado aislado; fixtures locales de reglas; simulación de hallazgo/error/reporte ausente. Después, ejecución real de los jobs en un repositorio autorizado. Validar redacción y descarga de informes. Esta spec no crea ese repositorio ni registra resultados futuros como PASS.

## 7. Riesgos, límites y deuda

La referencia oficial compatible de setup-gradle y el alcance de validación deben verificarse al implementar. La consulta remota detecta una referencia no resoluble en ese momento, no prueba un ataque ni un fallo de ejecución observado. Los escáneres pueden exponer datos en informes si no se revisa su redacción. El SBOM actual cubre runtime release, no plugins ni backend. Gitleaks de archivos presentes no acredita historial; su ampliación depende de que exista Git. CodeQL adicional queda como decisión de cobertura, no como instalación incluida. Backend/IaC/DAST y firma quedan fuera.

## Aprobación

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación autorizada | Usuario | 2026-10-30 | Petición de preparar cambios y specs semejantes en formato; no abre ejecución técnica |
| Diseño en borrador | Responsables por confirmar | Pendiente | Referencia oficial, validación integrada, informes, retención y política por revisar |
| Implementación no iniciada | Pendiente | Pendiente | Sin evidencias de aceptación |
| Ajuste técnico acotado autorizado | Usuario | 2026-11-03 | Fuente: preparación final para Git; actualiza el estado histórico anterior, sin producto |
| Configuración / revisión local parcial | Agente; herramientas identificadas en informe | 2026-11-03 | Referencias/rutas/política/helpers, no aprobación humana del diseño |
| Aceptación completa | Pendiente | Pendiente | Mantener AC1–AC5, negativo de acción Wrapper, motores y CI real |

Expediente: [plan](plan.md), [tareas](tasks.md), [checklist](checklists/checklist.md).


## Alcance autorizado de calidad — 2026-11-04

Usuario pide completar calidad del móvil local sin reiniciarlo ni copiar controles backend. [Comparación fijada](../../docs/preparacion/comparacion-profesor-calidad-2026-11-04.md). Se reutiliza android-security.yml como workflow_call y se centraliza grafo/disparadores en android-ci.yml; setup compartido en .github/actions/android-setup. No se crean jobs vacíos ni éxitos por herramientas no ejecutadas.

- RF5: gate final consume informes de trabajos obligatorios y comprueba run/attempt/source, matriz/helper, hashes de XML/APKs/SBOM y casos exactos; failure/skipped/cancelled/missing falla.
- RF6: conservar candidato debug y test APK antes de emulador; solo después de gate se publica como artefacto validado el mismo binario, sin recompilar ni desplegar.
- PG00-AC6: con upstream verde pero evidencia ausente, vieja, modificada, prueba omitida o APK diferente, el gate falla; con evidencia completa del mismo candidato pasa. Evidencia local sintética del helper y futura ejecución real se distinguen.

La ruta Gitleaks se corrige a /src después de separar el repositorio móvil, conservando reglas/exclusiones y prueba de alcance del comando. Permisos contents:read; no nuevas credenciales. Acciones referenciadas por SHA oficial y herramientas en imágenes existentes. La captura de pantalla contiene controles omitidos: no se copian ni se cuentan verdes.

Las frases iniciales de ausencia de CI describen antecedentes. Ya hubo CI remoto de candidatos antiguos; la configuración actual no se acredita ejecutada remotamente hasta subir por autorización. No cerrar AC1/AC4 integrales por revisión local de YAML o pruebas de sanitización.

Resultados actuales: [informe de calidad](../../docs/pruebas/verificacion-calidad-2026-11-04.md). La aprobación histórica de diseño no acredita aceptación final ni ejecución remota.
