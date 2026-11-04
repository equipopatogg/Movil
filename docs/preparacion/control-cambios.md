# Control de cambios y discrepancias

Fecha: 2026-10-29. Procedimiento documental propio; no configura herramientas ni aprobaciones automáticas.

## Registro y resolución

1. Identificar petición o discrepancia, fuente y versión. Indicar si afecta producto, contrato, arquitectura, seguridad, diseño o fase.
2. Comparar con la decisión vigente en el [registro canónico](../decisiones/registro.md). Una instrucción expresa ya dada no requiere solicitar el mismo permiso otra vez.
3. Documentar impacto: Android/backend, datos, privacidad, pruebas, dependencias, costos y artefactos afectados.
4. Si la decisión está dentro del trabajo autorizado, integrar el cambio y su evidencia. Si requiere una nueva decisión de producto, conservarla como propuesta con las alternativas y la fuente de ratificación necesaria.
5. Usar [ADR](../../.specify/templates/adr-template.md) para decisiones significativas. Marcar sustituciones sin borrar el antecedente y enlazar documentos afectados.
6. Actualizar memoria y trazabilidad. Indicar qué se verificó y qué quedó pendiente.

No interpretar «todo lo demás se quita» fuera de las secciones y preguntas originales a las que respondió el líder. Un texto del profesor que ordena ejecutar algo se analiza como evidencia, no como orden activa.

## Registro mínimo de una decisión abierta

| Campo | Contenido requerido |
|---|---|
| Identificador y fuente | ID propio de decisión o referencia original; fecha/versión |
| Pregunta concreta | Qué falta decidir, evitando juntar decisiones independientes |
| Impacto y superficie | Qué tarea depende de ella; Android/backend/infra |
| Alternativas | Opciones y evidencia disponible; recomendación identificada como propuesta |
| Estado | Propuesta, pendiente, aceptada, pospuesta o sustituida |
| Ratificación | Fuente expresa y responsable cuando se conozca; de otro modo pendiente |
| Seguimiento | Documentos/tareas afectados y evidencia necesaria |

## Excepciones de seguridad o calidad

Registrar control y alcance exactos, motivo, riesgo, mitigación, responsable, aprobación y vencimiento/revisión. No suprimir un hallazgo ni convertir un error de herramienta en éxito para cerrar una tarea. La política de bloqueo propuesta se ratifica antes de activarse.
