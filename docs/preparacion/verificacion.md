# Verificación documental de PactoGG

Este informe reúne las comprobaciones de preparación. No sustituye los resultados técnicos de docs/verificacion-local.md ni acredita seguridad completa, conformidad OWASP o aceptación de producto.

Estado posterior al historial inferior: backend técnico BE-00–BE-03 implementado/verificado localmente según informe propio (referencia histórica externa). La preparación Git/CI posterior centraliza workflows y comprueba referencias/rutas/exclusiones/secretos según informe común (referencia histórica externa). Las secciones siguientes mantienen fechas/recuentos y límites de sus revisiones históricas; sus declaraciones «sin código», «sin aplicación backend» o «sin YAML» no describen el estado vigente. Sin aceptación remota o funcional atribuida.

## Estructura física preparada sin código — 2026-11-03

D-SDD-14 registra la instrucción de completar las carpetas faltantes del profesor todavía sin programar. Se añaden 27 Markdown: guías en doce áreas API, índice y cinco áreas de pruebas del ejemplo, preparación del verificador BE-03 y sus pruebas, scripts, SAST, entrega, orientación CI raíz y documentos específicos de seguridad/endpoints/límites. Las guías no contienen implementaciones, csproj, scripts o YAML; no se añaden proyectos a la solución. Configuraciones comunes se describen como pendientes en scripts/README; no se generan archivos vacíos. El alcance formal ASVS y las rutas/cuotas de producto siguen pendientes.

Comprobación documental: 175 Markdown y 644 enlaces locales/anclas, sin destinos faltantes. Se comparan hashes contra una instantánea previa externa, excluyendo salidas/cache del IDE y compilaciones: 27 altas Markdown, sin eliminaciones y modificaciones solo Markdown. Ningún archivo técnico existente o expediente de spec backend cambia; solución, SDK, csproj, C#, configuración y workflows anteriores permanecen idénticos. El registro detallado queda en la carpeta temporal pactogg-estructura-sin-codigo-mmzj11fv, fuera del proyecto.

Se concilian mapa, fase, checklist, entradas y memorias. La comparación usa la fuente API_SDD fijada ya recuperada; no afirma novedades remotas o ejecución del ejemplo. No se repite compilación ni se ejecutan tests, escáneres, servicios, descargas, agentes o publicación. La existencia de carpetas no acredita capacidad técnica implementada; los resultados del arranque anterior conservan su alcance histórico.

## Creación inicial backend — 2026-11-03

D-SDD-13 registra la autorización posterior para crear la plantilla en Visual Studio y continuar sin interfaz. Se crea net10.0 con controladores, HTTPS y OpenAPI Development; sin auth, datos o contenedores. SDK 10.0.401 fijado con latestPatch. Se conserva OpenAPI 10.0.12 generado por la plantilla, sin evaluación SCA nueva. Solución trasladada mediante edición de archivo a Backend/PactoGG.Backend.slnx, con referencia src/PactoGG.Api/PactoGG.Api.csproj. Modelo/controller/solicitud del clima retirados; copia previa de esos archivos y solución original en carpeta temporal externa pactogg-base-creada-20261103-133618. Ignore ampliado para .vs/bin/obj y preferencias locales .NET.

Comandos de verificación desde Backend: `dotnet build PactoGG.Backend.slnx --no-restore -c Debug --nologo` finaliza correctamente con cero errores/advertencias; `dotnet sln PactoGG.Backend.slnx list` enumera solo src/PactoGG.Api/PactoGG.Api.csproj. Visual Studio ya había generado project.assets.json antes de esta comprobación. No se ejecuta servidor ni se cambia confianza de certificados. No hay suites de tests todavía; no se atribuyen pruebas o controles PASS por compilar. BE00-T1/T2 parciales, AC1–AC5 sin aceptación completa, resto de BE-00 pendiente. La documentación de fase/memoria se concilia sin habilitar tareas adicionales. No se usa interfaz en la continuación ni se publica material.

