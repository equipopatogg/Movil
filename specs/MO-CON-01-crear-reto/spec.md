# MO-CON-01 — Creación manual de reto

Fecha: 2026-11-04. Estado: **IMPLEMENTACIÓN LOCAL / aceptación funcional pendiente**. Correspondencia backend: `CON-01-create-contract`. No equivale a aceptación de sus reglas o a implementación móvil.

## 1. Contexto y objetivo

Conectar la UI Android existente con `POST /api/v1/contracts; POST /api/v1/groups/{gid}/contracts`. Fuentes fijadas al commit `f6c37d8a70de43765f870b78b452832a2c0842b9`: [OpenAPI](https://github.com/equipopatogg/backend/blob/f6c37d8a70de43765f870b78b452832a2c0842b9/api/openapi.yaml), [guía](https://github.com/equipopatogg/backend/blob/f6c37d8a70de43765f870b78b452832a2c0842b9/docs/API-movil.md), [spec backend](https://github.com/equipopatogg/backend/blob/f6c37d8a70de43765f870b78b452832a2c0842b9/specs/CON-01-create-contract/spec.md). La UI anterior es prototipo local; se conserva su historial real. Marco de alcance y conflictos: [alineación móvil/backend](../../docs/integracion/alineacion-backend-2026-11-04.md). PG-03 coordina acceso. La navegación necesaria se define en cada operación disponible, sin spec adicional de alcance.

## 2. Requisitos funcionales

- RF1: Usar inicialmente la ruta plana con group_id del contexto real creado/recibido; no inferir ni inventar IDs. La ruta anidada es alternativa, no otra funcionalidad; su validación de group_id requiere prueba de proveedor antes de adoptarla.
- RF2: Capturar game 1–60 libre, condition 1–280, reward 3–80 virtual sin dinero, deadline futura convertida a UTC y bond_required elegido exclusivamente de settings.bond_options recibidos. source=manual; target_type=group o member con selección real de MO-MEM-02.
- RF3: Objetivo member requiere rival real seleccionado desde MO-MEM-02 dentro del grupo activo; no usar participantes de muestra ni permitir auto-reto. No habilitar voz/IA por tener este endpoint manual.
- RF4: Mostrar resumen editable antes de enviar. 201 confirma únicamente contrato open devuelto, sin aceptación, votos ni puntos ganados. Conservar ID real para acciones posteriores autorizadas.
- RF5: Tratar 403 NOT_GROUP_MEMBER, 404, 422 VALIDATION_ERROR/INVALID_BOND/INVALID_TARGET/CONTENT_REJECTED, 401 y errores de red. No repetir creación incierta; no inventar listado de contratos inexistente.

## 3. Requisitos no funcionales

HTTPS sin bypass, URL por entorno (es pública, no un secreto), sin credenciales de servidor. Nunca registrar cuerpos con contraseña/tokens ni enviarlos a analítica. Mensajes por code y correlación request_id sanitizada; no depender de message. Compose/Material3, Hilt, UDF y diseño aprobado. Carga/vacío/error, bloqueo de doble toque, teclado, semántica, fuente ampliada y ciclo de vida. Sin nuevos permisos por copiar backend. No activar funciones ausentes mediante mocks de producción.

## 4. Arquitectura y contratos internos

| Área prevista | Cambio | Frontera |
|---|---|---|
| feature/contracts/presentation/ | Estado, eventos, ViewModel y adaptación de pantalla existente | Sin HTTP ni almacenamiento en Composable |
| feature/contracts/domain/ | Modelos e interfaz de repositorio | Sin Android/Retrofit; casos de uso solo si justifican orquestación |
| feature/contracts/data/ | DTO snake_case, mappers y repositorio/servicio | Envelope data/meta y errores sin filtrar secretos |
| core/network/ y core/session/ | Transporte, errores y sesión compartidos definidos por MO-AUTH-03 | Sin duplicar autenticadores o custodios por feature |
| prototype/AppRoot.kt y core/navigation/ | Reutilizar UI; seleccionar un único grafo efectivo | No mantener bypass de Submit local en recorrido conectado |
| src/test/ y src/androidTest/ | Fixtures HTTP, estados, UI y dispositivos | Fixtures no acreditan servidor |

Son rutas propuestas, no archivos creados. Inspeccionar componentes existentes antes de agregar clases o dependencias; conservar Hilt. No reproducir Mongo/Redis/AWS/Argon2 del servidor en Android.

Operación: `POST /api/v1/contracts; POST /api/v1/groups/{gid}/contracts`. JSON UTF-8 snake_case; IDs opacos de 24 hex cuando el contrato los exige; fechas UTC. Respuesta incoherente se trata como fallo, no como éxito vacío. Detalles transversales y URL real en la matriz canónica.

## 5. Criterios de aceptación

| ID | Resultado observable | Evidencia esperada / estado actual |
|---|---|---|
| MO-CON-01-AC1 | Contexto de grupo válido, fianza recibida y formulario válido producen un POST y resumen del contrato real open. | [PARCIAL](checklists/checklist.md#mo-con-01-ac1) · ejecución local 2026-11-05 |
| MO-CON-01-AC2 | UTC, fecha vencida, límites, fianza inválida y moderación rechazada se muestran sin éxito ficticio. | [PARCIAL](checklists/checklist.md#mo-con-01-ac2) · ejecución local 2026-11-05 |
| MO-CON-01-AC3 | Sin grupo/settings no se envía; member exige selección real y excluye usuario propio; voz sigue oculta y aceptar corresponde a MO-CON-06. | [PARCIAL](checklists/checklist.md#mo-con-01-ac3) · ejecución local 2026-11-05 |
| MO-CON-01-AC4 | Respuesta perdida no provoca reintento automático ni duplicación deliberada. | [PARCIAL](checklists/checklist.md#mo-con-01-ac4) · ejecución local 2026-11-05 |
| MO-CON-01-ACQ | Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente | [CONTROLES_LOCALES_PARCIALES](checklists/checklist.md#mo-con-01-acq) · ejecución local 2026-11-05 |

## 6. Plan de pruebas

Cada AC requiere casos JVM de estado/mapeo y HTTP con MockWebServer: payload exacto, códigos, cuerpo inválido, cancelación y ausencia de doble envío. Compose/dispositivo comprueba interacción, accesibilidad y navegación. Sesión/cifrado/selector necesitan mecanismo Android real cuando corresponda. Integración proveedor con cuentas y datos sintéticos autorizados, registrando commit/entorno/fecha y resultado redactado; no crear cuentas o modificar producción por esta preparación. Quality/SAST/SCA/secretos existentes se ejecutarán solo al abrir implementación y sobre el candidato afectado; sin porcentajes heredados del profesor.

## 7. Riesgos, límites y deuda

GET de contratos y miembros se integran por MO-CON-02/03 y MO-MEM-02. No sustituir aceptación por crear. Ruta anidada: filtro valida DTO antes de asignar gid; confirmar comportamiento real y evitarla inicialmente. Fianzas/reglas deben conciliarse con producto.

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


## Ajuste acotado de contratos — 2026-11-04

Autorización expresa: usuario solicita aplicar cambios de bajo impacto que encajen en specs existentes. Fuente backend actual: commit `8baa9e6d04038c1d6cf9588c5526ad63b94c3f87`; ver [revisión](../../docs/integracion/revision-contratos-nuevos-2026-11-04.md). La afirmación histórica de ausencia de GET de contratos/miembros queda superada en backend; aún no están integrados sus recorridos completos en Android.

El detalle `GET /api/v1/contracts/{id}` se incorpora exclusivamente como lectura auxiliar después de enviar evidencia en MO-EVI-02; no implementa tablero, apertura arbitraria de detalle, recuperación de historial, aceptar/rechazar ni el expediente completo MO-CON-03. La selección de rival sigue desactivada hasta implementar miembros reales bajo su propia spec. No se cambia la regla de quién puede enviar ni se exige aceptación previa mientras backend concilia su flujo.


## Especificaciones confirmadas posteriores — 2026-11-04

Usuario solicita equivalentes móviles de las once operaciones nuevas incluidas en Sistema.docx; ver [matriz documental](../../docs/integracion/specs-confirmadas-sistema-2026-11-04.md). Sus recorridos no se agregan silenciosamente a esta operación: lista/detalle de grupo, unión/miembros y tablero/detalle/aceptar/rechazar tienen expedientes propios. La lectura auxiliar ya implementada por MO-EVI-02 se reutiliza en MO-CON-03 sin duplicación ni atribuir implementación general. Restricciones previas de navegación describen el corte anterior; nuevas entradas solo se activarán al implementar y verificar la spec correspondiente. Fuera de este encargo documental no se cambió código.


## Pruebas específicas por operación — actualización 2026-11-05

[Escenarios y límites](../../docs/pruebas/matriz-operaciones.md) · [Manifestación canónica](../../docs/pruebas/operaciones.json). Cada archivo muestra sus escenarios y llama directamente al consumidor correspondiente. El ID de operación se conserva como prefijo del método (por ejemplo, AUTH_01_); la matriz valida la identidad completa:

- unit: `com.pactogg.app.feature.contracts.create.CreateChallengeValidationTest.CON_01_rejectsPastDeadlineAndMonetaryReward`.
- integration: `com.pactogg.app.feature.contracts.create.CreateChallengeApiTest.CON_01_sendsGroupAndTargetForChallengeCreation`.
- security: `com.pactogg.app.feature.contracts.create.CreateChallengeSecurityTest.CON_01_forbiddenCreationPreservesExistingSession`.

Se reutilizan además casos compartidos de TLS, sesión, estados, reglas y contrato. Android real prueba cifrado/Room/acceso por separado. Este apartado no convierte un caso de consumidor o fixture en aceptación de todos los AC ni prueba de autorización del servidor; controles y limitaciones finales se registran en el informe de esta fase.

### Archivos de prueba de esta operación

- Validación: [CreateChallengeValidationTest.kt](../../app/src/test/java/com/pactogg/app/feature/contracts/create/CreateChallengeValidationTest.kt).
- Comunicación con API: [CreateChallengeApiTest.kt](../../app/src/test/java/com/pactogg/app/feature/contracts/create/CreateChallengeApiTest.kt).
- Seguridad y errores: [CreateChallengeSecurityTest.kt](../../app/src/test/java/com/pactogg/app/feature/contracts/create/CreateChallengeSecurityTest.kt).

Reorganización local 2026-11-05: se conservan los escenarios y las pruebas compartidas de sesión/recorridos. Sin aceptación funcional nueva ni publicación por mover pruebas.

## Relación criterio → tareas → pruebas → resultado — 2026-11-05

La tabla de aceptación enlaza la revisión detallada en el checklist. Se identifican casos ejecutados, alcance y escenarios faltantes sin cerrar criterios compuestos por un PASS parcial. [Resumen de las 19 specs](../../docs/pruebas/revision-specs-2026-11-05.md). Cambios exclusivamente locales; privacidad/avatares esperan al backend y Google Play queda fuera.
