# Aislamiento de pruebas JVM - 2026-11-06

La captura de GitHub reporta 99 pruebas unitarias y un fallo UncaughtExceptionsBeforeTest en NicknameQueriesTest.elapsedCooldownAllowsNewQueryAndEditingWithoutAutomaticPolling. El resumen no incluye la excepcion original suprimida; no demuestra un fallo de la regla del apodo.

Revision: NicknameQueriesTest y EvidenceConfirmationTest no cerraban el ViewModel antes de resetMain, y comenzaban sin esperar la restauracion de sesion en Dispatchers.IO. Se registra el modelo en ViewModelStore, se espera restoring=false y al terminar se cancela y espera su Job antes de resetMain. ConnectedFlowTest tambien espera la finalizacion tras clear. Se conservan todos los casos y aserciones, sin excluir pruebas ni modificar app/backend.

Validacion: git diff --check correcto. Intento de ejecutar la categoria unit bloqueado antes de las pruebas: acceso denegado al lock del Wrapper global; con Gradle instalado y cache local, Could not connect to the Gradle daemon. Los XML previos no validan esta correccion. Pendiente ejecutar categoria unit y suite completa en terminal del usuario/CI. GitHub no accesible desde esta sesion (Could not resolve host); entrega remota pendiente.
# Preparación de entrega en rama - 2026-11-06

Usuario autoriza subir la ampliación local en una rama nueva. Rama: codex/integracion-completa-movil. El usuario informa que la comprobación final ya terminó bien; los informes locales revisados acreditan 329 pruebas sin fallos, cobertura y build PASS. Instrumentación y aceptación del proveedor siguen pendientes. Estado remoto pendiente de push; no atribuir entrega hasta confirmación de GitHub.

# Verificación de informes corregida - 2026-11-06

La ejecución del usuario terminó BUILD SUCCESSFUL con testDebugUnitTest, cobertura, lintDebug y detekt. El rechazo posterior de all se debía a cuatro casos de CommunitySecurityTest fuera de category_classes, no a fallos JVM. Se registró la clase en security, sus cuatro métodos como regresiones obligatorias y la anotación SecurityChecks en Kotlin. No se modificaron ni relajaron las reglas del validador.

Comprobación local posterior sobre los informes generados por el usuario: matrix OK; all PASS (329 pruebas, cero fallos, cero omitidas, sin faltantes ni categorías incorrectas); coverage PASS; build PASS. Los 27 tests Python de los controles pasan. Estos resultados revalidan los informes existentes; la nueva anotación de categoría requiere la siguiente ejecución Gradle. Pendiente repetir scripts/check-android.ps1 -UseWindowsTrust para completar el script incluido SBOM. No acredita instrumentación ni aceptación del proveedor. Sin cambios backend ni GitHub.

# Estado: JVM correctas, corrección de Lint - 2026-11-06

Los XML de la última ejecución del usuario confirman 329 pruebas JVM, cero fallos, cero errores y cero omitidas. La corrección de los fixtures HTTPS permite completar la suite y generar cobertura. Lint detuvo la ejecución con siete errores: una sangría incorrecta en CommunityActions y seis recursos sin referencias en community_strings.xml.

Se corrigieron esas siete causas y se normalizó la misma sangría en CommunityQueries, ConnectedCommunity y ConnectedRouteLoading. Solo cambios de formato y retirada de recursos no utilizados; sin supresiones ni baseline. XML válido y Detekt ejecutado directamente sin incidencias después del cambio. Pendiente repetir Lint/build mediante scripts/check-android.ps1 -UseWindowsTrust; no declarar aún verificación integral ni instrumentación completadas. Los dos hints sobre mutableIntStateOf son recomendaciones no bloqueantes.

## Antecedente: seguimiento de pruebas HTTPS - 2026-11-06

El registro posterior del usuario con UseWindowsTrust llega a assembleDebug, assembleDebugAndroidTest y testDebugUnitTest: 329 pruebas, 191 fallidas, cero omitidas. El XML local confirma SSLPeerUnverifiedException: Hostname gen-webserver.internal not verified, frente a un certificado de fixture con SAN localhost/127.0.0.1/::1. No confundirlo con el PKIX anterior al descargar JaCoCo.

Se corrigieron cinco fixtures JVM: bind explícito a 127.0.0.1 y URL HTTPS con ese host, ya incluido en el certificado. No se alteró hostnameVerifier, confianza del cliente real, backend ni reglas de aceptación. Cambios de pruebas pendientes de nueva ejecución; el resultado 329/191 pertenece al candidato anterior a esta corrección. Repetir scripts/check-android.ps1 -UseWindowsTrust desde la terminal local.

