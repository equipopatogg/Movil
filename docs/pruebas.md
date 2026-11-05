# Verificación

Ejecutar scripts/check-android.ps1 para compilación, pruebas JVM, Lint, Detekt y SBOM.
El script falla en cuanto falla un comando; no instala Android Studio ni inicia emuladores.

| Prueba | Qué demuestra |
|---|---|
| ApiClientFactoryTest | Rechazo de URLs inseguras, JSON vía TLS confiable y redirección sin seguir |
| ApiErrorMapperTest | Clasificación de fallos y rechazo de identificadores de correlación no seguros |
| CacheMetadataDaoTest (instrumentada) | Upsert real y borrado de metadatos en SQLite |
| AccessNavigationTest (instrumentada) | Bloqueo de acceso vacío y privacidad visible en registro |

La suite offline requiere dispositivo explícito: scripts/check-android.ps1 -Instrumented -Device SERIAL. No usar la ejecución genérica de androidTest para este gate: incluye pruebas live/capturas fuera del alcance offline.
No confundir compilar androidTest con ejecutar esas pruebas. No hay pruebas end-to-end contra backend.
Resultados de la preparación se registran en docs/verificacion-local.md.

## Fase vigente

Los comandos anteriores pertenecen a la fase técnica previa y no se ejecutan por esta preparación. El diseño general se mantiene en el [plan de pruebas](preparacion/plan-de-pruebas.md) y los casos funcionales pendientes en [primer flujo](preparacion/primer-flujo-movil.md). Los resultados históricos no acreditan el cumplimiento de esas specs.

## Calidad vigente 2026-11-04

[Matriz](pruebas/matriz-operaciones.md), [pipeline](pruebas/pipeline-movil.md) y [resultados locales](pruebas/verificacion-calidad-2026-11-04.md): 116 JVM, cinco Android, 19,48 % de cobertura; CI remoto pendiente. Pruebas live anteriores se conservan como historia separada, sin ejecución automática en PR.
