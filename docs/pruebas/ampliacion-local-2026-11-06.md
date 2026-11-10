# Ampliación local: revisión del 6 de noviembre de 2026

## Resultado más reciente: 329 JVM correctas y corrección de Lint

Los XML posteriores a la corrección TLS reportan 329 tests, cero fallos, cero errores y cero omitidos. La ejecución del usuario generó cobertura y se detuvo en lintDebug: SuspiciousIndentation en CommunityActions.kt y seis UnusedResources en community_strings.xml (community_complete, community_request, community_readall, community_read, community_vote y community_resolve).

Se ajustó la sangría del bloque y de los bloques equivalentes en CommunityQueries, ConnectedCommunity y ConnectedRouteLoading. Se verificó que los seis recursos no tuvieran referencias antes de retirarlos. XML válido; Detekt directo terminó con código 0. No se desactivó Lint, no se añadió baseline ni se modificó comportamiento funcional. Repetición de Lint/build pendiente en la terminal local; los 329 tests pertenecen al candidato previo a esta corrección de formato/recursos. Los dos hints de AutoboxingStateCreation no son errores bloqueantes.

## Resultado posterior del usuario y corrección de fixtures

El registro `Markdown pegado.md` del usuario y los XML locales de las 01:12 del 6 de noviembre muestran compilación debug y androidTest, seguida de 329 pruebas JVM con 191 fallos. Ese registro no se detiene por descarga PKIX: los fallos incluyen `SSLPeerUnverifiedException: Hostname gen-webserver.internal not verified`. El certificado presentado sí es el del fixture (CN localhost; SAN localhost, 127.0.0.1 y ::1), pero la URL usa un nombre distinto.

Corrección acotada: los cinco servidores de pruebas JVM se vinculan explícitamente a 127.0.0.1 y sus clientes usan una URL HTTPS con ese mismo host. Se mantiene la verificación del certificado y del nombre; no se añade confianza arbitraria ni se cambia el cliente de producción. Archivos: ApiTestFixture, ApiClientFactoryTest, ApiIntegrationTest, SocialApiIntegrationTest y SendEvidenceSecurityTest.

La ejecución 329/191 es anterior a esta corrección. Es necesario repetir la suite antes de declarar solucionados los fallos o concluir que las demás aserciones pasan. El fallo de instalación por falta de espacio del teléfono/emulador es independiente.

## Seguimiento: descarga de JaCoCo

El usuario ejecutó el script en su terminal y reportó `PKIX path building failed` al resolver `org.jacoco.ant:0.8.14` en Google Maven y Maven Central. El mensaje de caché de configuración envuelve ese fallo de descarga; no demuestra un error de código de la app.

Se añadió `-UseWindowsTrust` como opción local explícita del script. Usa `Windows-ROOT` y almacén no basado en archivo (`NONE`) únicamente en las invocaciones Gradle del script; no importa certificados, no modifica almacenes globales y mantiene verificación HTTPS. Es una alternativa pendiente de prueba en el equipo: no se ha identificado aún el certificado que rechaza Java. Se desactiva la caché de configuración para esta comprobación, conservando la cobertura. El archivo PowerShell usa UTF-8 con BOM para mostrar acentos correctamente en Windows PowerShell.

Sintaxis PowerShell comprobada: cero errores. La descarga no se pudo verificar desde esta sesión porque la consulta HTTPS devolvió error de resolución del host. Ejecutar `./scripts/check-android.ps1 -UseWindowsTrust` desde la terminal local y conservar el resultado; no declarar corregido PKIX hasta obtenerlo.

## Estado y alcance

Implementación local ampliada, pendiente de compilación y validación JVM/Android de este candidato.
Se trabajó en la copia `Documentos/ChatGPT/Diego/Movil`. No se modificó el backend, no se ejecutaron escrituras contra la API real y no se subieron cambios a GitHub.

La copia recibida ya incluía pantallas de jurado/votación, puntos/ranking, compromisos, notificaciones y fama; Memoria.md conservaba un estado previo. No se atribuye la creación de esas pantallas a esta revisión.

## Cambios de esta revisión

