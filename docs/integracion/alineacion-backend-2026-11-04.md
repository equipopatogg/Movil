# Actualización de implementación — 2026-11-04

Usuario autoriza ejecutar las specs. Se implementó consumidor local para las ocho operaciones y recorrido conectado. La preparación inferior conserva la recepción/hallazgos originales; su restricción documental no describe la fase actual. El aviso básico fue autorizado posteriormente y está disponible dentro del registro; ratificación del equipo y distribución pública siguen pendientes. No se crea PG-08 ni se enlazan recordatorios privados.

[Verificación de implementación](verificacion-consumidor-2026-11-04.md) registra resultados y límites del candidato. Las garantías y datos de proveedor no cubiertos conservan su pendiente, especialmente contexto de grupos/contratos sin GET, revocación global y reglas editables. Sin modificaciones backend ni publicación.

# Alineación del móvil con backend — 2026-11-04

Estado: contrato recibido y preparación solicitada; **integración no implementada**. Usuario pide specs antes de programar, correspondientes a las operaciones disponibles del backend. Esta revisión es documental: no se crean cuentas, ejecutan scripts externos ni se llama al servidor de negocio.

## Fuentes fijadas y recepción

Repositorio proveedor: https://github.com/equipopatogg/backend, commit `f6c37d8a70de43765f870b78b452832a2c0842b9`. Fuentes: [OpenAPI 1.0.0](https://github.com/equipopatogg/backend/blob/f6c37d8a70de43765f870b78b452832a2c0842b9/api/openapi.yaml), [guía actual](https://github.com/equipopatogg/backend/blob/f6c37d8a70de43765f870b78b452832a2c0842b9/docs/API-movil.md), specs y módulos del mismo commit. Archivos recibidos: Downloads/openapi.yaml y API-movil.md. OpenAPI coincide por líneas con el recibido; guía recibida es anterior a cambios vigentes de evidencia/auto-reto y moderación. Enlazar fuente canónica fijada, sin mantener copias divergentes.

La guía señala base pública `https://52-3-200-211.sslip.io`, CDN `https://d3shxplwv8bmvp.cloudfront.net`; OpenAPI conserva un dominio de ejemplo y localhost HTTP. Configurar entorno explícito, sin copiar ese HTTP al dispositivo ni quitar validación TLS. URL pública no es secreto; claves de infraestructura sí. Confirmar entorno estable antes de integración.

## Primera entrega y evaluación general

PDF local: «Requerimientos para el desarrollo del proyecto Móvil v3.pdf», página 4, corte 3–4 noviembre: backend desplegado, registro/login/logout y cuatro endpoints adicionales; móvil registro/login funcionales, pantallas después del primer ingreso y avance de registro/configuración Google Play. No dice que Android deba implementar literalmente cada endpoint adicional en ese corte; la alineación completa actual es solicitud expresa del usuario. La evaluación general del 2 diciembre conserva sus otros controles y requisitos: no se cierra por esta preparación ni por el frontend visual. Play no es un endpoint y permanece en checklist de evaluación.

## Cómo aparece desarrollado en su Git

El historial público revisado contiene tres commits: `c4107e6` incorpora ya base, siete operaciones de evaluación más refresh, specs, infraestructura y despliegue; `8ef88e3` ajusta publicación por SSM/OIDC, guía y refresh en contrato; `f6c37d8` ajusta moderación y CI. El primer commit agrupa código y specs: no permite demostrar el orden de autoría anterior, ni debemos inventarlo o reproducirlo retroactivamente.

Su patrón útil es expediente por operación (spec, plan, tasks, checklists), DTO/servicio y pruebas. Android adopta la trazabilidad de la operación; implementa consumidor, estado/errores, custodia y navegación, no Mongo, Redis, Argon2, S3, Terraform o moderación del servidor. AUTH-03 está en código/OpenAPI sin expediente propio; Android sí necesita su spec de sesión.

