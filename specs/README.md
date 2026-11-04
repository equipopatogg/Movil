# Specs activas del móvil

Actualizado 2026-11-04. Se mantienen 12 expedientes: PG-00/01/02 de base técnica, PG-03 de coordinación del acceso y ocho MO-* correspondientes a operaciones existentes del backend. Los borradores de pantallas adelantadas y la propuesta adicional de alcance se retiraron del expediente activo; no son dependencias ni tareas del proyecto.

| Expediente | Estado |
|---|---|
| [MO-AUTH-01 — Inicio de sesión](MO-AUTH-01-login/spec.md) | Implementación verificada en flujo positivo y rechazo 401; cierre integral pendiente |
| [MO-AUTH-03 — Custodia y renovación de sesión](MO-AUTH-03-sesion/spec.md) | Cifrado, rotación y arranque verificados; escenarios restantes pendientes |
| [MO-AUTH-04 — Cierre de sesión](MO-AUTH-04-logout/spec.md) | Logout y nuevo ingreso verificados; escenarios restantes pendientes |
| [MO-CON-01 — Creación manual de reto](MO-CON-01-crear-reto/spec.md) | Bloqueada por backend: 503 MODERATION_UNAVAILABLE |
| [MO-EVI-02 — Envío de evidencias](MO-EVI-02-evidencias/spec.md) | Bloqueada: sin contrato real confirmado para verificar envío |
| [MO-GRP-01 — Creación de grupo](MO-GRP-01-crear-grupo/spec.md) | Creación real verificada; negativos y recuperación de listado pendientes |
| [MO-TOR-02 — Consulta de torneos y bracket](MO-TOR-02-consultar-torneos/spec.md) | Consulta vacía verificada; datos/bracket/paginación reales pendientes |
| [MO-USR-01 — Registro de usuario](MO-USR-01-registro/spec.md) | Registro real verificado; negativos completos y aviso definitivo pendientes |
| [PG-00 — Integridad del Wrapper y evidencias de seguridad en CI](PG-00-integridad-ci/spec.md) | Abierta; sin aceptación de integración |
| [PG-01 — Ejecución instrumentada y cobertura Android verificable](PG-01-pruebas-android/spec.md) | Abierta; sin aceptación de integración |
| [PG-02 — Corregir compatibilidad nativa de 16 KB y verificar release local](PG-02-release-16kb/spec.md) | Abierta; sin aceptación de integración |
| [PG-03 — Registro, login y ciclo de sesión PactoGG](PG-03-acceso-cuenta/spec.md) | Abierta; sin aceptación de integración |

Cada expediente conserva spec.md, plan.md, tasks.md y checklists/checklist.md. Las ocho operaciones son registro, login, renovación de sesión, logout, creación de grupo, creación manual de reto, envío de evidencia y consulta de torneos. El backend tiene refresh en código/OpenAPI aunque carece de spec AUTH-03 independiente: su adaptación móvil es necesaria para consumir la sesión existente.

Orden: entradas específicas y base técnica → sesión compartida → registro/login/logout → grupo/reto/evidencia y consulta de torneos según dependencias. PG-03 coordina; no duplica implementaciones de MO-*. Navegación, errores y estados propios de cada operación pertenecen a su mismo expediente, sin agregar una funcionalidad ajena al backend.

[Correspondencia de contratos y dudas](../docs/integracion/alineacion-backend-2026-11-04.md). [Evaluación y Play](../docs/preparacion/checklist-evaluacion-movil.md). Implementación local autorizada y controles debug correctos; ver [evidencia](../docs/integracion/verificacion-consumidor-2026-11-04.md). Sin instalación en dispositivo, commit o push. UI existente conservada. Fuentes privadas de recordatorio no se enlazan ni usan desde el proyecto.

## Incorporación en Git — 2026-11-04

Copia previa verificada y preparación de ramas dependientes: base visual → acceso/sesión → grupos → consulta de torneos → implementación pendiente de retos/evidencias. Se conserva main publicado hasta revisión; commits locales no equivalen a cierre integral. [Registro de entrega](../docs/preparacion/entrega-por-ramas.md).
