# PG-03 — Registro, login y ciclo de sesión PactoGG

Tipo: aplicación/integración/pruebas. Fecha: 2026-10-30. Estado: borrador condicionado; no preparado para implementar por falta de contrato y recorrido visual ratificados.
Rector: [AGENTS](../../AGENTS.md) y [constitución SDD](../../.specify/memory/constitution.md).

## 1. Contexto y objetivo

La base navega entre vistas iniciales, pero no registra ni autentica usuarios. El corte del PDF v3 exige registro/login y pantallas posteriores al primer ingreso. Las historias originales 11, 13, 14, 15, 18 y 19 describen registro, login, continuidad de sesión, logout, validación y consentimiento; la 21 exige apodo, cuyo momento de captura falta cerrar.

El cambio implementará acceso con correo/contraseña y ciclo de sesión conforme al contrato del equipo, con navegación al primer destino ratificado. Fuentes: [primer flujo e IDs originales](../../docs/preparacion/primer-flujo-movil.md), [evaluación](../../docs/preparacion/evaluacion-unidad-2.md), [contrato por recibir](../../docs/integracion/contrato-api.md), [D-SDD-06](../../docs/decisiones/registro.md). No reemplaza las historias originales ni las declara completadas.

## 2. Requisitos funcionales

- RF1: registro/login en Compose con estado explícito de entrada, validación, carga, éxito y error; bloquear envío duplicado. Campos, consentimiento y reglas vienen del contrato ratificado, sin longitudes/regex inventadas.
- RF2: integrar operaciones de registro/login/logout del backend aprobado mediante DTO en datos y mapeadores explícitos; dominio independiente de Android/Retrofit. Casos de uso solo donde concentren reglas/orquestación justificadas.
- RF3: gestionar sesión conforme a emisión/expiración/refresh/revocación acordadas; diferenciar 401 y 403. No fijar TTL ni descartar refresh por analogía.
- RF4: definir almacenamiento sensible respaldado por Keystore y limpieza local/servidor al salir según política acordada, incluso ante fallo de red o cancelación. No basta con Room/DataStore planos.
- RF5: decidir inicio con/sin sesión y navegar al destino ratificado tras el primer acceso, conservando el diseño propio. El contenido funcional de otras pantallas necesita su alcance; no simular que un placeholder satisface el recorrido.
- RF6: tratar red/timeout/respuesta inválida, rotación/proceso/retorno y accesibilidad sin sesión falsa ni exposición de credenciales.

## 3. Requisitos no funcionales

HTTPS sin bypass; sin secretos de servidor en APK, tokens en logs/Bundle o contraseñas persistidas. Recursos de texto y diseño conforme a [DESIGN](../../DESIGN.md). Usar datos sintéticos y cuentas de prueba autorizadas fuera de documentos. Backend impone autenticación/autorización; ocultar acciones en UI no las sustituye.

## 4. Arquitectura y contratos internos

Rutas Kotlin propuestas bajo app/src/main/java/com/pactogg/app/. Son destino de diseño, no archivos creados:

| Área futura | Cambio delimitado |
|---|---|
| feature/auth/presentation/ | Register/Login Screen, ViewModel y estados/efectos de UI; nombres finales al cerrar diseño |
| feature/auth/domain/ | Modelos/interfaces de autenticación y reglas/casos de uso justificados, sin anotaciones Android/Retrofit |
| feature/auth/data/ | Servicio según contrato, DTO/mappers, repositorio y custodia de sesión elegida |
| core/di/AppModule.kt y core/navigation/ | Wiring Hilt y rutas/estado de arranque/salida, sin transportar secretos |
| res/values/strings.xml | Mensajes/etiquetas y accesibilidad del alcance |
| src/test/ y src/androidTest/ con paquetes correspondientes | Pruebas de reglas/estado/HTTP, almacenamiento real y UI/sesión |

La versión/ubicación de OpenAPI se enlaza desde docs/integracion/contrato-api.md; no duplicar allí rutas inventadas. Sesión compartida entre features puede justificar mover su interfaz/custodia a core: cerrar esa decisión antes de crear clases. Conservar Hilt; la solución de almacenamiento se elige por compatibilidad/amenazas, no se copia una biblioteca del ejemplo.

