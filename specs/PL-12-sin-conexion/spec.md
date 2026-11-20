# PL-12 — Modo sin conexión con Room

Fecha: 2026-11-16. Estado: **APROBADA POR EL PO E IMPLEMENTADA. Compila con Detekt y Lint sin advertencias; pruebas JVM (unitarias, integración y seguridad) en verde el 2026-11-19 (344 casos)**. Sprint planning: "Room (SQLite) y sincronización sin conexión" (requisito del docente). Sin cambios en el backend.

## Alcance acordado
Solo lectura sin conexión. Las acciones que mueven puntos, fianzas o votos **no** se guardan para enviarlas después: repetirlas sin saber el estado del servidor podría cobrar dos veces o votar fuera de tiempo. Se mantiene el mensaje actual de "Se perdió la conexión…" para esas acciones.

## Requisitos
- RF1: Cada consulta `GET /api/v1/...` que responde `200` con JSON se guarda en Room (tabla `snapshots`, base versión 2 con migración automática desde la 1) con clave = ruta + consulta y dueño = `sub` del token de acceso.
- RF2: Si la misma consulta falla por red, se devuelve lo guardado de **esa misma cuenta**. Otra cuenta en el mismo teléfono nunca lo ve.
- RF3: Mientras se muestra algo guardado aparece el aviso "Sin conexión: ves lo último guardado…". Desaparece con la siguiente respuesta del servidor.
- RF4: No se guardan escrituras, `auth/`, `devices/`, `check-nickname`, encabezados ni tokens; tampoco respuestas de más de 512 KB.
- RF5: Al cerrar sesión se borra toda la tabla. La base es privada de la app y no entra en respaldos (`allowBackup=false`).

## Criterios
| ID | Resultado |
|---|---|
| PL-12-AC1 | Sin internet, Inicio, grupos, retos, avisos y ranking muestran lo último visto con el aviso. |
| PL-12-AC2 | Otra cuenta no ve lo guardado y cerrar sesión lo borra. |
| PL-12-AC3 | Crear, aceptar o votar sin internet muestra el error de conexión y no se repite solo. |

## Permisos (sprint planning: "Pantallas de permisos y manejo de denegados")
- Micrófono: el dictado usa el reconocedor del sistema (`RecognizerIntent`), que pide su propio permiso; la app no declara `RECORD_AUDIO`. Si no hay reconocedor, PL-05 ofrece escribir a mano.
- Fotos: se usa el selector del sistema; no se pide permiso de cámara ni de almacenamiento.
- Notificaciones: PL-10 (se pide una vez y el perfil abre los ajustes si se negó).
