# Matriz de pruebas por operación — PactoGG Android

Fuente canónica: [operaciones.json](operaciones.json). Cada fila corresponde a una operación recibida en Sistema.docx, no a pantallas futuras. Las 44 pruebas existentes se conservan; los nuevos casos por operación son instancias JUnit4 parametrizadas con ID exacto, no casillas de documentación ni copias de un único éxito sin comprobar contenido.

Cada operación enlaza tres tipos de prueba, no una cantidad fija de tres casos. Las pruebas protegidas añaden además denegación sin sesión antes de enviar tráfico (logout permite cierre local sin llamada). Los alias y los dos formatos de evidencia no duplican la operación. El camino foto conserva sus casos MIME/multipart/límites previos; la nueva instancia EVI-02 prueba enlace y límite local de foto. No se pretende que un 403 simulado demuestre autorización efectiva del servidor.

Archivos de pruebas: [unitarias](../../app/src/test/java/com/pactogg/app/testing/EndpointUnitTest.kt), [contratos HTTP](../../app/src/test/java/com/pactogg/app/testing/EndpointContractTest.kt), [seguridad](../../app/src/test/java/com/pactogg/app/testing/EndpointSecurityTest.kt), [fixtures compartidas](../../app/src/test/java/com/pactogg/app/testing/EndpointFixture.kt). Las clases originales de red, sesión, estados, reglas, contrato y navegación permanecen.

| Operación / spec | Unit: enforcesConsumerBoundaryWithoutNetwork | Integration: consumesConfirmedWireContract | Security: deniedResponseIsSanitizedAndNeverReplayed |
|---|---|---|---|
| [USR-01](../../specs/MO-USR-01-registro/spec.md) `POST /api/v1/auth/register` | `[USR-01]` | `[USR-01]` | `[USR-01]` |
| [AUTH-01](../../specs/MO-AUTH-01-login/spec.md) `POST /api/v1/auth/login` | `[AUTH-01]` | `[AUTH-01]` | `[AUTH-01]` |
| [AUTH-03](../../specs/MO-AUTH-03-sesion/spec.md) `POST /api/v1/auth/refresh` | `[AUTH-03]` | `[AUTH-03]` | `[AUTH-03]` |
| [AUTH-04](../../specs/MO-AUTH-04-logout/spec.md) `POST /api/v1/auth/logout` | `[AUTH-04]` | `[AUTH-04]` | `[AUTH-04]` |
| [GRP-01](../../specs/MO-GRP-01-crear-grupo/spec.md) `POST /api/v1/groups` | `[GRP-01]` | `[GRP-01]` | `[GRP-01]` |
| [CON-01](../../specs/MO-CON-01-crear-reto/spec.md) `POST /api/v1/contracts` | `[CON-01]` | `[CON-01]` | `[CON-01]` |
| [EVI-02](../../specs/MO-EVI-02-evidencias/spec.md) `POST /api/v1/contracts/{id}/evidence` | `[EVI-02]` | `[EVI-02]` | `[EVI-02]` |
| [TOR-02](../../specs/MO-TOR-02-consultar-torneos/spec.md) `GET /api/v1/tournaments` | `[TOR-02]` | `[TOR-02]` | `[TOR-02]` |
| [USR-02](../../specs/MO-USR-02-consultar-perfil/spec.md) `GET /api/v1/users/me` | `[USR-02]` | `[USR-02]` | `[USR-02]` |
| [USR-03](../../specs/MO-USR-03-editar-perfil/spec.md) `PATCH /api/v1/users/me` | `[USR-03]` | `[USR-03]` | `[USR-03]` |
| [USR-06](../../specs/MO-USR-06-disponibilidad-apodo/spec.md) `GET /api/v1/users/check-nickname` | `[USR-06]` | `[USR-06]` | `[USR-06]` |
| [GRP-02](../../specs/MO-GRP-02-listar-grupos/spec.md) `GET /api/v1/groups` | `[GRP-02]` | `[GRP-02]` | `[GRP-02]` |
| [GRP-03](../../specs/MO-GRP-03-detalle-grupo/spec.md) `GET /api/v1/groups/{id}` | `[GRP-03]` | `[GRP-03]` | `[GRP-03]` |
| [MEM-01](../../specs/MO-MEM-01-unirse-grupo/spec.md) `POST /api/v1/groups/join` | `[MEM-01]` | `[MEM-01]` | `[MEM-01]` |
| [MEM-02](../../specs/MO-MEM-02-listar-miembros/spec.md) `GET /api/v1/groups/{id}/members` | `[MEM-02]` | `[MEM-02]` | `[MEM-02]` |
| [CON-02](../../specs/MO-CON-02-listar-retos/spec.md) `GET /api/v1/groups/{gid}/contracts` | `[CON-02]` | `[CON-02]` | `[CON-02]` |
| [CON-03](../../specs/MO-CON-03-detalle-reto/spec.md) `GET /api/v1/contracts/{id}` | `[CON-03]` | `[CON-03]` | `[CON-03]` |
| [CON-06](../../specs/MO-CON-06-aceptar-reto/spec.md) `POST /api/v1/contracts/{id}/accept` | `[CON-06]` | `[CON-06]` | `[CON-06]` |
| [CON-07](../../specs/MO-CON-07-rechazar-reto/spec.md) `POST /api/v1/contracts/{id}/reject` | `[CON-07]` | `[CON-07]` | `[CON-07]` |