Última ejecución remota observada: [41800402336](https://github.com/equipopatogg/backend/actions/runs/41800402336), success, con build/tests, CodeQL, SCA, contenedor, Terraform, despliegue y health. Esto no acredita compatibilidad Android ni permite trasladar PASS a nuestras specs. Specs backend conservan deudas y párrafos anteriores al despliegue: código, contrato y evidencia deben conciliarse por punto.

## Correspondencia de operaciones

| Backend | Método/ruta | Spec consumidor | Alcance Android |
|---|---|---|---|
| USR-01 | POST /api/v1/auth/register | [MO-USR-01](../../specs/MO-USR-01-registro/spec.md) | Formulario, privacidad, errores y sesión inicial |
| AUTH-01 | POST /api/v1/auth/login | [MO-AUTH-01](../../specs/MO-AUTH-01-login/spec.md) | Acceso real y destino |
| AUTH-03 (sin spec propia) | POST /api/v1/auth/refresh | [MO-AUTH-03](../../specs/MO-AUTH-03-sesion/spec.md) | Custodia, rotación, concurrencia, arranque |
| AUTH-04 | POST /api/v1/auth/logout | [MO-AUTH-04](../../specs/MO-AUTH-04-logout/spec.md) | Salida real/local y limpieza |
| GRP-01 | POST /api/v1/groups | [MO-GRP-01](../../specs/MO-GRP-01-crear-grupo/spec.md) | Crear y mostrar resultado, no listar/unirse |
| CON-01 | POST /api/v1/contracts y /api/v1/groups/{gid}/contracts | [MO-CON-01](../../specs/MO-CON-01-crear-reto/spec.md) | Reto manual; ruta plana inicial, objetivo group |
| EVI-02 | POST /api/v1/contracts/{id}/evidence | [MO-EVI-02](../../specs/MO-EVI-02-evidencias/spec.md) | Enlace/foto con contexto real autorizado |
| TOR-02 | GET /api/v1/tournaments y /api/v1/groups/{gid}/tournaments | [MO-TOR-02](../../specs/MO-TOR-02-consultar-torneos/spec.md) | Lista paginada y bracket solo lectura |

Ocho operaciones y diez rutas de negocio. GET /health y GET / son auxiliares observados en Program.cs, no funcionalidades a copiar como pantalla. Alias no cuentan como dos features. PG-03 conserva coordinación histórica de acceso; cada MO-* define su interacción y destino propios.

## Secuencia de consumidor

Sesión compartida → registro/login/logout → crear grupo → reto manual → evidencia con contexto válido. Consulta de torneos después del acceso puede avanzar sin listar grupos. La navegación necesaria para cada operación se resuelve en su propio expediente; no existe una spec adicional de funciones móviles sin equivalente backend. Las pantallas ya creadas permanecen sin modificaciones en esta preparación.

## Pendientes de contrato por resolver

| ID | Hallazgo y fuente | Efecto / siguiente decisión |
|---|---|---|
| API-M01 | servers de OpenAPI usa ejemplo; guía da IP dinámica HTTPS | Confirmar base/entorno y actualizar fuente proveedor |
| API-M02 | Privacidad exige versión; falta texto/URL del aviso | Bloquea registro público correcto; confirmar 2026-10-01 |
| API-M03 | GroupSettings schema permite ≥0, servicio exige 1–5 únicos 10–200; históricos divergen en nombre/apodo | Omitir settings editables inicialmente; ratificar producto y corregir schema |
| API-M04 | CreateContractRequest no exige group_id plano; anidado usa ValidationFilter antes de asignación gid | Usar plano con group_id explícito; prueba proveedor para alias, no asumir que funciona omitiendo body group_id |
| API-M05 | Respuestas sin required completo; Error.details declara objeto pero validación puede emitir lista | Concretar nulabilidad/campos mínimos; parseo seguro, sin asumir éxito |
| API-M06 | No GET grupos/miembros/contratos/evidencias | Contexto solo recién obtenido; reinicio no recupera historial; no declarar continuidad completa |
| API-M07 | Refresh revoca antes de crear; logout global solo revoca jti actual y todos refresh | Cliente serializa y limita reintentos; confirmar garantías backend, sin prometer revocación de todos access |
| API-M08 | No claves de idempotencia ni recuperación de POST incierto | Sin reintento automático de escrituras; definir recuperación con proveedor |
| API-M09 | Foto ≤5 MB en guía, límites y enum/tipos no completos en OpenAPI; moderación y tope cambiaron | Confirmar bytes y casos/errores; no activar controles con datos de muestra |
| API-M10 | TOR-02 no garantiza datos de prueba no vacíos ni nombres de grupos | Proveedor prepara fixture autorizada; UI no inventa torneos/nombres |

No modificar backend para resolverlos por esta instrucción; registrar respuesta del equipo y actualizar versión/commit cuando cambie. No todos bloquean todas las tareas: privacidad bloquea registro; settings editables quedan fuera; lecturas pueden manejar campos faltantes con estado seguro. Valores de servidor observados no equivalen a ratificación general de reglas por el usuario.

## Evidencia y límites de esta preparación

Lectura del PDF, archivos recibidos, árbol/historial/specs/DTO/endpoints/servicios y CI remoto mediante GitHub. Sin ejecución de build/tests, solicitudes de negocio, cambios Kotlin/Gradle/XML, instalaciones, borrado de pantallas, commit o push. Todas las specs nuevas permanecen abiertas; aceptación humana y compatibilidad Android pendientes.
