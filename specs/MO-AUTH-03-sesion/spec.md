# MO-AUTH-03 — Custodia y renovación de sesión

Fecha: 2026-11-04. Estado: **IMPLEMENTACIÓN LOCAL / aceptación funcional pendiente**. Correspondencia backend: `AUTH-01-login`. No equivale a aceptación de sus reglas o a implementación móvil.

## 1. Contexto y objetivo

Conectar la UI Android existente con `POST /api/v1/auth/refresh`. Fuentes fijadas al commit `f6c37d8a70de43765f870b78b452832a2c0842b9`: [OpenAPI](https://github.com/equipopatogg/backend/blob/f6c37d8a70de43765f870b78b452832a2c0842b9/api/openapi.yaml), [guía](https://github.com/equipopatogg/backend/blob/f6c37d8a70de43765f870b78b452832a2c0842b9/docs/API-movil.md), [spec backend](https://github.com/equipopatogg/backend/blob/f6c37d8a70de43765f870b78b452832a2c0842b9/specs/AUTH-01-login/spec.md); refresh se comprueba en [AuthService](https://github.com/equipopatogg/backend/blob/f6c37d8a70de43765f870b78b452832a2c0842b9/Pactogg.Api/Modules/Auth/AuthService.cs), sin spec propia backend.. La UI anterior es prototipo local; se conserva su historial real. Marco de alcance y conflictos: [alineación móvil/backend](../../docs/integracion/alineacion-backend-2026-11-04.md). PG-03 coordina acceso. La navegación necesaria se define en cada operación disponible, sin spec adicional de alcance.

## 2. Requisitos funcionales

- RF1: Compartir SessionRepository entre features. Propuesta Android: clave AES-GCM en Android Keystore y archivo privado cifrado con escritura atómica, excluido de backup/transferencia; no guardar contraseña. Revisar compatibilidad API 24 antes de implementar; DataStore por sí solo no cifra. No adoptar una biblioteca solo porque la guía la menciona.
- RF2: Tomar expires_in de la respuesta; no depender de un temporizador de fondo ni introducir WorkManager. Añadir Bearer solo a peticiones de la API autorizada y nunca al CDN o hosts externos.
- RF3: Ante expiración o 401 protegido, coordinar una sola renovación concurrente con mutex y cliente sin autenticador recursivo. Enviar refresh_token y omitir device_id inicialmente; persistir atómicamente access y refresh nuevos antes de usarlos.
- RF4: 401 del refresh invalida sesión y datos por cuenta; 403 protegido es permiso denegado y no borra por sí solo la sesión. Error de red/5xx no equivale a credencial inválida.
- RF5: Máximo una renovación por solicitud y sin bucles. No repetir escrituras automáticamente por pérdida de respuesta ni asumir idempotencia. Serializar también logout y cambio de cuenta mediante generación de sesión para impedir que una respuesta tardía restaure sesión cerrada.
- RF6: Reinicio descifra y valida el estado antes de mostrar datos; clave invalidada/archivo corrupto borra custodia y lleva a acceso. Pérdida de respuesta en refresh rotatorio deja resultado incierto: no insistir con el token antiguo; solicitar acceso si no se recupera de forma segura.

## 3. Requisitos no funcionales

HTTPS sin bypass, URL por entorno (es pública, no un secreto), sin credenciales de servidor. Nunca registrar cuerpos con contraseña/tokens ni enviarlos a analítica. Mensajes por code y correlación request_id sanitizada; no depender de message. Compose/Material3, Hilt, UDF y diseño aprobado. Carga/vacío/error, bloqueo de doble toque, teclado, semántica, fuente ampliada y ciclo de vida. Sin nuevos permisos por copiar backend. No activar funciones ausentes mediante mocks de producción.

## 4. Arquitectura y contratos internos

| Área prevista | Cambio | Frontera |
|---|---|---|
| feature/session/presentation/ | Estado, eventos, ViewModel y adaptación de pantalla existente | Sin HTTP ni almacenamiento en Composable |
| feature/session/domain/ | Modelos e interfaz de repositorio | Sin Android/Retrofit; casos de uso solo si justifican orquestación |
| feature/session/data/ | DTO snake_case, mappers y repositorio/servicio | Envelope data/meta y errores sin filtrar secretos |
| core/network/ y core/session/ | Transporte, errores y sesión compartidos definidos por MO-AUTH-03 | Sin duplicar autenticadores o custodios por feature |
| prototype/AppRoot.kt y core/navigation/ | Reutilizar UI; seleccionar un único grafo efectivo | No mantener bypass de Submit local en recorrido conectado |
| src/test/ y src/androidTest/ | Fixtures HTTP, estados, UI y dispositivos | Fixtures no acreditan servidor |

Son rutas propuestas, no archivos creados. Inspeccionar componentes existentes antes de agregar clases o dependencias; conservar Hilt. No reproducir Mongo/Redis/AWS/Argon2 del servidor en Android.

Operación: `POST /api/v1/auth/refresh`. JSON UTF-8 snake_case; IDs opacos de 24 hex cuando el contrato los exige; fechas UTC. Respuesta incoherente se trata como fallo, no como éxito vacío. Detalles transversales y URL real en la matriz canónica.

## 5. Criterios de aceptación

| ID | Resultado observable | Evidencia esperada / estado actual |
|---|---|---|
| MO-AUTH-03-AC1 | Dos 401 concurrentes originan una renovación y ambas observan el par de tokens vigente sin carreras. | Prueba consumidor + evidencia de proveedor aplicable; NO_EJECUTADO |
| MO-AUTH-03-AC2 | Refresh 401, 403 protegido, red caída y 5xx producen estados diferentes; no hay recursión o bucles. | Prueba consumidor + evidencia de proveedor aplicable; NO_EJECUTADO |
| MO-AUTH-03-AC3 | En dispositivo se verifica cifrado, backup, reinicio, corrupción y clave invalidada sin tokens en logs. | Prueba consumidor + evidencia de proveedor aplicable; NO_EJECUTADO |
| MO-AUTH-03-AC4 | Logout durante refresh impide restauración tardía; fallo de escritura atómica no mezcla tokens de generaciones distintas. | Prueba consumidor + evidencia de proveedor aplicable; NO_EJECUTADO |
| MO-AUTH-03-ACQ | Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente | Informes identificados; NO_EJECUTADO |

## 6. Plan de pruebas

Cada AC requiere casos JVM de estado/mapeo y HTTP con MockWebServer: payload exacto, códigos, cuerpo inválido, cancelación y ausencia de doble envío. Compose/dispositivo comprueba interacción, accesibilidad y navegación. Sesión/cifrado/selector necesitan mecanismo Android real cuando corresponda. Integración proveedor con cuentas y datos sintéticos autorizados, registrando commit/entorno/fecha y resultado redactado; no crear cuentas o modificar producción por esta preparación. Quality/SAST/SCA/secretos existentes se ejecutarán solo al abrir implementación y sobre el candidato afectado; sin porcentajes heredados del profesor.

## 7. Riesgos, límites y deuda

Backend implementa refresh pero no tiene expediente AUTH-03 independiente: se contrasta OpenAPI, AuthEndpoints y AuthService. Revocación seguida de emisión no demuestra rotación atómica frente a concurrencia externa. Android serializa sus solicitudes; garantía global requiere revisión backend.

No ejecutar código, instalar, modificar backend, desplegar o subir Git al redactar este expediente. Ningún PASS anterior del backend o del prototipo acredita esta integración.

## Aprobación y control de cambios

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación solicitada | Usuario | 2026-11-04 | Alinear specs con backend antes de programar |
| Diseño detallado | Pendiente | Pendiente | Resolver entradas aplicables; no aceptación inferida |
| Implementación / aceptación | Pendiente | Pendiente | NO_EJECUTADO |

[Plan](plan.md) · [Tareas](tasks.md) · [Checklist](checklists/checklist.md).


## Implementación autorizada — 2026-11-04

Usuario solicita empezar estas specs. Se implementa consumidor con componentes existentes y sin nuevas dependencias; esta autorización supera la restricción documental anterior. Las operaciones de producto comparten feature/connected/{data,domain,presentation}; auth usa feature/auth/data y core/session. MainActivity utiliza ConnectedApp/ConnectedViewModel con Hilt, DTO/mappers, estado de carga/error y datos reales; pantallas adelantadas conservadas sin entradas. No se crea otra spec de alcance.

Aviso básico local autorizado posteriormente por el usuario: disponible en registro con consentimiento explícito; revisión de producto/publicación pendiente. El contenido se identifica como provisional y la versión de protocolo se configura sin atribuir aprobación legal.

Ver [evidencia y límites del consumidor](../../docs/integracion/verificacion-consumidor-2026-11-04.md). Criterios con proveedor, dispositivo y aceptación se mantienen abiertos; NO_EJECUTADO de tablas originales se refiere a esa verificación integral, no a ausencia actual de código. No convertir fixtures HTTP en PASS del servidor ni UI anterior en aceptación de este candidato. Sin commit/push.


## Pruebas consumidor por tipo — actualización 2026-11-04

[Escenarios y límites](../../docs/pruebas/matriz-operaciones.md) · [Manifestación canónica](../../docs/pruebas/operaciones.json). Las instancias parametrizadas conservan el ID exacto en JUnit:

- unit: `com.pactogg.app.testing.EndpointUnitTest.enforcesConsumerBoundaryWithoutNetwork[AUTH-03]`.
- integration: `com.pactogg.app.testing.EndpointContractTest.consumesConfirmedWireContract[AUTH-03]`.
- security: `com.pactogg.app.testing.EndpointSecurityTest.deniedResponseIsSanitizedAndNeverReplayed[AUTH-03]`.

Se reutilizan además casos compartidos de TLS, sesión, estados, reglas y contrato. Android real prueba cifrado/Room/acceso por separado. Este apartado no convierte un caso de consumidor o fixture en aceptación de todos los AC ni prueba de autorización del servidor; controles y limitaciones finales se registran en el informe de esta fase.