- Creación de retos desde el grupo mediante POST groups/{gid}/contracts. Se conserva el consumidor de la ruta canónica.
- Selector de torneos de todos los grupos o del grupo seleccionado; este último consume GET groups/{gid}/tournaments con filtros y cursor. No hay fallback ni repetición de escrituras entre alias.
- Regreso de las evidencias a votación y selección coherente de la barra de navegación.
- Una actualización fallida retira los permisos anteriores; acciones y respuestas se condicionan al mismo contexto de cuenta, grupo y pantalla. Un resultado incierto requiere actualizar antes de confirmar otra escritura.
- Validación de cursores vacíos/repetidos y de la identidad del grupo al abrir un reto del jurado. No se resuelve nuevamente un contrato cerrado.
- Interfaces HTTP agrupadas por responsabilidad, repositorio de notificaciones separado y funciones de consulta, acciones y permisos descompuestas, manteniendo las reglas existentes de Detekt.
- Nueve casos JVM de regresión nuevos; matriz actualizada. Cambios de fixtures para la creación por grupo. Estos casos están escritos, pero no ejecutados en esta sesión.
- El script local de comprobación detecta el JDK 21 y Python instalados cuando no están configurados en PATH.

## Evidencia

| Control | Resultado | Límite |
|---|---|---|
| Comparación de rutas declaradas con las 36 solicitadas | 36/36; ninguna sin consumidor en código principal | Inspección estática, no prueba del servidor |
| Matriz de operaciones | 33 operaciones con identidades y tres categorías: OK | La matriz incluye refresh, pero GET de evidencias es auxiliar y los alias no se cuentan como nuevas operaciones |
| Tests Python de los controles del repositorio | 27 ejecutados, OK | No son los tests de la app Android |
| Detekt 1.23.8 | Ejecutado directamente con sus dependencias locales y la configuración sin relajar | Análisis estático; ver informe local en build/reports/detekt/detekt-review.xml |
| Build, JVM, Lint | Bloqueados antes de ejecutar tareas | Gradle: `Could not connect to the Gradle daemon`, tanto en ejecución normal como con --no-daemon |
| Pruebas instrumentadas | No ejecutadas | Requieren build y emulador aislado |
| API real | No probada en esta revisión | El 405 anterior de GET evidencias sigue siendo un antecedente, no un resultado nuevo |

El recuento solicitado por backend es 33 funcionalidades + refresh + 2 alias = 36 rutas. La matriz móvil histórica tiene 33 operaciones porque incluye refresh y trata GET evidencias como auxiliar. Ambos recuentos describen clasificaciones distintas de las mismas rutas; no se ha borrado ninguna ruta para cuadrarlos.

No se reutilizan los informes JVM de la copia como resultados del candidato actual. El fallo SSL/PKIX anterior no puede darse por corregido ni reproducido mientras no arranque la suite.

## Continuar la comprobación local

Abrir esta copia en Android Studio. En su terminal PowerShell ejecutar:

```powershell
.\scripts\check-android.ps1
```

El script compila debug y androidTest, ejecuta cobertura/JVM, Lint, Detekt, valida los informes y genera SBOM. Se detiene ante un fallo; no hace commit, push ni cambios del backend.

Después, con un emulador aislado identificado explícitamente:

```powershell
.\scripts\check-android.ps1 -Instrumented -Device SERIAL_DEL_EMULADOR
```

No marcar T5/T6 ni aceptación externa como completas por la existencia de pantallas, las pruebas Python o el análisis estático. Quedan por ejecutar las pruebas JVM/Android, revisar el recorrido visual y verificar los contratos con el proveedor.

# Verificación de informes corregida - 2026-11-06

La ejecución del usuario terminó BUILD SUCCESSFUL con testDebugUnitTest, cobertura, lintDebug y detekt. El rechazo posterior de all se debía a cuatro casos de CommunitySecurityTest fuera de category_classes, no a fallos JVM. Se registró la clase en security, sus cuatro métodos como regresiones obligatorias y la anotación SecurityChecks en Kotlin. No se modificaron ni relajaron las reglas del validador.

Comprobación local posterior sobre los informes generados por el usuario: matrix OK; all PASS (329 pruebas, cero fallos, cero omitidas, sin faltantes ni categorías incorrectas); coverage PASS; build PASS. Los 27 tests Python de los controles pasan. Estos resultados revalidan los informes existentes; la nueva anotación de categoría requiere la siguiente ejecución Gradle. Pendiente repetir scripts/check-android.ps1 -UseWindowsTrust para completar el script incluido SBOM. No acredita instrumentación ni aceptación del proveedor. Sin cambios backend ni GitHub.
