# Evidencia remota Android — 2026-11-03

Repositorio: https://github.com/equipopatogg/Movil. Android está directamente en la raíz remota. El espacio local original conserva Movil/ y Backend/ separados; no confundir las rutas de ambos formatos.

Commit verificado: `2a9fb5efeeec2e06c2d01c38dba4bfb1d009536a`, «Corregir preparación del SDK Android en CI».

| Ejecución | Resultado comprobado |
|---|---|
| [Calidad 41750351676](https://github.com/equipopatogg/Movil/actions/runs/41750351676) | SUCCESS; SDK, Wrapper, helper, build debug, 5 JVM/0 fallos/0 omitidas, Lint, Detekt, compilación androidTest y SBOM; resumen descargado PASS |
| [Seguridad 41749985667](https://github.com/equipopatogg/Movil/actions/runs/41749985667) | SUCCESS; informes descargados Gitleaks/Semgrep/Trivy y resúmenes PASS, vinculados al mismo commit |

Se descargaron los artefactos y se contrastaron source/run/estado. Corrección: preparar SDK con android-actions/setup-android fijado a 035682743ab223ec9c6169af09811dff8247e520; no cambiar versiones Android. Ejecuciones anteriores del primer commit fallaron por sdkmanager ausente. La ejecución intermedia 41749971028 fue cancelada por concurrencia y su informe falló correctamente; NO constituye PASS. La ejecución de calidad válida es la de la tabla.

Límites: no ejecución en dispositivo, cobertura ni release de 16 KB/firma/Play. Semgrep con reglas locales no acredita cobertura completa Kotlin ni sus fixtures positivas/negativas. No se demostró el negativo de Wrapper alterado en runner, ni funcionalidades/API de negocio ni aceptación humana. PG-00 sigue parcialmente abierto, PG-01/02/03/04 sin aceptación por estos resultados.

Estos PASS corresponden a ese commit, no se transfieren a futuros cambios. No se ejecuta CI nuevamente por esta preparación documental.
