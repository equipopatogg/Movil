# Specs activas del móvil

Actualizado 2026-11-04. Se mantienen 23 expedientes: PG-00/01/02 de base técnica, PG-03 de coordinación del acceso, ocho MO-* de la integración anterior y once MO-* nuevas confirmadas en Sistema.docx enviado por backend. Los borradores de pantallas adelantadas y la propuesta adicional de alcance se retiraron del expediente activo; no son dependencias ni tareas del proyecto.

| Expediente | Estado |
|---|---|
| [MO-AUTH-01 — Inicio de sesión](MO-AUTH-01-login/spec.md) | Implementación verificada en flujo positivo y rechazo 401; cierre integral pendiente |
| [MO-AUTH-03 — Custodia y renovación de sesión](MO-AUTH-03-sesion/spec.md) | Cifrado, rotación y arranque verificados; escenarios restantes pendientes |
| [MO-AUTH-04 — Cierre de sesión](MO-AUTH-04-logout/spec.md) | Logout y nuevo ingreso verificados; escenarios restantes pendientes |
| [MO-CON-01 — Creación manual de reto](MO-CON-01-crear-reto/spec.md) | Creación real verificada tras corrección AWS; criterios restantes pendientes |
| [MO-EVI-02 — Envío de evidencias](MO-EVI-02-evidencias/spec.md) | Enlace/foto sintética verificados; ajuste de estado y criterios restantes pendientes |
| [MO-GRP-01 — Creación de grupo](MO-GRP-01-crear-grupo/spec.md) | Creación real verificada; negativos y recuperación de listado pendientes |
| [MO-TOR-02 — Consulta de torneos y bracket](MO-TOR-02-consultar-torneos/spec.md) | Consulta vacía verificada; datos/bracket/paginación reales pendientes |
| [MO-USR-01 — Registro de usuario](MO-USR-01-registro/spec.md) | Registro real verificado; negativos completos y aviso definitivo pendientes |
| [PG-00 — Integridad del Wrapper y evidencias de seguridad en CI](PG-00-integridad-ci/spec.md) | Abierta; sin aceptación de integración |
| [PG-01 — Ejecución instrumentada y cobertura Android verificable](PG-01-pruebas-android/spec.md) | Abierta; sin aceptación de integración |
| [PG-02 — Corregir compatibilidad nativa de 16 KB y verificar release local](PG-02-release-16kb/spec.md) | Abierta; sin aceptación de integración |
| [PG-03 — Registro, login y ciclo de sesión PactoGG](PG-03-acceso-cuenta/spec.md) | Abierta; sin aceptación de integración |

## Once operaciones nuevas confirmadas en el documento recibido

Confirmación documental de alcance y posterior integración autorizada. Casos positivos de las once operaciones comprobados en teléfono; [candidato/límites](../docs/integracion/integracion-social-2026-11-04.md). [Correspondencia con cada spec backend](../docs/integracion/specs-confirmadas-sistema-2026-11-04.md).

| Expediente | Estado móvil |
|---|---|
| [MO-USR-02 — Consultar perfil](MO-USR-02-consultar-perfil/spec.md) | Integrada y caso positivo real probado; criterios restantes pendientes |
| [MO-USR-03 — Editar apodo/avatar](MO-USR-03-editar-perfil/spec.md) | Integrada y caso positivo real probado; criterios restantes pendientes |
| [MO-USR-06 — Consultar disponibilidad del apodo](MO-USR-06-disponibilidad-apodo/spec.md) | Integrada y caso positivo real probado; criterios restantes pendientes |
| [MO-GRP-02 — Listar mis grupos](MO-GRP-02-listar-grupos/spec.md) | Integrada y caso positivo real probado; criterios restantes pendientes |
| [MO-GRP-03 — Consultar grupo](MO-GRP-03-detalle-grupo/spec.md) | Integrada y caso positivo real probado; criterios restantes pendientes |
| [MO-MEM-01 — Unirse con código](MO-MEM-01-unirse-grupo/spec.md) | Integrada y caso positivo real probado; criterios restantes pendientes |
| [MO-MEM-02 — Consultar miembros](MO-MEM-02-listar-miembros/spec.md) | Integrada y caso positivo real probado; criterios restantes pendientes |
| [MO-CON-02 — Tablero de retos](MO-CON-02-listar-retos/spec.md) | Integrada y caso positivo real probado; criterios restantes pendientes |
| [MO-CON-03 — Consultar detalle del reto](MO-CON-03-detalle-reto/spec.md) | Detalle integrado y caso positivo real probado; criterios restantes pendientes |
| [MO-CON-06 — Aceptar reto](MO-CON-06-aceptar-reto/spec.md) | Integrada y caso positivo real probado; criterios restantes pendientes |
| [MO-CON-07 — Rechazar reto](MO-CON-07-rechazar-reto/spec.md) | Integrada y caso positivo real probado; criterios restantes pendientes |

Orden propuesto de implementación: perfil de lectura → lista/detalle de grupos → unión/miembros → tablero/detalle de retos → aceptar/rechazar; disponibilidad de apodo y edición de perfil después de consulta, con catálogo de avatar pendiente. Cada operación avanza solo con sus entradas resueltas.

Cada expediente conserva spec.md, plan.md, tasks.md y checklists/checklist.md. Las ocho operaciones son registro, login, renovación de sesión, logout, creación de grupo, creación manual de reto, envío de evidencia y consulta de torneos. El backend tiene refresh en código/OpenAPI aunque carece de spec AUTH-03 independiente: su adaptación móvil es necesaria para consumir la sesión existente.

Orden: entradas específicas y base técnica → sesión compartida → registro/login/logout → grupo/reto/evidencia y consulta de torneos según dependencias. PG-03 coordina; no duplica implementaciones de MO-*. Navegación, errores y estados propios de cada operación pertenecen a su mismo expediente, sin agregar una funcionalidad ajena al backend.

[Correspondencia de contratos y dudas](../docs/integracion/alineacion-backend-2026-11-04.md). [Evaluación y Play](../docs/preparacion/checklist-evaluacion-movil.md). Implementación local autorizada y controles debug correctos; ver [evidencia](../docs/integracion/verificacion-consumidor-2026-11-04.md). La prueba física posterior y merges a main constan en sus informes; el ajuste actual sigue local, sin commit/push. UI existente conservada. Fuentes privadas de recordatorio no se enlazan ni usan desde el proyecto.

## Incorporación en Git — 2026-11-04

Copia previa verificada y preparación de ramas dependientes: base visual → acceso/sesión → grupos → consulta de torneos → implementación pendiente de retos/evidencias. Se conserva main publicado hasta revisión; commits locales no equivalen a cierre integral. [Registro de entrega](../docs/preparacion/entrega-por-ramas.md).


## Ajuste acotado de contratos nuevos

Antecedente del ajuste acotado: MO-EVI-02 añadió lectura auxiliar de detalle tras envío. La solicitud posterior crea las once specs confirmadas, incluida MO-CON-03 que reutilizará ese avance; sus recorridos ya están integrados y probados en casos positivos; aceptación integral pendiente. [Comparación actual](../docs/integracion/revision-contratos-nuevos-2026-11-04.md). La preparación Git anterior es histórica; los cinco PR ya están fusionados.


USR-03: edición de apodo verificada; selección de avatar pendiente de catálogo. Las casillas integrales permanecen abiertas según evidencia por criterio, aunque T2/T3 de las otras operaciones acreditan implementación local.
