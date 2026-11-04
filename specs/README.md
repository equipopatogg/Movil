# Especificaciones de cambios implementables

Estado actualizado 2026-11-03: cuatro expedientes propios preparados en octubre. PG-00 tiene configuración/verificaciones locales parciales autorizadas en preparación Git; aceptación completa pendiente. PG-01/02/03 conservan tareas futuras/entradas abiertas. Los borradores generales MB-01/MB-02/ME-01 siguen retirados y sus guías en docs.

Una spec corresponde a un cambio delimitado de aplicación, configuración, pruebas o pipeline: estado actual, resultado esperado, archivos/artefactos afectados, contratos/decisiones necesarios y criterios verificables. Redactarla no ejecuta sus tareas. Registro/login tienen un borrador condicionado; su diseño no está cerrado sin contrato backend y alcance/maqueta acordados.

## Expedientes actuales y orden de trabajo

| ID / cambio | Qué se implementaría | Estado / dependencia |
|---|---|---|
| [PG-00 — integridad CI](PG-00-integridad-ci/spec.md) | Validación integrada del JAR, informes/estados y propagación de fallos | Configuración/local parcial; acción/motores/CI real y aceptación pendientes |
| [PG-01 — pruebas Android](PG-01-pruebas-android/spec.md) | Ejecución instrumentada, medición JVM y línea base de cobertura | Borrador; entorno y motor por elegir |
| [PG-02 — release 16 KB](PG-02-release-16kb/spec.md) | Corrección de dependencia nativa e inspección/ejecución de candidato release local | Borrador; solución y entorno de prueba por confirmar |
| [PG-03 — acceso a cuenta](PG-03-acceso-cuenta/spec.md) | Registro/login, ciclo de sesión y navegación al destino ratificado | Condicionado; contrato y maqueta/recorrido no recibidos |

Orden propuesto de ejecución futura: PG-00 → PG-01 → PG-02. La recepción de entradas de PG-03 puede avanzar en paralelo documentalmente; su implementación se ordenará con el corte del equipo. PG-02 puede investigarse independientemente cuando se abra fase técnica. Esta tabla no activa agentes ni trabajos en paralelo.

Cada spec tiene siete secciones: contexto/objetivo, requisitos funcionales, no funcionales, arquitectura/contratos internos, aceptación, plan de pruebas y riesgos/deuda; después registra aprobación. Plan y tareas describen cambios de archivos/código/configuración, no solo actividad administrativa. El formato se adapta al ejemplo; los requisitos y resultados son propios de PactoGG. La comparación externa permanece en [documentación](../docs/preparacion/repositorios-moviles-y-agentes.md).

## Estructura de cada spec

```text
specs/
  <ID-del-cambio>/
    spec.md
    plan.md
    tasks.md
    checklists/
      checklist.md
```

La [plantilla completa](../.specify/templates/spec/) reproduce físicamente ese árbol, incluida [checklists/checklist.md](../.specify/templates/spec/checklists/checklist.md). Se copia cuando exista un cambio concreto; no crear una spec vacía para mostrar la carpeta.

Guías: [arquitectura](../docs/arquitectura/android.md), [pruebas](../docs/preparacion/plan-de-pruebas.md), [controles y entrega](../docs/preparacion/controles-y-compuertas.md). Preparación del [primer flujo](../docs/preparacion/primer-flujo-movil.md) y [contrato](../docs/integracion/contrato-api.md). La corroboración del profesor permanece en docs/referencias.
