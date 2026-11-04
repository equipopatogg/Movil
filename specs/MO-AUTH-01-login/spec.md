# MO-AUTH-01 — Inicio de sesión

Fecha: 2026-11-04. Estado: **IMPLEMENTACIÓN LOCAL / aceptación funcional pendiente**. Correspondencia backend: `AUTH-01-login`. No equivale a aceptación de sus reglas o a implementación móvil.

## 1. Contexto y objetivo

Conectar la UI Android existente con `POST /api/v1/auth/login`. Fuentes fijadas al commit `f6c37d8a70de43765f870b78b452832a2c0842b9`: [OpenAPI](https://github.com/equipopatogg/backend/blob/f6c37d8a70de43765f870b78b452832a2c0842b9/api/openapi.yaml), [guía](https://github.com/equipopatogg/backend/blob/f6c37d8a70de43765f870b78b452832a2c0842b9/docs/API-movil.md), [spec backend](https://github.com/equipopatogg/backend/blob/f6c37d8a70de43765f870b78b452832a2c0842b9/specs/AUTH-01-login/spec.md). La UI anterior es prototipo local; se conserva su historial real. Marco de alcance y conflictos: [alineación móvil/backend](../../docs/integracion/alineacion-backend-2026-11-04.md). PG-03 coordina acceso. La navegación necesaria se define en cada operación disponible, sin spec adicional de alcance.

## 2. Requisitos funcionales

- RF1: Enviar email y password; device_id opcional omitido inicialmente. Login admite contraseña 1–128 según DTO del servidor; no aplicar el mínimo de registro al acceso ni alterar la contraseña.
- RF2: Procesar 200 con data.user, access_token, refresh_token, token_type y expires_in; no fabricar usuario, token o membresía si faltan.
- RF3: 401 INVALID_CREDENTIALS muestra error genérico; 401 UNAUTHORIZED de cuenta inactiva no inicia bucle de refresh. 422 valida campos; 429 muestra espera sin reintentos automáticos ni contador inventado.
- RF4: Bloquear doble envío, mantener carga/error y limpiar contraseña al salir. Atrás después del éxito no vuelve al formulario con credenciales.

## 3. Requisitos no funcionales

HTTPS sin bypass, URL por entorno (es pública, no un secreto), sin credenciales de servidor. Nunca registrar cuerpos con contraseña/tokens ni enviarlos a analítica. Mensajes por code y correlación request_id sanitizada; no depender de message. Compose/Material3, Hilt, UDF y diseño aprobado. Carga/vacío/error, bloqueo de doble toque, teclado, semántica, fuente ampliada y ciclo de vida. Sin nuevos permisos por copiar backend. No activar funciones ausentes mediante mocks de producción.

## 4. Arquitectura y contratos internos

| Área prevista | Cambio | Frontera |
|---|---|---|
| feature/auth/presentation/ | Estado, eventos, ViewModel y adaptación de pantalla existente | Sin HTTP ni almacenamiento en Composable |
| feature/auth/domain/ | Modelos e interfaz de repositorio | Sin Android/Retrofit; casos de uso solo si justifican orquestación |
| feature/auth/data/ | DTO snake_case, mappers y repositorio/servicio | Envelope data/meta y errores sin filtrar secretos |
| core/network/ y core/session/ | Transporte, errores y sesión compartidos definidos por MO-AUTH-03 | Sin duplicar autenticadores o custodios por feature |
| prototype/AppRoot.kt y core/navigation/ | Reutilizar UI; seleccionar un único grafo efectivo | No mantener bypass de Submit local en recorrido conectado |
| src/test/ y src/androidTest/ | Fixtures HTTP, estados, UI y dispositivos | Fixtures no acreditan servidor |

Son rutas propuestas, no archivos creados. Inspeccionar componentes existentes antes de agregar clases o dependencias; conservar Hilt. No reproducir Mongo/Redis/AWS/Argon2 del servidor en Android.

Operación: `POST /api/v1/auth/login`. JSON UTF-8 snake_case; IDs opacos de 24 hex cuando el contrato los exige; fechas UTC. Respuesta incoherente se trata como fallo, no como éxito vacío. Detalles transversales y URL real en la matriz canónica.

## 5. Criterios de aceptación

| ID | Resultado observable | Evidencia esperada / estado actual |
|---|---|---|
| MO-AUTH-01-AC1 | 200 válido almacena sesión y abre inicio; credenciales inválidas 401 permanecen en acceso sin refresh recursivo. | Prueba consumidor + evidencia de proveedor aplicable; NO_EJECUTADO |
| MO-AUTH-01-AC2 | 429, 422, timeout y respuesta malformada tienen estado recuperable y no inician sesión. | Prueba consumidor + evidencia de proveedor aplicable; NO_EJECUTADO |
| MO-AUTH-01-AC3 | Doble toque, rotación y recreación no duplican login ni persisten contraseña en SavedState/Bundle. | Prueba consumidor + evidencia de proveedor aplicable; NO_EJECUTADO |
| MO-AUTH-01-ACQ | Build, análisis y pruebas aplicables del mismo candidato correctos, sin secretos ni informe ausente | Informes identificados; NO_EJECUTADO |

## 6. Plan de pruebas

Cada AC requiere casos JVM de estado/mapeo y HTTP con MockWebServer: payload exacto, códigos, cuerpo inválido, cancelación y ausencia de doble envío. Compose/dispositivo comprueba interacción, accesibilidad y navegación. Sesión/cifrado/selector necesitan mecanismo Android real cuando corresponda. Integración proveedor con cuentas y datos sintéticos autorizados, registrando commit/entorno/fecha y resultado redactado; no crear cuentas o modificar producción por esta preparación. Quality/SAST/SCA/secretos existentes se ejecutarán solo al abrir implementación y sobre el candidato afectado; sin porcentajes heredados del profesor.

## 7. Riesgos, límites y deuda

Depende de custodia MO-AUTH-03 y destino posterior al ingreso. Bloqueo por correo/IP es responsabilidad del servidor; no simular su contador en Android.

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
