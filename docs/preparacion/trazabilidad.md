# Trazabilidad de preparación

| ID | Fuente | Documento | Comprobación |
|---|---|---|---|
| PREP-01 | Corrección del usuario | PREPARACION_SDD, constitución y AGENTS | Una fase vigente y antecedentes identificados |
| PREP-04 | CI/reglas/scripts/muestras del profesor | controles-y-compuertas, plan-de-pruebas | Superficies y evidencia diferenciadas |
| PREP-05 | Método propio SDD | PREPARACION_SDD, CHECKLIST_PREPARACION y .specify/templates | Preparación en docs; plantillas para specs propias futuras |
| PREP-06 | Decisiones y discrepancias previas | pendientes y CHECKLIST_PREPARACION | Reglas abiertas visibles, sin cifras inventadas |
| PREP-07 | Alcance documental actual | verificacion.md e integridad-agentes-inicial.json | Huellas y enlaces comprobados; duplicado idéntico retirado en D-SDD-07 |

PROF identifica prácticas analizadas, no controles aprobados. PREP identifica entregables documentales, no historias del producto.
La trazabilidad funcional se completará con las historias originales; no se creó una lista sustituta de historias de usuario.

## Ampliaciones posteriores

| ID | Fuente | Documento | Comprobación |
|---|---|---|---|
| PREP-08 | Petición de ayudas móviles del profesor | .opencode/, repositorios-moviles-y-agentes, matriz de selección | verificacion.md; 13 perfiles, 14 skills, 6 comandos sin invocación |
| Integración documental | Petición de constitución global y piezas faltantes | constitution.md y mapa-estructura-sdd.md; D-SDD-03 | verificacion.md; fuera de specs por corrección del usuario |

Las antiguas etiquetas PREP-02/PREP-03 identificaban inventario y comparación externa; se retiraron de la spec y sus tareas. Sus evidencias permanecen en referencias/profesor/ y revision-profesor.md como corroboración histórica, sin renumerar otros IDs ni crear requisitos nuevos.

## Consolidación de la preparación

Se retiró la carpeta specs/000-preparacion-sdd: el alcance y el orden de trabajo ya están en PREPARACION_SDD.md; los entregables/documentos en esta matriz y el mapa; el cierre en CHECKLIST_PREPARACION.md. Los IDs PREP se conservan como referencias documentales históricas, sin expediente de spec.

La secuencia futura sigue siendo resolver decisiones aplicables, definir una spec propia, acordar plan/tareas y retomar implementación cuando el usuario la solicite. No se ejecutan tareas técnicas al limpiar carpetas.

## Reorganización de borradores generales — 2026-10-30

Los identificadores MB-01/MB-02/ME-01 pertenecen a una propuesta retirada; ya no identifican specs vigentes ni tareas activas. Su contenido se conserva como guías de arquitectura, pruebas y compuertas/entrega. D-SDD-05 registra el motivo.

La trazabilidad de un cambio futuro será fuente/historia o decisión técnica → resultado implementable → archivos/artefactos → criterio → tarea → prueba/evidencia. El expediente incluye spec.md, plan.md, tasks.md y checklists/checklist.md. No se inventa un cambio para completar ese árbol.

Las historias 11/13/14/15/18/19/21/22 y componentes reales siguen relacionados en primer-flujo-movil.md, sin dar esas historias por completadas. El cierre de diseño funcional depende de contrato y maqueta/alcance acordados.

## Expedientes concretos — D-SDD-06

| Fuente propia | Spec | Criterios | Tareas / evidencia futura |
|---|---|---|---|
| Incidencias de integridad y evidencia en CI; D-SDD-06 | [PG-00](../../specs/PG-00-integridad-ci/spec.md) | PG00-AC1–AC5 | T00.1–T00.7; Wrapper, informes, fixtures y estado de jobs |
| Suite final pendiente y ausencia de medición; D-SDD-06 | [PG-01](../../specs/PG-01-pruebas-android/spec.md) | PG01-AC1–AC5 | T01.1–T01.7; entorno, resultados reales y línea base |
| Incidencia nativa registrada en decisiones/seguridad; D-SDD-06 | [PG-02](../../specs/PG-02-release-16kb/spec.md) | PG02-AC1–AC5 | T02.1–T02.7; solución, binarios, paquete y ejecución identificados |
| Historias 11/13/14/15/18/19 y apodo 21 según alcance; PDF v3; D-SDD-06 | [PG-03](../../specs/PG-03-acceso-cuenta/spec.md) | PG03-AC1–AC6 | T03.1–T03.8; primero contrato/maqueta, luego pruebas e integración |

PG son IDs técnicos propios y AC son criterios del cambio; no sustituyen historias originales. Las historias 12/16/17/22 conservan sus decisiones de alcance abiertas. No hay evidencia técnica nueva ni tareas completadas; cada checklist conserva NO_EJECUTADO. Comparación del profesor y guías generales permanecen fuera de los expedientes.

La limpieza D-SDD-07 reúne las verificaciones en verificacion.md y conserva una sola referencia de los 58 hashes. Las fuentes, decisiones y expedientes no se eliminan. Los informes detallados previos quedan recuperables en la copia completa externa.
