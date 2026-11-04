# Método SDD de PactoGG

Fase y entrada vigente: [PREPARACION_SDD.md](../PREPARACION_SDD.md).

1. Registrar fuentes, alcance, hechos, propuestas y discrepancias.
2. Definir resultado y criterios en spec.md.
3. Revisar decisiones y riesgos con su fuente de ratificación.
4. Describir arquitectura, dependencias, pruebas y evidencia en plan.md.
5. Dividir tareas verificables en tasks.md.
6. Cuando el usuario retome implementación, ejecutar la spec acordada.
7. Verificar criterios/controles aplicables y conservar evidencia con límites explícitos.

El profesor aporta ejemplos DevSecOps, pruebas y operación: [extracción adaptada](preparacion/revision-profesor.md). La estructura SDD, las prioridades y reglas de producto pertenecen a PactoGG.
La fase actual prepara documentos. La implementación previa y sus resultados se conservan como antecedentes.

## Gobierno y revisión

Scrum mantiene la gestión del equipo; SDD no reemplaza backlog ni declara sprints ejecutados. La [constitución global](../constitution.md) reúne principios y la constitución SDD mantiene la fase. Aplicar [preparación/cierre](preparacion/criterios-ready-done.md), [control de cambios](preparacion/control-cambios.md) y el [índice de plantillas](../.specify/README.md). El [mapa de integración](preparacion/mapa-estructura-sdd.md) evita mantener copias paralelas de decisiones o contratos.

## Specs de cambios implementables

Las guías generales se mantienen en arquitectura, pruebas y controles. [specs](../specs/README.md) queda reservado a cambios concretos: diferencia respecto al estado actual, archivos/artefactos, entradas necesarias, criterios y verificación. Un cambio de Gradle, XML, pruebas o pipeline también puede tener spec; no hace falta que sea una pantalla.

La [plantilla completa](../.specify/templates/spec/) contiene spec.md, plan.md, tasks.md y checklists/checklist.md. Al crear un expediente se conserva la carpeta de checklist. La primera funcionalidad sigue pendiente de contrato/maqueta; redactar instrucciones no ejecuta el código.