## 5. Criterios de aceptación

| ID | Dado / cuando / entonces | Evidencia futura |
|---|---|---|
| PG03-AC1 | Dado contrato versionado y recorrido/maqueta acordados, campos, errores y destino están definidos y trazados a historias reales | Contrato/maqueta identificados y revisión resuelta; criterio de entrada |
| PG03-AC2 | Con registro válido/inválido según contrato, se muestra resultado/transición correctos, validación pertinente y consentimiento acordado, sin doble envío | JVM, HTTP/MockWebServer y UI; historias 11/18/19 y apodo según alcance ratificado |
| PG03-AC3 | Con login válido/rechazado, red fallida o respuesta inválida, solo se crea sesión válida y se diferencian errores de autenticación/autorización | JVM/HTTP/UI e integración real; historia 13 |
| PG03-AC4 | Con reinicio, expiración/revocación o logout/fallo remoto, se cumple la política de sesión/limpieza aprobada sin exponer credenciales | Pruebas de política y almacenamiento Android real; historias 14/15 |
| PG03-AC5 | Tras primer acceso y cambios de ciclo de vida, el destino ratificado y los estados son coherentes y accesibles | Compose/dispositivo, referencia visual y recorrido acordado |
| PG03-AC6 | Al revisar build/estático/seguridad/integración, hay evidencia pertinente y límites visibles; una fixture no acredita al proveedor | Informes y pruebas con API autorizada, sin datos reales en evidencias |

PG03-AC2–AC6 deben concretar ejemplos del contrato/maqueta al satisfacer PG03-AC1. Sus valores pendientes no se rellenan con los del profesor.

Coordinación actualizada el 2026-11-03: la [propuesta de backend](../../../Backend/docs/propuesta-backend.md#6-specs-futuras-y-correspondencia-con-el-móvil) relaciona candidatos de registro/login/logout con estos criterios. Esa planificación no satisface PG03-AC1 ni acepta código. La conexión Android se implementará después de verificar el backend del alcance acordado; los cambios de contrato se contrastarán con esta spec durante su desarrollo.

## 6. Plan de pruebas

Desarrollar PF-C1–PF-C6 del primer-flujo-movil.md contra reglas reales: validación, estado, DTO/mappers, MockWebServer, carga/doble toque, ciclo de vida, errores, sesión/limpieza y almacenamiento sensible real. Coordinar integración con API/roles autorizados; distinguir fixtures de contrato de compatibilidad del proveedor. Reutilizar infraestructura PG-00/PG-01 cuando esté verificada. Seguridad dinámica móvil/API queda coordinada por superficie, sin afirmar que SAST basta.

## 7. Riesgos, límites y deuda

Bloqueantes: contrato y URL HTTPS por entorno, JSON de éxito/error, política de sesión/almacenamiento/logout, maqueta y destino tras primer ingreso. Falta decidir si verificación de correo, apodo/avatar y consentimiento condicionan el acceso y dónde se capturan. Las historias 12/16/17/22 no se añaden completas por inferencia; si condicionan el flujo, revisar explícitamente alcance y tareas antes de cerrar diseño.

Fuera de alcance: implementar backend/.NET/Redis/nube, proveedor Google/SSO no ratificado, recuperación completa no acordada, cámara/video, FCM, WorkManager, Offline-First, Deep Links y publicación Play. No dar por implementados grupos o todo el onboarding al definir un destino. No fijar nombres/campos HTTP, reglas de contraseña o políticas de reintento sin fuente.

## Aprobación

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación autorizada | Usuario | 2026-10-30 | Crear borrador concreto sin inventar entradas de backend/diseño |
| Diseño condicionado | Responsables móvil/backend/producto por confirmar | Pendiente | PG03-AC1 aún no satisfecho; no es una spec funcional cerrada |
| Implementación no iniciada | Pendiente | Pendiente | Sin registro/login/sesión aceptados |

Expediente: [plan](plan.md), [tareas](tasks.md), [checklist](checklists/checklist.md).
