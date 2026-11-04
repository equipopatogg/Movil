# Diseño de pruebas futuras

Se adapta la separación de suites del profesor. Esta fase no añade tests de aplicación ni ejecuta suites.

| Área | Qué debe cerrar la spec | Evidencia propuesta |
|---|---|---|
| Lógica cliente | Reglas propias y límites/errores | JVM sobre comportamiento real |
| Transporte | Payloads/códigos, timeouts, cancelación y TLS | MockWebServer con fixtures OpenAPI aprobados |
| Persistencia | Datos, retención y migración | Room real y cambios desde versiones reales |
| UI | Maqueta, navegación, carga/vacío/error, fuente y accesibilidad | Compose/dispositivo y revisión visual |
| Sesión | Rotación/revocación/logout y pérdida de proceso | Móvil/API; sin TTLs inventados |
| Autorización | Acceso por grupo/recurso | Usuarios/roles distintos; casos IDOR backend |
| JWT | Emisor/audiencia/algoritmo/expiración | Casos adversos backend; APK no firma tokens |
| Reintentos | Operaciones y duplicados permitidos | API/idempotencia según reglas ratificadas |
| Evidencias/IA | Validación y análisis previo; esquema y fallos | Rechazo/timeout/salida inválida y alternativa definida |
| Datos backend | Motor, aislamiento, índices y recuperación | Testcontainers del motor elegido; no MSSQL por defecto |
| Contratos | Garantías consumidor/proveedor | OpenAPI/fixtures primero; Pact selectivo |
| Rendimiento | Dispositivo/red/carga/memoria/costo | Línea base y presupuestos propios |
| Operación | Servicio caído, alerta y restauración | Guiones controlados; caos avanzado pospuesto |

Aislar estado y datos de cada prueba, limpiar solo recursos propios, comprobar socket/proceso requerido por la herramienta y distinguir fallo de suite/entorno/producto.
Los dobles en memoria no demuestran transacciones, índices, cambios de datos ni consistencia distribuida.
Las fixtures sintéticas se vincularán a la versión del contrato y al criterio probado; compartir datos auxiliares solo cuando evite duplicación. Una captura real no se incorpora automáticamente: revisar anonimización y autorización antes de conservarla. Cada reporte futuro identificará variante, casos ejecutados y omitidos; una suite con cero pruebas no demuestra éxito.
Medir recursos/duración antes de fijar tiempos CI. No modificar reglas de negocio para lograr una prueba verde.

Las evidencias de la fase previa están en ../verificacion-local.md. Instrumentación y compatibilidad nativa siguen como pendientes técnicos de la siguiente fase autorizada; preparar SDD no reabre su ejecución.

## Criterios transversales consolidados

El borrador general MB-02 se retiró de specs; sus criterios permanecen aquí y en controles-y-compuertas.md. En cada cambio concreto: criterio → caso → datos/entorno → prueba/revisión → reporte y resultado. No basta que exista un archivo cuyo nombre incluya test o security.

JVM cubre comportamiento aislado; MockWebServer verifica transporte/serialización contra fixtures versionadas; Room y sus migraciones requieren datos locales reales del alcance; UI, accesibilidad y ciclo de vida necesitan la evidencia de dispositivo correspondiente. Un fake de almacén no demuestra cifrado/Keystore, y un snapshot local no acredita al proveedor de API.

El reporte distingue pruebas escritas, compiladas, ejecutadas, omitidas y fallidas, además de variante/API/ABI cuando apliquen. Ausencia de reporte o cero casos esperados ejecutados no permiten declarar éxito. Línea base de cobertura/rendimiento, umbrales y excepciones se acuerdan con evidencia propia.

Estas son pautas mantenidas. Una spec de pruebas solo corresponde cuando delimite qué pruebas, utilidades o configuración concretas se crearán/cambiarán y qué comportamiento verificarán. Los casos del primer flujo siguen pendientes de contrato y maqueta.
