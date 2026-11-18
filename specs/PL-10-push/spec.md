# PL-10 — Notificaciones push

Fecha: 2026-11-16. Estado: **APROBADA POR EL PO; ESCRITA, FALTA COMPILAR Y PROBAR EN EL CELULAR**. Backend: DEV-01 `PUT /api/v1/devices/me`, DEV-02 `DELETE /api/v1/devices/me?fcm_token=` y el envío FCM de NOT-05 (ya desplegados). Origen: la rama sin fusionar `codex/push-google-notificaciones-20261109`; se trae el registro y la recepción, adaptados a la app actual. Sprint planning: ID 69 y ID 143.

## Requisitos
- RF1: La configuración de Firebase sale de `app/google-services.json` (no versionado). Gradle solo lee la parte pública del cliente Android `com.pactogg.app` y rechaza una cuenta de servicio. Sin el archivo la app compila y muestra "no configurado".
- RF2: Al iniciar sesión, con permiso de notificaciones, la app obtiene el token FCM y lo registra con `PUT devices/me` (`fcm_token`, `platform=android`, `app_version`). Un token rotado reemplaza al anterior: primero se borra el viejo y después se registra el nuevo.
- RF3: Al cerrar sesión se borra el token en el servidor (`DELETE devices/me`) y en Firebase antes de limpiar la sesión. Si no hay internet, el cierre local sigue funcionando.
- RF4: En Android 13 o superior, la app pide el permiso una sola vez al entrar. Si se niega, el perfil explica cómo activarlo y abre los ajustes del sistema.
- RF5: Con la app abierta, el aviso se muestra con el título y texto que manda el servidor en el canal `pactogg_default`. En segundo plano lo muestra Android en el mismo canal. El contenido es privado en la pantalla bloqueada.
- RF6: Tocar el aviso abre la app en Notificaciones.
- RF7: El token vive solo en memoria y no se escribe en logs. No se usa Firebase Analytics.

## Criterios
| ID | Resultado |
|---|---|
| PL-10-AC1 | Con sesión y permiso, el teléfono queda registrado y el perfil lo dice. |
| PL-10-AC2 | Un reto recibido llega como aviso y al tocarlo abre Notificaciones. |
| PL-10-AC3 | Cerrar sesión borra el dispositivo en el servidor; ya no llegan avisos. |
| PL-10-AC4 | Sin permiso, token inválido o sesión cambiada no se registra nada. |

## Dependencia externa
El servidor necesita `Firebase__ProjectId` y `Firebase__ServiceAccountJson` en SSM (`scripts/configure-firebase.sh` del backend). Sin eso registra el dispositivo pero no envía.