## Estructura backend corregida — 2026-11-03

D-SDD-12 registra la precisión del usuario: completar estructura conforme al profesor, todavía sin avanzar con acceso. Se detuvo la sesión anterior y se retiraron sus cuatro Markdown parciales a recuperación externa; no quedan expedientes BE-04/05/06 ni una carpeta specs accidental en la raíz. Una sesión nueva de OpenCode redactó directamente en Backend diez Markdown: CHECKLIST_PR, índice .opencode, cinco perfiles de lectura y tres skills propias. Actualizó ocho documentos existentes; no reescribió BE-00–BE-03 ni sus tareas/checklists. Las cuatro plantillas ya disponibles no se duplicaron.

Revisión del supervisor: 148 Markdown y 578 enlaces locales con anclas, sin destinos faltantes. Se contrastaron hashes contra la instantánea previa al encargo: diez altas Markdown, ninguna eliminación y cambios solo documentales; código, dependencias, configuración técnica y manifiestos de fuentes intactos. Los cuatro expedientes backend se conservan idénticos. Se revisaron nombres/descripciones de skills y los cinco permisos edit/bash/task denegados. El validador oficial de skills no se ejecutó porque PyYAML no está disponible; no se instaló la dependencia. La revisión directa de campos, contenido y referencias no acredita carga de ayudas en OpenCode.

Se concilian memoria y decisiones comunes. La comparación indica piezas disponibles, completadas y futuras; no se crean carpetas técnicas vacías. Descarga NO_REQUERIDA: las tres guías son propias y no se instala el catálogo del profesor. Los cinco perfiles preparados no se invocaron; la sesión documental terminó. Sin proyecto .NET, contratos/endpoints nuevos, ejecución de aplicación/CI, tests, escáneres, servicios, Git, firma o publicación. Los registros de comprobación y la copia documental anterior permanecen fuera del producto.

## Primer bloque backend redactado con OpenCode — 2026-11-03

D-SDD-11 registra la delegación expresa. OpenCode trabajó en una copia temporal con lectura/escritura Markdown y sin shell o subagentes. Un fallo transitorio de conexión se resolvió retomando su misma sesión. La redacción finalizó correctamente; no se incorporan logs, configuración temporal o datos de autenticación al proyecto.

Resultado: 26 Markdown nuevos del backend y actualización de sus tres documentos de entrada/gobierno/propuesta. Los cuatro expedientes BE-00 base, BE-01 CI, BE-02 molde y BE-03 compuertas conservan siete secciones más aprobación y spec/plan/tasks/checklists. Hay 19 criterios y 25 tareas técnicas pendientes; ninguna tarea o criterio técnico se marca realizado. También quedan principios, reglas de fase, preparación/memoria/checklist, índice y cuatro plantillas físicas. BE-04/BE-05/BE-06 siguen candidatos sin carpetas.

El supervisor revisó contenido y límites, correspondencia de criterios/tareas/checklists, estructura y referencias; eliminó menciones al contexto temporal y corrigió declaraciones de estado antes de incorporar. Comprobación final: 138 Markdown y 461 enlaces locales con sus anclas, sin destinos faltantes. Los archivos previos se contrastaron con la instantánea inicial: solo cambian Markdown autorizados; ninguna eliminación o modificación de app, Gradle, dependencias, helpers, workflows, perfiles/skills o manifiestos de fuentes.

Se concilian entrada común, memoria y decisión con la preparación nueva. No se crea aplicación .NET, proveedor de datos, contrato de negocio, servicios backend o CI ejecutable; tampoco hay builds, tests/escáneres de aplicación, instalación, Git, firma o despliegue. La revisión documental automática no reemplaza revisión humana de diseño o aceptación técnica. El registro de archivos y comprobaciones queda en la carpeta temporal de esta delegación, fuera del producto.

## Precisión del orden técnico — 2026-11-03