## Estado anterior de la ampliación local, 2026-11-06

- Alcance: petición actual de completar los consumidores de las 36 rutas; backend sin cambios y sin GitHub.
- Código: se conservaron las pantallas nuevas presentes en la copia; se conectaron los alias por grupo, se corrigieron regreso a votación, selección de navegación, invalidación de permisos/contexto y validación de paginación. Se separaron interfaces, notificaciones y políticas para cumplir el análisis estático sin relajar reglas.
- Evidencia: auditoría estática 36/36 rutas con consumidores; matriz de 33 operaciones correcta; 27 pruebas Python de controles correctas. Nueve regresiones JVM nuevas escritas. Detekt ejecutado directamente, ver el informe de esta revisión.
- Bloqueo: Gradle no puede conectar a su daemon local; build, JVM y Lint no llegaron a ejecutarse. No hay resultado nuevo de tests JVM, instrumentación ni API real. El anterior fallo SSL/PKIX no se declara resuelto.
- Pendientes: ejecutar scripts/check-android.ps1 en la terminal de Android Studio, instrumentación en emulador aislado, revisión visual y aceptación del proveedor; GET evidencias 405 es evidencia histórica pendiente de reprueba.
- Informe: [ampliación local](docs/pruebas/ampliacion-local-2026-11-06.md). No reutilizar resultados previos como verificación de este candidato.

## Antecedentes conservados

# Ampliación móvil en curso — 2026-11-05

- Autorización: integrar 14 operaciones restantes, con SDD y pruebas; backend solo referencia, sin cambios.
- Fuente: backend 451c8ae1bfae3df64cf95f5a26ace7b1eacb3b1e. Decisión D-SDD-40.
- Preparado: 14 expedientes spec/plan/tasks/checklist; CommunityApi, DTO y primer repositorio consumidor; cuatro casos de seguridad nuevos.
- Incompleto: mapeo a dominio, pantallas/navegación/estado, paginación validada, alias dentro de CON-01/TOR-02, matrices y pruebas completas por operación, instrumentación, Lint/Detekt y controles de seguridad.
- Ejecución inicial: 256 tests JVM, 158 fallidos, sin acreditar cambios nuevos. Numerosos fallos SSLHandshakeException/PKIX frente a MockWebServer HTTPS; causa raíz todavía no determinada. No desactivar TLS para resolverlos.
- No hay aceptación, CI remoto ni entrega funcional de esta ampliación. Las tareas permanecen abiertas.

# Estado actual — imágenes y navegación, 2026-11-05

- Estado: integración local implementada y verificada; 256 JVM y 22 Android correctos, sin fallos/omisiones. Lint/Detekt, 27 validadores, secretos y generación SBOM correctos; cobertura JVM 907/3105 líneas (29,21 %). No hay CI remoto de este candidato.
- Decisión/motivo: petición expresa del usuario. Solo las imágenes amplían MO-EVI-02; revisión general independiente, sin expediente de relevo. Conservar 19 operaciones principales; GET de evidencias es auxiliar. Barra Make de cinco opciones, Explorar con aviso, perfil en cabecera, mensajes/zona segura conservados.
- Deuda: GET real de evidencias devuelve 405 aunque aparece en contrato/repositorio 451c8ae. Nueve sondeos previos positivos y logout del sondeo 204, sin escrituras de negocio; no afirmar listado/CDN real ni reglas de primer voto verificados. Revisión manual del selector real y visual del usuario pendiente. Privacidad, catálogo de avatares, S3 directo ID156 y Play mantienen sus diferimientos.
- A evitar: no resubir una foto por fallo de consulta, no abrir muestras como funciones conectadas, no atribuir aceptación ni CI viejo al candidato nuevo. No hay modificaciones backend ni nueva entrega Git.
- Slice actual/siguiente paso: resultado trazado en MO-EVI-02/checklist y docs/pruebas/trazabilidad-criterios.json; pendiente que backend habilite/corrija GET de evidencias para repetir compatibilidad real. Cambios disponibles localmente para revisión normal.

Los apartados inferiores son historia por fecha, no sustituyen este estado.

# Pruebas separadas por operación — 2026-11-05

Reorganización local solicitada, sin subir. Las 19 operaciones tienen archivos de validación, API y seguridad; los casos comunes de conexión/sesión/recorridos se conservan. La matriz y las referencias de specs se actualizan a nombres completos, con ID como prefijo AUTH_01_. Se contrastaron los 196 casos previos uno a uno: 129 renombrados y 67 compartidos intactos. Conjunto y categorías 56/44/96 correctos, sin fallos/omisiones; selección login de seis casos correcta; 27 validadores correctos. [Organización y evidencia](docs/pruebas/reorganizacion-especifica-2026-11-05.md). No crear commits/PR/push hasta nuevo encargo.

