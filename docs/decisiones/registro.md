# Registro de decisiones — 2026-10-29

## Incorporadas

| ID | Decisión | Fuente/estado |
|---|---|---|
| D01 | Android Kotlin; Compose, MVVM y separación ligera por capas | Propuesta móvil adoptada para el cascarón |
| D02 | Backend .NET; no Go/Fiber ni Kotlin/Ktor | Respuesta expresa del líder |
| D03 | Fotos seleccionadas y enlaces; sin cámara integrada ni carga de videos | Respuesta expresa del líder |
| D04 | Torneos por grupos, apodo obligatorio y juego libre | Respuesta expresa del líder |
| D05 | Análisis antes de almacenar contenido | Líder y requisitos del docente |
| D06 | Google Play para evaluación | Líder y PDF del docente |
| D07 | DTO/Mapper, casos de uso selectivos, MockWebServer, Room Testing, Version Catalog, Gitleaks | Aprobaciones previas visibles en conversación |
| D08 | Cascarón local autorizado, sin subir a Git | Instrucción actual del usuario |
| D09 | Un módulo :app; paquetes por responsabilidad | Decisión técnica reversible de esta base |
| D10 | Cuatro pestañas Retos/Torneos/Cuentas/Perfil | Descripción del Móvil, sección 6 |
| D11 | com.pactogg.app provisional; API 24 mínima, target36, compile36.1 | Asistente de Android Studio; confirmar identificador antes de Play |

## Pendientes de producto e integración

- Determinar si CLARIFICACION.docx está ratificado íntegramente. Sus cifras no se asumen aprobadas.
- FCM/push: docente exige push; la decisión previa pospuso FCM. Resolver eventos, proveedor y alcance de entrega.
- WorkManager/Offline-First: pospuestos. SQLite/sincronización es condicional en el PDF; caché no equivale a acciones offline.
- Deep Links pospuestos. OCR opcional; proveedor y costos de IA/moderación sin elegir.
- IA sugiere fórmula del Índice de Palabra; equipo ratifica y backend calcula determinísticamente.
- API OpenAPI, autenticación, token refresh/revocación/logout y almacenamiento seguro de tokens.
- Estados del reto, jurado/consenso, fianzas, puntos, torneos, retención, borrado de cuenta y apelación.
- Moderación antes de almacenamiento frente a URL firmada directa y cuarentena posterior.
- Diferenciar seguridad de URLs y análisis de contenido de enlaces externos; lista de dominios no demuestra contenido apropiado.
- Versión .NET, base NoSQL concreta, nube/costos, dominios, cuenta Play y responsables de firma.
- Maqueta y criterios de aceptación por historia; no se han implementado funcionalidades completas.

## Correcciones pendientes en fuentes externas

Los Excel/Word/ZIP/RAR originales no fueron editados. Aún pueden decir Go, CameraX, clips,
autocompletado de juegos, inscripción individual o moderación después de subir.
Este registro documenta el criterio del cascarón; no altera automáticamente esas fuentes.

## Compatibilidad nativa descubierta en la comprobación

El emulador Small_Phone usa Android 17/API 37.1 con páginas de 16 KB.
Se detectó aviso de compatibilidad en DataStore 1.2.0 y graphics-path transitivo.
Se actualizó DataStore a 1.2.1 (ELF de arm64/x86_64 con LOAD y fin de RELRO alineados a 16 KB)
y graphics-path a 1.1.0 estable. El ELF de graphics-path 1.1.0 aún presenta fin de RELRO no alineado
a 16 KB en la inspección de sus AAR; requiere resolución/validación antes de publicar.
No se modificaron los binarios de terceros ni se deshabilitó el verificador del sistema.
Fuente de criterio: https://developer.android.com/guide/practices/page-sizes .

## D-SDD-01 — alcance corregido por el usuario

Instrucción vigente del 2026-10-29: cerrar el trabajo anterior y preparar SDD con extracción/adaptación de lo útil del profesor. Se conserva la base previa y se detiene su ampliación. La entrada del expediente es PREPARACION_SDD.md; los checks históricos no autorizan nuevas implementaciones.

## D-SDD-02 — referencias móviles y ayudas locales

La petición del usuario amplía la preparación a revisar la cuenta pública del profesor y extraer agentes, skills y ayudas útiles. Se identificó MovilSDD como referencia móvil SDD, API_SDD para backend y otras complementarias. Se preparan perfiles/skills/comandos locales de revisión, sin invocarlos, cambiar la aplicación ni instalar herramientas globales. La descarga de skills externas de implementación se vincula a una spec/capacidad y revisión concreta. Las fuentes y adaptaciones quedan registradas por commit.

