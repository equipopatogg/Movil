# Plan MO-PTS-01

1. Contrastar DTO, parámetros, permisos y errores contra OpenAPI y servicio del commit fijado.
2. Añadir interfaz HTTP tipada y mapeo de datos a dominio, reutilizando sesión y errores seguros.
3. Conectar estado observable y pantalla con confirmación para escrituras; controles según permisos devueltos.
4. Probar contrato HTTP, entradas inválidas, errores, identidad, cancelación y cambio de cuenta/grupo.
5. Ejecutar build, JVM, Lint, Detekt, instrumentación y controles de repositorio; registrar resultados y límites.

Sin dependencias nuevas previstas. Aceptación del proveedor y revisión humana permanecen pendientes.

## Archivos y pruebas del candidato

- CommunityApi.kt; feature/community/data y domain.
- feature/connected/presentation/Community*.kt y ConnectedCommunity.kt.
- `PTS_01ConsumerTest`: `PTS_01_httpsContractAndAuthentication`, `PTS_01_anonymousCannotSend`, `PTS_01_serverErrorsNeverSucceedOrReplay`.
- Resultado: pendiente de ejecución completa; estos casos no acreditan por sí solos todo C1–C6.

## Correspondencia actual de pruebas - 2026-11-06

- unit: `com.pactogg.app.feature.community.PTS_01ValidationTest.PTS_01_missingRequiredResponseFieldsAreRejected`.
- integration: `com.pactogg.app.feature.community.PTS_01ApiTest.PTS_01_httpsContractAndAuthentication`.
- security: `com.pactogg.app.feature.community.PTS_01SecurityTest.PTS_01_serverErrorsNeverSucceedOrReplay`.

Ejecución de estas pruebas pendiente; no heredar los resultados backend.
