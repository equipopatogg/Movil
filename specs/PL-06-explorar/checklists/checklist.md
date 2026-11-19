# Checklist — PL-06

## Estado al 2026-11-14

Pantalla compilada y pruebas JVM en verde en local (2026-11-14). Falta la comprobación formal en teléfono.

| Criterio | Prueba | Estado |
|---|---|---|
| PL-06-AC1 | `EvidenceUiTest.navigationKeepsFiveMakeEntriesAndExploreOpensIdeas` (dispositivo) | Compila; sin ejecutar en dispositivo |
| PL-06-AC2 | `DailyIdeasTest.PL_06_sameDayShowsSameFourAndAnotherDayChanges` | PASS JVM local 2026-11-14 |
| PL-06-AC3 | `DailyIdeasTest.PL_06_discardReplacesOnlyThatIdeaAndFiltersApply` | PASS JVM local 2026-11-14 |
| PL-06-AC4 | Sin prueba automática | Pendiente en teléfono |
| PL-06-AC5 | `DailyIdeasTest.PL_06_bundledCatalogIsCompleteAndFitsTheChallengeForm`, `PL_06_invalidRowsAreSkippedAndCategoriesAreMapped` | PASS JVM local 2026-11-14 |

- [ ] Comprobado en teléfono
- [ ] Contenido del catálogo revisado por el equipo