## D-SDD-03 — constitución global e integración complementaria

Fuente: petición expresa del usuario de integrar piezas faltantes, incluida la constitución global, 2026-10-29. Se incorpora constitution.md con alcance de proyecto y se mantiene .specify/memory/constitution.md como reglas de fase; no se configura una constitución global del usuario ni se duplican ambas.

Se completan índices, plantillas de checklist/traspaso, checklist por expediente, revisión local/PR futuro, criterios de preparación/cierre y control de cambios. Se prepara coordinación API/límites sin rutas o cifras y revisión amenazas/MASVS para Android, separada de ASVS backend. Perfiles existentes enlazados a ambas constituciones, sin invocarlos ni ampliar permisos. El mapa y la verificación de esta ampliación viven en docs/preparacion/.

Esto integra documentación solicitada; no ratifica reglas de producto abiertas, niveles OWASP, umbrales o decisiones del equipo/docente. No habilita código, instalaciones, firma, Git o publicación.

Corrección posterior del usuario: MovilSDD se utiliza solo para corroborar faltantes y adaptar lo necesario a PactoGG. Se retiró PREP-09 de spec/plan/tasks y la checklist añadida en specs/; el inventario/comparación externa también queda fuera de la spec activa. La corroboración permanece en docs/preparacion/ y referencias/, sin nuevas specs, funciones o herramientas por similitud con el profesor.

Limpieza posterior solicitada: retirar carpetas de reserva Backend/LandingPage/infra/api y expedientes specs/000-preparacion-sdd y specs/base-tecnica. La información útil se conserva en documentación existente de integración, preparación y trazabilidad. El índice specs queda disponible para futuras especificaciones propias; no se elimina código ni configuración técnica.

## D-SDD-04 — preparación propia mientras llega el backend

Fuente: instrucción expresa del usuario del 2026-10-30 de realizar lo posible sin programar y profundizar la corroboración del repositorio del profesor. Se preparan MB-01 base Android, MB-02 calidad/seguridad y ME-01 CI/entrega, con cuatro documentos por expediente; el primer flujo, sus pruebas y la matriz de versiones se preparan sin contrato inventado.

Los nuevos IDs son técnicos propios y los expedientes siguen como borradores. La limpieza anterior conserva valor para carpetas de reserva/comparación; las specs técnicas propias se reconstruyen revisadas, sin restaurar el antiguo historial como aprobado. El contrato del backend y la maqueta/alcance condicionan las specs funcionales.

Se incorpora al expediente el alcance documentado del corte del 3/4 de noviembre y la evaluación general del 2 de diciembre según PDF v3; configuración de cuenta, nube, publicación y desarrollo funcional no se dan por realizados. Se actualizan formatos de aprobación y memoria y ayudas existentes, sin nuevas descargas/instalaciones o invocaciones. La corroboración permanece en docs/referencias y no amplía requisitos de PactoGG.

## D-SDD-05 — specs implementables y carpeta de checklist

Fuente: corrección discutida y autorización expresa del usuario del 2026-10-30. Los borradores MB-01/MB-02/ME-01 eran demasiado generales y se retiran de specs; sus lineamientos útiles se integran en arquitectura, plan de pruebas y compuertas/entrega. No se pierden los requisitos de calidad ni se alteran aplicación o controles ejecutables.

Una spec futura debe delimitar cambio, archivos/artefactos, entradas y verificación. Puede tratar código de app, configuración, pruebas o CI. La plantilla física .specify/templates/spec/ contiene spec.md, plan.md, tasks.md y checklists/checklist.md; al crear un expediente se conserva ese árbol. No se inventa una spec funcional para mostrar una carpeta. La documentación y corroboración general permanecen fuera de specs. No se abre programación, instalación o publicación.

## D-SDD-06 — expedientes concretos con formato de referencia

Fuente: petición expresa del usuario del 2026-10-30 de realizar los cambios documentales y preparar specs semejantes a las del profesor. Se adapta la estructura de siete secciones más aprobación, con plan, tareas y checklists/checklist.md. Las specs definen modificaciones de aplicación/configuración/pruebas/CI, sin copiar requisitos, cifras o resultados externos.