## Historial conservado

# Pruebas solicitadas — 2026-11-05

Giro/recreación, cambio de cuenta y permisos comprobados en el alcance del [informe](docs/pruebas/rotacion-cuentas-permisos-2026-11-05.md). 196 JVM y ocho Android offline correctos; 26 verificaciones Python correctas. Proveedor: miembro 200/no miembro 403 para grupo, miembros y tablero; sesiones nuevas de prueba cerradas con 204. Se conserva la sesión del teléfono y no se modifica producción Kotlin.

Resultados nuevos locales: no atribuir CI remoto nuevo. Main con PR9 ya pasó su ejecución remota anterior. Privacidad/avatares esperan al backend y Play queda diferido expresamente. No confundir estas comprobaciones con aceptación de todas las escrituras/roles o de todos los AC.

## Historial conservado

# Cierre verificable de pendientes — 2026-11-05

192 JVM correctas y cobertura 27,14 % local (748/2756), 26,96 % CI (743/2756), sin cambiar denominador. Release local y ocho ELF/ZIP correctos; instalación/arranque 4 KB y recorrido de acceso 16 KB correctos. Regresiones reales Wrapper/Semgrep PASS en run41977160594. Rechazo live sin sesión: quince operaciones con 401. Cierre por PR9 con su comprobación final. [Informe](docs/pruebas/cierre-pendientes-2026-11-04.md) y [84 criterios](docs/pruebas/estado-criterios-2026-11-04.md).

Conservar como pendientes recorridos autenticados adicionales, privacidad definitiva, catálogo de avatar, producción/Play, porcentaje mínimo y aceptación funcional/humana restante. Backend nuevo no amplía automáticamente las 19 operaciones. No cambiar funciones ni dependencias por el diagnóstico RELRO ya corregido.

## Historial conservado

# Entrega remota autorizada — 2026-11-04

Estado: ramas y PR 6/7/8 creados en equipopatogg/Movil. Candidato PR8 con todos los controles remotos correctos; 116 JVM y cinco Android, scanners reales y SBOM de 192 componentes. Gate PASS y APK descargado coincidente con el probado. Main protegida con check de GitHub Actions obligatorio, rama actualizada y sin push forzado/borrado. [Entrega y evidencia](docs/pruebas/entrega-github-2026-11-04.md).

Decisión: integrar PR dependientes preservando orden y volver a verificar el candidato final antes de main. No inventar aprobación humana ni cerrar todos los AC. Deuda: cobertura 19,48 %, política por ratificar; negativos específicos Wrapper/Semgrep y criterios funcionales pendientes. No desplegar, firmar producción ni modificar backend.

## Historial local conservado

# Estado vigente — calidad móvil, 2026-11-04

Estado: 116 JVM y cinco instrumentadas Android correctas; 19 verificaciones Python y actionlint correctos. [Evidencia y límites](docs/pruebas/verificacion-calidad-2026-11-04.md).

Decisión: adaptar MovilSDD conservando stack y funciones. Tres categorías por las 19 operaciones, matriz canónica, grafo CI con compuerta obligatoria y APK probado por hash. Sin herramientas/backend extra ni umbral heredado.

Deuda: cobertura JVM 19,48 %, mejorar y ratificar política; scanners/Wrapper y CI remoto nuevo pendientes. Specs funcionales no aceptadas integralmente. Evitar declarar PASS por omisiones o fixtures de proveedor. Siguiente: revisión del cambio local; ramas/commits/push requieren el encargo correspondiente.

## Memoria anterior conservada

# Estado vigente — once operaciones integradas y probadas, 2026-11-04