D-SDD-10 registra la nueva comprobación de API_SDD y la prioridad recordada por el usuario. HEAD conserva la revisión fijada. Se contrastan NV-00/NV-01/NV-02/NV-03/NV-03.1 y la secuencia real de ci-pr.yml: la base, el pipeline, el molde y las compuertas preceden a NV-04 usuarios, NV-05 login y NV-06 logout.

La propuesta backend separa los candidatos BE-00 base, BE-01 CI, BE-02 molde y BE-03 compuertas/coherencia; registro/login/logout pasan a BE-04/BE-05/BE-06 antes de crear expedientes. Pipeline HTTP y CI se distinguen. Solo se ajustan documentos y memoria; no hay specs backend creadas, código o configuración técnica modificados, dependencias o ejecuciones. Enlaces locales revisados: 310, sin destinos faltantes; secuencia de la tabla propuesta comprobada.

## Revisión API y propuesta backend — 2026-11-03

La petición registrada en D-SDD-09 prepara el backend y su correspondencia con el móvil. Backend contiene cinco archivos nuevos: entrada, AGENTS de preparación, propuesta, revisión del profesor y manifiesto de procedencia. No hay aplicación .NET, endpoints, nuevos expedientes de spec ni carpetas técnicas vacías.

API_SDD conserva 03480aee0d761b10c64655de10bfd236ed233ced. Se recuperaron externamente los 207 archivos del árbol, contrastados por SHA-256 y por identificador Git de blob. El manifiesto registra 51 rutas de lectura dirigida por archivo/sección y tres lecturas adicionales de encabezados de planes; no acredita auditoría funcional exhaustiva o ejecución de pruebas. Los cinco perfiles y 35 skills fuente son material de lectura, sin instalación o incorporación como ayudas activas.

Se registran diferencias frente a PactoGG: SQL/una instancia/CRUD administrativo y revocación local del ejemplo frente a NoSQL/réplicas/registro público y sesión compartida por definir. Su archivo Semgrep tiene reglas vacías; esto no demuestra qué reglas pueda obtener su acción remota. Los controles y aprobaciones ajenos no se transfieren. La propuesta de estructura y candidatos de specs son revisables; no se eligen HTTP/JSON, políticas o proveedores pendientes.

Comprobación: 112 Markdown y 310 enlaces locales revisados, sin destinos o anclas faltantes. Los cambios existentes se limitan a Markdown y las altas a cuatro Markdown más un JSON de metadatos. Ninguna eliminación. App, Gradle, dependencias, helpers, workflows, perfiles y skills permanecen iguales a la instantánea tomada antes de esta revisión. Los cuatro expedientes móviles conservan su árbol y tareas técnicas pendientes; PG-03 enlaza la propuesta de coordinación sin declarar recibido o aprobado un contrato.

Se actualizan las entradas, memoria, decisión, recepción de contrato y contexto de evaluación. Se propone verificar primero backend del alcance acordado e integrar Android después, manteniendo el contrato alineado durante ese trabajo. No se instalan o ejecutan herramientas del profesor, builds, pruebas Android/.NET, escáneres, servicios, contenedores o agentes; no se crea Git, firma o publicación. Las siguientes secciones conservan las verificaciones anteriores.

## Reorganización anterior — 2026-11-03

Por petición expresa del usuario, PactoGG contiene dos carpetas principales: Movil y Backend. El proyecto Android conserva PactoGG/Movil como ubicación; sus documentos, cuatro specs, fuentes, plantillas, perfiles/skills y definiciones locales de control se reúnen dentro de ella. No hay Movil/Movil. Backend existe vacío; no se crea un proyecto .NET ni se inicia su implementación. README, AGENTS e ignore comunes quedan en la raíz general.

Antes de mover se creó una copia completa externa PactoGG_antes_dividir_2026-11-03_120510 en Downloads/Respaldos_PactoGG: ZIP y manifiesto de 2,839 archivos, releídos y contrastados por SHA-256. resultado-reorganizacion.json, junto a la copia, registra la correspondencia de rutas antiguas/nuevas; no se deja un archivo de recuperación dentro del proyecto.

