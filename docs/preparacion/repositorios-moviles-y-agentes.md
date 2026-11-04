# Referencias móviles, agentes y skills del profesor

Revisión: 2026-10-29. Metadatos y huellas: [repositorios-sdd.json](../../referencias/profesor/repositorios-sdd.json). Catálogo de skills candidatas: [catalogo-skills.json](../../referencias/profesor/catalogo-skills.json).

## Hallazgo que actualiza la revisión anterior

La referencia WebAPIDevSecOpsScalling proporcionada inicialmente no era todo el material público del profesor. Al revisar su cuenta aparecieron repositorios SDD con perfiles y skills reales. La conclusión anterior sobre la ausencia de esos archivos estaba limitada al primer repositorio; no describía toda la cuenta.

| Repositorio | Revisión fijada | Perfiles | Skills | Uso en PactoGG |
|---|---|---:|---:|---|
| MovilSDD | 763355ac9b8760cde40143b465079d3f7fa789c5 | 13 | 10 | Corroboración de estructura móvil SDD |
| MobileDevSecOps | ae7fe7a9ab1492f1382039ee77811010f2b2d73f | 0 | 1 | CI, CodeQL y análisis de dependencias Android |
| API_SDD | 03480aee0d761b10c64655de10bfd236ed233ced | 5 | 35 | SDD y responsabilidades del backend .NET |
| WebAPIDevSecOpsScallingSDD | 409239ee317a165fa2cb673b3df9efb7d8ec0cd5 | 3 | 48 | Lecciones de fases, seguridad, contratos y operación |
| WebSDD | 269e525a06eeea68ea3d449af6fc89a1ecf2646a | 6 | 5 | Patrones de revisión, memoria y separación por tareas; Razor no es Android |

Se registraron 188 archivos seleccionados por estructura/metadatos y se realizó lectura dirigida de perfiles, guías móviles, controles y muestras. Las 99 skills son un inventario de candidatas de fuentes; no 99 skills instaladas, únicas por capacidad o auditadas exhaustivamente.
Los archivos de licencia hallados en WebSDD pertenecen a bibliotecas web de terceros y no se tomaron como licencia de todo el proyecto. No se copiaron código de aplicación ni manuales completos; los perfiles/skills de PactoGG se redactaron de nuevo con atribución y adaptación.

## Perfiles de MovilSDD y adaptación

| Perfil fuente | Preparación local PactoGG |
|---|---|
| spec-drafter | pactogg-spec-drafter |
| plan-architect | pactogg-plan-architect |
| task-decomposer | pactogg-task-decomposer |
| traceability-clerk | pactogg-traceability-reviewer |
| layer-guardian | pactogg-layer-guardian |
| endpoints-validator | pactogg-api-contract-reviewer |
| security-reviewer | pactogg-security-reviewer |
| test-writer | pactogg-test-designer; diseña, no escribe/ejecuta tests en la fase actual |
| coverage-delta-reporter | Integrado en test-designer; línea base pendiente, sin duplicar agente |
| docs-keeper y readme-keeper | Un solo pactogg-docs-keeper |
| release-checker | pactogg-release-reviewer; checklist, sin firma o publicación |
| slice-scaffolder | Plan de implementación futura en .specify/templates/implementation-agent.md; fuera de descubrimiento activo |

Se agregaron coordinador SDD, revisor UI y arquitecto backend por las necesidades de PactoGG. Los 13 perfiles locales son propuestas de solo lectura con edición, bash y delegación denegados. No se lanzaron procesos de agentes.

## Skills de MovilSDD y adaptación