- **Estado:** 19 operaciones consumidor locales, 23 expedientes (19 MO + cuatro de base/coordinación). Once nuevas conectadas; perfil/apodo/disponibilidad, grupos/lista/detalle/unión/miembros y tablero/detalle/aceptar/rechazar con caso positivo real PASS. [Informe y candidato](docs/integracion/integracion-social-2026-11-04.md).
- **Decisión/motivo:** implementación expresa del usuario sobre specs confirmadas en Sistema.docx; D-SDD-31. Sin nuevas dependencias/versiones, cambios backend o funciones futuras. Avatar permanece bloqueado por catálogo.
- **Verificación:** 44 JVM, debug/Lint/Detekt/androidTest compilado, Gitleaks y dos métodos físicos correctos; aceptación retiene fianza real y rechazo no cambia saldo. QA original recuperada con apodo original y sesión iniciada; nueva cuenta auxiliar solo para pruebas, credenciales fuera de Git. Cinco grupos QA existentes: no repetir creación indiscriminada.
- **Deuda:** negativos completos por AC, UI manual/accesibilidad/ciclo de vida/paginación real, aviso definitivo/Play, avatar y reglas discrepantes de evidencia. Fotos/enlaces no se repitieron en nuevo candidato. Aceptación humana y CI remoto nuevo pendientes.
- **A evitar:** heredar PASS backend, marcar specs completas por un caso positivo, repetir escrituras inciertas o restaurar puntos/borrar datos con operaciones inexistentes. Se conservan dos retos QA (aceptado y rechazado).
- **Siguiente paso:** revisión del usuario y completar criterios pendientes; sin commit/push de esta integración. Estados inferiores son antecedentes.

# Estado vigente — once specs confirmadas por documento backend, 2026-11-04

- **Estado:** 23 expedientes activos: 19 consumidores (8 anteriores + 11 nuevos) y 4 de base/coordinación. Once nuevos con spec/plan/tasks/checklists, mismo ID base que backend y estructura equivalente. [Matriz de recepción](docs/integracion/specs-confirmadas-sistema-2026-11-04.md).
- **Decisión/motivo:** usuario define confirmado como operación presente en Sistema.docx. Todas las once se corroboraron también con specs/OpenAPI al commit 8baa9e6d04038c1d6cf9588c5526ad63b94c3f87. No se añaden funciones ausentes.
- **Deuda:** implementación/aceptación y comprobación del despliegue de las nuevas operaciones, catálogo de avatar y discrepancias registradas. MO-CON-03 reconoce GET auxiliar anterior como avance parcial; no declara pantalla/modelo completos.
- **A evitar:** heredar PASS backend, marcar listas por crear Markdown, falsear secuencia previa o seguir la sugerencia externa de datos falsos en funciones ausentes.
- **Siguiente paso:** implementar por dependencias solo cuando se solicite. Este encargo únicamente agrega/concilia documentos; sin nuevo código, build/tests, instalaciones, cuentas, AWS, commit o push. D-SDD-30.

# Estado vigente — ajuste acotado de evidencias, 2026-11-04

- **Estado:** MO-EVI-02 consulta detalle tras confirmar subida; si falla consulta mantiene confirmación y oculta estado obsoleto. Misma estructura/diseño, sin expedientes nuevos. [Resultado y límites](docs/integracion/ajuste-estado-evidencia-2026-11-04.md).
- **Decisión/motivo:** usuario autoriza cambios pequeños que encajen en specs existentes; D-SDD-29. El GET es apoyo interno al envío, no implementación completa de consulta de retos.
- **Verificación:** 28 JVM correctos, debug/Lint/Detekt/androidTest compilados; APK actualizado instalado y prueba real de acceso/logout/rechazo 401 correcta. Sin repetir grupos/subidas ni acreditar en proveedor el nuevo refresco posterior. Scanner portable de secretos no disponible; no se atribuye PASS nuevo.
- **Deuda:** criterios pendientes, recorrido real de refresco tras evidencia y once operaciones con specs propias. Sin commit/push.

# Estado vigente — revisión de once contratos nuevos, 2026-11-04

Backend al commit 8baa9e6d04038c1d6cf9588c5526ad63b94c3f87 incorpora las once operaciones anunciadas en Sistema.docx (19 totales, ocho consumidas por Android). [Comparación y pendientes](docs/integracion/revision-contratos-nuevos-2026-11-04.md). Solo revisión documental: no specs nuevas ni implementación. Prioridad propuesta: recuperar perfil/grupos/miembros y retos, después escrituras con versión/saldo. Conciliar evidencia permitida en open vs aceptación previa, catálogo de avatares, schemas/versión y recuperación de fotos. Las operaciones nuevas no se acreditan desplegadas ni verificadas por la prueba previa. Sin commit/push.

# Estado vigente — reprueba AWS en teléfono, 2026-11-04

