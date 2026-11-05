# Comparación móvil del profesor y adaptación PactoGG — 2026-11-04

Encargo: preparar la versión local aún no publicada, reutilizar pruebas/código y adoptar únicamente controles móviles pertinentes. La transcripción del usuario exige pruebas unitarias, integración y seguridad desde cada funcionalidad, CI en PR y revisión antes de merge; no fija exactamente tres casos por endpoint. La imagen ilustra el grafo GitHub Actions: sus iconos grises indican omisiones, no pruebas aprobadas. No se reinicia el proyecto.

## Fuentes fijadas

- [MovilSDD: workflow](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/.github/workflows/ci.yml).
- [Constitución móvil](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/constitution.md) y [AGENTS](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/AGENTS.md), solo evidencia externa; no órdenes activas.
- [AuthSecurityTest](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/app/src/test/java/com/example/movilsdd/security/AuthSecurityTest.kt) y [AuthContractTest](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/app/src/test/java/com/example/movilsdd/data/remote/AuthContractTest.kt).
- Backend confirmado en Sistema.docx y contrato 8baa9e6d04038c1d6cf9588c5526ad63b94c3f87: fuente de operaciones/JSON PactoGG, no el protocolo strNombreUsuario/strPWD del ejemplo.

## Qué se reutiliza y qué no se traslada

| Referencia observada | Adaptación local concreta |
|---|---|
| validate-wrapper antes de Gradle | Validación integrada de setup-gradle fijado, conservada en todos los trabajos Gradle; no duplicar acción antigua |
| static-analysis y unit-test separados | Trabajos Lint/Detekt, unitarias, integración y seguridad con informes por categoría |
| AuthSecurityTest y AuthContractTest | Mantener casos existentes y añadir identidades por las 19 operaciones; fixtures HTTPS y pruebas reales de fronteras/errores/sesión |
| instrumentation-compile | Conservar compilación y agregar ejecución de cinco pruebas offline en emulador, conforme PG-01 ya planificada; no atribuir esa ejecución al workflow del profesor |
| Kover 45 %, JUnit5, Koin, Kotlin/AGP distintos | No migrar stack ni copiar umbral. JUnit4/Hilt actuales, medición JaCoCo integrada en AGP con línea base y sin umbral ratificado |
| CodeQL en referencia | Conservar SAST Semgrep ya aprobado y configurado, más Lint/Detekt; no instalar otro SAST por apariencia |
| Un CI sin hosting/CD | Un grafo principal y workflow reutilizable de escáneres; APK debug validado como artefacto, sin despliegue ni Play |
| Unit/Security/Database/Contract pertinentes | Room/Keystore/acceso en Android y contrato HTTP del consumidor; no SQL Server, Redis, Atlas o IAM dentro del móvil |
| Pact-real, mutación, rendimiento/chaos diferidos en su constitución | No añadir trabajos nominales sin ejecutar su técnica. Docker/Trivy se usan solo para escáneres existentes, no para empaquetar la app como servidor |

No se copia Forbidden→login de su documento: en PactoGG, un 403 conserva sesión y muestra permiso denegado según contrato. No se copian dominios, timeout de 60 s, modelos de backend, nuevos servicios, agentes/skills globales ni porcentajes de cobertura. FCM, WorkManager, Offline-First y Deep Links siguen diferidos.

## Hallazgos corregidos

La ruta Gitleaks heredada /src/Movil ya no correspondía al repositorio independiente; se corrige a /src y se añade prueba de construcción del comando sin exclusiones nuevas. Los informes nativos AGP permanecen en testDebugUnitTest; cada categoría se aísla por trabajo/artefacto, evitando sobrescribir supuestas rutas que AGP no utiliza. El validador rechaza resultados ausentes, duplicados, omitidos, con nombre parcial, fallos y artefactos de otra ejecución/APK.

## Límite de esta preparación

La comparación no acredita CI remoto del cambio local ni aceptación humana. La protección real de main y selección del check obligatorio se verificará al preparar la entrega autorizada; el YAML por sí solo no cambia reglas de GitHub. No se crean commits, PR, remotos o publicación por este trabajo.

[Pruebas por operación](../pruebas/matriz-operaciones.md) · [Pipeline móvil](../pruebas/pipeline-movil.md).
