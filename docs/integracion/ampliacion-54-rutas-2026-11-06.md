# Ampliacion de 36 a 54 rutas - 2026-11-06

Estado: parcial, contrato actualizado requerido. Autorizacion: implementacion y pruebas locales, sin push y sin modificar backend.

## Fuentes y discrepancia

La lista recibida del usuario define 54 rutas: 52 API y dos de servicio. La copia leida de Downloads/backend/docs/API-movil.md declara v0.6.0, 36 rutas; su seccion 6 dice que Google, verificacion de correo, editar/cancelar retos y push no existen. Su api/openapi.yaml conserva v0.6.0. No hay cuerpos/respuestas verificables para las 18 incorporaciones. Intentos de lectura remota de la guia/OpenAPI y servidor no disponibles; no sustituir el contrato por payloads imaginados.

## Rutas nuevas respecto al consumidor existente

| Metodo | Ruta | Sesion | Recorrido previsto |
|---|---|---|---|
| GET | / | No | Informacion del servicio |
| GET | /health | No | Estado del servicio |
| POST | /api/v1/auth/google | No | Acceso con Google |
| POST | /api/v1/auth/verify-email | No | Confirmacion de correo |
| POST | /api/v1/auth/verify-email/resend | Si | Reenviar verificacion |
| POST | /api/v1/auth/forgot-password | No | Recuperar acceso |
| POST | /api/v1/auth/reset-password | No | Establecer nueva contrasena |
| GET | /api/v1/users/{id} | Si | Perfil de un miembro |
| POST | /api/v1/users/me/password | Si | Seguridad de la cuenta |
| POST | /api/v1/users/me/consent | Si | Aviso de privacidad vigente |
| GET | /api/v1/groups/{gid}/audit | Si, admin | Historial del grupo |
| PATCH | /api/v1/contracts/{id} | Si, autor y sin aceptacion | Editar reto |
| DELETE | /api/v1/contracts/{id} | Si | Cancelar con confirmacion |
| DELETE | /api/v1/notifications/{id} | Si | Eliminar con confirmacion |
| PUT | /api/v1/devices/me | Si | Registrar dispositivo para push |
| DELETE | /api/v1/devices/me | Si | Retirar dispositivo al salir |
| GET | /api/v1/devices/me/preferences | Si | Leer preferencias |
| PATCH | /api/v1/devices/me/preferences | Si | Guardar preferencias |

## Cambios en rutas existentes

CON-06, VOT-02 y VOT-04: cabecera Idempotency-Key obligatoria segun mensaje. Implementada con UUID generado antes de la llamada autorizada. Tests HTTPS existentes ampliados para exigir cabecera y formato, sin eliminar comprobaciones de ruta, token ni numero de solicitudes. No se activa repeticion automatica de escrituras. Vigencia/reutilizacion tras respuesta perdida requiere contrato actualizado.

PATCH users/me: confirmar catalogo/validacion de avatar_id; el consumidor previo solo modifica apodo. GET tournaments: reconciliar group_id y consulta global con el alias por grupo. Correo y FCM: distinguir operacion aceptada por API de entrega real, sin anunciar correo enviado o push operativo cuando proveedor no esta configurado.

## Plan y tareas pendientes

1. Recibir docs/API-movil.md y api/openapi.yaml actualizados (campos, respuestas, codigos, permisos, paginacion y versiones). Fijar fuente y reconciliar cambios en las 36 rutas existentes.
2. Crear specs por IDs oficiales, plan y tareas para cada nueva operacion; actualizar matriz sin declarar cobertura de rutas aun no implementadas.
3. Implementar DTO/mappers, consumidores y permisos; luego pantallas, estados carga/error/vacio y navegacion. No exponer tokens como campos normales de UI ni guardar contrasenas en estado persistido.
4. Google requiere cliente OAuth autorizado/configuracion Android; FCM requiere configuracion cliente Android, permisos y asociacion/desasociacion por cuenta. La cuenta de servicio Firebase y contrasena Gmail son del servidor y nunca del APK.
5. Pruebas por operacion: entradas invalidas sin red, HTTPS y contrato exacto, errores/permisos, aislamiento de cuentas, cancelacion/recreacion, doble accion y respuesta perdida; no escribir en produccion para probar.
6. Ejecutar pruebas JVM, build, Lint/Detekt e instrumentacion aislada. Registrar bloqueos y resultados actuales. Sin push.

## Validacion de este corte

- Detekt directo sobre fuentes main: salida 0, sin incidencias.
- Matriz existente: 33 operaciones, tres categorias e identidades exactas correctas. Esto no certifica 54 rutas integradas.
- Intento JVM focalizado AcceptChallengeApiTest, VOT_02 y VOT_04: no ejecutado, Gradle no conecta con su daemon local.
- No se reutilizan los XML antiguos como prueba de este candidato. Sin aceptacion de proveedor ni instrumentacion nueva.
