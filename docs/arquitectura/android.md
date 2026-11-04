# Arquitectura de la base Android

Una aplicación y un módulo Gradle :app. Separación por paquetes; no se justifican módulos físicos adicionales en esta base.

| Paquete | Responsabilidad |
|---|---|
| core/navigation | NavHost, destinos y ViewModel del contenedor |
| core/designsystem y ui/theme | Componentes y tema comunes |
| core/environment | Contrato y adaptador de configuración de compilación |
| core/network/dto y mapper | Transporte y traducción a errores interiores |
| core/result | Categorías de fallos independientes del cliente HTTP |
| core/database | Metadatos técnicos de caché Room; sin modelos del negocio |
| core/preferences | Preferencias no sensibles; no usar como almacén de tokens |
| core/di | Ensamblado Hilt |
| feature/*/presentation | Pantallas iniciales de cada área |

Los futuros paquetes feature/<área>/domain y data contendrán contratos/modelos y adaptadores solo al existir una spec aprobada.
No crear casos de uso vacíos, repositorios universales, servicios para endpoints supuestos ni entidades de negocio preventivas.
La aplicación nunca accede directamente a MongoDB, Redis, S3 ni a un proveedor de IA.

## Red

ApiClientFactory valida HTTPS, credenciales ausentes, ausencia de query/fragment y barra final.
OkHttp usa timeouts y no sigue redirecciones; no incluye logging HTTP ni bypass de certificados.
No existe aún sesión, refresh, retry de operaciones de negocio o envío de evidencias.

## Datos

Room exporta esquema de versión 1; no hay fallback destructivo. Toda modificación posterior requiere migración y prueba.
La tabla cache_metadata contiene únicamente claves y marcas temporales técnicas.
La clase NavigationPreferences está preparada y no modifica la navegación actual ni guarda información por sí sola.
Room disponible no implica modo Offline-First, sincronización ni cola de votos.

## Evidencia futura

Photo Picker de imágenes → análisis de contenido → aprobación → almacenamiento de lo aprobado.
Resolver con backend el transporte temporal y la contradicción sobre cuarentena antes de implementar.
Validar el tipo real y límites en servidor; la selección móvil no reemplaza esa validación.

## Lineamientos y revisión de reutilización

Esta guía concentra el contenido general del borrador retirado MB-01; no es una spec activa ni transfiere aceptación a la base existente. Conservar Kotlin/Hilt, versiones centralizadas, separación ligera por paquetes, UDF, DTO/mapeadores y casos de uso selectivos. Evitar módulos, repositorios o entidades preventivas sin necesidad real.

Antes de proponer un cambio de base, identificar el estado actual y la diferencia concreta: archivos a crear/modificar, comportamiento o configuración resultante, dependencias justificadas y prueba que lo demuestra. La mera repetición de estas reglas pertenece a esta guía.

La reutilización técnica requiere revisar build/configuración, fronteras, navegación, transporte y persistencia, además de las incidencias instrumentadas y nativas. Si aparece un ajuste delimitado de Kotlin, Gradle, XML, pruebas o CI, puede tener spec propia con resultado verificable. Preparar esa spec no ejecuta el ajuste. Los resultados históricos permanecen en docs/verificacion-local.md.
