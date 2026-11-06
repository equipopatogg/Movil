# MO-EVI-02 — Envío de evidencias

## Alcance vigente — imágenes, 2026-11-05

Usuario autoriza completar la integración a partir del mensaje enviado por backend. Esta ampliación pertenece únicamente a evidencias; la revisión general y los ajustes de navegación no generan otra spec. Las afirmaciones inferiores sobre ausencia de GET/galería son historia superada.

- IMG-RF1: selector nativo sin cámara/permisos generales; cancelar no lee ni envía. JPEG/PNG/WebP válidos se decodifican y normalizan a JPEG sin metadatos, respetando orientación; rechazar originales mayores de 8000 px por lado. Límite defensivo local de lectura: 32 MiB; reducir hasta 2048 px por lado para acotar memoria y tráfico. Archivo final <= 5 MiB (5 242 880 bytes, conforme a opciones backend), sin cambiar versiones de aplicación. ExifInterface 1.4.1 se declara explícitamente; ya estaba presente de forma transitiva por Coil.
- IMG-RF2: vista previa y envío explícito; cambiar/quitar/salir borra el búfer pendiente. Nunca persistir fotos en SavedState/Room ni registrar bytes/URI/tokens. Multipart `type=photo` text/plain, `file` image/jpeg con nombre .jpg. Enlaces conservan JSON y hosts permitidos.
- IMG-RF3: recuperar `url`, `created_at`, `expires_at`; consultar `GET /api/v1/contracts/{id}/evidence` con sesión como lectura auxiliar al envío/visualización dentro de esta spec. Validar IDs de cada resultado. La imagen HTTPS se carga con Coil mediante cliente sin Authorization/cookies/redirects; sin caché persistente ni memoria compartida entre cuentas. Enlaces se abren solo si corresponden a los hosts permitidos. Vencimiento se muestra, no se atribuye borrado S3 a Android.
- IMG-RF4: solo ejecutor/aceptante/destinatario confirmado en in_progress o voting, plazo vigente y contador inferior a tres puede subir; el resto consulta. Tras 403 NOT_CONTRACT_TARGET o 409, ocultar envío hasta refrescar detalle. El primer voto no figura en ContractResponse: no inventar un contador ni consultar endpoints nuevos; manejar rechazo del proveedor. La autorización final siempre es servidor.
- IMG-RF5: 422 de tamaño/formato/moderación, 403, 409, 401, 429 y fallos de red/servicio no inventan aprobación ni repiten POST. Para 409 de evidencia mostrar mensaje acotado/sanitizado del servidor y decidir por code/status. No mostrar details.labels. Fallo del GET posterior conserva el éxito confirmado y permite actualizar imágenes sin resubir. Una escritura incierta exige actualizar detalle antes de otro envío.