- **Estado:** recorrido real PASS: login, grupo, reto, enlace, foto sintética, torneos vacíos, refresh y logout. Rechazo de contraseña incorrecta y renovación al arrancar también correctos. Cuatro métodos instrumentados correctos; uno prepara expiración QA.
- **Decisión/motivo:** usuario reconectó teléfono y solicitó repetir pruebas tras informar backend una corrección de política AWS. No se cambió AWS ni código. [Evidencia y límites](docs/integracion/verificacion-telefono-2026-11-04.md).
- **Deuda:** selector nativo, casos negativos/límites, autorización entre cuentas, torneos poblados y aceptación; contrastar nuevos contratos de Sistema.docx antes de integrarlos.
- **A evitar:** repetir creación de grupos sin revisar cupos, declarar todas las specs terminadas o inferir la política exacta modificada. Credenciales fuera de Git.
- **Siguiente paso:** revisar contratos nuevos y completar verificaciones pendientes cuando se solicite. Main ya recibió los cinco PR y sus checks finales pasaron; esta reprueba solo modifica documentación local, sin commit/push. Los estados inferiores son antecedentes cronológicos.

# Estado vigente — copia y commits locales, 2026-11-04

Copia previa verificada y ramas dependientes preparadas bajo identidad equipopatogg. [Registro de ramas/commits](docs/preparacion/entrega-por-ramas.md). Base visual: 9 JVM; acceso/grupos/torneos/integración pendiente: 24 JVM por etapa, debug/Lint/Detekt y compilación androidTest correctos; candidatos sin secretos. Sin push, PR o cambios de main publicado. No se declara aceptación integral de specs: retos/evidencias siguen bloqueados por 503, torneos con datos y otros criterios permanecen abiertos.

# Estado vigente — teléfono y API real, 2026-11-04

Cuenta QA creada por autorización del usuario; APK final instalado y sesión QA iniciada. Registro/login/grupo/torneos vacíos/refresh/logout comprobados; cifrado y renovación al arrancar probados físicamente. Creación de reto falla en proveedor con 503 MODERATION_UNAVAILABLE, lo que bloquea evidencias. No todo funciona ni se aceptan specs completas. [Informe y request_id](docs/integracion/verificacion-telefono-2026-11-04.md).

Se mejoraron mensajes/diagnóstico seguro y busy de sesión. Debug, 24 JVM, Lint/Detekt y pruebas físicas independientes correctos; recorrido completo conserva FAIL por backend. Credenciales privadas fuera del proyecto. Sin modificación/despliegue backend, commit o push. Siguiente paso: proveedor resuelve moderación y se retoman retos/evidencias, sin omitir controles.

# Estado vigente — consumidor implementado localmente, 2026-11-04

- **Estado:** ocho operaciones MO-* implementadas y recorrido conectado en MainActivity. Debug/24 JVM/Lint/Detekt/Gitleaks correctos; androidTest compilado, sin dispositivo disponible para ejecutar. [Evidencia](docs/integracion/verificacion-consumidor-2026-11-04.md).
- **Decisión/motivo:** usuario solicita empezar; D-SDD-27. Aviso básico provisional autorizado y consentimiento explícito. Conservar diseño/UI futura sin entradas al recorrido conectado y sin PG-08 activo.
- **Deuda:** ejecución Android/cuentas autorizadas y aceptación; contexto de grupos/contratos no recuperable sin GET. Aviso definitivo, release/Play/base técnica pendiente mantienen sus expedientes.
- **A evitar:** confundir MockWebServer con proveedor, declarar cifrado probado en dispositivo por compilar, repetir POST incierto, publicar o recuperar recordatorios privados como requisitos.
- **Siguiente paso:** probar este candidato en dispositivo y con cuentas de prueba autorizadas; actualizar solo criterios demostrados. No se modificó backend ni se hizo commit/push.

# Corrección vigente — 2026-11-04

Specs activas limitadas a operaciones backend, base técnica y coordinación de acceso: 12 expedientes. Los borradores de pantallas adelantadas y PG-08 se retiraron del expediente activo. No hay PG-08 en backend. Recordatorios personales ignorados quedan fuera de las dependencias del proyecto y no se enlazan. Pantallas y código conservados. Solo corrección documental, sin build/tests o push. D-SDD-26 prevalece sobre la propuesta inferior de 17 specs/PG-08, que es antecedente superado.

# Estado vigente — specs alineadas con backend, 2026-11-04

- **Estado:** ocho operaciones proveedor revisadas al commit f6c37d8a70de43765f870b78b452832a2c0842b9. Ocho expedientes MO-* y PG-08 creados; 17 specs móviles totales, abiertas. Solo documentación, no integración ejecutada.
- **Decisión/motivo:** D-SDD-25; usuario pide equivalentes Android antes de programar. Conservar UI adelantada y planificar exclusión del recorrido conectado. PG-03/04 conciliados; PG-05/06/07 diferidos donde no hay contrato.
- **Deuda:** privacidad, discrepancias contrato/código, custodia Android real y pruebas; no hay listar grupos/miembros/contratos ni recuperación de contexto. Play y PG-00/01/02 permanecen independientes.
- **A evitar:** presentar prototipo como conectado, borrar pantallas, ejecutar instrucciones del repositorio, copiar aceptación backend o inventar cronología. No se desactivaron botones aún.
- **Siguiente paso:** resolver entradas por tarea y abrir implementación de sesión/acceso/alcance cuando lo solicite el usuario. Sin Kotlin/Gradle/XML, build/tests, dependencias, cuentas reales, commit o push en esta preparación.

