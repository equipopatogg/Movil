# PG-01 — Ejecución instrumentada y cobertura Android verificable

Tipo: pruebas/configuración/pipeline. Fecha: 2026-10-30. Estado: borrador; planificación autorizada, ejecución pendiente.
Rector: [AGENTS](../../AGENTS.md) y [constitución SDD](../../.specify/memory/constitution.md).

## 1. Contexto y objetivo

La base tiene pruebas JVM de HTTPS/mapper y pruebas instrumentadas de Room/navegación. El workflow compila androidTest, pero no lo ejecuta. La aceptación de la suite final anterior quedó pendiente por problemas del emulador. No hay compuerta de cobertura en los workflows revisados.

El cambio configurará una ejecución instrumentada reproducible y una medición real de cobertura JVM, sin agregar funciones de producto. Fuentes: [D-SDD-06](../../docs/decisiones/registro.md), [plan de pruebas](../../docs/preparacion/plan-de-pruebas.md), [verificación histórica](../../docs/verificacion-local.md).

## 2. Requisitos funcionales

- RF1: ejecutar CacheMetadataDaoTest y ShellNavigationTest en un dispositivo/emulador identificado y compatible; compilar no equivale a ejecutarlas.
- RF2: añadir al workflow Android una vía explícita de ejecución instrumentada, inicialmente manual y separada del trabajo rápido. Una ejecución omitida no permite cerrar el criterio instrumentado.
- RF3: medir cobertura de pruebas JVM con motor compatible con el stack acordado. Registrar código incluido/excluido, líneas/clases medidas, pruebas ejecutadas y versión; cero pruebas o informe ausente no es éxito.
- RF4: establecer línea base medida y presentar una propuesta de umbral. Este cambio no inventa un porcentaje mínimo ni activa uno sin revisión; bloquea fallos, cero pruebas donde se esperan y ausencia de los informes exigidos.
- RF5: conservar XML/resultados, cobertura y diagnóstico redactado incluso al fallar; distinguir fallo de test de ERROR del entorno.

## 3. Requisitos no funcionales

Datos aislados sin PII; elegir imagen/API/ABI y recursos reproducibles, sin asumir que el emulador anterior es estable. No excluir código real para aparentar cobertura ni añadir pruebas que solo reflejen la implementación. Pruebas de sesión/login llegarán con PG-03 y no se declaran satisfechas por esta base.

## 4. Arquitectura y contratos internos

| Ruta o artefacto futuro | Cambio delimitado |
|---|---|
| .github/workflows/android-ci.yml | Job instrumentado manual identificado, resultados/diagnóstico y medición JVM |
| app/build.gradle.kts y gradle/libs.versions.toml | Configuración mínima de motor de cobertura compatible, solo después de seleccionarlo |
| app/src/androidTest/java/com/pactogg/app/core/database/CacheMetadataDaoTest.kt | Revisar aislamiento y ajustar solo fallos relevantes demostrados |
| app/src/androidTest/java/com/pactogg/app/navigation/ShellNavigationTest.kt | Revisar navegación/ciclo de vida del cascarón y ajustar fallos demostrados |
| Artefactos temporales de CI y docs/pruebas.md | Resultados, cobertura, entorno, límites y línea base |

No migrar a JUnit5, instalar Ktlint o cambiar Hilt solo por analogía. Si no existe solución de cobertura compatible, registrar ERROR y decisión técnica; no activar una comprobación vacía.

## 5. Criterios de aceptación

| ID | Dado / cuando / entonces | Evidencia futura |
|---|---|---|
| PG01-AC1 | Al ejecutar la suite en entorno identificado, Room y navegación producen resultados reales con cantidad de pruebas, fallos y omisiones | XML/informe, API/ABI/imagen y diagnóstico |
| PG01-AC2 | Al solicitar la vía instrumentada en CI, se ejecutan pruebas en dispositivo; una omisión o error de arranque no aparece como PASS | Ejecución identificada y casos de error |
| PG01-AC3 | Al medir JVM, el informe identifica código medido, exclusiones justificadas y pruebas ejecutadas; ausencia o cero pruebas esperadas falla | Informe de cobertura y resultados JVM |
| PG01-AC4 | Al cerrar la medición, hay línea base reproducible y propuesta de umbral separada de su ratificación | Informe y decisión, sin porcentajes heredados |
| PG01-AC5 | Al fallar un test o el entorno, se conservan resultados/diagnóstico y se distingue FAIL de ERROR | Artefactos redactados y estado correcto |

## 6. Plan de pruebas

Ejecutar las suites existentes de forma aislada y conjunta, verificando que los casos esperados realmente corren. Validar el rechazo de reporte ausente/cero pruebas y un fallo sintético controlado en copia de trabajo. Medir JVM con entorno/configuración registrados. Una suite verde no acredita autenticación, accesibilidad completa ni compatibilidad de release.

## 7. Riesgos, límites y deuda

Recursos del host, virtualización e imagen pueden impedir concluir. Debe elegirse entorno antes de configurar el job; el estado anterior no identifica una solución ya aceptada. Dependencias de medición requieren revisión de compatibilidad y autorización técnica aplicable. Backend, carga, mutación, DAST, firma y publicación quedan fuera. El umbral bloqueante queda como decisión posterior basada en la medición.

## Aprobación

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación autorizada | Usuario | 2026-10-30 | Preparar expediente y tareas, sin ejecución |
| Diseño en borrador | Responsables por confirmar | Pendiente | Entorno, motor y política de cobertura por revisar |
| Implementación no iniciada | Pendiente | Pendiente | Sin aceptación de suite final ni medición nueva |

Expediente: [plan](plan.md), [tareas](tasks.md), [checklist](checklists/checklist.md).
