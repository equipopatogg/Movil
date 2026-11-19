# Plan

- `core/database/SnapshotEntity.kt` (entidad y DAO sin `suspend`) y `PactoDatabase` versión 2 con `AutoMigration(1, 2)`. El esquema `app/schemas/.../2.json` lo genera la compilación y se sube a Git.
- `core/offline/OfflineCache.kt`: interceptor de OkHttp en los clientes de `PactoApi` y `PlayApi`; `SnapshotStore` para probar sin Room.
- `core/offline/OfflineBanner.kt` debajo de la barra superior.
- `PactoGGApplication` borra la tabla cuando la sesión pasa a vacía.
