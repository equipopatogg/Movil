# Push Android — 2026-11-08

Estado: implementación local parcial, validación Android/proveedor abierta. Sin modificaciones backend, commit ni push. Google sigue pendiente de habilitación en el servidor.

## Resultado posterior de Gradle — ejecución del usuario

Actualización posterior: el usuario confirma aviso interno presente y push ausente. Se añade estado visible de conexión/registro en preferencias, sin tokens ni secretos, y reintento explícito del registro fallido. No repetir retos para comprobar registro. Hay tres regresiones nuevas del coordinador, con 15 JVM directas correctas en coordinador/sesión. La ejecución Gradle de 356 casos descrita debajo es anterior a este cambio y no valida la nueva UI. Pendientes build/Lint/Android actuales, lectura del estado en la cuenta receptora y envío real.

2026-11-08: BUILD SUCCESSFUL; informes locales artifacts/android-ci/all.json, coverage.json y build.json en PASS. Los XML actuales suman 356 pruebas, cero fallos/errores/omisiones. Las 14 pruebas push pasan: PushRegistrationTest (9), PushSessionTest (3), PushLogoutApiTest (2). Los dos casos HTTP antes bloqueados se ejecutaron correctamente en el entorno del usuario. Lint y Detekt completados; matriz correcta, sin faltantes ni categorías incorrectas. T2/T4 verificadas en este alcance local; sin instrumentación Android ni entrega real acreditadas.

La captura termina durante cyclonedxDirectBom; existen bom.json y bom.xml, pero no acredita todavía la terminación exitosa de esa tarea. El aviso de resolución de releaseRuntimeClasspath durante configuración corresponde a rendimiento. Los resultados de intentos anteriores debajo se conservan como historial, superados por esta ejecución donde aplica.

## Alcance y fuentes

Autorización expresa del usuario para continuar push. Contrato: `referencias/backend-v08/API-movil.md`, sección Push, y OpenAPI DEV-01/02/03. El texto antiguo «push nativo todavía no» es antecedente; la sección de integración v0.8 define token, canal y baja. [Spec PG-09](../../specs/PG-09-push/spec.md).

Referencias oficiales consultadas el 2026-11-08: [configuración y permisos FCM](https://firebase.google.com/docs/cloud-messaging/android/get-started), [versiones Android](https://firebase.google.com/support/release-notes/android). SDK Messaging fijado en 25.0.1, módulo principal, sin Analytics ni Firebase Auth. No se afirma que sea la última versión.

## Implementación

- Configuración Android pública del JSON existente convertida en recursos Gradle. Sin plugin adicional Google Services, cuentas de servicio ni secretos SMTP. Sin JSON el resto de la aplicación conserva su acceso habitual.
- Servicio FCM no exportado; canal `pactogg_default`, icono monocromático y permiso contextual Android 13+ en preferencias. La denegación no cambia las preferencias remotas ni borra avisos del centro.
- Registro con sesión y permiso al restaurar/iniciar sesión, volver al primer plano y rotar token. Deduplización por cuenta/token durante el proceso. Retiro del token anterior al rotar y del actual antes de `auth/logout`; nunca DELETE sin token.
- Baja y eliminación del token Firebase con tiempos acotados. Se desactiva auto-init y se cancela el aviso local. Un fallo no impide borrar la sesión local y se informa que el cierre remoto no quedó confirmado. Sin token conocido no se declara baja remota confirmada.
- Los callbacks FCM esperan una operación acotada. Sin logs de tokens/payloads ni reintentos en bucle.
- El servicio muestra texto genérico y privado. PendingIntent explícito/inmutable. Pulsarlo abre avisos reales después de restaurar/iniciar sesión; soporta Activity existente y espera operaciones activas. Sin acciones automáticas ni URLs recibidas.

## Evidencia del candidato

Informes directos: `artifacts/push-check/`, ignorados por Git. Kotlin 2.4.0 en caché y JUnit 4.13.2: se compilan las fuentes actuales PushRegistration/SessionManager/SessionMutations usando clases anteriores del proyecto como dependencias. Comprobación acotada, sin equivaler a compilación Android/Gradle de todo el árbol.

| Comprobación | Resultado y límite |
| --- | --- |
| PushRegistrationTest + PushSessionTest | 12 JVM directos correctos: permiso/sesión/token, rotación, cuenta, concurrencia, errores de baja, orden hook/logout y cierre sin Firebase. |
| PushLogoutApiTest | Dos intentos fallidos por `SocketException: Permission denied: getsockopt` frente al fixture HTTPS local; no acredita orden HTTP real. Casos obligatorios para la siguiente ejecución normal. |
| Detekt CLI 1.23.8 | Correcto sobre todo app/src/main/java, sin supresiones nuevas. |
| Matriz canónica | 33 operaciones MO y categorías exactas correctas; push como regresiones adicionales obligatorias. No acredita las 54 rutas. |
| Gradle Wrapper | No inicia por acceso denegado al lock global. |
| Gradle con caché alternativa local | --version correcto; tareas bloqueadas por `Could not connect to the Gradle daemon`. Sin resultados Gradle nuevos. |
| Android/ADB | No conecta al daemon ADB. Sin instalación ni entrega real acreditadas. |

Los 342 casos anteriores no verifican estos cambios. La compuerta ahora exigirá las nuevas regresiones; no se relajaron controles para aceptar los informes anteriores.

## Pendientes y siguiente comprobación

Revisión posterior de Lint del usuario (2026-11-08): cinco errores, dos por constantes de ajustes que requieren API 26 y tres apariciones del mismo recurso generado project_id sin referencia estática. Corregidos con rama SDK >=26 y alternativa de detalles de aplicación para API 24-25; comprobación explícita del project_id al inicializar Firebase. Detekt directo correcto después del cambio. Lint Gradle nuevo no pudo iniciar por acceso denegado al lock global; pendiente reprueba. Sin baseline, supresiones ni aumento de minSdk.

Ejecutar desde esta carpeta, en la terminal normal de Android Studio:

```powershell
.\scripts\check-android.ps1 -UseWindowsTrust
```

Después, en emulador aislado con Google Play: permiso concedido/denegado, canal desactivado, restauración, rotación, cambio de cuenta y logout offline. No alterar cuentas del teléfono. Con proveedor disponible, probar PUT/DELETE y entrega con app abierta, segundo plano y proceso terminado. No se inspeccionó ni modificó la configuración del servidor.

Pendiente navegación directa a reto/grupo/compromiso usando los IDs documentados, con consultas y autorización actuales. El destino actual es el centro de avisos. Preferencias DEV-03 conservan su consumidor y requieren validación integral.

Limitaciones: en segundo plano los mensajes con bloque notification pueden ser mostrados por el SDK con texto del servidor; el texto genérico del servicio no lo reemplaza. Usar contenido remoto no sensible y probar ese caso. Si la baja falla offline no se promete desvinculación inmediata: se intenta invalidar el token Firebase y se informa que el cierre remoto no quedó confirmado.
