# PL-09 — Avatar, eliminar cuenta, reportar y bloquear

Fecha: 2026-11-15. Estado: **APROBADA POR EL PO; ESCRITA, FALTA COMPILAR Y PROBAR EN EL CELULAR**. Backend ya desplegado: USR-03 (`PATCH /users/me` con `avatar_id`), USR-04 (`DELETE /users/me`), REP-01 (`POST /reports`) y REP-05 (`POST /users/{id}/block`). Historias: ID 22/23 (avatar), ID 29 (eliminar cuenta), ID 149 (reportar), ID 153 (bloquear) e ID 98 (celebración del reto cumplido).

## Requisitos
- RF1 **Avatar**: en el perfil se elige uno de tres colores (violeta, cian, naranja). Se guarda en el servidor y se usa en el perfil y en la lista de miembros. `default` se muestra naranja.
- RF2 **Eliminar cuenta**: en el perfil, "Eliminar mi cuenta" pide la contraseña (vacía si la cuenta es de Google) y escribir `ELIMINAR`. Solo entonces se llama al servidor; al confirmar (204) se borra la sesión del teléfono. Contraseña incorrecta muestra el error y no borra nada.
- RF3 **Reportar miembro**: en la lista de miembros, "Reportar" pide un motivo (ofensivo, trampa, spam, otro) y lo envía con el grupo actual. `ALREADY_REPORTED` tiene mensaje propio.
- RF4 **Bloquear**: "Bloquear" pide confirmación y avisa que ya no podrán retarse.
- RF5 **Celebración**: el resultado de un reto cumplido muestra "¡Reto cumplido!" y la recompensa; si hubo trampa, lo dice sin adornos.
- RF6: Ninguna de estas acciones se reintenta sola ni se ejecuta sin confirmación explícita.

- RF-FOTO (2026-11-17, pedido del PO): "Usar una foto" en el perfil elige una imagen de la galería, la prepara igual que una evidencia (JPEG ≤ 5 MB, sin metadatos) y la sube a `POST users/me/avatar` (backend USR-07). Si la moderación la rechaza se muestra el mensaje de `CONTENT_REJECTED`. La foto aparece en el perfil y en la lista de miembros; si no carga, se ve el círculo de color. Elegir un color quita la foto.
- RF-FECHA (2026-11-17): crear reto y crear torneo eligen fecha y hora con el calendario y el reloj del sistema (solo fechas futuras) en lugar de escribirlas.

- RF-DESBLOQUEAR (2026-11-18): en Miembros, "Desbloquear" llama a `DELETE users/{id}/block` (REP-06); el servidor responde 204 aunque no estuviera bloqueado, así que no hace falta saber a quién bloqueaste.
- RF-EVIDENCIA (2026-11-18): en la revisión del jurado, cada evidencia tiene "Reportar evidencia" con motivo (ofensivo, sexual, trampa, otro) → `POST reports` con `target_type=evidence`; la revisa el administrador del grupo.

## Criterios
| ID | Resultado |
|---|---|
| PL-09-AC1 | Cambiar el avatar se refleja en el perfil y lo ven los demás en miembros. |
| PL-09-AC2 | Eliminar la cuenta exige `ELIMINAR`, borra la sesión local y ya no se puede entrar. |
| PL-09-AC3 | Reportar y bloquear llegan al servidor con el grupo, la persona y el motivo correctos. |
| PL-09-AC4 | Avatar o motivo desconocidos no llegan a la red. |
| PL-09-AC6 | Subir una foto la manda como multipart y el perfil muestra `avatar_url` (`PlayApiTest.PL_09_avatarPhotoUploadsMultipartAndMapsPhotoUrl`). |
| PL-09-AC7 | Desbloquear y reportar evidencia usan las rutas del servidor (`PlayApiTest.PL_09_unblockAndReportEvidenceUseServerRoutes`). |