Se crean PG-00 integridad del Wrapper/evidencias de seguridad CI, PG-01 ejecución instrumentada/cobertura JVM, PG-02 corrección nativa de 16 KB/verificación release local y PG-03 registro/login/ciclo de sesión. Se identifican archivos/artefactos, criterios, dependencias y tareas comprobables. Los tres borradores generales antiguos no se restauran; sus políticas siguen en docs. La revisión externa permanece fuera de specs.

La petición autoriza esta preparación, sin constituir aprobación del diseño pendiente ni aceptación de implementación. PG-03 queda condicionado a contrato backend y maqueta/recorrido; no se fijan HTTP/JSON, reglas o bibliotecas sin fuente. Las specs técnicas también registran elecciones pendientes de compatibilidad, entorno y política. Todas sus tareas técnicas siguen sin ejecutar; aplicación, dependencias, scripts y workflows permanecen intactos. Se conserva la fase documental y no se abre Git, instalación, firma o publicación.

## D-SDD-07 — limpieza documental con copia verificada

Fuente: petición expresa del usuario del 2026-10-30 de hacer una copia antes de limpiar. Se crea una copia completa externa en Downloads/Respaldos_PactoGG/PactoGG_antes_limpieza_2026-10-30_133128, con ZIP de los 2,842 archivos originales y manifiesto. Cada archivo se relee del ZIP y se compara por SHA-256 antes de modificar el proyecto.

Se reúnen los cuatro informes de preparación en docs/preparacion/verificacion.md y se retiran sus tres archivos separados. Se elimina integridad-inicial.json por ser idéntico a integridad-agentes-inicial.json, que se conserva. Se simplifican las entradas y el mapa, actualizando memoria, checklist, trazabilidad y referencias. El historial útil queda en este registro y el informe consolidado; los detalles originales son recuperables del respaldo externo, sin una nueva carpeta de archivo dentro del proyecto.

Se conservan aplicación y configuración técnica, los cuatro expedientes PG-00–PG-03, constituciones, fuentes, plantillas, perfiles/skills/comandos y resultados técnicos históricos. No se ejecutan tareas Android, se añaden dependencias, invocan agentes o se instala/publica material. La fase continúa siendo preparación SDD.

## D-SDD-08 — separación móvil/backend, 2026-11-03

Fuente: instrucción expresa del usuario de dividir PactoGG en Movil y Backend, reunir lo existente del móvil y definir después el backend que prepararemos aquí. El proyecto Android ya estaba en Movil: se conserva su ubicación y se trasladan allí documentos, specs, referencias, plantillas, ayudas y definiciones locales de control. No se crea una carpeta Movil anidada. Backend se crea vacío; no se inicia un proyecto .NET ni se decide su estructura por inferencia.

Antes del traslado se crea y verifica una copia completa externa PactoGG_antes_dividir_2026-11-03_120510 en Downloads/Respaldos_PactoGG, con ZIP y manifiesto de 2,839 archivos. Las carpetas raíz quedan limitadas a Movil y Backend; README, AGENTS e ignore comunes orientan el espacio. Se fusionan los dos README móviles conservando las instrucciones de Android Studio como referencias futuras.

La constitución del móvil pasa a versión documental 2 para reflejar el nuevo alcance de responsabilidad y la separación del backend. La fase sigue siendo preparación SDD. Se actualizan rutas de los borradores y el helper local para la ubicación de scripts; el helper no se ejecuta. Los workflows se conservan dentro del móvil como antecedentes y PG-00 registra la revisión de ubicación/configuración necesaria al definir Git; no hay CI activo acreditado.

No se mueve el código Android a otra ruta ni se modifican Kotlin, Gradle, dependencias, manifiestos o binarios; no se ejecutan builds, tests, escáneres, servicios o agentes, ni se publica material. Las decisiones de backend y contratos quedan para la siguiente instrucción.

## D-SDD-09 — revisión API_SDD y propuesta coordinada, 2026-11-03

Fuente: petición expresa del usuario de revisar el API del profesor, su estructura y adaptación a PactoGG, manteniendo el móvil alineado para su conexión después de crear/verificar el backend. Se consulta API_SDD en 03480aee0d761b10c64655de10bfd236ed233ced: árbol de 207 archivos, todos recuperados externamente y contrastados por hashes; lectura dirigida por archivos/secciones, sin ejecución ni auditoría funcional exhaustiva.

Se crea en Backend una entrada, reglas de preparación, propuesta de arquitectura/orden/controles, comparación y manifiesto de metadatos. El árbol futuro y candidatos de specs se describen, sin crear una solución .NET, carpetas técnicas vacías, endpoints, contratos inventados o agentes de ejecución. SQL Server, una instancia, revocación local, reglas/DTO legacy y exclusiones de nube del ejemplo no sustituyen requisitos de NoSQL, réplicas, registro público y despliegue de PactoGG.

