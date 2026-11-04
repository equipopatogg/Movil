---
name: pactogg-local-storage
description: Diseñar caché, preferencias, migraciones y privacidad de datos locales
  de PactoGG. Usar al especificar persistencia Android.
---

# pactogg-local-storage

Raíz del proyecto: tres niveles arriba de esta carpeta. Aplicar la fase vigente de AGENTS.md y la constitución; esta skill no amplía la autorización del usuario.

Clasificar dato, finalidad, sensibilidad, retención, respaldo y eliminación antes de escoger almacén. Separar Room de caché y preferencias no sensibles de credenciales cifradas.
Room no implica Offline-First ni una cola de acciones: WorkManager/sincronización siguen sujetos al alcance pendiente. No autorizar votos, puntos o inscripciones offline por existir SQLite.
Planear migraciones con datos de versiones reales y pruebas de borrado/cierre de sesión; evitar recuperación destructiva por comodidad. No imponer prefijos, semillas ni campos de concurrencia del ejemplo backend. Entregar tabla de datos y matriz de migración/privacidad propuestas.
