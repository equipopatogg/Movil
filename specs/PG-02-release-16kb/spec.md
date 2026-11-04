# PG-02 — Corregir compatibilidad nativa de 16 KB y verificar release local

Tipo: dependencia/configuración/verificación de artefacto. Fecha: 2026-10-30. Estado: borrador; alternativa de corrección pendiente.
Rector: [AGENTS](../../AGENTS.md) y [constitución SDD](../../.specify/memory/constitution.md).

## 1. Contexto y objetivo

La inspección histórica detectó fin de RELRO no alineado a 16 KB en graphics-path 1.1.0. La compilación previa no resolvió esa incidencia. DataStore 1.2.1 pasó la inspección de sus AAR, pero esto no acredita todos los binarios del paquete final.

El cambio verificará el diagnóstico, resolverá cualquier incompatibilidad confirmada y comprobará el artefacto release local resultante, sin firma de producción ni publicación. Fuentes: [registro y D-SDD-06](../../docs/decisiones/registro.md), [incidencia documentada](../../docs/seguridad/controles.md), [versiones](../../docs/versiones.md).

El final de RELRO señalado históricamente no se toma por sí solo como prueba suficiente de incompatibilidad. Contrastar con los criterios oficiales de segmentos de carga ELF, empaquetado y ejecución; corregir el diagnóstico si la evidencia lo exige, antes de cambiar una dependencia. Referencia: [guía Android de páginas de 16 KB](https://developer.android.com/guide/practices/page-sizes).

## 2. Requisitos funcionales

- RF1: identificar de nuevo la cadena que incorpora graphics-path y los binarios/ABI afectados, con hashes y evidencia de las inspecciones. El diagnóstico anterior se reproduce antes de concluir su estado actual.
- RF2: si se confirma incompatibilidad, seleccionar una versión corregida, alternativa compatible o retirar la dependencia si se demuestra prescindible. Si el diagnóstico no se confirma, documentar su corrección con evidencia, sin cambiar dependencias innecesariamente. No elegir una versión sin evidencia, modificar binarios de terceros ni desactivar validaciones.
- RF3: comprobar alineación ELF y empaquetado de todas las bibliotecas nativas incluidas en el candidato, no solo la biblioteca actualizada.
- RF4: generar el candidato release local y comprobar instalación/arranque y navegación disponible en entorno de 16 KB identificado, contrastándolo con un entorno de 4 KB cuando aplique.
- RF5: ligar versión de fuente, configuración, dependencias/SBOM y hashes a los artefactos inspeccionados. Mantener visibles firma, Play y aceptación funcional pendientes.

## 3. Requisitos no funcionales

Sin credenciales, bypass TLS ni cambios de producto. Preservar el diseño y Hilt. No interpretar advertencia ausente, AAR alineado o assembleRelease exitoso como prueba completa de compatibilidad. La selección de SDK/ABI y criterios de empaquetado se verificará contra las guías oficiales aplicables al implementar.

## 4. Arquitectura y contratos internos

| Ruta o artefacto futuro | Cambio delimitado |
|---|---|
| gradle/libs.versions.toml y app/build.gradle.kts | Solo ajustes de dependencia/configuración justificados por la solución elegida |
| APK/AAB y APK instalable derivado si corresponde | Candidato local con identidad/hash; salidas generadas fuera de control de versiones |
| Informes ELF/empaquetado/dispositivo y SBOM | Evidencia del mismo candidato y ABI presentes |
| docs/versiones.md y docs/seguridad/controles.md | Cadena de dependencia, solución y alcance real verificado |

Si instalar un artefacto exige firma de prueba, definir previamente una identidad local de pruebas autorizada, sin reutilizar credenciales de producción ni dar por configurada Play. Una firma/distribución posterior cambia el artefacto y requiere sus verificaciones aplicables.

## 5. Criterios de aceptación

| ID | Dado / cuando / entonces | Evidencia futura |
|---|---|---|
| PG02-AC1 | Al inspeccionar la resolución actual, se identifica la dependencia introductora, binarios/ABI y condición problemática o su cambio demostrado | Árbol, hashes e informe reproducible |
| PG02-AC2 | Tras verificar el diagnóstico, se aplica una solución de procedencia/compatibilidad verificadas si hace falta, o se corrige el diagnóstico con evidencia; sin parche binario ni validación desactivada | Decisión técnica, diferencias necesarias e inspección |
| PG02-AC3 | Al inspeccionar el candidato, todas sus bibliotecas nativas y su empaquetado cumplen los criterios aplicables de 16 KB | Informe por binario/ABI y artefacto identificado |
| PG02-AC4 | Al instalar/arrancar el candidato comprobable, funciona el recorrido existente en el entorno acordado de 16 KB, con contraste de 4 KB aplicable | Dispositivo, page size, instalación y pruebas/smoke |
| PG02-AC5 | Al cerrar, fuente/configuración/SBOM/hashes se corresponden y se distinguen compatibilidad local, firma, Play y aceptación funcional | Manifiesto y documentación sin afirmaciones de publicación |

## 6. Plan de pruebas

Resolver dependencias e inspeccionar ELF y empaquetado; compilar/verificar JVM, Lint y Detekt pertinentes al cambio. Probar el candidato en entorno de 16 KB y contraste acordado, con errores nativos/arranque visibles. Reutilizar PG-01 si proporciona el entorno apropiado, sin confundir su ejecución debug con este candidato release. No se ejecuta ningún comando en preparación.

## 7. Riesgos, límites y deuda

No se conoce aún una versión de graphics-path ratificada como solución. Retirarla puede afectar dependencias/UI y requiere comprobar uso real. Host/emulador pueden impedir concluir. PG-00 aporta evidencias de CI y PG-01 una vía de pruebas, pero esta corrección puede investigarse independientemente cuando se abra fase técnica. Cuenta/canal Play, firma de distribución, ficha/privacidad y publicación quedan fuera de este cambio; siguen en [pendientes](../../docs/preparacion/pendientes.md).

## Aprobación

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Preparación autorizada | Usuario | 2026-10-30 | Preparar corrección concreta y verificación, sin ejecutarlas |
| Diseño en borrador | Responsables por confirmar | Pendiente | Alternativa de dependencia, entorno e identidad de pruebas por acordar |
| Implementación no iniciada | Pendiente | Pendiente | Incidencia conservada; ninguna versión nueva validada |

Expediente: [plan](plan.md), [tareas](tasks.md), [checklist](checklists/checklist.md).
