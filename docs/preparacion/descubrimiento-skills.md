# Descubrimiento y descarga de skills según la spec

## Distinción necesaria

OpenCode descubre descripciones de skills disponibles y carga su contenido cuando lo necesita. Esto no descarga contenido externo.
La documentación nativa permite skills locales en .opencode/skills/<nombre>/SKILL.md; ese es el formato usado en PactoGG. La carga efectiva depende del cliente, versión y sesión. No se afirma que los perfiles estén corriendo por crear archivos.

No se identificó un paquete concreto «AutoSkills» configurado en los repositorios inspeccionados. Si se pretende otra herramienta con ese nombre, hay que identificar su origen antes de incorporarla. Se preparó la selección contextual nativa y una skill propia de descubrimiento, sin instalar un componente ambiguo.

## Secuencia de selección

1. Leer spec/plan vigente y distinguir preparación, implementación o revisión.
2. Identificar una capacidad faltante y revisar primero las 14 skills locales; cargar solo las pertinentes.
3. Si falta ayuda, buscar por stack y necesidad concreta; usar el catálogo de fuentes como índice, no como recomendación de instalarlo entero.
4. Registrar candidata con URL, commit, ruta, nombre, necesidad de la spec, compatibilidad, permisos, contenido y licencia identificada. Revisar scripts, hooks, dependencias y destinos externos.
5. Resolver conflictos con Hilt, contrato, sesión, maqueta y fase. Adaptar o descartar en vez de permitir instrucciones contradictorias.
6. Cuando haya una petición concreta de instalación para esa spec, descargar a alcance local, sin reemplazar nombres existentes, y validar antes de habilitarla.
7. Registrar fuente/versión/huella, adaptación y resultado; actualizar deliberadamente, no de forma global automática.

Una spec suficientemente definida orienta skills de implementación; no hace falta esperar a todas las specs del producto para preparar las skills de SDD y revisión ya conocidas.

## Herramientas candidatas

La CLI del ecosistema skills documenta find/add y fuentes compatibles con OpenCode. Su uso futuro puede servir para buscar y descargar una skill seleccionada, pero no se ejecutó npx, no se añadió package.json ni se instalaron paquetes en esta preparación.
No usar latest, instalación global o descarga de paquetes enteros sin que la petición y el plan lo justifiquen. La presencia de una skill no autoriza sus scripts ni acciones externas.
La guía skill-installer leída permite destino y ref explícitos para repos GitHub; elegir la herramienta según cliente objetivo y alcance, sin duplicar instalaciones.

## Registro por spec

| Campo | Valor que debe quedar documentado |
|---|---|
| Spec y criterio | ID y capacidad requerida |
| Skill local disponible | Nombre y razón de suficiencia/insuficiencia |
| Fuente candidata | Repositorio, commit y ruta exacta |
| Revisión | Contenido, scripts/herramientas, permisos, compatibilidad y licencia identificada |
| Decisión | Reusar / adaptar / descargar cuando se solicite / descartar |
| Alcance | Carpeta local y nombres/colisiones; sin cambio global implícito |
| Evidencia | Validador, referencias y prueba de comportamiento aplicable |

Fuentes: https://opencode.ai/docs/skills/ ; https://github.com/vercel-labs/skills/blob/main/docs/commands.md .
