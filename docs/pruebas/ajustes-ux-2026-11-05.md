# Ajustes UX — mensajes y zona segura, 2026-11-05

El usuario autorizó mejorar mensajes y evitar que cámara/barras tapen contenido, sin subir. Se conservaron paleta, pantallas, contratos y reglas funcionales.

## Resultado

- Zona segura dinámica en la raíz: barra superior, cámara/recortes laterales, barra inferior y teclado. Fondo hasta los bordes; contenido dentro de límites.
- Errores generales en modal con Entendido; no se repite automáticamente una operación incierta.
- Correo, credenciales y apodo: errores junto al campo. Al corregir se retira el error anterior y se conserva el formulario.
- Éxitos en aviso breve descartable; navegación y cambio de cuenta retiran mensajes anteriores.
- Aceptar/rechazar y cerrar sesión: confirmación modal explícita. Cancelar sigue disponible cuando confirmar deja de ser válido.
- Textos más sencillos, sin códigos HTTP ni detalles privados del servidor.

## Comprobación local

239 JVM, 14 Android offline y 27 verificadores correctos, sin fallos/omisiones. Build, Lint y Detekt correctos (Lint: cero errores/advertencias, dos sugerencias).

Casos nuevos: FeedbackDialogsTest (error descartable, cancelar sin acción, error de apodo localizado, aviso de éxito); SafeAreaTest (barras/recorte en vertical y horizontal); ConnectedFlowTest (descarte antiguo no borra mensajes nuevos, ubicación de errores por pantalla). La prueba de acceso vacío ahora comprueba los mensajes junto a correo/contraseña. Se conserva la matriz de 19 operaciones, con dos regresiones JVM adicionales y cinco Android adicionales.

APK probado: `app/build/outputs/apk/debug/app-debug.apk`; informes JUnit en `app/build/test-results/testDebugUnitTest/` y `app/build/outputs/instrumented-offline/`. Inventario de casos/hashes en [trazabilidad](trazabilidad-criterios.json). Registro local ignorado: `artifacts/visual-ux/verification.json`.

## Límites

Prueba en emulador aislado con recorte de cámara simulado (tall); no acredita todos los teléfonos ni sustituye revisión visual del usuario. No se usaron cuentas reales, no se modificaron datos del teléfono ni se llamó al backend para estas pruebas. SAST/SCA y CI remoto no se atribuyen al candidato nuevo. No se cerraron todos los criterios funcionales ni aceptación humana de las 19 specs. Privacidad, avatares y Play siguen diferidos.

[Spec, tareas y checklist](../../specs/UX-01-mensajes-zona-segura/spec.md).