| Skill fuente | Skill local propia | Ajuste importante |
|---|---|---|
| android-clean-base | pactogg-android-architecture | Paquetes actuales y casos de uso selectivos; sin modelos inventados |
| compose-mvvm-state | pactogg-compose-state | Eventos/restauración según criterio; no confundir 403 con expiración |
| api-client-integration | pactogg-api-contracts | OpenAPI PactoGG, sin rutas, hosts/IP o campos del profesor |
| auth-session-management | pactogg-session-design | No heredar MVP sin refresh ni cierre con finishAffinity como regla universal |
| store-local | pactogg-local-storage | NoSQL/API y Room son ámbitos distintos; preferencias no cifran credenciales |
| koin-di-generic | pactogg-hilt-di-review | Conservar Hilt de la base; no migrar por copiar instrucciones |
| android-testing-generic | pactogg-android-test-plan | MockWebServer/Room/Compose; sin un test por clase ni porcentajes heredados |
| devsecops-gates-generic | pactogg-devsecops-plan | Mantener plan propio SCA/SBOM/secretos/firma; diferenciar superficies |
| contracts-docs | pactogg-api-contracts + pactogg-traceability-evidence | Una fuente contractual; datos/rates por ratificar |
| mobile-app-ui-design | pactogg-mobile-ui-review | Maqueta oscura naranja, sin glow/sombras decorativas ni autocomplete de juegos |

Se incorporaron además sdd-base, coherencia, backend-api-review y skill-discovery para preparación, revisión y selección contextual. Las dos skills documentales del ZIP anterior aportaban esa intención; no se extrajeron sin cambiar rutas y alcance antiguos.

## Precauciones de adaptación observadas

- MovilSDD usa Koin, compileSdk 37 y valores propios de sesión/red. PactoGG no hereda cambios de stack o versiones por leerlos.
- La guía genérica UI sugiere glow y sombras; contradice el diseño de PactoGG y no prevalece.
- Su guía de auth excluye refresh en su MVP y agrupa 401/403; PactoGG necesita cerrar su propio contrato de sesión y autorización.
- MovilSDD menciona helpers/gates que no están implementados en su carpeta scripts, que contiene README. Una instrucción de ejecutar un helper no prueba su existencia ni resultado.
- Su CI compila androidTest; eso no prueba ejecución en dispositivo. Usa Kover con 45%; MobileDevSecOps configura Kover con mínimo 0. No importar esos umbrales.
- MobileDevSecOps configura dependency-check y supresiones: cada supresión necesita criterio propio, alcance y vencimiento antes de reutilizarla.
- CodeQL, herramientas, versiones prerelease y permisos necesitan validación de compatibilidad antes de activarse. No se copiaron workflows ejecutables en esta revisión.
- Metadata como opencode/slash no crea por sí sola comandos ni un descargador automático. Se prepararon comandos reales Markdown en la ruta documentada.

## Fuentes y formato