La propuesta relaciona cambios futuros de registro/login/logout con PG-03 y la tabla móvil de contrato. El backend del alcance acordado se implementará/verificará primero cuando se autorice; durante ese trabajo se revisará el contrato contra el consumidor, y la conexión Android vendrá después. Hay una sola definición OpenAPI futura, pendiente de ratificar ubicación/esquema, no copias divergentes ni evidencia de integración nueva.

Autorización de preparación separada de aprobación de diseño y aceptación de implementación. .NET 10 LTS es candidato; versión/parches, NoSQL, políticas de sesión, cuatro endpoints adicionales, nube/costos y reglas abiertas permanecen pendientes. No se alteran app, Gradle, scripts/workflows, perfiles o skills, ni se instalan dependencias, ejecutan tests/escáneres/servicios o se crea/publica Git. La comparación sigue fuera de specs.

## D-SDD-10 — base y CI antes de funcionalidades, 2026-11-03

Fuente: precisión expresa del usuario de comprobar primero las specs de orden/CI/pipeline del profesor. Se vuelve a consultar HEAD de API_SDD, que conserva 03480aee0d761b10c64655de10bfd236ed233ced, y se releen las secciones pertinentes de NV-00, NV-01, NV-02, NV-03, NV-03.1 y el workflow, con NV-04/NV-05/NV-06 como etapas posteriores.

La secuencia existe en la fuente: gobierno → base/pipeline → molde → compuertas → comprobación de solución → usuarios/login/logout. Se corrige la propuesta de PactoGG, que agrupaba demasiado los controles en el arranque. Primero se prepararán cambios concretos BE-00 base, BE-01 CI, BE-02 molde y BE-03 compuertas/coherencia; los candidatos de registro, login/sesión y logout pasan a BE-04/BE-05/BE-06. No había expedientes backend creados que renombrar ni historias de producto afectadas por esos IDs propuestos.

Pipeline HTTP y automatización CI son diseños distintos. La comprobación de solución se incluye en base/compuertas si aporta un control real, sin crear otra spec por imitar un número. No se copian SQL, porcentajes, límites o comandos del profesor ni su aprobación. Se actualizan solo documentos de orden y memoria; no se crean specs genéricas, código, helpers, workflows, dependencias o servicios y no se ejecuta el pipeline.

## D-SDD-11 — redacción delegada a OpenCode, 2026-11-03

Fuente: petición expresa del usuario de utilizar OpenCode para crear la preparación y reducir la redacción directa del supervisor. Se ejecuta una sesión documental de OpenCode con un único agente, en una copia temporal de trabajo con herramientas de lectura/escritura Markdown; sin shell, delegación adicional o acciones de aplicación. La configuración del encargo es temporal, sin instalar plugins/skills o cambiar permisos globales. No se incorporan datos de autenticación al proyecto.

OpenCode redacta 26 Markdown nuevos: los cuatro expedientes BE-00 base, BE-01 pipeline CI, BE-02 molde y BE-03 compuertas (16 archivos), índice, constitución propia y de fase, preparación/memoria/checklist y cuatro plantillas. Actualiza README, AGENTS y propuesta del backend. Hay 19 criterios y 25 tareas técnicas pendientes, sin aceptación de implementación. BE-04/BE-05/BE-06 siguen candidatos sin carpetas.

El supervisor revisa el contenido, criterios/tareas/checklists, enlaces/anclas e integridad de las entradas; corrige únicamente referencias al espacio temporal y declaraciones de estado antes de incorporar los Markdown autorizados. La copia de trabajo no modifica el proyecto real durante la redacción. No se trasladan configuraciones temporales, logs de sesión, código del profesor o archivos distintos de Markdown.

Se concilian entrada común, memoria y verificación. No cambian código Android, Gradle, dependencias, helpers, workflows o perfiles/skills del móvil. No se crea aplicación .NET, base de datos, contratos de negocio, servicios, Git, firma o despliegue; tampoco se ejecutan pruebas/escáneres de aplicación. Diseño y decisiones pendientes se revisan antes de abrir la fase técnica.

## D-SDD-12 — corrección a estructura SDD pendiente, 2026-11-03

