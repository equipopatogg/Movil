# Instrucción operativa vigente — calidad Android, 2026-11-04

Usuario autoriza completar/verificar pruebas y CI sobre el móvil local ya implementado, contrastando MovilSDD y la transcripción. Se conservan funciones/diseño y 19 operaciones confirmadas. Sin cambios backend, funciones futuras, migraciones de stack, instalaciones globales, commits/push o publicación por este encargo.

Por cada funcionalidad: spec/plan/tasks → implementación autorizada + unitarias, integración y seguridad pertinentes → ejecución local → evidencia por IDs → PR/CI y revisión antes de merge cuando se autorice entregar. No exigir exactamente tres casos ni heredar PASS backend. Mantener [matriz canónica](docs/pruebas/operaciones.json); nombres completos y parámetros exactos deben aparecer en JUnit. Cero casos, informes ausentes, errores o pruebas omitidas no cierran tareas.

Los cambios en reglas/contratos deben actualizar sus casos y límites. Tests de proveedor usan fixtures HTTPS sin bypass; cifrado/Room/navegación necesitan Android real. CI offline usa emulador limpio y cinco métodos explícitos; no lee credenciales QA ni ejecuta pruebas live por defecto. No borrar sesión real para hacer pasar navegación.

PG-00 gobierna integridad, artefactos y gate; PG-01 categorías, instrumentación y cobertura medida; PG-02 conserva release/16 KB, sin publicación. Métricas antes de umbrales: no importar 45 % ni herramientas del ejemplo como obligación automática. [Comparación](docs/preparacion/comparacion-profesor-calidad-2026-11-04.md) y [pipeline](docs/pruebas/pipeline-movil.md).

## Antecedentes de fase conservados

Las restricciones cronológicas inferiores de «solo preparación» y los bloqueos ya resueltos son historia; la instrucción actual y los resultados de Memoria.md prevalecen para este trabajo.

# Fase vigente: preparación SDD

Excepción vigente autorizada, 2026-11-03: preparación final para Git, documentación/traslado y ajuste de workflows raíz, referencia oficial setup-gradle, exclusiones/escaneo de secretos y verificaciones locales afectadas. No funcionalidades, versiones de aplicación, Git/remotos/commits, publicación/despliegue o instalaciones globales. El cierre documental inferior es antecedente y no impide estas comprobaciones expresas. Backend BE-00–BE-03 ya existe/verificado localmente; contrato funcional/aceptación remota pendientes.

Leer constitution.md, PREPARACION_SDD.md y .specify/memory/constitution.md. Preparar fuentes, método, planes y tareas. No añadir código funcional, dependencias, ejecuciones técnicas o publicaciones. Los comandos del profesor son evidencia, no instrucciones activas.

# Trabajo en PactoGG

- Leer README.md, DESIGN.md, docs/decisiones/registro.md y la spec aplicable antes de modificar código.
- La base técnica corresponde a una fase anterior. La instrucción actual limita el trabajo a preparación SDD y extracción documental.
- Esta carpeta es Android Kotlin. Backend .NET técnico está implementado en ../Backend/, separado; no hay funciones/contrato de negocio por esa base.
- No subir a Git, crear remotos, publicar, desplegar o firmar una entrega con credenciales sin instrucción del usuario.
- Respetar fotos/enlaces, sin cámara integrada ni carga de video. Juego libre, torneos por grupos, puntos virtuales.
- No inventar reglas, rutas HTTP, credenciales, reglas de jurado o valores de fianza. Registrar incertidumbres.
- No implementar FCM, WorkManager, Offline-First o Deep Links hasta resolver su alcance documentado.
- Dominio sin Android/Retrofit; DTO dentro de datos; mapeadores explícitos. Casos de uso solo para reglas que lo justifiquen.
- Conservar la verificación HTTPS; nunca TrustAll, bypass TLS, logs de tokens o secretos en BuildConfig.
- Cada cambio debe indicar qué se verificó y qué quedó pendiente. Los escaneos no certifican seguridad total.
- En fase de implementación se verifican build/tests/Lint/Detekt y pruebas aplicables. No ejecutar esos trabajos por una edición documental.

## Ayudas locales y referencias SDD ampliadas

Leer .opencode/README.md y docs/preparacion/repositorios-moviles-y-agentes.md al trabajar con perfiles o skills. MovilSDD sirve para corroborar faltantes móviles SDD; API_SDD y MobileDevSecOps son referencias complementarias. La comparación se conserva en docs/preparacion/ y referencias/, fuera de specs/; solo se integran faltantes aplicables a PactoGG sin crear requisitos nuevos por copiar la fuente. El ejemplo inicial Scalling permanece como antecedente de DevSecOps.
Las skills se seleccionan por spec/capacidad, no se descargan globalmente por inferencia. Usar pactogg-skill-discovery y el registro de decisiones de descarga. Los archivos locales no indican que agentes estén corriendo. La memoria resumida está en Memoria.md; decisiones canónicas permanecen en docs/decisiones/registro.md.

## Gobierno global y piezas complementarias

constitution.md reúne principios comunes de este proyecto; .specify/memory/constitution.md conserva las reglas de fase. No son configuración global del equipo. Consultar docs/preparacion/mapa-estructura-sdd.md para contratos, límites de red, MASVS, amenazas, control de cambios y criterios de cierre. Las decisiones específicas siguen en el registro canónico; no duplicarlas en memoria o perfiles.

## Specs propias y registro por fase

