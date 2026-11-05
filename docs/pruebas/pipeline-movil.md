# Pipeline Android — preparación local y evidencia

Un solo disparador en [android-ci.yml](../../.github/workflows/android-ci.yml), con escáneres reutilizados en [android-security.yml](../../.github/workflows/android-security.yml). Segundo archivo es workflow_call, sin push/PR propios: no dispara duplicados. Preparado para PR, push y ejecución manual. Sin CD, Docker de aplicación, despliegue backend ni Play.

```mermaid
flowchart LR
  W[Wrapper y política SDD] --> A[Lint y Detekt]
  A --> U[Unitarias]
  A --> I[Integración HTTP]
  A --> S[Seguridad consumidor]
  A --> C[Cobertura JVM medida]
  W --> L[Gitleaks]
  W --> G[Semgrep SAST]
  W --> D[SCA y SBOM]
  U --> B[Build APK y androidTest]
  I --> B
  S --> B
  C --> B
  B --> E[Emulador Room / Keystore / acceso]
  E --> Q[Android PR quality gate]
  B --> Q
  U --> Q
  I --> Q
  S --> Q
  C --> Q
  L --> Q
  G --> Q
  D --> Q
  Q --> P[APK debug validado]
```

## Comprobaciones obligatorias

- Wrapper integrado en setup-gradle antes de cualquier Gradle. Acciones fijadas a SHA y permisos contents:read. No hay secretos para pruebas consumidor.
- Tres categorías con resultados JUnit, IDs por operación y fallo ante pruebas ausentes/duplicadas/omitidas. Se conservan pruebas anteriores y casos compartidos.
- Cobertura JaCoCo 0.8.14 integrada en AGP, sobre suite completa y alcance predeterminado del plugin, incluyendo UI/código generado. Gate exige medición/inventario no vacíos e informes íntegros; **no certifica un mínimo porcentual**. No se copia el 45 % de la referencia ni se excluye código para elevar el número.
- Gitleaks, Semgrep y Trivy/SBOM reutilizan motores fijados, sanitización y manifestación de estado. Hallazgo, error de herramienta o informe ausente fallan; subir artefacto no convierte fallo en éxito.
- Build genera APK debug y APK de pruebas. Emulador CI API 35/google_atd/x86_64 con acción oficial fijada; ejecuta cinco métodos offline por selector explícito y compara APK instalado con candidato. Arranque/instalación/runner fallidos no se aceptan por exit code 0 de adb.
- Gate valida resultados de todos los trabajos, run/attempt/source, helper/matriz, huellas de informes/APKs, inventario SBOM y casos exactos. Estados skipped/cancelled/missing no equivalen a PASS. Artefactos se conservan siete días aun cuando un trabajo falla.
- Solo después del gate se reexpone el mismo APK como pactogg-debug-validated, sin recompilar otro candidato. Antes existe android-build, explícitamente candidato sin aprobación de calidad.

## Entrega y protección

El usuario autorizó la entrega por PR. El candidato del PR8 terminó con grafo verde real, gate PASS y APK descargado. La regla de main exige **Android PR quality gate** de GitHub Actions, rama actualizada y conversaciones resueltas, también para administradores, sin push forzado/borrado. [Ejecución y límites](entrega-github-2026-11-04.md). La revisión humana independiente sigue pendiente; ningún job sustituye aceptación del profesor.

No se agregan Pact-real, mutación, carga/chaos, ZAP, SQL Server, Terraform o publicación automática: no son controles móviles obligatorios corroborados para este corte. No se migran Hilt/JUnit4/stack por copiar el ejemplo.

[Comparación de fuente](../preparacion/comparacion-profesor-calidad-2026-11-04.md) · [Matriz de operaciones](matriz-operaciones.md).

Los PR ejecutan el grafo antes del merge y push a main lo repite sobre la revisión integrada. Los pushes de ramas con PR no duplican otro grafo idéntico; workflow_dispatch conserva la comprobación manual.