[MovilSDD](https://github.com/edelomeza/MovilSDD/tree/763355ac9b8760cde40143b465079d3f7fa789c5), [MobileDevSecOps](https://github.com/edelomeza/MobileDevSecOps/tree/ae7fe7a9ab1492f1382039ee77811010f2b2d73f), [API_SDD](https://github.com/edelomeza/API_SDD/tree/03480aee0d761b10c64655de10bfd236ed233ced), [ScallingSDD](https://github.com/edelomeza/WebAPIDevSecOpsScallingSDD/tree/409239ee317a165fa2cb673b3df9efb7d8ec0cd5), [WebSDD](https://github.com/edelomeza/WebSDD/tree/269e525a06eeea68ea3d449af6fc89a1ecf2646a).
Formato contrastado con [agentes OpenCode](https://opencode.ai/docs/agents/), [skills](https://opencode.ai/docs/skills/) y [comandos](https://opencode.ai/docs/commands/). No se instaló/inició OpenCode para verificar carga en runtime.

## Corroboración profunda del 2026-10-30

Se consultó nuevamente main: conserva 763355ac9b8760cde40143b465079d3f7fa789c5. Árbol completo: 165 archivos. Se recuperaron 139 textos pertinentes fuera del workspace de producto; el manifiesto [revision-profunda-movil.json](../../referencias/profesor/revision-profunda-movil.json) registra rutas, tamaños, hashes y 31 rutas de lectura dirigida. Se revisaron código, pruebas, specs, perfiles y CI; no se ejecutó ninguno. La cobertura incluye inventario/búsquedas y lectura dirigida, no una auditoría funcional exhaustiva de cada archivo.

Las observaciones siguientes son estáticas y limitadas a esa revisión. No son resultados de escaneo, ejecución de pruebas o evaluación del autor.

| Evidencia del ejemplo | Límite encontrado | Adaptación propia |
|---|---|---|
| [TokenLocalDataSourceTest](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/app/src/test/java/com/example/movilsdd/data/local/TokenLocalDataSourceTest.kt) usa InMemoryTokenStore | El roundtrip no prueba cifrado o Keystore real | La guía de pruebas exige evidencia del mecanismo Android para ese criterio; conserva dobles para lógica aislada |
| [AuthSecurityTest](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/app/src/test/java/com/example/movilsdd/security/AuthSecurityTest.kt) inspecciona constructor/tipos y rutas relativas | La forma de una clase no demuestra cifrado ni una conexión TLS real | Separar inspección estructural, pruebas de transporte y almacenamiento real |
| [Única prueba instrumentada](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/app/src/androidTest/java/com/example/movilsdd/ExampleInstrumentedTest.kt) comprueba packageName | No demuestra el smoke de sesión ni UI/Compose que las tareas declaran completado | No transferir checks; criterios propios con pruebas ejecutadas y entorno identificado |
| [AuthContractTest](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/app/src/test/java/com/example/movilsdd/data/remote/AuthContractTest.kt) compara serialización local y fixtures | No contacta un proveedor ni acredita compatibilidad de servidor | Fixtures versionadas más evidencia de integración cuando exista contrato |
| [AuthRemoteDataSource](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/app/src/main/java/com/example/movilsdd/data/remote/AuthRemoteDataSource.kt) agrupa 401/403 | Pierde la distinción autenticación/autorización | Mantener el significado del contrato de PactoGG |
| [LogoutUseCase](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/app/src/main/java/com/example/movilsdd/domain/usecase/LogoutUseCase.kt) espera remoto antes de limpiar | La etiqueta local-first no basta para definir orden, cancelación y revocación | Acordar política y casos de fallo con backend; no heredar finishAffinity |
| [SessionManagerImpl](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/app/src/main/java/com/example/movilsdd/data/local/SessionManagerImpl.kt) conserva expiración y expone validación por consulta | No demuestra renovación/revocación ni continuidad de sesión del requisito de PactoGG | Contrato de sesión explícito, sin TTL ni exclusión de refresh heredados |
| [build.gradle.kts](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/app/build.gradle.kts) incluye localhost/IP y valores del ejemplo | No es configuración pública de producción de PactoGG | URL por entorno y release verificable, sin copiar hosts |
| [ci.yml](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/.github/workflows/ci.yml) ejecuta Lint en static-analysis y compila androidTest | No ejecuta allí Detekt/Ktlint ni pruebas instrumentadas; build final es debug | Revisar cada job real frente a controles exigidos, no inferirlo del README |
| [scripts/README.md](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/scripts/README.md) anuncia helpers futuros | No existen helpers ejecutables en esa carpeta | No copiar referencias a comprobaciones inexistentes ni crear scripts por rellenar |
| [Logger](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/app/src/main/java/com/example/movilsdd/util/Logger.kt) sanitiza texto, pero puede pasar Throwable a Log.e en debug | Esas pruebas de texto no demuestran ausencia de datos sensibles en toda excepción | Revisar también excepciones/logs de debug según datos reales; no se constató filtración concreta |

Se inventariaron 15 archivos JVM terminados en Test.kt y una prueba instrumentada; contar archivos no equivale a contar casos ejecutados o medir cobertura. Las aprobaciones/resultados declarados en documentos del ejemplo no se validaron mediante sus CI o servicios y no se trasladan a PactoGG.

La integración de esta revisión es conceptual: evidencia adecuada al criterio, separación de superficies y coherencia de estado. Las guías propias y los documentos de primer flujo/versiones conservan esas reglas sin rutas, campos, cifras, código o aprobaciones copiadas. Los borradores generales iniciales fueron retirados de specs al precisar que cada expediente debe delimitar un cambio implementable. El catálogo histórico de 99 skills candidatas y los cinco repositorios previos conserva su alcance original; esta revisión no instala otras skills.

## Comparación final con PactoGG — 2026-10-30

Se volvió a consultar HEAD de los cinco repositorios de la tabla inicial. Todos conservan las revisiones fijadas. MovilSDD permanece en 763355ac9b8760cde40143b465079d3f7fa789c5, fechado 2026-10-13T20:38:10Z. Por ello se reutilizó el material recuperado y se contrastó con el estado local posterior a la reorganización de specs. No hay novedades remotas detectadas en esas referencias; esto no constituye una revisión de todos los repositorios de la cuenta ni de ramas distintas.

### Diferencias de preparación y estructura

| Aspecto | Profesor | PactoGG y conclusión |
|---|---|---|
| Gobierno SDD | AGENTS, constitución, memoria y guías | Ya existen. La constitución raíz y la de fase son complementarias; no necesitan ser copias idénticas para cubrir responsabilidades |
| Expediente de cambio | FT-00 a FT-04 con spec, plan y tareas | Tras la petición posterior D-SDD-06 hay cuatro borradores propios de cambios concretos; los manuales generales siguen fuera de specs y no se copian los cinco expedientes fuente |
| Checklist física | FT-00 a FT-03 tienen checklists/checklist.md; FT-04 no lo tiene en el árbol revisado | La plantilla propia sí incluye la carpeta y el archivo. Conservarlo en cada futura spec, sin reproducir esa omisión |
| Perfiles | 13 perfiles, con funciones de implementación | Hay 13 perfiles propios de preparación, solo lectura. La capacidad de programar es una activación futura de fase/permisos, no una descarga pendiente |
| Skills y comandos | 10 skills y convenciones del ejemplo | Hay 14 skills adaptadas y seis comandos locales. El número no acredita calidad; no se detecta una capacidad documental que exija instalar más ahora |
| Arquitectura | Koin, modelos comunes y flujo de login propios | Conservar Hilt y responsabilidades propias. Nombres como Page, Logger o CorrelationId no son carpetas/clases obligatorias: se justifican por un cambio real |
| UI y dominio | Pantalla de login y destino provisional | Falta el flujo funcional propio de registro/login/sesión/primer ingreso; sus reglas, maqueta y contrato siguen pendientes. Su HomePlaceholder no satisface por sí solo el alcance PactoGG |

Las specs del profesor incluyen cambios concretos de Gradle, pruebas, almacenamiento/configuración y CI, además de login. Por tanto, una spec puede definir un cambio técnico verificable; no tiene que ser exclusivamente una pantalla. La condición es delimitar archivos/comportamiento, aceptación y evidencia, evitando convertir políticas generales en expedientes de implementación.

### Diferencias técnicas observadas, sin ejecución

| Control | Comparación estática | Pendiente propio |
|---|---|---|
| Integridad del Wrapper | Su CI tiene validate-wrapper. El nuestro fija distributionSha256Sum y usa setup-gradle, que desde v4 integra validación del JAR; la referencia configurada no resolvió en consulta posterior | Corregir/verificar referencia oficial y alcance bajo Movil/ antes de Gradle; no duplicar acciones. El checksum de distribución cubre otro artefacto |
| Estático y SAST | Su CI invoca Lint y CodeQL. El nuestro invoca Lint/Detekt y Semgrep con reglas locales | Comprobar alcance y efectividad de reglas Kotlin/Android; decidir si CodeQL aporta cobertura necesaria. No es obligatorio duplicar herramientas por coincidencia con el ejemplo |
| SCA/SBOM | Nuestros archivos ya generan SBOM de runtime release y lo revisan con Trivy | Verificar ejecución, correspondencia con el candidato y tratamiento de hallazgos; revisar aparte herramientas/plugins no incluidos en ese alcance |
| Secretos | Gitleaks local configurado para archivos presentes | Incorporar historial cuando exista Git y comprobar exclusiones; no hay escaneo histórico acreditado |
| Cobertura | Su CI invoca koverVerify, con limitaciones ya registradas | No hay una compuerta de cobertura configurada en los dos workflows propios. Elegir medición compatible, línea base y umbral justificado; no heredar 45/80 ni aceptar una comprobación vacía |
| Pruebas instrumentadas | Ambos CI revisados preparan/compilan androidTest sin ejecutarlo en dispositivo | Resolver el entorno y ejecutar casos relevantes de Room/Compose/sesión; la suite final anterior sigue sin aceptación |
| Informes de seguridad | El workflow de calidad propio sube reports, pero android-security no exporta/publica informes estructurados de sus escáneres | Definir informes redactados, estado incluso ante fallo y retención. Los logs de un job no equivalen a un expediente completo de hallazgos |
| Entrega Android | El CI principal del profesor termina en assembleDebug; el propio también construye debug | Preparar validación de release, firma/canal autorizados y evidencia del artefacto. Resolver además la incidencia nativa de 16 KB documentada; esta revisión no la reprodujo |
| DAST y seguridad de negocio | No quedan acreditados por tener SAST, compilación o fixtures locales | Planificar dinámica móvil MASVS/MASTG y pruebas de la API real, con roles/alcance. ZAP contra backend no analiza por sí solo el APK |

Fuentes locales contrastadas: .github/workflows/android-ci.yml, .github/workflows/android-security.yml, Movil/gradle/wrapper/gradle-wrapper.properties y las guías de seguridad/pruebas. Son archivos de una base anterior; no se ejecutaron ni modificaron. La existencia de configuración y los resultados históricos no acreditan el estado de una entrega nueva.

### Orden propuesto para continuar

1. Recibir contrato/specs de backend y cerrar el recorrido/maqueta del primer ingreso y el alcance del corte. Estos datos condicionan registro/login/sesión, no toda la preparación.
2. Revisar qué base se conserva y las decisiones de los cuatro borradores concretos preparados en [specs](../../specs/README.md) por D-SDD-06. Cada expediente tiene cuatro archivos; su creación no abre implementación.
3. Al autorizar implementación, habilitar solo las capacidades necesarias y resolver pruebas, compatibilidad, medición y evidencia conforme al cambio. No descargar globalmente el catálogo de skills.
4. Preparar entrega con titular de Play, identificador, privacidad, custodios de firma y canal definidos. No hay autorización implícita de publicación.

Redis, balanceador, nube, contenedores, Terraform y controles de imagen pertenecen al backend/infra cuando existan; Android necesita conocer sus contratos y efectos de red, no alojar esas carpetas. Koin, hosts/campos del profesor, porcentajes de cobertura y helpers ausentes no se incorporan. FCM, WorkManager, Offline-First y Deep Links mantienen su estado pendiente.

Conclusión documental: la preparación cubre las piezas estructurales útiles observadas. La petición posterior del usuario concretó PG-00 CI, PG-01 pruebas, PG-02 release 16 KB y PG-03 acceso a cuenta en el formato de siete secciones del ejemplo, con plan/tareas/checklist. Corresponden a cambios propios detectados, no a copiar su núcleo o almacenamiento sin necesidad. PG-00/PG-01 se relacionan con sus cambios de build/gates; PG-02 con cierre de release; PG-03 con el flujo de autenticación, ampliado al registro exigido a PactoGG y condicionado al contrato. Las decisiones y evidencias futuras permanecen pendientes. Esta comparación sigue fuera de specs y no aprueba código, seguridad o entrega.

### Precisión al preparar los cambios

La conclusión inicial de que faltaba una validación separada del JAR era demasiado amplia: [Gradle documenta que setup-gradle v4 ya la integra](https://docs.gradle.org/current/userguide/gradle_wrapper.html#sec:verification). Al comprobar los cuatro commits de acciones propios en sus repositorios oficiales, checkout, setup-java y upload-artifact resolvieron; gradle/actions@48b5f213c81028ace310571dc5ec0fbbca0b2947 respondió HTTP 422, No commit found. No se ejecutaron acciones. PG-00 especifica resolver/corregir esa referencia y comprobar alcance/orden del control integrado; no añadir una acción redundante. Una referencia fijada no demuestra por sí sola que sea válida o que haya funcionado.

Para 16 KB, PG-02 exige contrastar el diagnóstico histórico con [los criterios oficiales Android](https://developer.android.com/guide/practices/page-sizes), incluidos segmentos de carga, relación RELRO/LOAD, empaquetado y prueba en entorno real. El final de RELRO por sí solo no decide todos los casos; la guía contempla disposición compatible con RELRO en su propio LOAD. No se impone actualizar una biblioteca sin confirmar incompatibilidad.