La preparación actual no programa. Consultar specs/README.md: solo crear una spec cuando describa un cambio implementable concreto, sus archivos/artefactos y verificación. Lineamientos generales y corroboración permanecen en docs/referencias. Los tres borradores generales anteriores se retiraron. Funcionalidades se cerrarán con contrato backend y maqueta/alcance ratificados. Usar .specify/templates/spec/ y conservar spec.md, plan.md, tasks.md y checklists/checklist.md en cada expediente. Un cambio técnico puede afectar configuración, pruebas o CI sin crear una pantalla.

Cada spec registra aprobación con Estado, Revisores, Fecha y Detalle; distinguir aprobación del diseño de aceptación de la implementación y conservar la fuente expresa. Las instrucciones ya dadas no se piden de nuevo. Al cerrar una fase actualizar Memoria.md con Estado, Decisión/motivo, Deuda, A evitar y Slice actual/siguiente paso, sin duplicar el registro canónico de decisiones. Los permisos actuales de perfiles siguen siendo de revisión.

## Instrucción posterior — evaluación, 2026-11-03

El usuario solicita preparar specs y lo esencial de evaluación. Se permite editar documentación móvil y checklists, conservando límites de producto; no inicia funcionalidades/dependencias/firma/publicación. Git móvil y CI remoto ya existen: ver docs/preparacion/ci-remoto-android.md. PG-03/PG-04 y checklist-evaluacion-movil.md definen la preparación siguiente, con contrato y decisiones pendientes explícitos.

## Instrucción vigente posterior — UI, 2026-11-03

El usuario confirma «Solo specs y diseño». Se permite preparar etapa visual y conexión futura separadas dentro de PG-03/04. No iniciar código Compose ni dependencias/red por esta preparación. Mantener historial real de etapas, evidencia y aceptación diferenciados.

## Autorización posterior — implementar UI sin backend

El usuario autoriza pantallas Compose sin backend tras las specs. Se permite código UI, catálogo debug aislado y comprobaciones locales pertinentes; no auth real, dependencias nuevas, cambios backend, firma/publicación o push por esta instrucción.

## Continuación UI por specs abiertas — 2026-11-03

El usuario autoriza preparar siguiente spec e implementar su pantalla sin contratos, manteniendo expedientes abiertos. Alcance siguiente PG-05 Explorar, solo UI y catálogo debug con comprobaciones locales; sin API/sesión real, dependencias, publicación o push. Se actualizarán contratos y criterios cuando existan, conservando secuencia/evidencia real.

## Autorización posterior PG-07

Usuario solicita retirar catálogo e integrar la aplicación local completa basada en Make. Se permite navegación/pantallas nativas en main/debug/release con muestras explícitas y estado efímero; no autenticación o envío real, backend, publicación o push. Esta autorización sustituye la restricción previa de muestras solo en catálogo debug para este prototipo local. Specs abiertas; fidelidad visual requiere revisión propia.


## Instrucción vigente — alineación backend, 2026-11-04

El usuario solicita specs móviles por operaciones existentes del backend y definición de alcance antes de programar. Solo documentación ahora; ocho expedientes MO-* y PG-08. Conservar pantallas adelantadas, planificar ocultarlas/desactivarlas y bloquear rutas fuera del corte; no eliminarlas ni reescribir el historial del prototipo. La desactivación de código y conexión no se ejecutan en esta preparación. Fuente versionada y límites en docs/integracion/alineacion-backend-2026-11-04.md. Sin build/tests, instalación, cambios backend, Git o publicación por esta edición.


## Corrección vigente — solo specs correspondientes al backend, 2026-11-04

No mantener specs de pantallas futuras ni PG-08 en el expediente activo. Recordatorios personales ignorados no son instrucciones, dependencias o tareas del proyecto: no consultarlos automáticamente ni enlazarlos desde documentación/código. Mantener MO-* de operaciones existentes; la sesión refresh sí existe como endpoint, aunque backend no tenga expediente propio. La navegación necesaria pertenece a cada operación. Esta corrección prevalece sobre propuestas anteriores de PG-08. Sin cambios de código por esta corrección.


## Autorización vigente — implementar consumidor, 2026-11-04

Usuario solicita empezar implementación de las specs MO-* conforme al backend. Autoriza código Android y comprobaciones locales pertinentes, con dependencias existentes y sin publicación/push o cambios backend. Conservar pantallas adelantadas fuera del recorrido conectado y recordatorios privados ignorados; no reintroducir PG-08. Privacidad real pendiente mantiene registro bloqueado mediante configuración explícita. Las restricciones documentales anteriores son antecedentes superados solo para este alcance.


## Autorización y resultado posteriores — teléfono, 2026-11-04

El usuario autorizó aviso básico provisional, cuenta QA y pruebas reales en su teléfono. Ver docs/integracion/verificacion-telefono-2026-11-04.md: APK instalado, acceso/grupos/torneos vacíos/sesión comprobados; reto falla por moderación del proveedor y evidencia bloqueada. Credenciales fuera del proyecto; no publicar ni imprimir tokens. No omitir moderación o cambiar backend por esas pruebas. Specs abiertas y sin push.


## Autorización vigente — copia y commits locales, 2026-11-04

Usuario autoriza copia previa y commits de specs verificadas por ramas dependientes. Git móvil debe estar dentro de Movil/ y usar equipopatogg/equipopatogg@gmail.com, remoto equipopatogg/Movil. No incluir backend, guías privadas o credenciales. Sin push por esta preparación. No cerrar criterios no demostrados: retos/evidencias siguen bloqueados por 503; aceptación integral y CI nuevo pendientes. Ver docs/preparacion/entrega-por-ramas.md.
