# MO-TOR-02 — Consulta de torneos y bracket

Fecha: 2026-11-04. Estado: **IMPLEMENTACIÓN LOCAL / aceptación funcional pendiente**. Correspondencia backend: `TOR-02-list-tournaments`. No equivale a aceptación de sus reglas o a implementación móvil.

## 1. Contexto y objetivo

Conectar la UI Android existente con `GET /api/v1/tournaments; GET /api/v1/groups/{gid}/tournaments`. Fuentes fijadas al commit `f6c37d8a70de43765f870b78b452832a2c0842b9`: [OpenAPI](https://github.com/equipopatogg/backend/blob/f6c37d8a70de43765f870b78b452832a2c0842b9/api/openapi.yaml), [guía](https://github.com/equipopatogg/backend/blob/f6c37d8a70de43765f870b78b452832a2c0842b9/docs/API-movil.md), [spec backend](https://github.com/equipopatogg/backend/blob/f6c37d8a70de43765f870b78b452832a2c0842b9/specs/TOR-02-list-tournaments/spec.md). La UI anterior es prototipo local; se conserva su historial real. Marco de alcance y conflictos: [alineación móvil/backend](../../docs/integracion/alineacion-backend-2026-11-04.md). PG-03 coordina acceso. La navegación necesaria se define en cada operación disponible, sin spec adicional de alcance.

## 2. Requisitos funcionales

- RF1: Consultar ruta general sin necesitar listado de grupos; servidor filtra membresías. Ruta anidada solo con gid real conocido. Enviar status open|in_progress|finished|cancelled, game hasta 60, limit 1–50 (20 inicial) y cursor devuelto por API.
- RF2: Leer data y meta.has_more/next_cursor, cargar páginas sin repetir ni mezclar filtros/cuentas; cancelar o descartar respuestas antiguas mediante clave de consulta. Estado vacío es una respuesta válida, no error ni torneo de demostración.
- RF3: Mostrar campos recibidos de torneo y bracket.matches en solo lectura; nombres no entregados no se inventan ni se resuelven mediante rutas inexistentes. Posiciones/grupos desconocidos se muestran pendientes.
- RF4: Crear torneo, inscribir, reportar marcador y modificar bracket quedan ocultos; pulsar un partido permite solo detalle de sus datos recibidos, sin operación remota inventada.
- RF5: Diferenciar 401/403, 422, red y respuesta malformada. Reintento manual acotado para GET; caché si se usa queda por cuenta/consulta y se limpia al salir, sin convertirlo en Offline-First.

## 3. Requisitos no funcionales

HTTPS sin bypass, URL por entorno (es pública, no un secreto), sin credenciales de servidor. Nunca registrar cuerpos con contraseña/tokens ni enviarlos a analítica. Mensajes por code y correlación request_id sanitizada; no depender de message. Compose/Material3, Hilt, UDF y diseño aprobado. Carga/vacío/error, bloqueo de doble toque, teclado, semántica, fuente ampliada y ciclo de vida. Sin nuevos permisos por copiar backend. No activar funciones ausentes mediante mocks de producción.

## 4. Arquitectura y contratos internos

| Área prevista | Cambio | Frontera |
|---|---|---|
| feature/tournaments/presentation/ | Estado, eventos, ViewModel y adaptación de pantalla existente | Sin HTTP ni almacenamiento en Composable |
| feature/tournaments/domain/ | Modelos e interfaz de repositorio | Sin Android/Retrofit; casos de uso solo si justifican orquestación |
| feature/tournaments/data/ | DTO snake_case, mappers y repositorio/servicio | Envelope data/meta y errores sin filtrar secretos |
| core/network/ y core/session/ | Transporte, errores y sesión compartidos definidos por MO-AUTH-03 | Sin duplicar autenticadores o custodios por feature |
| prototype/AppRoot.kt y core/navigation/ | Reutilizar UI; seleccionar un único grafo efectivo | No mantener bypass de Submit local en recorrido conectado |
| src/test/ y src/androidTest/ | Fixtures HTTP, estados, UI y dispositivos | Fixtures no acreditan servidor |

Son rutas propuestas, no archivos creados. Inspeccionar componentes existentes antes de agregar clases o dependencias; conservar Hilt. No reproducir Mongo/Redis/AWS/Argon2 del servidor en Android.

Operación: `GET /api/v1/tournaments; GET /api/v1/groups/{gid}/tournaments`. JSON UTF-8 snake_case; IDs opacos de 24 hex cuando el contrato los exige; fechas UTC. Respuesta incoherente se trata como fallo, no como éxito vacío. Detalles transversales y URL real en la matriz canónica.

## 5. Criterios de aceptación

| ID | Resultado observable | Evidencia esperada / estado actual |
|---|---|---|
| MO-TOR-02-AC1 | Lista vacía/llena, filtros y páginas respetan meta y no inventan torneos ni nombres. | [PARCIAL](checklists/checklist.md#mo-tor-02-ac1) · ejecución local 2026-11-05 |
| MO-TOR-02-AC2 | Cambio rápido de filtro y cuenta no mezcla páginas ni datos previos; cursor final detiene carga. | [PARCIAL](checklists/checklist.md#mo-tor-02-ac2) · ejecución local 2026-11-05 |
| MO-TOR-02-AC3 | Bracket y partido muestran datos reales solo lectura; no hay acceso a crear/inscribir/reportar. | [COMPROBADO_LOCAL_CONSUMIDOR](checklists/checklist.md#mo-tor-02-ac3) · ejecución local 2026-11-05 |
| MO-TOR-02-AC4 | 401, 403, 422 y red caída muestran estados diferenciados; 403 no borra sesión. | [PARCIAL](checklists/checklist.md#mo-tor-02-ac4) · ejecución local 2026-11-05 |
| MO-TOR-02-ACQ | Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente | [CONTROLES_LOCALES_PARCIALES](checklists/checklist.md#mo-tor-02-acq) · ejecución local 2026-11-05 |

## 6. Plan de pruebas

Cada AC requiere casos JVM de estado/mapeo y HTTP con MockWebServer: payload exacto, códigos, cuerpo inválido, cancelación y ausencia de doble envío. Compose/dispositivo comprueba interacción, accesibilidad y navegación. Sesión/cifrado/selector necesitan mecanismo Android real cuando corresponda. Integración proveedor con cuentas y datos sintéticos autorizados, registrando commit/entorno/fecha y resultado redactado; no crear cuentas o modificar producción por esta preparación. Quality/SAST/SCA/secretos existentes se ejecutarán solo al abrir implementación y sobre el candidato afectado; sin porcentajes heredados del profesor.

## 7. Riesgos, límites y deuda

No hay creación/inscripción pública de torneos; proveedor debe aportar fixtures/torneos de prueba para validar respuesta no vacía sin inventarlos. Campos opcionales y enums de respuesta requieren conciliación.

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


## Pruebas específicas por operación — actualización 2026-11-05

[Escenarios y límites](../../docs/pruebas/matriz-operaciones.md) · [Manifestación canónica](../../docs/pruebas/operaciones.json). Cada archivo muestra sus escenarios y llama directamente al consumidor correspondiente. El ID de operación se conserva como prefijo del método (por ejemplo, AUTH_01_); la matriz valida la identidad completa:

- unit: `com.pactogg.app.feature.tournaments.list.ListTournamentsValidationTest.TOR_02_rejectsUnknownTournamentStatus`.
- integration: `com.pactogg.app.feature.tournaments.list.ListTournamentsApiTest.TOR_02_sendsGameFilterForTournamentListing`.
- security: `com.pactogg.app.feature.tournaments.list.ListTournamentsSecurityTest.TOR_02_forbiddenListingPreservesExistingSession`.

Se reutilizan además casos compartidos de TLS, sesión, estados, reglas y contrato. Android real prueba cifrado/Room/acceso por separado. Este apartado no convierte un caso de consumidor o fixture en aceptación de todos los AC ni prueba de autorización del servidor; controles y limitaciones finales se registran en el informe de esta fase.

### Archivos de prueba de esta operación

- Validación: [ListTournamentsValidationTest.kt](../../app/src/test/java/com/pactogg/app/feature/tournaments/list/ListTournamentsValidationTest.kt).
- Comunicación con API: [ListTournamentsApiTest.kt](../../app/src/test/java/com/pactogg/app/feature/tournaments/list/ListTournamentsApiTest.kt).
- Seguridad y errores: [ListTournamentsSecurityTest.kt](../../app/src/test/java/com/pactogg/app/feature/tournaments/list/ListTournamentsSecurityTest.kt).

Reorganización local 2026-11-05: se conservan los escenarios y las pruebas compartidas de sesión/recorridos. Sin aceptación funcional nueva ni publicación por mover pruebas.

## Relación criterio → tareas → pruebas → resultado — 2026-11-05

La tabla de aceptación enlaza la revisión detallada en el checklist. Se identifican casos ejecutados, alcance y escenarios faltantes sin cerrar criterios compuestos por un PASS parcial. [Resumen de las 19 specs](../../docs/pruebas/revision-specs-2026-11-05.md). Cambios exclusivamente locales; privacidad/avatares esperan al backend y Google Play queda fuera.
