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
