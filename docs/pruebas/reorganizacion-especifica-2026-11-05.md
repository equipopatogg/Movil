# Pruebas organizadas por función — 2026-11-05

Se reorganizaron localmente las pruebas generales para que cada una de las 19 operaciones tenga archivos propios de validación, comunicación con API y seguridad/errores. Son 57 archivos específicos; los métodos llaman directamente a la función probada. No hay un test general que elija qué comprobar mediante un dispatcher de endpoints.

Por ejemplo, [inicio de sesión](../../specs/MO-AUTH-01-login/spec.md) tiene:

- [LoginValidationTest](../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginValidationTest.kt): datos de entrada inválidos.
- [LoginApiTest](../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginApiTest.kt): petición de login y respuesta del consumidor.
- [LoginSecurityTest](../../app/src/test/java/com/pactogg/app/feature/auth/login/LoginSecurityTest.kt): acceso rechazado, fallo del servicio, límite de solicitudes y éxito malformado.

Las demás operaciones se encuentran en auth/register, auth/session, auth/logout, groups/{create,list,detail,join,members}, contracts/{create,list,detail,accept,reject}, evidence/send, tournaments/list y profile/{detail,edit,nickname}, bajo app/src/test/java/com/pactogg/app/feature/.

[Tabla de las 19 operaciones y archivos](matriz-operaciones.md). Cada spec enlaza sus tres archivos y conserva las identidades exactas de sus casos en [operaciones.json](operaciones.json).

## Conservación comprobada

Antes de modificar archivos se registraron las 196 identidades JUnit ejecutadas. Se trazó cada identidad antigua a la nueva, con correspondencia uno a uno: 129 casos reorganizados y 67 casos compartidos intactos. La comparación del resultado completo no encontró casos perdidos, inesperados, duplicados u omitidos. Se conservaron los escenarios y comprobaciones existentes.

| Ejecución local | Correctas | Fallidas | Omitidas |
|---|---|---|---|
| Validación/unitarias | 56 | 0 | 0 |
| Integración/API | 44 | 0 | 0 |
| Seguridad/errores | 96 | 0 | 0 |
| Conjunto completo | 196 | 0 | 0 |
| Solo login, tres archivos | 6 | 0 | 0 |
| Validadores Python | 27 | 0 | 0 |

La medición JVM disponible quedó en 748/2756 líneas (27,14 %), sin modificar filtros. Esta reorganización no ratifica un mínimo de cobertura ni acredita aceptación funcional nueva.

La infraestructura y los datos HTTPS se reutilizan en [ApiTestFixture](../../app/src/test/java/com/pactogg/app/testing/shared/ApiTestFixture.kt). Las comprobaciones compartidas de conexión, sesión, estado y recorridos se mantienen. La comparación por hash de todos los archivos de app/src/main confirmó que el código de aplicación no cambió.

## Ejecución por función

Desde Movil, con JDK/SDK configurados, este comando ejecuta los seis casos específicos de login:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests 'com.pactogg.app.feature.auth.login.*'
```

Para ejecutar cada categoría, usar el argumento completo entre comillas en PowerShell:

```powershell
.\gradlew.bat :app:testDebugUnitTest '-Ppactogg.testSuite=unit'
.\gradlew.bat :app:testDebugUnitTest '-Ppactogg.testSuite=integration'
.\gradlew.bat :app:testDebugUnitTest '-Ppactogg.testSuite=security'
.\gradlew.bat :app:testDebugUnitTest
```

Cada invocación sustituye la carpeta nativa de resultados. Se archivaron inmediatamente los informes de cada categoría, del conjunto y de la selección login bajo artifacts/test-reorganization/, ignorado. before.json, migration.json y preservation.json registran el contraste, las 129 correspondencias y el resultado. Logs en ../artifacts/pruebas-especificas-*.log, fuera del repositorio móvil.

La matriz usa schema 2: ID como prefijo del método, por ejemplo AUTH_01_, nombre de clase/método completo y caso explícito de ausencia de sesión para cada operación protegida. El verificador sigue rechazando identidades parciales, categorías erróneas, pruebas ausentes y omisiones; se añadió una regresión contra confusión de IDs y pérdida del caso de sesión.

## Alcance

Trabajo únicamente local, sin commits, push, PR ni publicación por esta fase. Se conserva el trabajo local anterior. No se repiten pruebas contra el proveedor ni pruebas Android por un cambio limitado a nombres/ubicación de pruebas JVM y sus referencias. Los ocho casos Android y los resultados live anteriores mantienen su evidencia original, sin atribuirles una ejecución nueva.

Privacidad definitiva y avatares esperan al backend; Google Play sigue diferido. Esta organización facilita revisar spec → tareas → pruebas → resultado. La conformidad final con la evaluación del profesor se corroborará después, como solicitó el usuario.
