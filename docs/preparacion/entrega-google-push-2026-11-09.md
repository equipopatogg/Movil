# Entrega movil Google/push — 2026-11-09

Autorizacion del usuario: subir el trabajo movil aunque el backend siga pendiente. Destino: equipopatogg/Movil, rama nueva `codex/push-google-notificaciones-20261109`. Base local y remota de integracion: `14579926b37a10d7718febd55b22bd115f3fd63b`. Sin fusion de main ni cambios backend.

Contenido: consumidores de cuenta/perfil/mantenimiento, acceso con Google, FCM Android y diagnostico de registro, permiso/canal, renovacion/baja, mensajes de errores, idempotencia, pruebas y documentacion local acumulada. No se declara aceptacion de todas las rutas ni proveedor. Las 33 operaciones de la matriz canónica se conservan; las adiciones tienen regresiones registradas.

Validacion: informes existentes del usuario revalidados con la matriz vigente: 361 pruebas JVM sin fallos, errores, omisiones, faltantes ni categorias incorrectas; coverage/build PASS. No se presenta esa lectura como una nueva ejecucion Gradle. Lint/Detekt y build de la ejecucion del usuario se conservan con sus fuentes; instrumentacion Android, entrega real push, destinos detallados y Google en servidor siguen abiertos.

Exclusiones: configuracion local `app/google-services.json` (conservada en el equipo), cuentas de servicio Firebase, secretos, APK, caches, informes generados y referencias privadas. Al clonar se debe obtener la configuracion Android publica y colocarla en app/google-services.json. No desactivar los escaneos para publicar configuracion de servidor.

Limites de entrega observados: el conector GitHub tiene permiso `pull=true`, `push=false` sobre este repositorio. Git local no resuelve github.com desde esta sesion. No se ha creado un commit ni publicado una rama nueva. La rama remota anterior codex/integracion-completa-movil sigue en la base indicada.

Se prepara `scripts/push-reviewed-mobile.ps1` para la terminal normal del usuario. Verifica destino, base, ausencia de staging previo, lista exacta y SHA-256 de cada archivo revisado; crea rama y commit con identidad equipopatogg; hace push sin force ni fusion. Usa artifacts/push-review/candidate.json, un manifiesto local ignorado por Git. Si algun archivo cambia despues de la revision, se detiene. Si falla solo la red despues del commit, reintentar el git push que muestra el script, sin recrear commits.

No considerar esta entrega completada hasta comprobar la rama/commit publicados en GitHub. Los pendientes Firebase y Google del backend se conservan explicitos.
