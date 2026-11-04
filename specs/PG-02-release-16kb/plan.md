# Plan — PG-02

Estado: propuesta condicionada a seleccionar solución y entorno. [Spec](spec.md).

## 1. Diseño y secuencia

| Paso | Cambio / ruta | Dependencia | Criterios |
|---|---|---|---|
| 1 | Reproducir resolución y diagnóstico de AAR/binarios, incluido graphics-path | Fase técnica y herramientas verificadas | PG02-AC1 |
| 2 | Contrastar diagnóstico con criterios oficiales; comparar soluciones solo si se confirma incompatibilidad | Evidencia del paso 1 y procedencia de alternativas | PG02-AC2 |
| 3 | Registrar decisión y modificar solo Gradle/catálogo necesarios; sin cambio si el diagnóstico se corrige con evidencia | Alternativa ratificada o diagnóstico corregido y demostrado | PG02-AC2 |
| 4 | Generar candidato release, identificar hash y SBOM e inspeccionar todos los binarios/empaquetado | Toolchain revisada y configuración segura | PG02-AC3, PG02-AC5 |
| 5 | Verificar instalación/arranque/recorrido en 16 KB y contraste acordado | Entorno e identidad local de prueba autorizados | PG02-AC4 |
| 6 | Actualizar docs/versiones.md y docs/seguridad/controles.md; cerrar límites | Evidencia del candidato, no de otra variante | PG02-AC5 |

No se fija una versión de reemplazo sin comprobarla. Si la solución exige modificar comportamiento fuera de este alcance, revisar la spec antes de implementarlo.

## 2. UI, navegación y DI

Preservar pantallas y Hilt existentes. El smoke verifica ausencia de regresiones visibles del cascarón; no acredita registro/login ni otros flujos no implementados. No pedir permisos nuevos ni cambiar el diseño para ocultar fallos de una dependencia.

## 3. Verificación y recuperación

Inspeccionar ELF, empaquetado y ejecución del mismo candidato, dejando versión/hashes/ABI/entorno. Comprobar también pruebas JVM/Lint/Detekt afectadas por cambios de dependencias. Para paquetes no directamente instalables, identificar el artefacto derivado y la relación con el candidato. Verificar por separado artefactos firmados/distribuidos posteriores.

Ante regresión, revertir el cambio de dependencia/configuración acotado y conservar incidencia abierta; no publicar el candidato problemático ni desactivar validadores. Un entorno sin recursos produce ERROR, no PASS.

## 4. Gates y cierre

PG02-AC1–AC5 con evidencia pertinente. No cerrar por compilar o por inspeccionar un solo AAR. Conciliar [tareas](tasks.md), [checklist](checklists/checklist.md), decisión de versión y memoria. La preparación actual deja implementación y verificaciones sin ejecutar.
