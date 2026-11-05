# Tasks — PG-02

Estado: ejecución local parcial; instalación 16 KB y aceptación integral pendientes. [Spec](spec.md) · [plan](plan.md).

- [x] T02.1 Reproducir resolución/diagnóstico de graphics-path y demás bibliotecas nativas — PG02-AC1; evidencia: cadena, hashes y ABI.
- [x] T02.2 Contrastar diagnóstico con criterios oficiales; evaluar alternativas si se confirma incompatibilidad — PG02-AC2; evidencia: decisión sin versión supuesta o corrección demostrada del diagnóstico.
- [x] T02.3 Aplicar solo ajuste necesario en Gradle/catálogo, preservando validaciones; justificar ausencia de cambio si procede — PG02-AC2; evidencia: diferencias/resolución o diagnóstico corregido.
- [x] T02.4 Generar/identificar candidato release y su SBOM; inspeccionar todos los ELF y empaquetado — PG02-AC3/AC5; evidencia: informes ligados a hashes.
- [ ] T02.5 Verificar pruebas JVM/Lint/Detekt pertinentes e instalación/arranque en 16 KB y contraste acordado — PG02-AC4; evidencia: entorno y resultados, con firma local solo si se autoriza.
- [x] T02.6 Actualizar docs/versiones.md y docs/seguridad/controles.md con alcance/límites reales — PG02-AC5; evidencia: identidad del candidato y pendientes de distribución.
- [x] T02.7 Conciliar criterios, aceptación, checklist y memoria — todos; evidencia: sin trasladar resultados de otra variante/artefacto.

Sin instalación, compilación, firma o modificación de dependencias durante preparación. Cierre en [checklists/checklist.md](checklists/checklist.md).


## Ejecución autorizada posterior — 2026-11-04

[Resultado vigente](../../docs/pruebas/cierre-pendientes-2026-11-04.md): build release, ocho ELF/ZIP y arranque 4 KB correctos; diagnóstico RELRO corregido sin cambiar dependencias. Runtime 16 KB y aceptación integral pendientes. Firma local de prueba autorizada, sin producción/Play.
