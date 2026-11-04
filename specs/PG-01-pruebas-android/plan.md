# Plan — PG-01

Estado: propuesta. [Spec](spec.md). No ejecutar pruebas ni instalar componentes en preparación.

## 1. Diseño y secuencia

| Paso | Cambio / ruta | Dependencia | Criterios |
|---|---|---|---|
| 1 | Inventariar suites y seleccionar entorno Android y motor de cobertura | Versiones/reutilización revisadas y fase técnica abierta | PG01-AC1, PG01-AC3 |
| 2 | Revisar aislamiento de CacheMetadataDaoTest y ShellNavigationTest; corregir solo fallos demostrados | Dispositivo estable y ejecución inicial | PG01-AC1 |
| 3 | Configurar medición JVM en app/build.gradle.kts y catálogo si hace falta | Motor compatible elegido y cambios de dependencias autorizados | PG01-AC3 |
| 4 | Agregar vía manual de instrumentación y resultados en android-ci.yml | Imagen/API/ABI y permisos acordados; coordinar PG-00 | PG01-AC2, PG01-AC5 |
| 5 | Ejecutar, registrar línea base/exclusiones y proponer política medida | Suites e informes reales | PG01-AC4 |
| 6 | Actualizar docs/pruebas.md con entorno, resultados, límites y decisiones | Evidencia anterior, sin copiar resultados históricos | Todos |

El job rápido conserva compilación y pruebas JVM; su éxito no representa el job instrumentado. La línea base cubre el código JVM acordado, sin presentarse como cobertura Compose/dispositivo.

## 2. UI, navegación y DI

Sin nuevas pantallas ni módulos. Las pruebas ejercitan el cascarón existente; no simulan autenticación como si fuera funcional. Reutilizar Hilt y mecanismos de prueba existentes cuando sean adecuados. Cambios de comportamiento detectados fuera del alcance se registran antes de ampliar la spec.

## 3. Verificación y recuperación

Identificar API, ABI, imagen, recursos y versión de herramientas. Ejecutar suites separadas y combinadas; inspeccionar cantidad y nombres de casos, errores y omisiones. Conservar XML, cobertura y diagnóstico sin datos sensibles. Probar el manejo de ausencia/cero pruebas y fallo controlado en copia aislada.

Si falla el entorno, registrar ERROR y conservar evidencia; no eliminar tests ni sustituir ejecución por compilación. Revertir configuración incompatible y documentar alternativa antes de reintentar; no retirar controles anteriores para conseguir verde.

## 4. Gates y cierre

PG01-AC1–AC5 demostrados. Umbral nuevo solo con decisión registrada; mientras tanto, la línea base no permite afirmar un porcentaje mínimo aprobado. Conciliar [tareas](tasks.md), [checklist](checklists/checklist.md) y memoria. La fase documental conserva todos los criterios como NO_EJECUTADO.