Fuentes: mensaje backend aportado en este chat; [backend fijado 451c8ae](https://github.com/equipopatogg/backend/tree/451c8ae1bfae3df64cf95f5a26ace7b1eacb3b1e/Pactogg.Api/Modules/Evidence), guía/OpenAPI del mismo commit; Sprint_Planning_PactoGG.xlsx consultado, fila ID156 «No se ha iniciado». S3 directo con URL firmada permanece diferido.

Discrepancias comprobadas en código fuente backend (no prueba de despliegue): POST de foto puede responder url=null; GET genera URL de lectura. EvidenceService aún admite estados anteriores y conserva TODO para primer voto. Android sigue la regla más reciente comunicada, consulta GET para obtener URL y no certifica cumplimiento de reglas servidor. No editar backend ni omitir moderación.

**Prueba contra despliegue 2026-11-05:** nueve comprobaciones previas correctas (sesión QA y consultas); GET de evidencias devuelve 405. Se registra bloqueo del proveedor, sin inventar historial vacío. El móvil mantiene éxito confirmado y muestra indisponibilidad; verificar de nuevo cuando backend despliegue esta ruta. No se hicieron escrituras de negocio y se cerró únicamente la sesión del sondeo.

Criterios añadidos: IMG-AC1 preparación/preview/envío explícito; IMG-AC2 listado seguro y expiración; IMG-AC3 controles de autorización/errores/no duplicación. Tareas y evidencia en los archivos de este expediente. Las casillas integrales anteriores permanecen abiertas donde falta proveedor o aceptación humana.


Fecha: 2026-11-04. Estado: **IMPLEMENTACIÓN LOCAL / aceptación funcional pendiente**. Correspondencia backend: `EVI-02-evidence`. No equivale a aceptación de sus reglas o a implementación móvil.

## 1. Contexto y objetivo

Conectar la UI Android existente con `POST /api/v1/contracts/{id}/evidence`. Fuentes fijadas al commit `f6c37d8a70de43765f870b78b452832a2c0842b9`: [OpenAPI](https://github.com/equipopatogg/backend/blob/f6c37d8a70de43765f870b78b452832a2c0842b9/api/openapi.yaml), [guía](https://github.com/equipopatogg/backend/blob/f6c37d8a70de43765f870b78b452832a2c0842b9/docs/API-movil.md), [spec backend](https://github.com/equipopatogg/backend/blob/f6c37d8a70de43765f870b78b452832a2c0842b9/specs/EVI-02-evidence/spec.md). La UI anterior es prototipo local; se conserva su historial real. Marco de alcance y conflictos: [alineación móvil/backend](../../docs/integracion/alineacion-backend-2026-11-04.md). PG-03 coordina acceso. La navegación necesaria se define en cada operación disponible, sin spec adicional de alcance.

## 2. Requisitos funcionales

- RF1: Abrir únicamente con ID real de contrato y contexto de autorización; no usar IDs del prototipo. JSON de enlace: type=link,url HTTPS, 8–2048; hosts de guía youtube.com,youtu.be,twitch.tv,clips.twitch.tv,medal.tv,streamable.com. Validar host exacto o subdominio permitido según contrato, no por contiene-texto.
- RF2: Foto mediante selector Android de imágenes, sin cámara y sin pedir acceso general innecesario: multipart file, type=photo explícito; JPEG/PNG/WebP hasta 5 MB según guía, confirmar límite exacto en bytes. Leer URI de forma acotada, gestionar cancelación/permisos transitorios; no subir video.
- RF3: Esperar 201 y moderation del servidor; no guardar ni marcar como aprobada una imagen por validación local. El servidor realiza moderación previa; Android no llama AWS ni certifica política.
- RF4: Manejar 422 CONTENT_REJECTED/IMAGE_TOO_LARGE/UNSUPPORTED_MEDIA_TYPE, 409 EVIDENCE_LIMIT_REACHED/INVALID_CONTRACT_STATE, 403 NOT_CONTRACT_TARGET, 404, 429, 503 y 401. details puede venir objeto o lista: parseo tolerante y seguro.
- RF5: Máximo tres evidencias es regla servidor; reflejar respuestas reales sin contador global inventado. No reintentar POST incierto. El creador no es automáticamente el autorizado si el reto es para otro miembro.

## 3. Requisitos no funcionales

HTTPS sin bypass, URL por entorno (es pública, no un secreto), sin credenciales de servidor. Nunca registrar cuerpos con contraseña/tokens ni enviarlos a analítica. Mensajes por code y correlación request_id sanitizada; no depender de message. Compose/Material3, Hilt, UDF y diseño aprobado. Carga/vacío/error, bloqueo de doble toque, teclado, semántica, fuente ampliada y ciclo de vida. Sin nuevos permisos por copiar backend. No activar funciones ausentes mediante mocks de producción.

## 4. Arquitectura y contratos internos

| Área prevista | Cambio | Frontera |
|---|---|---|
| feature/evidence/presentation/ | Estado, eventos, ViewModel y adaptación de pantalla existente | Sin HTTP ni almacenamiento en Composable |
| feature/evidence/domain/ | Modelos e interfaz de repositorio | Sin Android/Retrofit; casos de uso solo si justifican orquestación |
| feature/evidence/data/ | DTO snake_case, mappers y repositorio/servicio | Envelope data/meta y errores sin filtrar secretos |
| core/network/ y core/session/ | Transporte, errores y sesión compartidos definidos por MO-AUTH-03 | Sin duplicar autenticadores o custodios por feature |
| prototype/AppRoot.kt y core/navigation/ | Reutilizar UI; seleccionar un único grafo efectivo | No mantener bypass de Submit local en recorrido conectado |
| src/test/ y src/androidTest/ | Fixtures HTTP, estados, UI y dispositivos | Fixtures no acreditan servidor |

Son rutas propuestas, no archivos creados. Inspeccionar componentes existentes antes de agregar clases o dependencias; conservar Hilt. No reproducir Mongo/Redis/AWS/Argon2 del servidor en Android.

Operación: `POST /api/v1/contracts/{id}/evidence`. JSON UTF-8 snake_case; IDs opacos de 24 hex cuando el contrato los exige; fechas UTC. Respuesta incoherente se trata como fallo, no como éxito vacío. Detalles transversales y URL real en la matriz canónica.

## 5. Criterios de aceptación

| ID | Resultado observable | Evidencia esperada / estado actual |
|---|---|---|
| MO-EVI-02-AC1 | Enlace y foto válidos usan serialización/content-type correctos y muestran solo evidencia confirmada por 201. | [PARCIAL](checklists/checklist.md#mo-evi-02-ac1) · ejecución local 2026-11-05 |
| MO-EVI-02-AC2 | Selector cancelado, archivo vacío/grande/tipo inválido no envían; no existen cámara ni carga de video. | [PARCIAL](checklists/checklist.md#mo-evi-02-ac2) · ejecución local 2026-11-05 |
| MO-EVI-02-AC3 | Moderación rechazada, límite 409, autorización 403 y servicio 503 presentan acciones correctas sin falsos aprobados. | [PARCIAL](checklists/checklist.md#mo-evi-02-ac3) · ejecución local 2026-11-05 |
| MO-EVI-02-AC4 | Sin contrato autorizado no se abre envío; pérdida de respuesta no repite la subida. | [PARCIAL](checklists/checklist.md#mo-evi-02-ac4) · ejecución local 2026-11-05 |
| MO-EVI-02-ACQ | Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente | [CONTROLES_LOCALES_PARCIALES](checklists/checklist.md#mo-evi-02-acq) · ejecución local 2026-11-05 |

## 6. Plan de pruebas

Cada AC requiere casos JVM de estado/mapeo y HTTP con MockWebServer: payload exacto, códigos, cuerpo inválido, cancelación y ausencia de doble envío. Compose/dispositivo comprueba interacción, accesibilidad y navegación. Sesión/cifrado/selector necesitan mecanismo Android real cuando corresponda. Integración proveedor con cuentas y datos sintéticos autorizados, registrando commit/entorno/fecha y resultado redactado; no crear cuentas o modificar producción por esta preparación. Quality/SAST/SCA/secretos existentes se ejecutarán solo al abrir implementación y sobre el candidato afectado; sin porcentajes heredados del profesor.

## 7. Riesgos, límites y deuda

No hay GET de evidencias ni votos; GET de contratos ya existe y se integra por MO-CON-02/03. No se promete galería después de reinicio. Backend mantiene TODO de bloqueo tras primer voto. Política de moderación modificada en f6c37d8: mostrar mensaje seguro, no copiar umbrales como validación Android.

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

- RF6: tras evidencia confirmada, consultar detalle con sesión compartida; validar coincidencia de ID y grupo y mostrar estado devuelto. No deducir voting localmente ni presentar la instantánea open anterior como estado actual.
- RF7: si falla esa lectura, conservar evidencia confirmada, identificar estado como pendiente y avisar que no se repita el envío. Respuesta tardía no puede reponer datos tras cierre/cambio de cuenta.
- MO-EVI-02-AC5: POST 201 seguido de GET correcto actualiza estado; GET fallido conserva éxito de subida sin repetir POST; detalle de otro reto/grupo se rechaza; cierre de sesión durante lectura no repone datos. Evidencia: pruebas HTTP y estado, ver informe del ajuste cuando finalice. No cierra otros AC.


## Especificaciones confirmadas posteriores — 2026-11-04

Usuario solicita equivalentes móviles de las once operaciones nuevas incluidas en Sistema.docx; ver [matriz documental](../../docs/integracion/specs-confirmadas-sistema-2026-11-04.md). Sus recorridos no se agregan silenciosamente a esta operación: lista/detalle de grupo, unión/miembros y tablero/detalle/aceptar/rechazar tienen expedientes propios. La lectura auxiliar ya implementada por MO-EVI-02 se reutiliza en MO-CON-03 sin duplicación ni atribuir implementación general. Restricciones previas de navegación describen el corte anterior; nuevas entradas solo se activarán al implementar y verificar la spec correspondiente. Fuera de este encargo documental no se cambió código.


## Pruebas específicas por operación — actualización 2026-11-05

[Escenarios y límites](../../docs/pruebas/matriz-operaciones.md) · [Manifestación canónica](../../docs/pruebas/operaciones.json). Cada archivo muestra sus escenarios y llama directamente al consumidor correspondiente. El ID de operación se conserva como prefijo del método (por ejemplo, AUTH_01_); la matriz valida la identidad completa:

- unit: `com.pactogg.app.feature.evidence.send.SendEvidenceValidationTest.EVI_02_rejectsLookalikeDomainAndEmptyPhoto`.
- integration: `com.pactogg.app.feature.evidence.send.SendEvidenceApiTest.EVI_02_sendsAllowedEvidenceLink`.
- security: `com.pactogg.app.feature.evidence.send.SendEvidenceSecurityTest.EVI_02_forbiddenEvidencePreservesExistingSession`.

Se reutilizan además casos compartidos de TLS, sesión, estados, reglas y contrato. Android real prueba cifrado/Room/acceso por separado. Este apartado no convierte un caso de consumidor o fixture en aceptación de todos los AC ni prueba de autorización del servidor; controles y limitaciones finales se registran en el informe de esta fase.

### Archivos de prueba de esta operación

- Validación: [SendEvidenceValidationTest.kt](../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceValidationTest.kt).
- Comunicación con API: [SendEvidenceApiTest.kt](../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceApiTest.kt).
- Seguridad y errores: [SendEvidenceSecurityTest.kt](../../app/src/test/java/com/pactogg/app/feature/evidence/send/SendEvidenceSecurityTest.kt).

Reorganización local 2026-11-05: se conservan los escenarios y las pruebas compartidas de sesión/recorridos. Sin aceptación funcional nueva ni publicación por mover pruebas.

## Relación criterio → tareas → pruebas → resultado — 2026-11-05

La tabla de aceptación enlaza la revisión detallada en el checklist. Se identifican casos ejecutados, alcance y escenarios faltantes sin cerrar criterios compuestos por un PASS parcial. [Resumen de las 19 specs](../../docs/pruebas/revision-specs-2026-11-05.md). Cambios exclusivamente locales; privacidad/avatares esperan al backend y Google Play queda fuera.