Comprobación documental: 108 Markdown y 287 enlaces locales, sin destinos faltantes. Todos los archivos originales tienen ubicación de destino, con fusión documentada de los dos README móviles. Los 2,725 archivos originales bajo Movil/ distintos de su README permanecen idénticos; código, Gradle, manifiestos, dependencias, configuración de Android Studio y binarios no cambian. Las cuatro specs mantienen su árbol y tareas pendientes; se adaptan rutas/ubicación en sus borradores, guías y memoria.

El único ajuste ejecutable es la resolución de carpeta en scripts/check-android.ps1, ahora dentro del móvil; no se ejecutó. Los 58 hashes históricos conservan su registro original: al remapear rutas, 57 contenidos coinciden y el helper tiene ese ajuste explícito. Los YAML y reglas de análisis se trasladan sin modificarse ni ejecutarse. Su activación/ubicación/configuración para el conjunto móvil/backend queda pendiente en PG-00 al definir Git; moverlos dentro de Movil no acredita CI operativo.

D-SDD-08 registra el cambio de organización y responsabilidad. La fase continúa siendo preparación: no hay nuevas dependencias, builds, pruebas Android, escáneres, servicios, agentes, firma, Git o publicación. Los recuentos y rutas de las secciones siguientes describen revisiones anteriores.

## Estado vigente — 2026-10-30

- Cuatro specs concretas: PG-00 integridad/evidencias CI, PG-01 pruebas/cobertura, PG-02 release 16 KB y PG-03 acceso a cuenta condicionado a contrato/maqueta.
- Cada expediente conserva siete secciones más aprobación y cuatro archivos: spec.md, plan.md, tasks.md y checklists/checklist.md.
- 21 criterios y 29 tareas técnicas pendientes. Sus checklists mantienen NO_EJECUTADO; la autorización de preparar documentos no aprueba decisiones abiertas ni acepta implementaciones.
- 13 perfiles de revisión, 14 skills y 6 comandos preparados. Sus archivos no implican ejecución; la carga efectiva en OpenCode sigue sin comprobarse.
- Contrato/URL backend, maqueta/recorrido, sesión, entorno/motor de pruebas, política de cobertura/informes, solución nativa y cuenta/canal Play conservan los pendientes del expediente.

## Limpieza con copia de seguridad

Petición expresa del usuario: hacer una copia antes de limpiar. Respaldo completo externo PactoGG_antes_limpieza_2026-10-30_133128, guardado en Downloads/Respaldos_PactoGG; contiene PactoGG_completo.zip y respaldo-verificado.json.

El ZIP incluye los 2,842 archivos originales, ocultos y salidas generadas, con un total de 140,159,743 bytes de contenido. Cada archivo se leyó de vuelta del ZIP y se comparó por SHA-256; se comprobó también el origen antes de limpiar. El archivo comprimido ocupa 51,928,739 bytes. La copia no se guarda dentro del proyecto ni se publica.

Cambios de limpieza:

- Se reúnen en este documento los cuatro informes de preparación: verificación inicial, ayudas, integración SDD y specs. Los tres informes separados se retiran; no se pierden las decisiones canónicas ni sus fuentes.
- Se retira integridad-inicial.json porque su contenido coincide exactamente con integridad-agentes-inicial.json: los mismos 58 archivos y hashes. Se conserva esta última referencia y se actualizan menciones.
- Se simplifican README, PREPARACION_SDD y el mapa; se concilian checklist, memoria, trazabilidad y enlaces.
- Aplicación Android, configuración técnica, fuentes del profesor, cuatro specs, plantillas y perfiles/skills/comandos se conservan. Solo el README explicativo de las ayudas cambia sus referencias al informe unificado.

