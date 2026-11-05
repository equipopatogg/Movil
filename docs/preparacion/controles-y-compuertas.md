# Controles y compuertas propuestas

Estado: preparación documental. No es una política ratificada ni una nueva configuración ejecutable.

Actualización posterior 2026-11-03: base backend técnica verificada localmente y workflows raíz Android ajustados bajo encargo pre-Git; ver evidencia y alcance (referencia histórica externa). Las propuestas generales inferiores conservan requisitos futuros y no equivalen a aceptación de producto/CI/entrega. La revisión acotada sí configura referencias/rutas/permisos/informes; no ratifica toda esta matriz.

| Control | Android | Backend/infra | Evidencia futura |
|---|---|---|---|
| Compilación/estático | Gradle, Lint, Detekt | dotnet/analizadores | Versión, resultado e informe |
| Integridad de compilación | Revisar Gradle Wrapper y checksum de su distribución antes de ejecutar en CI | Dependencias de herramientas según plataforma | Versión, procedencia y resultado de validación; sin ejecución en esta fase |
| SAST | Reglas Kotlin/Android verificadas | Reglas .NET/API | Casos positivos/negativos y tratamiento de hallazgos |
| Secretos | Código/configuración e historial al existir | Igual, incluyendo IaC | Gitleaks y excepciones documentadas |
| SCA | Runtime y transitivas de la variante móvil | NuGet/SO/imagen separados | Fuente de vulnerabilidades, fecha, severidad y tratamiento |
| SBOM | CycloneDX ligado al APK/AAB | Aplicación e imagen | Inventario, identidad/hash y alcance |
| DAST | Dinámica móvil MASVS/MASTG | ZAP sobre API autenticada en staging | Alcance, cuentas por rol y reporte |
| Firma/procedencia | Clave de subida y Play App Signing | Cosign/OIDC si hay imagen | Identidad y digest verificados; custodios pendientes |
| Persistencia | Room/migraciones reales | NoSQL elegida y recuperación | Datos aislados y evidencia de cambios |
| Contenedor/IaC | No verifican el APK | Imagen, red, permisos y secretos | Informes cuando esos artefactos existan |
| Seguridad de negocio | Cliente respeta contrato/errores | Autorización e invariantes del servidor | Casos de abuso con reglas aprobadas |
| Operación | Sesión/ciclo de vida/privacidad local | Logs, alertas, respaldo y rollback | Guiones, responsables y resultados sin PII |
| Compatibilidad | APIs, permisos, ABI/16 KB, accesibilidad | Runtime y servicios | Matriz de versiones/dispositivos con evidencia |

## Estados de evidencia

PASS: ejecutado y criterio satisfecho. FAIL: criterio incumplido. ERROR: herramienta/entorno impide concluir.
NO_EJECUTADO: sin ejecución/evidencia. NO_APLICA: justificado para esa superficie.
Excepciones: riesgo, responsable, mitigación y vencimiento. No ocultar controles tras un total global.

## Secuencia futura

G0 documentación clara y trazable → G1 base técnica verificable → G2 contrato/roles/integración → G3 controles aplicables y hallazgos tratados → G4 firma, privacidad, compatibilidad y recuperación de entrega.
Proponer bloqueo por secretos confirmados, fallos de autorización y vulnerabilidades altas/críticas aplicables sin excepción aprobada. Ratificar la política antes de activarla.
Cobertura, mutación, latencia, memoria, costo y duración de CI quedan sin cifras hasta medir y acordar; no se heredan porcentajes del profesor.

## Diseño del pipeline

Separar documentos, Android, backend, seguridad, integración y entrega. Considerar dependencias compartidas al filtrar cambios.
Permisos mínimos por job, acciones fijadas, credenciales temporales y evidencias de resultados reales. No publicar desde PR ni heredar despliegue/destrucción programados.
Los workflows se centralizaron/ajustaron en raíz; su configuración/política local se verifican sin afirmar cumplimiento de toda la propuesta o ejecución remota.

## Documentos complementarios

La revisión Android se organiza en la [matriz MASVS](../seguridad/masvs-checklist.md) y el [modelo de amenazas](../seguridad/modelo-amenazas.md). La coordinación de API y sus límites está en [contrato](../integracion/contrato-api.md) y [matriz de red](../integracion/limites-y-reintentos.md). Los [criterios de cierre](criterios-ready-done.md) separan aceptación documental de ejecución técnica. Todos conservan decisiones pendientes y no actualizan los workflows anteriores.

## Preparación de CI y entrega móvil

Los contenidos generales de MB-02/ME-01 se mantienen como guías, no como specs activas. La propuesta de secuencia es revisión de alcance/contrato → construcción/estático → pruebas/seguridad → candidato identificado → firma y canal autorizados → distribución/verificación → cierre. Cada paso requiere evidencia apropiada; la secuencia no está configurada por documentarla.

| Asunto por coordinar | Información necesaria | Estado |
|---|---|---|
| CI | Jobs/controles, reportes, permisos mínimos, artefactos y tratamiento de ausencia/error | Definición raíz/local revisada; runner/aceptación remota pendientes |
| Candidato | Versión/código, identidad de fuente, variante/configuración, hash y contrato compatible | Ningún candidato nuevo |
| Firma | Clave de subida y firma de distribución diferenciadas, custodios y permisos acordados | Sin configurar credenciales |
| Play | Titular/tipo/estado de cuenta, applicationId definitivo, canal y acceso del evaluador | Pendiente de comprobar con el equipo |
| Ficha y datos | Recursos de ficha, enlaces de privacidad y declaraciones acordes a funciones/SDK reales | Pendiente de responsables y alcance |
| Entrega | Instalación/actualización desde canal acordado, pruebas del alcance y recuperación | Sin publicación o verificación nueva |

La matriz de versiones se mantiene en docs/versiones.md. Los requisitos del corte están en evaluacion-unidad-2.md; no se equipara iniciar configuración con una entrega instalada. El backend gestiona su nube y entrega un contrato/URL versionados; Android prepara su artefacto y canal.

Fuentes oficiales de referencia: [firma Android](https://developer.android.com/studio/publish/app-signing), [pruebas y canales de Play](https://support.google.com/googleplay/android-developer/answer/14151465?hl=es) y [compatibilidad de páginas](https://developer.android.com/guide/practices/page-sizes). Verificar condiciones aplicables al publicar; no garantizar costos, plazos o disponibilidad.

Una futura spec de CI será concreta: qué workflow/configuración se cambia, qué ejecuta cada job, qué artefactos produce y qué resultado demuestra éxito o fallo. Una checklist administrativa de cuenta no reemplaza esa especificación. La firma/publicación y los controles técnicos permanecen fuera de la fase actual.


## Conciliación de calidad móvil — 2026-11-04

La matriz de 19 operaciones y los resultados actuales están en [informe de calidad](../pruebas/verificacion-calidad-2026-11-04.md), [matriz](../pruebas/matriz-operaciones.md) y [pipeline](../pruebas/pipeline-movil.md). Mantener resultados locales separados de CI remoto, SAST/SCA y aceptación humana pendientes. Sin controles backend añadidos por analogía.