Fuente: [matriz canónica](docs/integracion/alineacion-backend-2026-11-04.md), [índice](specs/README.md). Los estados inferiores son antecedentes.

# Revisión visual posterior del Make

El usuario pidió conservar diseño de todas las pantallas e incorporar iconos/imágenes. Se revisan Figma visible y CSS del archivo local; se adaptan tipografías originales Inter/Space Grotesk con OFL, composición por sección, iconos y geometría de ilustración. Debug/Lint/Detekt/9 JVM correctos y dos pruebas físicas pasadas (19 vistas y navegación integrada). Se inspeccionaron 20 capturas nativas ignoradas. Ver revision-diseno-make.md y verificacion-ui-local.md. Aprobación del usuario, contrato e integración siguen pendientes; specs abiertas. Sin push.

# Última corrección — aplicación local integrada

Usuario rechaza catálogo y pide la app completa basada en Make. Se elimina su entrada/actividad y se integran navegación inferior, acceso, inicio, exploración, creación, grupos/perfil, torneos, revisión/evidencia, voz de ejemplo, ranking/notificaciones y acceso auxiliar. Código nativo, sin WebView. Estado efímero y aviso de backend pendiente; no autenticación real. PG-07 abierta, fidelidad visual/dispositivo/contratos pendientes. Las instrucciones históricas de catálogo exclusivo debug ya no describen este encargo. Sin subida a GitHub.

# Continuación vigente — PG-05 UI, 2026-11-03

- **Estado:** PG-05 Explorar preparada e implementada solo en catálogo debug; 9 JVM/0 fallos, debug/Lint/Detekt correctos. Ver checklist PG-05.
- **Decisión:** mantener specs ABIERTAS, con evidencia de avance parcial, sin marcar funciones listas mientras falten revisión/contratos/integración.
- **Deuda:** revisión visual de Explorar, dispositivo y datos/proveedor reales; PG-03/04 siguen parciales.
- **A evitar:** tratar filtrado local o usar idea del catálogo como operación backend, falsear aceptación o cerrar specs por compilar.
- **Siguiente paso:** usuario revisa Explorar; continuar otra UI en expediente delimitado. Creación manual es candidata, aún no implementada por mencionarla. Sin push.

# Última autorización — UI implementada sin backend, 2026-11-03

- **Estado:** usuario autorizó implementar pantallas sin backend. Acceso/registro en arranque y catálogo debug aislado para tablero/perfil/grupos. Builds debug/release, 7 JVM, Lint/Detekt y Gitleaks de fuentes correctos; ver docs/preparacion/verificacion-ui-local.md.
- **Decisión:** sin cuenta/sesión falsas ni integración; catálogo sin entrada release. Sin dependencias/permisos/versiones nuevos.
- **Deuda:** dispositivo/revisión visual, criterios UI completos, contrato y AC funcionales, cobertura/16 KB/Play. CI remoto anterior no verifica este árbol local.
- **A evitar:** presentar pantallas como autenticación, datos sintéticos en producción o pruebas compiladas como ejecutadas.
- **Siguiente paso:** revisión visual/dispositivo; luego contrato e integración. Sin commit/push de este cambio.

# Actualización vigente — diseño UI, 2026-11-03

- **Estado:** usuario confirma solo specs/diseño. PG-03/04 distinguen etapa UI y conexión posterior; mapa de pantallas/estados/eventos en docs/preparacion/diseno-ui-acceso-primer-ingreso.md.
- **Decisión:** avanzar composición visual sin contrato HTTP, manteniendo una frontera clara para integración futura. No se alteran fechas/resultados para cambiar la secuencia real.
- **Deuda:** código UI no iniciado; alcance interno propuesto, contratos/reglas y aceptación funcional pendientes.
- **A evitar:** datos de demo en producción, login fingido, credenciales persistidas, asumir requisitos de Figma o marcar AC funcionales por UI.
- **Siguiente paso:** revisar/aceptar diseño; implementar solo cuando se abra esa fase. Sin commit/push de esta preparación.

# Estado vigente — preparación de evaluación, 2026-11-03

