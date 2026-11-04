# Preparación y cierre del trabajo

Estado: guía documental integrada el 2026-10-29. No acredita un sprint, una historia aceptada ni ejecución técnica. Scrum gestiona el equipo; SDD aporta el expediente técnico. No se asignan personas o fechas del equipo por inferencia.

## Preparado para revisión documental

La petición tiene alcance y fuentes identificados; distingue hechos, propuestas, conflictos y aplazamientos. La spec expresa resultados verificables; el plan y las tareas enlazan esos resultados. Las incógnitas que afectan el trabajo se registran con impacto y con el rol que deberá resolverlas, sin inventar respuestas.

## Preparado para implementar una spec

- Existe una instrucción del usuario que abre esa implementación y delimita rutas y resultados.
- Las decisiones que condicionan la tarea están ratificadas o el alcance se reduce expresamente para no depender de ellas.
- Spec, criterios, diseño y contrato aplicables tienen fuente y versión; los IDs proceden de las historias originales.
- El plan identifica arquitectura, dependencias justificadas, riesgos, pruebas, seguridad y evidencia esperada.
- Las tareas son acotadas y verificables; no activan instalaciones, cuentas, firma o despliegue ajenos a su autorización.
- Se decide qué base anterior se reutiliza y cómo se tratarán sus incidencias conocidas.

Las decisiones no relacionadas pueden seguir pendientes. La revisión debe señalar cuál impide una tarea concreta, sin bloquear por defecto toda la documentación.

## Cierre documental de esta fase

Documentos enlazados, decisiones abiertas visibles, trazabilidad, formato y preservación de la base revisados. La evidencia describe inspección documental y sus límites. Las tareas técnicas futuras permanecen pendientes. La ratificación del equipo o docente se registra aparte, solo si existe.

## Cierre futuro de una implementación

Cuando se solicite esa fase: criterios demostrados con pruebas pertinentes y evidencia real; controles aplicables revisados; fallos y excepciones tratados; API/diseño/versiones coherentes; accesibilidad y privacidad revisadas según alcance; memoria y decisiones actualizadas. La ejecución se distinguirá de la mera compilación de pruebas.

La [plantilla de checklist](../../.specify/templates/spec/checklists/checklist.md) permite registrar este cierre por spec. Firma y publicación son pasos posteriores cuando se soliciten; un cierre local no los implica. Los resultados y excepciones siguen los estados de [compuertas](controles-y-compuertas.md).
