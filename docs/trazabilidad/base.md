# Trazabilidad de la base

| Requisito técnico | Archivo/área | Evidencia |
|---|---|---|
| BASE-01 Kotlin/Compose | Movil/app | assembleDebug |
| BASE-02 Capas/DI | core, feature, Hilt | Compilación KSP/Hilt |
| BASE-03 HTTPS y DTO/Mapper | core/network, core/result | ApiClientFactoryTest, ApiErrorMapperTest |
| BASE-04 Caché local técnica | core/database | Esquema Room + CacheMetadataDaoTest |
| BASE-05 Tema/navegación | ui/theme, core/navigation | ShellNavigationTest y revisión visual pendiente |
| BASE-06 Calidad | config/detekt.yml, Android Lint | Informes de verificación |
| BASE-07 Cadena de suministro | CycloneDX, workflows | SBOM y futura ejecución de escáneres |

Para cada funcionalidad futura: ID de historia original → criterio aprobado → MV/BE → contrato OpenAPI
→ pantalla/servicio → prueba → resultado. No se inventaron IDs de historias ni se marcaron historias del backlog completadas.

## Antecedente consolidado de la base técnica

Se retiró specs/base-tecnica/, que solo duplicaba el historial técnico. El objetivo previo fue una base Android compilable y navegable con capas/DI, HTTPS y DTO/mapper técnico, Room/DataStore no sensibles, tema y navegación, pruebas y controles. No incluía autenticación real, CRUD, puntos, jurado, IA, moderación, torneos funcionales, backend o despliegue.

El trabajo anterior consistió en conservar la base Android Studio, incorporar dependencias fijadas, preparar vistas sin datos de negocio ficticios, transporte/persistencia técnica, pruebas y controles locales. Sus resultados y limitaciones reales permanecen en [verificacion-local.md](../verificacion-local.md), no en tareas activas. La instrumentación final y la compatibilidad nativa siguen pendientes. Esta consolidación no borra ni cambia la aplicación.