- **Estado:** repositorio móvil publicado en equipopatogg/Movil con Android en raíz. CI remoto de 2a9fb5e PASS en calidad/seguridad, 5 JVM correctas, informes descargados. Ver docs/preparacion/ci-remoto-android.md; no implica instrumentación ejecutada o producto.
- **Decisión/motivo:** preparar specs/esenciales del corte solicitado: PG-03 acceso/sesión y PG-04 primer ingreso; checklist separado de Play y evaluación. Boceto disponible como guía, documentos/decisiones prevalecen.
- **Deuda:** contrato backend, conciliación de recorrido/estilo, funciones, dispositivo/cobertura/16 KB/Play y requisitos generales. PG-04 propone tablero/perfil/grupos; no está ratificado ni implementado.
- **A evitar:** copiar reglas/datos de demo, fingir aceptación, subir backend al repo móvil, versionar notas locales o transferir PASS a commits nuevos.
- **Siguiente paso:** revisar/cerrar base build/CI, núcleo y almacenamiento; resolver PG-00/01/02 aplicables y cerrar en paralelo documental contrato/alcance antes de PG-03; después PG-04/Play. Comparación directa en docs/preparacion/correspondencia-specs-profesor.md. Preparación documental actual sin commit/push ni cambio de funcionalidades.

El texto inferior conserva antecedentes cronológicos; los estados de Git/CI previos quedaron superados por D-SDD-17.

# Memoria de PactoGG

Último estado, 2026-11-03: backend BE-00–BE-03 implementado/verificado localmente; funciones/contrato/aceptación remota pendientes. D-SDD-15 concilia este avance posterior al arranque documental. D-SDD-16 registra preparación Git con workflows Android centralizados/ajustados, referencias oficiales/exclusiones y verificaciones acotadas; no funcionalidades ni cambio de stack de app.

## Estado actual — preparación para Git

- **Estado:** dos proyectos existentes, tres workflows raíz definidos; backend con 116 correctas/0 fallidas/1 omitida en su ejecución local identificada. Ajustes Android limitados a CI/documentación; evidencia local de política y helpers en informe común (referencia histórica externa), sin build Android nuevo o ejecución GitHub acreditada.
- **Decisión/motivo:** fuente expresa del usuario: preparar el primer contenido Git sin crearlo. setup-gradle v6.4.1 oficial, validación integrada del JAR bajo Movil sin duplicar acción, distribución conservada; permisos mínimos e informes sanitizados ante fallos.
- **Deuda:** funciones/contrato, CI remoto/descarga y negativo de acción Wrapper, fixtures de motores en Linux, cobertura/instrumentación/16 KB, Play, nube/NoSQL/réplicas y pendientes completos del PDF. Corte 3–4 noviembre distinto de evaluación 2 diciembre.
- **A evitar:** usar resultados históricos como tests recién ejecutados, confundir infraestructura con funciones, publicar credenciales/generados o atribuir aceptación humana/remota. Recomendaciones del PDF no son obligaciones nuevas.
- **Slice actual/siguiente paso:** contenido preparado/revisado según informe; iniciar Git solo por autorización posterior. Sin commits/push, firma, despliegue o instalación global.

Entrada: [PREPARACION_SDD](PREPARACION_SDD.md). Decisiones canónicas: [registro](docs/decisiones/registro.md). Esta memoria resume resultados reales; no aprueba specs ni convierte documentos en implementación.

## Antecedente documental — preparación y separación de proyectos, 2026-11-03

- **Estado:** expediente y aplicación reunidos en PactoGG/Movil. Backend/ conserva cuatro specs técnicas BE-00–BE-03, gobierno y plantillas; se completaron CHECKLIST_PR y .opencode con cinco perfiles de lectura y tres guías propias. No hay proyecto .NET creado. Las cuatro specs móviles PG-00–PG-03 siguen en borrador y sus tareas técnicas pendientes. Verificación en docs/preparacion/verificacion.md; copias previas fuera del proyecto.
- **Decisión y motivo:** D-SDD-12 recoge la corrección del usuario: completar únicamente estructura SDD conforme al profesor. Se detuvo la continuación de acceso y se retiró su borrador parcial; las ayudas nuevas son de revisión, sin instalación externa ni invocación. D-SDD-11 conserva la redacción inicial con OpenCode y D-SDD-10 el orden base/CI/molde/compuertas. Conservar Kotlin/Hilt, diseño y casos de uso selectivos; la comparación externa permanece fuera de specs.
- **Deuda y pendientes:** definir specs/contrato del backend, identificar maqueta visual ratificada, precisar primer ingreso, cuenta/canal Play y políticas de sesión/versiones. No son decisiones cerradas por copiar documentos.
- **A evitar:** inventar HTTP/JSON, trasladar aprobaciones o umbrales del ejemplo, declarar pruebas/seguridad/Play verificados por preparar archivos, multiplicar perfiles o skills sin necesidad.
- **Slice actual / siguiente paso:** estructura documental backend completada en el alcance corregido, con comparación de piezas disponibles y futuras. Revisar después las decisiones de BE-00–BE-03; acceso y contrato siguen para una etapa posterior. PG-02 conserva su diagnóstico pendiente. Implementación, instalación, firma, Git y publicación permanecen fuera de la fase actual.

