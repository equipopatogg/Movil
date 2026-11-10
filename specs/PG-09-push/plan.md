# Plan PG-09

1. Añadir SDK messaging fijado y recursos de configuración Android obtenidos del JSON existente; sin Analytics ni Firebase Auth.
2. Coordinador serializado: sesión/permisos/token, callback previo a logout y baja específica. Aplicación instala el coordinador; Activity reevalúa permiso al volver al primer plano.
3. Servicio no exportado, canal privado y PendingIntent explícito e inmutable. Avisos genéricos, sin URLs arbitrarias ni contenido sensible.
4. Acceso contextual a permiso desde preferencias, canal desactivado respetado. Navegación a avisos tras restauración/login, sin acciones automáticas.
5. Pruebas JVM/políticas y orden HTTP, Gradle/Lint/Detekt; registrar los bloqueos reales. Entrega real y destino detallado quedan abiertos.

6. Diagnóstico solicitado tras aviso interno sin push (2026-11-08): estado visible de configuración, permiso, conexión Firebase, registro pendiente/confirmado y error de registro. Solo enums y textos locales; sin tokens, credenciales, payloads ni mensajes arbitrarios del proveedor. Registro confirmado no equivale a entrega. Reintento explícito solo del registro fallido; nunca repetir un reto para diagnosticarlo.
