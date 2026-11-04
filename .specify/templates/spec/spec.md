# <ID — cambio implementable>

Tipo: aplicación / configuración / pruebas / pipeline / migración. Estado: borrador. Responsable y fecha por registrar según la fuente real.

## 1. Contexto y objetivo

Qué comportamiento o artefacto cambiará, qué existe hoy y qué resultado nuevo se espera. No repetir la constitución, el manual de calidad o un informe de corroboración como si fueran una funcionalidad.

Historia/IDs originales si aplica, decisión técnica y versiones de contrato/maqueta. Un cambio técnico puede tener ID propio sin inventar una historia. Indicar límites y decisiones pendientes que condicionan el cambio.

## 2. Requisitos funcionales

Definir comportamiento/operaciones o resultados técnicos concretos. Entradas/salidas, estados y errores; no limitarse a «mejorar calidad» o repetir políticas. No inventar HTTP, JSON o reglas pendientes.

## 3. Requisitos no funcionales

Restricciones de seguridad, privacidad, accesibilidad, compatibilidad y rendimiento pertinentes, enlazadas a las guías canónicas. No fijar porcentajes o SLAs por copia.

## 4. Arquitectura y contratos internos

| Archivo/área por crear o modificar | Cambio previsto | Motivo |
|---|---|---|
| <ruta definida por el diseño> | <resultado observable> | <requisito> |

Puede afectar Kotlin, Gradle, XML, pruebas o CI. Debe ser suficientemente preciso para guiar implementación; no exige crear una pantalla si el cambio es técnico.

Identificar responsabilidades, contratos/maqueta versionados y dependencias del diseño. Enlazar reglas mantenidas en docs sin duplicar fuentes o rellenar decisiones desconocidas.

## 5. Criterios de aceptación

| ID | Condición observable | Prueba/revisión y evidencia esperada |
|---|---|---|
| <ID> | <Dado/Cuando/Entonces o condición técnica> | <caso y reporte> |

Distinguir resultado esperado de evidencia ejecutada. Registrar ausencia, omisiones y fallos; un archivo de prueba o un comando exitoso no demuestra por sí solo el criterio.

## 6. Plan de pruebas

Casos por criterio, nivel/entorno/variante y evidencia esperada. Distinguir inspección, compilación, ejecución de pruebas e integración real. No aceptar cero casos o informe ausente como éxito.

## 7. Riesgos, límites y deuda

Qué entrada falta y qué tarea afecta; qué queda fuera y por qué. La documentación general permanece en docs. No abrir implementación por terminar este texto.

## Aprobación y control de cambios

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Borrador | Pendiente de designar | Pendiente | Fuente, alcance y evidencia pendientes; no inventar aprobación |

Separar aprobación de la spec de aceptación de la implementación. Respetar instrucciones ya dadas sin pedir el mismo permiso otra vez.

Expediente: [plan](plan.md), [tareas](tasks.md) y [checklists/checklist.md](checklists/checklist.md).