## Antecedente — base técnica, 2026-10-29

- **Estado:** aplicación Android previa conservada. Sus funciones reales, controles y resultados históricos están en docs/verificacion-local.md; no se transfieren como aceptación de los borradores actuales.
- **Decisión y motivo:** preservar el trabajo anterior y no ampliarlo al corregir el alcance a preparación SDD.
- **Deuda y pendientes:** instrumentación final y compatibilidad nativa de bibliotecas, además de decisiones de integración.
- **A evitar:** confundir navegación técnica o pruebas compiladas con flujos reales o pruebas instrumentadas ejecutadas.
- **Slice actual / siguiente paso:** delimitar un ajuste técnico concreto de reutilización cuando se abra la fase correspondiente; el borrador general anterior se retiró.

## Antecedente — ayudas y limpieza, 2026-10-29

- **Estado:** 13 perfiles, 14 skills y 6 comandos locales preparados; no son procesos en ejecución. Se retiraron carpetas de reserva y expedientes de comparación/historia técnica de specs.
- **Decisión y motivo:** mantener solo documentación útil del móvil y corroboración fuera de specs. D-SDD-02/D-SDD-03 conservan el detalle; fuentes y hashes en referencias/profesor/.
- **Deuda y pendientes:** carga efectiva de ayudas en OpenCode no probada; habilidades externas se seleccionarán por necesidad concreta.
- **A evitar:** instalaciones globales o copia literal de stack, credenciales, contratos y estados del profesor.
- **Slice actual / siguiente paso:** ayudas enlazadas a specs propias y formatos comunes; no crear otras por completar una lista.

## Revisión posterior del usuario — 2026-11-03

Inicio de sesión revisado y apariencia conforme según el usuario («se ve igualito»). Catálogo revisado con observación de interacción rara, pendiente de precisar. No reabrir la aprobación visual del acceso sin nuevo cambio; no transferirla a todo el catálogo, instrumentación o integración. Siguiente paso: precisar el ajuste del catálogo y preparar contrato funcional backend; ninguna subida nueva autorizada por esta observación.

## Último avance: PG-06 interactiva local

Usuario autoriza botones navegables. En catálogo: acceso → tablero; Explorar/detalle → usar idea → borrador → condiciones → rival/acuerdo → resumen/edición → confirmación local. Botón Crear inicia sin plantilla. Datos efímeros y aviso de simulación, no invitación remota ni cuenta creada. PG-05/06 siguen abiertas. Contrato/integración y pruebas de interacción en dispositivo pendientes; sin push.


## Revisión documental actual — 2026-11-05

[Revisión de las 19 specs y sus 84 criterios](docs/pruebas/revision-specs-2026-11-05.md): 60 criterios parcialmente comprobados, cinco sin caso específico y 19 criterios de calidad con controles locales parciales. Se vinculan tareas, casos y resultados existentes; no se ejecutaron nuevas pruebas por esta edición. Los estados anteriores se conservan como historial. No hay aceptación completa ni publicación.


## Pruebas pendientes ejecutadas localmente — 2026-11-05

[Resultado actual](docs/pruebas/pruebas-pendientes-2026-11-05.md): 237 JVM y nueve Android correctos; 41 casos JVM nuevos. Los cinco criterios sin caso específico tienen comprobación local de consumidor. No equivale a cerrar íntegramente las 19 specs; se conservan límites por criterio y aceptación humana/proveedor pendiente. Sin cambios visuales ni entrega a GitHub. Los recuentos de revisiones anteriores son históricos.


## Ajustes visuales locales — 2026-11-05

Zona segura dinámica, mensajes contextuales y confirmaciones modales implementados. [Resultado UX-01](docs/pruebas/ajustes-ux-2026-11-05.md): 239 JVM, 14 Android y controles locales correctos. Revisión visual del usuario pendiente. Sin Git/publicación; recuentos anteriores conservados como históricos.
