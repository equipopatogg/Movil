# Gobierno y plantillas SDD

[Constitución global](../constitution.md) y [reglas de fase](memory/constitution.md). Las guías generales viven en docs; una spec delimita un cambio implementable y verificable.

## Plantilla de expediente

```text
templates/spec/
  spec.md
  plan.md
  tasks.md
  checklists/
    checklist.md
```

| Plantilla | Uso |
|---|---|
| [spec](templates/spec/spec.md) | Diferencia respecto al estado actual, alcance, entregables y criterios |
| [plan](templates/spec/plan.md) | Archivos/artefactos, orden, decisiones y verificación |
| [tasks](templates/spec/tasks.md) | Tareas concretas ligadas a criterios |
| [checklist](templates/spec/checklists/checklist.md) | Revisión/cierre dentro de checklists/ de la spec |
| [revisión](templates/revision-template.md) | Hallazgos y resolución pendiente |
| [ADR](templates/adr-template.md) | Decisión y fuente de ratificación |
| [evidencia](templates/evidencia-template.md) | Resultado real y límites |
| [traspaso](templates/handoff-template.md) | Continuidad con fase y pendientes |
| [perfil futuro](templates/implementation-agent.md) | Implementador únicamente al abrir esa fase |

Copiar el árbol de templates/spec al expediente real conservando checklists/checklist.md. Los cuatro documentos permanecen juntos; el checklist no va suelto en la raíz de la spec. No se instalaron generadores, Spec Kit, hooks o herramientas por preparar esta plantilla.

Aprobación con Estado/Revisores/Fecha/Detalle, separada de aceptación de implementación. Memoria por fase: Estado, Decisión/motivo, Deuda, A evitar y Slice actual/siguiente paso. [Índice de specs](../specs/README.md).
