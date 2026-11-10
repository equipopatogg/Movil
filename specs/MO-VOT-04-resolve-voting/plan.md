# Plan MO-VOT-04

1. Contrastar DTO, parámetros, permisos y errores contra OpenAPI y servicio del commit fijado.
2. Añadir interfaz HTTP tipada y mapeo de datos a dominio, reutilizando sesión y errores seguros.
3. Conectar estado observable y pantalla con confirmación para escrituras; controles según permisos devueltos.
4. Probar contrato HTTP, entradas inválidas, errores, identidad, cancelación y cambio de cuenta/grupo.
5. Ejecutar build, JVM, Lint, Detekt, instrumentación y controles de repositorio; registrar resultados y límites.

Sin dependencias nuevas previstas. Aceptación del proveedor y revisión humana permanecen pendientes.

## Archivos y pruebas del candidato

- CommunityApi.kt; feature/community/data y domain.
- feature/connected/presentation/Community*.kt y ConnectedCommunity.kt.
- `VOT_04ConsumerTest`: `VOT_04_httpsContractAndAuthentication`, `VOT_04_anonymousCannotSend`, `VOT_04_serverErrorsNeverSucceedOrReplay`.
- Resultado: pendiente de ejecución completa; estos casos no acreditan por sí solos todo C1–C6.

## Correspondencia actual de pruebas - 2026-11-06

- unit: `com.pactogg.app.feature.community.VOT_04ValidationTest.VOT_04_missingRequiredResponseFieldsAreRejected`.
- integration: `com.pactogg.app.feature.community.VOT_04ApiTest.VOT_04_httpsContractAndAuthentication`.
- security: `com.pactogg.app.feature.community.VOT_04SecurityTest.VOT_04_serverErrorsNeverSucceedOrReplay`.

Ejecución de estas pruebas pendiente; no heredar los resultados backend.


## Actualizacion de contrato recibida - 2026-11-06

La lista de 54 rutas del usuario exige Idempotency-Key en esta operacion. El consumidor genera un UUID por invocacion antes de SessionManager y lo envia como cabecera obligatoria. No activa reintentos automaticos ni persiste claves en logs. La prueba HTTPS existente verifica presencia y formato UUID junto con autorizacion, ruta y numero de peticiones. Implementacion local; ejecucion del candidato pendiente. La semantica de reenvio manual despues de respuesta perdida y la vigencia del registro del servidor requieren el contrato actualizado.