Comprobación posterior: 107 Markdown y 285 enlaces locales revisados, sin destinos faltantes. Frente al respaldo hay nueve documentos Markdown modificados, cuatro archivos documentales retirados y ninguno añadido. Los 2,726 archivos de Movil/ y los 17 de specs/ permanecen idénticos por SHA-256; también los 58 archivos de la referencia técnica. Estructura de los cuatro expedientes conservada, con tareas pendientes. El registro detallado resultado-limpieza.json se guarda junto al respaldo externo.

## Verificaciones anteriores conservadas

| Revisión | Alcance y resultado histórico | Límite |
|---|---|---|
| Preparación inicial, 2026-10-29 | 31 archivos/prácticas de Scalling registrados; JSON/CSV coherentes; 58 archivos técnicos preservados | Lectura dirigida; no ejecución del profesor |
| Ayudas locales, 2026-10-29 | 13 perfiles con permisos edit/bash/task denegados; 14 skills con formato/nombres validados; 6 comandos con destinos existentes | No prueba de modelos o carga en OpenCode |
| Integración de gobierno | Constitución global y de fase, plantillas/checklist, contrato/límites, amenazas y MASVS; cinco fuentes MovilSDD contrastadas por hash | Corroboración fuera de specs, sin ratificación de reglas abiertas |
| Borradores generales y reorganización, 2026-10-30 | MB-01/MB-02/ME-01 retirados y guías consolidadas; plantilla física con checklists/checklist.md; 58 archivos técnicos sin cambios | Historial en D-SDD-04/D-SDD-05; no specs vigentes |
| Specs concretas, 2026-10-30 | 16 documentos en cuatro expedientes, 21 criterios y 29 tareas pendientes; 110 Markdown y 281 enlaces locales sin destinos faltantes antes de esta limpieza | Diseño en borrador; sin ejecución técnica nueva |
| Fuentes móviles | Árbol de 165 archivos, 139 textos recuperados y 31 rutas de lectura dirigida; manifiesto en referencias/profesor | Inventario/lectura dirigida, no auditoría funcional de todos los archivos |

Los recuentos históricos de enlaces describen aquellas revisiones, no el árbol actual. Los informes completos originales y la copia del duplicado JSON permanecen recuperables en el respaldo externo.

Se contrastaron documentalmente escenarios de sesión sin contrato, cambio injustificado a Koin, estilos ajenos a DESIGN, descarga masiva de skills y reportes ausentes/cero pruebas. Las ayudas escritas conservan contrato pendiente, Hilt, diseño propio, selección contextual y prohibición de declarar éxito sin evidencia. No se probaron esos escenarios con agentes ejecutándose.

## Precisiones técnicas registradas, sin ejecución

setup-gradle v4 integra validación del JAR del Wrapper; no se necesita una acción redundante por definición. La consulta oficial anterior no resolvió el commit configurado 48b5f213c81028ace310571dc5ec0fbbca0b2947 en gradle/actions (HTTP 422); los de checkout/setup-java/upload-artifact consultados sí resolvieron. PG-00 exige resolver/corregir referencia y comprobar validación bajo Movil/ antes de Gradle. No se observó una ejecución de CI ni se demostró ataque.

PG-02 exige contrastar diagnóstico histórico de 16 KB con LOAD, relación RELRO/LOAD, empaquetado y ejecución antes de cambiar bibliotecas. Un final RELRO aislado no resuelve todos los casos. No se inspeccionaron otra vez binarios ni se ratificó una versión de reemplazo.

## Integridad y límites

La referencia conservada es [integridad-agentes-inicial.json](integridad-agentes-inicial.json). Las fuentes originales y sus metadatos no se modifican. Aprobación documental, aceptación técnica y publicación se registran por separado.

Esta limpieza no ejecuta compilaciones, pruebas Android, escáneres, emuladores, agentes o servicios, ni instala herramientas. No modifica aplicación, Gradle, dependencias, scripts o workflows, configura cuentas, firma, crea Git o publica material. Las verificaciones son de archivos/documentación y recuperación del respaldo.
