# Verificación local — 2026-10-29

## Comprobado

- APK debug compilado con la estructura actual.
- APK release optimizado con R8, sin firma de producción: compilado correctamente.
- Cinco pruebas JVM: 5 correctas, 0 fallos (TLS real con certificado de prueba, redirecciones, URLs y mapper).
- Android Lint: 0 incidencias con la política de mantenimiento descrita en versiones.md.
- Detekt: correcto.
- APK de pruebas instrumentadas: compilado.
- Esquema Room v1 exportado y conservado en Movil/app/schemas/.
- CycloneDX 1.6: SBOM de runtime release, 192 componentes más el componente raíz.
- Trivy 0.76.1: 193 paquetes analizados; sin hallazgos HIGH/CRITICAL en esta ejecución. No se evaluaron severidades inferiores.
- Gitleaks 8.30.1: sin hallazgos en archivos locales; no existe historial Git que analizar.
- Configuraciones TOML y YAML: sintaxis válida; expresiones regulares de Semgrep comprobadas sintácticamente.
- Acciones CI fijadas a commit e imágenes de escáner a versión y digest.

## Alcance y pendientes

Semgrep no se ejecutó localmente: su workflow queda preparado; validar sus tres reglas con el motor antes de activar CI.
Los workflows no se han ejecutado en GitHub; no se creó repositorio, remoto ni publicación.
Las pruebas instrumentadas no tienen aprobación final: una ejecución anterior confirmó Room, pero la suite final en API 37.1 terminó con proceso de instrumentación caído. También se observó bloqueo de System UI, presión de memoria en el host y aviso 16 KB. No se consideran esas ejecuciones una suite aprobada.
Pendiente repetir Room/navegación en un entorno estable y resolver el RELRO de graphics-path antes de publicar.
No hay backend, autenticación, proveedor IA, firma Play, DAST, escaneo de infraestructura ni auditoría manual completa de seguridad.
No se afirma que la maqueta o las historias de producto estén implementadas por disponer de estas vistas iniciales.

## Artefactos locales

- Movil/app/build/outputs/apk/debug/app-debug.apk
- Movil/app/build/outputs/apk/release/app-release-unsigned.apk
- Movil/app/build/reports/tests/testDebugUnitTest/index.html
- Movil/app/build/reports/lint-results-debug.html
- Movil/app/build/reports/cyclonedx-direct/bom.json
- Movil/app/build/reports/trivy-release.json

Los informes y paquetes son salidas generadas excluidas por .gitignore.

## Confirmación final de esta preparación

Build final: correcto (debug, release sin firma de producción, APK androidTest, Detekt, Lint y SBOM).
Pruebas JVM: 5, fallos: 0. Incidencias Lint: 0.
SBOM final: 192 componentes más el componente raíz. Trivy: 193 paquetes, 0 hallazgos HIGH/CRITICAL. Gitleaks final: sin hallazgos.
La suite instrumentada permanece pendiente; su compilación no se declara una ejecución exitosa.
Los emuladores y procesos de compilación iniciados para estas pruebas se cerraron.
