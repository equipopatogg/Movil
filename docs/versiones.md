# Versiones fijadas de la base

Fecha de preparación: 2026-10-29. Fuente ejecutable: Movil/gradle/libs.versions.toml.

| Componente | Versión |
|---|---|
| agp | 9.3.3 |
| coreKtx | 1.10.1 |
| junit | 4.13.2 |
| junitVersion | 1.3.0 |
| espressoCore | 3.7.0 |
| lifecycleRuntimeKtx | 2.10.0 |
| activityCompose | 1.12.0 |
| kotlin | 2.2.10 |
| composeBom | 2026.02.01 |
| serialization | 1.9.0 |
| coroutines | 1.10.2 |
| navigation | 2.9.7 |
| hilt | 2.60.1 |
| ksp | 2.3.9 |
| retrofit | 3.0.0 |
| okhttp | 4.12.0 |
| room | 2.8.4 |
| datastore | 1.2.1 |
| graphicsPath | 1.1.0 |
| coil | 3.3.0 |
| detekt | 1.23.8 |
| cyclonedx | 3.5.1 |

Gradle Wrapper 9.5.0, compileSdk 36.1, targetSdk 36, minSdk 24, bytecode JVM 17.
La versión de aplicación es 0.1.0 (código 1). Confirmar applicationId y requisito vigente de Play antes de publicar.
Lint conserva advertencias como errores salvo avisos de versiones más nuevas/target más nuevo: las actualizaciones se revisan expresamente. No se desactivaron controles de seguridad de código o manifiesto.

## Matriz de versionamiento y compatibilidad previa a nuevos desarrollos

Preparada el 2026-10-30 conforme al punto 11 de requisitos v3. No modifica Gradle ni asigna nuevas versiones. La política siguiente es propuesta para ratificar con el equipo; las versiones de librerías superiores son las efectivamente declaradas, no una selección nueva.

| Elemento | Valor conocido | Regla de seguimiento propuesta | Pendiente |
|---|---|---|---|
| App Android | versionName 0.1.0, versionCode 1 | Nombre semántico y código de compilación creciente para cada nuevo candidato distribuido | Ratificar política y próxima versión; 0.1.0 no acredita registro/login |
| Identidad Android | com.pactogg.app provisional | Confirmar identidad antes de entrega; vincular artefactos y canal | Titular y nombre definitivo |
| API backend | Sin contrato recibido | Referencia de spec/OpenAPI y compatibilidad soportada por cada versión móvil | Versión, URL y política de cambios del equipo backend |
| Runtime backend | .NET, versión pendiente | Registrar por el responsable backend con su artefacto | No seleccionar versión desde móvil |
| Room | Esquema técnico 1 exportado | Cambios de esquema necesitan versión, migración pertinente y prueba real | Futuras entidades y sus requisitos |
| Aviso/consentimiento | Sin versión ratificada | Referencia al documento aplicable cuando se acepte consentimiento | Responsable y contrato de registro |
| Candidato de entrega | Ninguno nuevo | versión app + código + identidad de fuente + hash + variante + contrato + evidencia | Firma/canal/aceptación |

Compatibilidad por completar antes de integrar la primera funcionalidad:

| Cliente | Contrato API | Esquema local | Entorno/canal | Evidencia | Estado |
|---|---|---|---|---|---|
| Base técnica 0.1.0 (1) | No integra API funcional | Técnico 1 | Local, sin distribución nueva | docs/verificacion-local.md, solo antecedente | Sin compatibilidad funcional acreditada |
| Próximo candidato, versión por acordar | Pendiente de recibir | Según alcance ratificado | Pendiente del equipo | Pendiente | NO_EJECUTADO |

Por despliegue futuro registrar fecha, responsables, fuente/commit si existe, versiones, hash/artefacto, reportes, canal, aceptación y recuperación. Git, etiquetas automáticas y publicación solo cuando se soliciten. No reutilizar el roadmap antiguo 0.1/0.2/0.3 como compromiso funcional aprobado.
