# Acceso y recuperacion - 2026-11-08

Solicitud: ordenar la pantalla de entrada y explicar el fallo posterior al selector Google. Cambios preparados en la copia original; la copia activa fuera de OneDrive requiere aplicar scripts/apply-access-layout.ps1. No atribuir estos cambios a la APK instalada antes de aplicar y compilar.

- Login: correo, contrasena, enlace secundario Olvide mi contrasena bajo los campos, accion principal Iniciar sesion y cambio a registro. Google usa boton secundario con borde.
- Recuperacion: pantalla separada con correo; Ya tengo un codigo de recuperacion abre nueva contrasena. Volver desde nueva contrasena regresa a recuperacion. Los enlaces de correo siguen abriendo la pantalla correspondiente.
- Verificacion: conserva las acciones del perfil y el enlace recibido. Se retiran los botones de codigo/verificacion de la entrada.
- Google: se limpia el error anterior al abrir de nuevo el selector; el aviso aparece junto al boton con estilo de error. GOOGLE_AUTH_UNAVAILABLE indica que el servidor no tiene disponible el acceso Google; el selector por si solo no demuestra una sesion PactoGG creada. No se modifico backend ni se desactivo la validacion de tokens.

Verificacion realizada: Detekt CLI completo termina con codigo 0. Compilacion, Lint, pruebas JVM y comprobacion visual del candidato nuevo pendientes; los 342 casos previos no acreditan esta edicion. No se pudo editar directamente la copia activa por las restricciones de escritura de esta sesion. El aplicador valida hashes antes de reemplazar archivos, conserva copia previa y ejecuta check-android.ps1 en el destino. Sin push.
