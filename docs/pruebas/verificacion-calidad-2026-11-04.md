# Verificación local de calidad Android — 2026-11-04

Base Git: `3b128f94fd2c68ece9ec5d9639f5ccc4cd0ad566`, con cambios locales conservados. Sin commit, push ni ejecución remota nueva. Comparación contra MovilSDD `763355ac9b8760cde40143b465079d3f7fa789c5`: [fuentes y límites](../preparacion/comparacion-profesor-calidad-2026-11-04.md).

| Control ejecutado | Resultado | Alcance |
|---|---|---|
| Unitarias JVM | PASS: 35 | Reglas y fronteras; 19 identidades por operación |
| Integración JVM | PASS: 42 | Consumidor HTTP con MockWebServer HTTPS; 19 identidades por operación |
| Seguridad JVM | PASS: 39 | Sesión, transporte y errores; 19 identidades y 15 casos sin tráfico protegido |
| Suite JVM conjunta | PASS: 116, cero fallos/omisiones | Conserva las 44 pruebas anteriores |
| Android offline | PASS: 5, cero fallos/omisiones | Room, Keystore y navegación de acceso |
| Build debug/app y androidTest, Lint, Detekt | PASS | Candidato local; no firma de producción |
| Validadores Python | PASS: 19 | Incluye rechazo de ausencias, omisiones, categoría incorrecta, evidencia alterada y APK distinto |
| Gitleaks 8.30.1 | PASS: 384 archivos candidatos, cero hallazgos | Fuentes versionadas y nuevas no ignoradas; instantánea local |
| actionlint 1.7.12 | PASS | Sintaxis/política estructural del workflow; no ejecución de Actions |
| Cobertura JaCoCo 0.8.14 | 537/2756 líneas: 19,48 % | 2219 no cubiertas; sin exclusiones nuevas ni mínimo ratificado |

Las categorías se ejecutaron separadas con `-Ppactogg.testSuite=unit`, `integration` y `security`; después se ejecutó el conjunto completo y `:app:createDebugUnitTestCoverageReport`. AGP reutiliza la misma ruta JUnit; cada categoría se archivó inmediatamente antes de la siguiente ejecución. El validador contrastó identidades completas contra [operaciones.json](operaciones.json), no solo contadores. [Matriz y comandos](matriz-operaciones.md).

Android: emulador nuevo y aislado API 35 / google_atd / x86_64. Imagen oficial revisión 1, checksum publicado verificado antes de extraerla en herramientas locales. `scripts/run-instrumented.py --device emulator-5582` ejecutó únicamente los cinco métodos autorizados. No leyó credenciales QA ni borró una sesión del teléfono. El primer intento API 37.1 no terminó de arrancar: ERROR de entorno, sin atribuirle pruebas ejecutadas; el intento API 35 sí terminó y fue detenido al cerrar. No se modificaron AVD del usuario ni se instaló nada globalmente.

APK probado SHA256: `04969ab22dc4b30bdcfe840221ba689b1f412e54455acae41d3777d661aeba1d`. APK de pruebas SHA256: `dde30fa6817f0c1cae9ea5330af446ada6d29c543285c95a377072a9d7839cb2`. El manifiesto de ejecución instrumentada coincide con ambos hashes del candidato.

Evidencia generada e ignorada: `artifacts/local-ci/`, `artifacts/android-ci/`, `app/build/test-results/testDebugUnitTest/`, `app/build/reports/coverage/test/debug/` y `app/build/outputs/instrumented-offline/`. Logs locales adicionales en `../artifacts/profesor-*.log`, fuera del repositorio móvil. No confundir fixtures negativas del verificador con hallazgos o ejecuciones reales de los scanners.

## Pendientes y aceptación

El 19,48 % es una línea base, no cobertura suficiente acreditada. Propuesta: aumentar casos de reglas/estado y errores según riesgo, medir también UI antes de ratificar un mínimo bloqueante; no heredar el 45 % del profesor ni reducir el denominador para obtener verde. No se activó umbral arbitrario.

CI remoto, validación positiva/negativa real de la acción Wrapper, Semgrep/Trivy con sus motores y descarga de todos sus artefactos siguen pendientes. El grafo está preparado para fallar si falta cualquier control; no hay PASS global de seguridad ni de CI. [Pipeline](pipeline-movil.md). Las fixtures HTTP no prueban autorización del servidor ni reemplazan pruebas de proveedor. No se ejecutaron de nuevo operaciones live que crean datos.

La tarea TQ de cada operación acredita sus tres categorías locales; no cierra todos sus AC, T4/T5 integrales ni aceptación humana. PG-00 conserva pendientes de scanners/runner; PG-01 conserva ejecución remota y ratificación de política. Sin backend Docker/IaC/DAST, despliegue, herramientas duplicadas ni nuevas funciones.

El escaneo inicial de todo el directorio también recorrió artefactos locales ignorados y señaló 16 hashes públicos en los informes de pruebas. Se conservó su salida redactada, se comprobó el origen y se repitió sobre la instantánea exacta de fuentes candidatas (384 archivos), sin cambiar reglas ni añadir exclusiones. CI escanea un checkout nuevo, sin esos artefactos locales. No se ocultaron secretos de fuentes.