Fuente: aclaración expresa del usuario de que todavía solicita la estructura faltante conforme al Git del profesor y su respuesta anterior «Seguir solo con documentos». Se detiene la continuación adelantada de acceso. Sus cuatro Markdown parciales se conservan fuera del producto y se retiran del árbol; BE-04/05/06 siguen sin expedientes. BE-00–BE-03 y todas sus tareas/checklists permanecen idénticos.

Una sesión nueva de OpenCode completa directamente en Backend diez piezas documentales: CHECKLIST_PR, índice .opencode, cinco perfiles de planificación/revisión con edit/bash/task denegados y tres guías propias de base, CI/calidad y fronteras API. No se invocan esos perfiles ni se instala el catálogo de 35 skills; descarga NO_REQUERIDA. Las cuatro plantillas disponibles ya cubren el formato del profesor y no se duplican. Se ajustan ocho documentos de gobierno/entrada/propuesta/comparación para distinguir lo preparado de código, pruebas, automatización y entrega futuros, sin crear carpetas técnicas vacías.

El supervisor revisa contenido, nombres, permisos declarados, enlaces/anclas e integridad de archivos y actualiza memoria/verificación comunes. La comparación sigue fuera de specs y no ratifica diseños abiertos. Solo cambian Markdown; no se modifica aplicación o configuración técnica, se crea un contrato, se instalan dependencias, ejecutan pruebas/escáneres, activan servicios o publica material. La carga efectiva de las ayudas sigue sin probarse.

## D-SDD-13 — creación inicial del backend, 2026-11-03

Fuente: el usuario abrió Visual Studio y pidió crear el proyecto; tras detener el control de interfaz con Esc, pidió continuar sin vista. Se abre únicamente la creación inicial de la base y su limpieza/ubicación/verificación, no toda la implementación de BE-00 ni funciones posteriores. La autorización expresa prevalece sobre el cierre documental previo; se actualizan las entradas y reglas de fase con esta excepción limitada.

Se crea ASP.NET Core Web API mediante el asistente de Visual Studio 2026: net10.0, controladores, HTTPS, sin autenticación, contenedores o Aspire; OpenAPI de plantilla solo Development. Se fija SDK estable instalado 10.0.401 con latestPatch y sin prereleases, compatible con Visual Studio 18 según documentación Microsoft consultada. Se conserva Microsoft.AspNetCore.OpenApi 10.0.12 de la plantilla; no se certifica su suministro ni se eligen proveedores de datos. D1/D3 y el resto de D2 siguen abiertos.

Continuación directa: solución en Backend/PactoGG.Backend.slnx con referencia a src/PactoGG.Api/PactoGG.Api.csproj; se retiran controller/modelo/solicitud WeatherForecast, con copia externa previa, y se excluyen salidas .NET/Visual Studio del ignore. Build Debug sin restore correcto, cero errores y advertencias, y listado de solución con una API. Visual Studio ya había generado assets/restaurado dependencias de plantilla; no se instala SDK, extensión o herramienta global. No hay suites de tests, ejecución HTTP/servidor, controles de seguridad ni aceptación completa de BE-00; T1/T2 se registran parciales, casillas abiertas. Android, funciones, CI, datos, Git, firma y despliegue no se modifican/activan. Para continuar en Visual Studio abrir la solución nueva de la raíz Backend.

## D-SDD-14 — estructura física adaptada, todavía sin código, 2026-11-03

Fuente: tras señalar las carpetas ausentes respecto al profesor, el usuario pide completar lo faltante «todavía no code». Se interpreta como autorización de estructura física con guías útiles, sin implementar capacidades ni configuración ejecutable. Esta precisión sustituye la restricción anterior de no preparar esas carpetas; se conserva intacta la base creada en D-SDD-13.

Se añaden 27 Markdown: guías de Controllers/Hosting/Middleware/Health/Logging/Common/Dtos/Validators/Services/Security/OpenApi/Data, índice y cinco áreas de pruebas del ejemplo, pruebas/verificador de calidad propios de BE-03, scripts, SAST, entrega, orientación CI raíz y tres documentos de estado de seguridad/endpoints/límites. No se crean csproj, C#, scripts, YAML, reglas o configuraciones técnicas. Las guías distinguen preparación de implementación y no se incorporan como proyectos a la solución.

La comparación queda en docs, fuera de specs; helpers/configuraciones pendientes se mapean en scripts/README. No se copian migraciones SQL, cuotas, DTO legacy, permisos o resultados externos, ni las 35 skills. .specify/scripts fuente solo contiene orientación; no se duplica su README. Domain/Integrations se valorarán al existir reglas/capacidades reales. Git/ubicación definitiva de CI siguen pendientes; .github/workflows raíz contiene solo orientación, sin modificar workflows móviles.

