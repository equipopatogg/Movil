# Estado vigente del contrato — 2026-11-04

**Recibido:** OpenAPI 1.0.0 y guía del equipo; revisados contra backend externo commit f6c37d8a70de43765f870b78b452832a2c0842b9. [Matriz canónica: operaciones, fuente, discrepancias y pendientes](alineacion-backend-2026-11-04.md). Datos recibidos no equivalen a contrato conciliado ni compatibilidad Android verificada.

La recepción histórica inferior queda superada: ya existen rutas, campos, URL de guía y políticas de sesión. Faltan privacidad vigente, conciliaciones concretas y pruebas de proveedor/consumidor. El backend externo publicado no es el antiguo cierre local BE-00–BE-03 de la carpeta hermana; no se modifica ni sustituye esa carpeta por inferencia.

# Preparación del contrato Android/backend

Estado: pendiente de contrato aprobado. Documento de coordinación, sin endpoints ni DTO de negocio inventados. La app Kotlin consume el backend .NET del equipo; esta preparación no implementa ninguno de los dos.

Actualización del 2026-11-03: Backend (referencia histórica externa) ya tiene soporte técnico BE-00–BE-03 verificado localmente. Su correspondencia (referencia histórica externa) enlaza funciones futuras con PG-03; no hay contrato funcional o registro/login/logout implementados. La tabla siguiente es checklist de recepción/revisión, no otra definición OpenAPI; la fuente canónica se versionará en Backend antes de crear DTO de producto Android.

La ubicación del futuro OpenAPI ratificado se acordará con el equipo backend. Este documento enlazará su versión y ubicación; no mantendrá una segunda definición de rutas o campos. Las hojas originales son candidatas que necesitan resolver sus discrepancias.

## Información necesaria por operación aprobada

| Asunto | Qué deberá definir el contrato | Responsabilidad de coordinación |
|---|---|---|
| Identidad | ID original, spec, versión OpenAPI, método/ruta y entornos HTTPS | Backend propone; móvil contrasta sus criterios |
| Entrada y salida | Campos, tipos, nulabilidad, formatos, zona horaria, precisión, IDs opacos y errores | Acuerdo entre consumidores y proveedor |
| Acceso | Autenticación, permisos por objeto/grupo y tratamiento de sesión | Backend impone autorización; móvil maneja estados |
| Sesión | Emisión, expiración, refresh si aplica, revocación, logout y fallos de conectividad | Decisión de integración pendiente |
| Lecturas | Paginación, orden estable, filtros y compatibilidad de versión | Backend documenta; móvil evita suposiciones |
| Escrituras | Validación, duplicados, concurrencia, idempotencia y recuperación | Backend define garantías; móvil respeta contrato |
| Red | Cancelación, límites, errores temporales y reintentos permitidos | Véase [matriz de límites](limites-y-reintentos.md) |
| Fotos y enlaces | Tipos, tamaños, autorización, moderación previa, transporte temporal y borrado | Flujo pendiente; sin cámara ni video cargado |
| Privacidad | Datos mínimos, exposición, retención y borrado | Responsables del producto/backend por confirmar |
| Pruebas | Fixtures sintéticas, casos de error, compatibilidad y evidencia de proveedor/consumidor | Plan compartido cuando exista contrato |

No se asignan plazos ni responsables personales no confirmados. Los datos de esta tabla son campos de revisión, no decisiones técnicas nuevas aprobadas.

## Comportamientos que deben distinguirse

401 requiere el tratamiento de autenticación establecido; 403 expresa denegación de acceso y no obliga por sí solo a borrar sesión o ir a login. La interfaz distinguirá cancelación, timeout, red no disponible, validación, conflicto y error del proveedor según el contrato acordado. Un 404 no significa siempre lista vacía.

La cancelación de una solicitud en el cliente no demuestra que una escritura se haya revertido en servidor. No reintentar operaciones con efectos sin conocer garantías de idempotencia. Evitar acciones duplicadas y carreras de renovación de sesión en el plan que se apruebe.

## Fronteras y pendientes

El móvil no guarda secretos de infraestructura ni llama directamente a la base central, Redis o servicios de IA. La ruta `/probe` de los tests anteriores es una fixture técnica, no una API del producto. No se adoptan login/logout, nombres de campos, hosts o políticas del profesor.

Los contratos internos móviles se describirán en el plan de la spec correspondiente, manteniendo DTO dentro de datos y dominio independiente de Retrofit. Actualmente no hay un contrato funcional ratificado que permita marcar integración como verificada.

## Responsabilidad del equipo

Backend .NET 10/SDK declarado 10.0.401 y soporte técnico ya implementados/verificados localmente; NoSQL/servicios/contrato funcional siguen pendientes. SQL Server/entidades del profesor no son decisiones de PactoGG. No existe despliegue o integración funcional acreditados.

Los tests actuales usan /probe solo como fixture técnica. Hasta existir OpenAPI aprobado no se necesita una carpeta api/ con un README de reserva.

## Recepción del primer contrato backend

Estado al 2026-10-30: no recibido. Esta tabla se completa al llegar las specs del equipo; no constituye un OpenAPI o acuerdo cerrado.

| Entrada | Registro | Login | Logout |
|---|---|---|---|
| ID/versión de spec y OpenAPI | Pendiente | Pendiente | Pendiente |
| Método/ruta/URL HTTPS por entorno | Pendiente | Pendiente | Pendiente |
| Campos, tipos, requeridos, formatos y validación | Pendiente | Pendiente | Pendiente |
| JSON sintético de solicitud y respuesta | Pendiente | Pendiente | Pendiente |
| Códigos/cuerpo de error y correlación segura | Pendiente | Pendiente | Pendiente |
| Autenticación, autorización y datos expuestos | Pendiente | Pendiente | Pendiente |
| Sesión/refresh/revocación/expiración | Pendiente | Pendiente | Pendiente |
| Idempotencia, reintento, timeout y cancelación | Pendiente | Pendiente | Pendiente |
| Verificación de correo/apodo/consentimiento | Pendiente | Pendiente si condiciona acceso | No aplica salvo regla acordada |
| Entorno de prueba, responsables y evidencia del proveedor | Pendiente | Pendiente | Pendiente |

Criterio documental de recepción: revisar completitud, coherencia con las historias y decisiones, fixtures sin datos reales y dudas de seguridad/versión. Una URL que responde no demuestra todo el contrato. La integración real requiere además servicio accesible y pruebas del alcance acordado.

El [primer flujo](../preparacion/primer-flujo-movil.md) contiene los IDs originales. Los cuatro endpoints adicionales del backend para el corte los selecciona su equipo; no se inventan desde Android. Google, verificación y recuperación se aclaran si afectan el primer flujo. No se solicitan credenciales reales mediante documentación.