## Qué comprueban

- Unitarias: credenciales/privacidad y longitud, expiración inválida de sesión, campos wire de logout, límites de grupo/apodo/búsqueda, IDs, tipo/estado/versión de reto, saldo, destinatario y enlaces/archivos inválidos. El proxy de pruebas impide cualquier I/O inesperado en esas fronteras.
- Integración consumidor: URL/método, Bearer o ausencia en públicos, snake_case/payload, código esperado, mapeos de perfil/grupo/reto/miembros/listas, rotación, limpieza local y versión/saldo. MockWebServer usa certificado de test para localhost/127.0.0.1/::1; no existe bypass TLS de producción.
- Seguridad consumidor: 401/403/429 no se presentan como éxito, no se filtran message/details/tokens/cause, no hay reenvío automático, 403 conserva sesión y refresh/logout fallidos eliminan la custodia correspondiente. Quince instancias adicionales prueban ausencia de tráfico protegido sin sesión.
- Android real: Room en memoria, cifrado/limpieza y corrupción/clave ausente en alias aislado, formulario vacío sin bypass y privacidad visible; cinco métodos offline seleccionados explícitamente. Las pruebas live y capturas no se ejecutan en CI ni se cuentan como omitidas exitosas.

## Ejecución local

Desde la raíz Movil, con JDK/SDK configurados:

```powershell
.\gradlew.bat :app:testDebugUnitTest -Ppactogg.testSuite=unit
.\gradlew.bat :app:testDebugUnitTest -Ppactogg.testSuite=integration
.\gradlew.bat :app:testDebugUnitTest -Ppactogg.testSuite=security
.\gradlew.bat :app:createDebugUnitTestCoverageReport
```

Cada invocación de la misma tarea usa su carpeta nativa y reemplaza resultados de la invocación anterior; CI separa máquinas y artefactos. Para la ejecución completa, omitir testSuite (all predeterminado). El helper valida cada categoría contra IDs exactos y falla con cero casos, informes ausentes, omisiones o resultados parciales. No cambiar una prueba a skipped para obtener verde.

Instrumentación sobre un emulador fresco y aislado con APKs de app/androidTest ya compilados:

```powershell
python scripts/run-instrumented.py --device emulator-5554
```

Usar el Python disponible en el entorno. Selector obligatorio; no se borran datos del teléfono ni se sustituyen cuentas. AccessNavigationTest requiere app sin sesión: no usar la cuenta QA del teléfono como sustituto del emulador limpio. No se envían credenciales ni flags live desde ese runner.

## Alcance de aceptación

Estos casos acreditan escenarios concretos por operación, no todos los AC ni seguridad total. Pruebas HTTP simulan proveedor; autorización real entre cuentas, negativos completos, accesibilidad/ciclo de vida y campos discrepantes mantienen pendientes específicos. Avatar sigue pendiente de catálogo. PG-00/01 describen infraestructura de pruebas; MO-* define comportamiento funcional.
