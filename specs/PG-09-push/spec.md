# PG-09 — Transporte y recepción push Android

Estado: implementación autorizada, aceptación pendiente. Revisores: usuario (alcance), agente (implementación). Fecha: 2026-11-08.

Fuente: solicitud de continuar push, AGENTS.md y referencias/backend-v08/API-movil.md, sección Push, OpenAPI DEV-01/02/03. La autorización actual supera el aplazamiento histórico de FCM solo para este alcance móvil. Google queda pendiente de habilitación del servidor.

Cambio transversal de transporte Android sobre consumidores DEV existentes. No amplía ni declara verificada la matriz canónica de 33 operaciones MO. Casos DEV adicionales quedan registrados como regresiones con sus categorías y evidencia separada.

- AC1: registrar token FCM con sesión y permiso; actualizarlo al rotar, sin registrar tokens en logs ni incluir cuenta de servicio.
- AC2: retirar solo el token actual antes de auth/logout; cerrar localmente incluso si falla y avisar que no se confirmó la baja remota.
- AC3: canal pactogg_default, permiso contextual Android 13+, sin pedirlo repetidamente al iniciar.
- AC4: aviso privado genérico; pulsarlo abre avisos tras autenticación. IDs remotos no autorizan acciones ni escrituras. Destino detallado por recurso queda pendiente de validación Android/proveedor.
- AC5: sin configuración Firebase la app sigue funcionando; no simular entrega ni aceptación. Pruebas de políticas y orden HTTP; dispositivo/proveedor independientes.

Fuera: backend, GitHub, Firebase Auth, WorkManager, publicaciones y credenciales de servidor. Las preferencias DEV-03 existentes siguen en su consumidor actual.
