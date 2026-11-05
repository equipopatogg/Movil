# Checklist — PG-02

Estado técnico: NO_EJECUTADO. Revisor/fecha pendientes.

- [ ] [Spec](../spec.md), [plan](../plan.md) y [tareas](../tasks.md) coherentes con fase e incidencia.
- [ ] Dependencia introductora, hashes, ABI y diagnóstico actual identificados.
- [ ] Solución de procedencia verificada, sin parche binario ni validación desactivada.
- [ ] Todos los binarios y empaquetado del candidato inspeccionados.
- [ ] Instalación/arranque/recorrido comprobados en entorno de 16 KB y contraste acordado.
- [ ] Pruebas/estático pertinentes revisados tras cambios de dependencia.
- [ ] Fuente, SBOM y hashes vinculados al artefacto inspeccionado.
- [ ] Firma/Play/publicación y aceptación funcional diferenciadas del cierre local; memoria actualizada.

| Criterio | Estado | Evidencia | Pendiente |
|---|---|---|---|
| PG02-AC1 | NO_EJECUTADO | Solo diagnóstico histórico enlazado en spec | Reproducción actual |
| PG02-AC2 | NO_EJECUTADO | Ninguna nueva | Solución verificada |
| PG02-AC3 | NO_EJECUTADO | Ninguna nueva | Candidato e inspección completa |
| PG02-AC4 | NO_EJECUTADO | Ninguna nueva | Entorno y ejecución |
| PG02-AC5 | NO_EJECUTADO | Ninguna nueva | Manifiesto y cierre |

Estados futuros: PASS, FAIL, ERROR, NO_EJECUTADO o NO_APLICA justificado. Preparar este expediente no resuelve la incidencia.


## Ejecución autorizada posterior — 2026-11-04

[Resultado vigente](../../../docs/pruebas/cierre-pendientes-2026-11-04.md): build release, ocho ELF/ZIP y arranque 4 KB correctos; diagnóstico RELRO corregido sin cambiar dependencias. Runtime 16 KB y aceptación integral pendientes. Firma local de prueba autorizada, sin producción/Play.
