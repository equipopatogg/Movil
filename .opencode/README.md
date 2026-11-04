# Ayudas de programación preparadas para PactoGG

Paquete local en formato OpenCode: 13 perfiles, 14 skills y 6 comandos. Se redactaron para PactoGG a partir de conceptos revisados del profesor; no se copiaron sus proyectos ni sus instrucciones literalmente.

- agents/*.md: perfiles de preparación de solo lectura; sin editar, bash ni delegar.
- skills/<nombre>/SKILL.md: guías reutilizables cargadas según contexto.
- commands/*.md: solicitudes documentales con $ARGUMENTS; no contienen ejecución shell.

No hay un agente por proceso ni invocaciones realizadas. No se ha elegido modelo/proveedor ni instalado contenido global. Este paquete no configura MCP, cuentas, hooks ni descargas automáticas.
Los comandos declaran un subagente; al invocarlos en un cliente compatible se iniciará esa revisión. No se han invocado durante la preparación.

Ejemplos futuros dentro de OpenCode, abiertos desde la raíz PactoGG:

- /pactogg-spec-draft <fuentes o spec>
- /pactogg-plan-review <spec o plan>
- /pactogg-sdd-audit <expediente>
- /pactogg-security-plan <spec>
- /pactogg-skills-plan <spec>
- /pactogg-handoff <tarea>

Todos emiten propuestas en la conversación. Integrar cambios documentales sigue la petición vigente del usuario. Los permisos actuales bloquean implementación y comandos; para programar después se debe preparar un perfil con la spec y rutas autorizadas mediante .specify/templates/implementation-agent.md.

La validación de formato y enlaces se documenta en el [informe consolidado](../docs/preparacion/verificacion.md). La detección/carga efectiva no se ha probado con OpenCode en ejecución.
Referencias, decisiones de adaptación y descubrimiento: ../docs/preparacion/repositorios-moviles-y-agentes.md y ../docs/preparacion/descubrimiento-skills.md.

## Gobierno y documentos de apoyo

Todos los perfiles remiten a [constitution.md](../constitution.md) y a la [constitución SDD](../.specify/memory/constitution.md). El [mapa integrado](../docs/preparacion/mapa-estructura-sdd.md) enlaza contrato API, límites, amenazas, MASVS y criterios de cierre. Para continuar una tarea usar la [plantilla de traspaso](../.specify/templates/handoff-template.md). Esta integración no cambia el modo ni permisos de los perfiles.

## Ayudas alineadas con expedientes propios — 2026-10-30

Los perfiles/skills pertinentes remiten a las guías de arquitectura, pruebas y entrega y al primer flujo/contrato. Una spec debe delimitar un cambio implementable y conservar checklists/checklist.md; la plantilla completa está en .specify/templates/spec/. Siguen siendo 13 perfiles, 14 skills y 6 comandos; no se incorporaron herramientas, proveedores, perfiles de implementación o permisos nuevos. Ver [specs](../specs/README.md) y [verificación consolidada](../docs/preparacion/verificacion.md).
