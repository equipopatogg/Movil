# Verificación física y API real — 2026-11-04

Estado: **PARCIAL; bloqueo del proveedor confirmado**. Usuario autorizó instalar en su teléfono, crear cuenta QA y comprobar las funciones. Se creó una sola cuenta sintética, se usó el mismo usuario en los reintentos y se conservaron sus datos de prueba. Credenciales guardadas fuera del proyecto/Git en el directorio local de QA del equipo, sin contraseñas o tokens en este informe.

## Resultado por operación

| Operación / control | Resultado observado |
|---|---|
| Registro | Cuenta creada por formulario Compose, consentimiento explícito; identidad y acceso posterior confirmados. La corrida inicial no pasó completa por interferencia de Samsung Pass después del registro |
| Login | PASS con API real; PASS de rechazo 401 INVALID_CREDENTIALS con contraseña incorrecta y acceso posterior correcto |
| Grupo | PASS: se crearon grupos de QA y se recibieron ID/configuración/puntos del servidor |
| Crear reto | FAIL proveedor: POST /api/v1/contracts devuelve 503 MODERATION_UNAVAILABLE |
| Evidencia enlace/foto | BLOQUEADAS: no se obtuvo contrato confirmado; no se inventaron IDs ni se omitió moderación |
| Torneos | PASS de consulta con respuesta vacía (0 resultados); bracket con datos no verificado |
| Refresh API | PASS: entrega tokens nuevos y rechaza el refresh anterior con 401 |
| Arranque con sesión expirada | PASS en teléfono: se forzó solamente la expiración local de la cuenta QA, se terminó el proceso y SessionManager renovó al iniciar, comprobando cambio de hash del refresh sin registrarlo |
| Logout | PASS en interfaz y API; desaparece la sesión, permite nuevo acceso y no se repone cuenta previa |
| Keystore / cifrado | PASS de dos pruebas físicas: archivo cifrado/roundtrip/limpieza y rechazo por corrupción o clave ausente; carpeta/alias de test aislados |
| Acceso / privacidad | Dos pruebas físicas correctas antes de crear la cuenta: login vacío no omite acceso y aviso provisional puede abrirse |

El teléfono quedó con el APK final instalado y la cuenta QA iniciada. No se cambiaron ajustes globales del teléfono ni otras cuentas. Se canceló la sugerencia de Samsung Pass; solo la ventana de la prueba deshabilita autofill para evitar guardar credenciales sintéticas.

## Error que debe revisar backend

- Método/ruta: POST `/api/v1/contracts`.
- HTTP: **503**.
- code: **MODERATION_UNAVAILABLE**.
- request_id: `8cb5f9fd4a0feda16ff42d69a2f5dafd`.
- Datos QA: grupo creado por el mismo usuario, juego de texto libre, condición y recompensa sin dinero, fecha futura y fianza devuelta por ese grupo.
- El servicio de moderación del proveedor no confirmó el texto. La fuente AwsModerationService convierte excepciones de AWS Comprehend en este código; el motivo específico requiere registros del servidor. No se puede concluir desde Android si son permisos, disponibilidad, configuración o cuenta de AWS.
- No se modificó backend, IAM, despliegue ni reglas de moderación. Tampoco se transformó el error en éxito local.

## Correcciones móviles realizadas durante la revisión

Se conserva y sanitiza request_id junto con HTTP/code para diagnóstico, sin cuerpos, tokens o mensajes arbitrarios. MODERATION_UNAVAILABLE/STORAGE_UNAVAILABLE tienen mensajes claros. Se conserva busy durante cambios de identidad para evitar habilitar formularios antes de terminar login/logout. No cambia diseño, versión o dependencias.

LiveBackendTest es **opt-in**: sin pactogg.live=true omite las pruebas de producción. Usa solo cuenta QA indicada y rechaza reemplazar otra cuenta del teléfono. La selección de foto no fue automatizada: el camino de envío preparaba imagen sintética propia, pero quedó bloqueado antes por creación de contrato. No se accedió a fotos personales.

## Evidencia local y candidato

Logs ignorados bajo artifacts/ en raíz del espacio:

- live-backend-device-final.log: recorrido real, pasos positivos y fallo 503; resultado global FAIL (no ocultarlo).
- qa-auth-final-device.log: interfaz logout, credencial inválida y login válido, **OK (1 test)** en 12.682 s.
- qa-expiry-final-prepare.log y qa-expiry-final-verify.log: preparación controlada y arranque con renovación, **OK (1 test)** cada uno.
- qa-cifrado-final.log: **OK (2 tests)** en 1.398 s.
- telefono-final-build.log: **BUILD SUCCESSFUL** en 1m59s, debug/androidTest, 24 JVM, Lint y Detekt correctos. Sin advertencias bloqueantes ni supresiones añadidas.
- qa-auth-test-build.log: compilación final de la prueba adicional de autenticación, correcta. No modifica el APK principal.
- qa-final-ui.xml: lectura de interfaz propia, confirma bienvenida de la cuenta QA después de recuperar sesión.

APK final instalado: app/build/outputs/apk/debug/app-debug.apk. SHA-256 público: `bd231fb20c584f10d853759e1884a57ce17808499159e8a43b440f4c15c113e2`.

La ejecución completa falló por el proveedor: **no se cierran las ocho specs como listas**. Las pruebas independientes sí tienen resultados positivos. Los 24 JVM y fixtures TLS anteriores no sustituyen las verificaciones reales pendientes. Sin CI remoto nuevo, commit o push.

## Pendientes

Backend debe resolver moderación de texto y confirmar despliegue. Después: crear reto y verificar envío/selección de imagen, enlace, rechazo de contenido y límites; probar torneos con datos/bracket reales y autorización entre cuentas. Faltan endpoints de recuperación de grupos/contratos, por lo que al reiniciar no se reconstruye ese historial aunque la sesión sí continúe. Aviso definitivo/Play y controles generales mantienen sus pendientes propios.

[Specs activas](../../specs/README.md) · [Cierre local anterior](verificacion-consumidor-2026-11-04.md).
