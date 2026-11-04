# Verificación

Ejecutar scripts/check-android.ps1 para compilación, pruebas JVM, Lint, Detekt y SBOM.
El script falla en cuanto falla un comando; no instala Android Studio ni inicia emuladores.

| Prueba | Qué demuestra |
|---|---|
| ApiClientFactoryTest | Rechazo de URLs inseguras, JSON vía TLS confiable y redirección sin seguir |
| ApiErrorMapperTest | Clasificación de fallos y rechazo de identificadores de correlación no seguros |
| CacheMetadataDaoTest (instrumentada) | Upsert real y borrado de metadatos en SQLite |
| ShellNavigationTest (instrumentada) | Cambio de pestañas y retorno de Grupos al Perfil |

Se requiere un teléfono o emulador en adb para connectedDebugAndroidTest.
No confundir compilar androidTest con ejecutar esas pruebas. No hay pruebas end-to-end contra backend.
Resultados de la preparación se registran en docs/verificacion-local.md.

## Fase vigente

Los comandos anteriores pertenecen a la fase técnica previa y no se ejecutan por esta preparación. El diseño general se mantiene en el [plan de pruebas](preparacion/plan-de-pruebas.md) y los casos funcionales pendientes en [primer flujo](preparacion/primer-flujo-movil.md). Los resultados históricos no acreditan el cumplimiento de esas specs.
