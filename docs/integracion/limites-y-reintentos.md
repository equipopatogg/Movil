# Límites de red y política de reintentos

Estado: registro pendiente de medición y acuerdo, 2026-10-29. No modifica los valores técnicos ya presentes en la base anterior ni prescribe valores nuevos.

| Decisión | Estado | Evidencia que falta | Superficie |
|---|---|---|---|
| Timeouts de conexión/lectura/escritura y presupuesto de operación | Pendiente de ratificar por operación | Condiciones de red, latencia y fallos observados | Cliente y contrato |
| Operaciones reintentables, máximo de intentos y cancelación | Pendiente | Idempotencia y resultado de solicitudes interrumpidas | Cliente/backend |
| Backoff, jitter y señales de espera del servidor | Pendiente si se justifican | Códigos/cabeceras acordados y casos de abuso | Cliente/backend |
| Límites de peticiones y respuesta ante excederlos | Pendiente | Reglas del servidor, identidad y entorno | Backend impone; cliente informa |
| Página, orden, cursores y límites de listado | Pendiente | Contrato y mediciones de datos reales o sintéticos representativos | Cliente/backend |
| Tamaño, formato y duración del transporte temporal de fotos | Pendiente | Flujo de moderación previa, memoria y costos | Cliente/backend/almacenamiento |
| TTL de sesión, caché y retención de datos | Pendiente por concepto | Seguridad, privacidad y reglas de producto | Compartida; no son un mismo TTL |
| Recuperación tras pérdida de red o proceso | Pendiente por spec | Criterio funcional y estado seguro observable | Android/backend |

Los timeouts y páginas del profesor no se trasladan. Antes de fijar un número se registra operación, entorno, versión, método de medición, resultados y decisión. No confundir un valor por defecto de biblioteca con una política aprobada.

Los reintentos necesitan un presupuesto total y un tratamiento de cancelación; no se activan bucles ilimitados. Debe definirse cómo evitar duplicados de operaciones con efectos. WorkManager y Offline-First siguen pospuestos: esta matriz no autoriza colas ni sincronización en segundo plano.

Cuando exista OpenAPI, enlazar sus operaciones desde aquí sin duplicar sus reglas. Los acuerdos se registrarán en [decisiones](../decisiones/registro.md) y en la spec correspondiente; la evidencia usará la [plantilla](../../.specify/templates/evidencia-template.md).
