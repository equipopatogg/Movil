# Controles y límites de seguridad

## Implementados en la base

- HTTPS obligatorio; confianza en certificados del sistema; sin HTTP claro ni bypass TLS.
- Sin claves/credenciales de servicios ni datos de usuarios. API vacía por defecto.
- Sin permisos de cámara, micrófono, contactos, ubicación o acceso general a galería. Solo INTERNET.
- Backup y transferencia de datos excluidos; actividad launcher exportada, resto sin componentes exportados adicionales.
- Cliente sin logging de payloads/tokens. Errores categorizados y request_id restringido.
- R8/optimización de release habilitada. No equivale a protección absoluta ante ingeniería inversa.
- .gitignore excluye local.properties, claves, paquetes y salidas. Gradle Wrapper verifica SHA-256 de su distribución.

## Preparados para ejecución

Estado vigente 2026-11-03: workflows Android centralizados en `.github/workflows/` de PactoGG; setup-gradle oficial **v6.4.1**, SHA `8da46b539cd8fe6ab27ca75128919a18d757604c`, validación JAR recursiva habilitada y distribución checksum conservada. Helpers de evidencia con salidas reales y whitelist de campos, JSON sin snippets/matches/valores, recogida always y fallos nativos preservados; retención 7 días y contents:read. Ver preparación Git (referencia histórica externa). La consulta previa que detectó la referencia inválida se conserva abajo como historial, ya resuelta en configuración.

| Control | Android | Backend técnico / pendiente |
|---|---|---|
| Lint/Detekt | Base con evidencia histórica; CI definido, no remoto aceptado | Analizadores SDK .NET verificados localmente |
| SAST | Semgrep con reglas locales limitadas; motor remoto pendiente | Analizadores .NET ejecutados localmente, no Semgrep acreditado |
| Secretos | Gitleaks sobre candidatos actuales; historial pendiente de Git | Gitleaks local de fuentes; no historial |
| SCA/SBOM | CycloneDX releaseRuntimeClasspath y Trivy definidos; ejecución nueva pendiente | NuGet/CycloneDX locales; contenedor solo si existe |
| Pruebas | JVM HTTPS/mapper; Room y navegación en dispositivo | Autorización, integración, contrato, carga |
| DAST | Revisión dinámica móvil MASVS/MASTG pendiente | ZAP contra staging autorizado |
| Firma | Play App Signing y clave de subida pendientes | Cosign solo si existe imagen de contenedor |
| IaC | No aplica al APK | Terraform/escaneo al definir nube |

Los workflows quedan como archivos locales. No han corrido en GitHub. Los contenedores de escaneo tienen versión fija;
sus digest también están fijados para reproducibilidad. Las acciones de build están fijadas a commit.
El SBOM cubre las dependencias de runtime de release, no todos los plugins/servidores. La generación de SBOM no evalúa vulnerabilidades.
Semgrep local contiene controles básicos y no sustituye una auditoría de autenticación, permisos o lógica de negocio.

Auditoría pre-Git: Gitleaks portable sobre todos los candidatos no ignorados, no solo el móvil. Cinco hallazgos iniciales fueron digests públicos de archivos en el manifiesto histórico; excepción limitada a regla/ruta/cinco valores exactos, sin excluir el archivo ni relajar reglas por defecto. Cuatro fixtures con motor real prueban que otros valores/rutas siguen detectándose. Resultado final y procedencia en informe común; sin historial Git, aceptación remota o seguridad completa.

Revisión documental del 2026-10-30 al preparar PG-00: fijar referencias no demuestra su resolución. La API oficial no encontró el commit configurado para gradle/actions (48b5f213c81028ace310571dc5ec0fbbca0b2947, HTTP 422); los de checkout/setup-java/upload-artifact consultados sí resolvieron. Debe corregirse/verificarse en fase técnica. setup-gradle v4 integra validación del JAR del Wrapper; comprobar su alcance bajo Movil/ y orden, sin duplicar acciones por inferencia. No se ejecutó CI ni se modificó configuración.

## Antes de incorporar datos reales

Modelar amenazas y revisar controles MASVS/MASTG para Android y ASVS/API Security para backend.
Definir tokens con cifrado respaldado por Keystore, revocación y limpieza en logout; DataStore y Room no cifran por sí mismos.
Revisar autorización por recurso en servidor, idempotencia de votos/puntos/inscripciones, privacidad y retención.
La IA propone borradores editables: salida no confiable validada por esquema, sin secretos de proveedor en APK.
S3 privado, validación de archivo, EXIF, cuotas y análisis previo son responsabilidades de integración pendientes.

## Publicación

No existe keystore de producción, cuenta de servicio, conexión Play ni despliegue automático.
Antes de publicar: identificador definitivo, firma, aviso de privacidad, declaración de datos, SDK requerido por Play,
pruebas instrumentadas, revisión de versión release y controles pendientes ratificados.

Fuentes oficiales: https://mas.owasp.org/MASVS/ ; https://developer.android.com/privacy-and-security ;
https://dagger.dev/hilt/gradle-setup ; https://github.com/CycloneDX/cyclonedx-gradle-plugin .

## Pendiente de compatibilidad de bibliotecas nativas

La inspección 16 KB detectó que graphics-path 1.1.0 mantiene un final de segmento RELRO no alineado.
La compilación por sí sola no demuestra compatibilidad nativa. Resolver con versión corregida o recompilación
controlada del proveedor y validar ELF/paquetes/dispositivo antes de Google Play. DataStore 1.2.1 sí pasó esa inspección de sus AAR.

Precisión de PG-02: ese registro histórico requiere contrastar segmentos LOAD, relación RELRO/LOAD, empaquetado y ejecución con los criterios oficiales vigentes antes de decidir un cambio. El final de RELRO aislado no prueba todos los casos de incompatibilidad. No se reprodujo el diagnóstico ni se ratificó una versión de reemplazo en esta preparación.