Se concilian fase, entrada, mapa, checklist y memoria. La verificación se limita a contenido, enlaces e integridad de archivos; no se ejecutan builds, tests, escáneres, servicios o herramientas del profesor, instalan dependencias, invocan agentes o publica material. Tareas y criterios técnicos no cambian a completados.

## D-SDD-15 — base técnica backend posterior, 2026-11-03

Fuente: autorización posterior del usuario para implementar/verificar BE-00–BE-03 y su cierre retomado. Esta amplió el arranque/estructura de D-SDD-13/14: host/DI/configuración/errores/JSON/diagnóstico, cuatro suites, verificadores y SAST/secretos/SCA/SBOM/workflow backend, sin funciones. Resultado local identificado en [verificación backend](../../../Backend/docs/verificacion-base-tecnica.md): RunId b029c954d6bd43aeb26b207d37aa44e3, SDK efectivo 10.0.401, restore locked/build Release exit0, 116 correctas/0 fallidas/1 omitida symlink Windows. Cobertura medida sin umbral; aceptación humana/CI remoto/TLS exterior pendientes. El historial documental anterior se conserva, no describe ausencia actual de backend.

## D-SDD-16 — preparación final de contenido Git y workflows raíz, 2026-11-03

Fuente: encargo expreso del usuario de actualizar entradas, centralizar workflows Android, corregir setup-gradle con repositorio oficial, revisar candidatos/ignore/secretos y contrastar PDF v3; sin crear Git/remotos/commits/push, producto, publicación/despliegue o herramientas globales. Se trasladan android-ci.yml/android-security.yml de Movil/.github/workflows a .github/workflows raíz, junto al backend; se retiran ubicaciones anteriores, no copias. Gradle desde Movil, configs Semgrep/Gitleaks dentro de Movil, mounts fuente read-only y reportes/SBOM con rutas raíz explícitas. Helpers mínimos de evidencia y tests de parsers/estados, sin motores nuevos de aplicación.

Se fija setup-gradle v6.4.1 al commit oficial 8da46b539cd8fe6ab27ca75128919a18d757604c tras dereferenciar tag anotado y revisar action.yml/código. Valida JAR recursivamente desde GITHUB_WORKSPACE, incluido Movil; no se agrega acción redundante. El JAR existente es oficial conocido de 9.0/9.1, y la distribución seleccionada sigue 9.5.0 con su checksum: son controles distintos, no requieren igualdad de versiones. Cache basic/read-only; dependency submission/Build Scan desactivados. Al comprobar setup-java v4 se detectó deprecación explícita oficial; se adopta v6.1.0/Node24 con SHA verificada, **JDK21 y versiones de aplicación intactos**. Checkout/upload se alinean con las referencias oficiales ya comprobadas del workflow backend, sin cambiar ese YAML.

Se amplían exclusiones de material local/firma/credenciales, cachés y generados, sin borrar archivos. Se conservan fuentes/recursos/esquemas Room, locks reales, configuraciones reproducibles y Wrapper. Inventario y escaneo actual de candidatos, validaciones de YAML/política/enlaces y decisión de validez de evidencia backend en [preparación Git](../../../PREPARACION_GIT.md). El YAML backend y sus entradas de huella no se modifican; su integridad se comprueba, no se presenta la suite previa como nueva ejecución. Si esa comprobación detecta cambio, exige ejecución nueva.

Hallazgo de preparación: el escaneo completo detecta cinco digests públicos del manifiesto histórico como generic-api-key. Tras inspección se conserva el original y se acota allowlist Gitleaks a regla/ruta/cinco valores exactos (AND). Cuatro fixtures ejecutadas con el motor real prueban conservación de detección fuera de esos valores/ruta; sin exención a secretos reales, filtro global de hashes o modificación de Semgrep. Informe inicial redactado y resultado final separados en artifacts; no aprobación humana atribuida.

El PDF facilitado en Downloads se relee completo: corte 3–4 noviembre y evaluación general 2 diciembre separados; pendientes funcionales/nube/NoSQL/réplicas/privacidad/push/IA/landing/entrega visibles en [evaluación](../preparacion/evaluacion-unidad-2.md). MCP opcional, Terraform/correo único/GitHub→Play sugeridos; no se añaden cámara/video o reglas de producto por redacción genérica. Preparar archivos/CI no satisface el corte ni acredita runner, permisos efectivos, despliegue o aceptación humana.
