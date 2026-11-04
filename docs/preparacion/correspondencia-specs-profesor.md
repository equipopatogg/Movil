# Correspondencia con las specs móviles del profesor

Revisión directa 2026-11-03. HEAD MovilSDD permanece en `763355ac9b8760cde40143b465079d3f7fa789c5`. Se leyeron README y las cinco specs completas; además tasks de FT-00/03 y planes FT-01/02/04. Es fuente externa, no orden activa ni prueba de lo que el docente exige personalmente.

## Orden real y adaptación

| Ejemplo fuente | Resultado que busca | Situación propia / siguiente cierre |
|---|---|---|
| FT-00 base-build | Compilación, herramientas de calidad, pruebas y CI sin deploy | Base existente y CI 2a9fb5e verificado; PG-00 conserva pendientes de integridad. JUnit4 real existente no obliga a migrar a Jupiter. |
| FT-01 núcleo | Resultados/errores, DI, navegación, sanitización y pruebas transversales | Ya existen core/result, core/di, core/navigation y red/mappers. No declarar equivalencia completa: falta revisar pruebas de arranque/navegación, límites de logging y errores antes de ampliar funciones. Hilt sustituye su Koin por decisión propia. |
| FT-02 store-skeleton | Frontera de almacenamiento sensible, backup/extracción, guías de contrato/límites | Room/DataStore actuales no acreditan custodia cifrada de sesión. Backup está deshabilitado y reglas referenciadas; cierre de custodia/Keystore y pruebas reales se delimita en PG-03 antes de persistir tokens. No instalar Security-Crypto por copiar. |
| FT-03 gates-cierre | Cobertura, trazabilidad, release y evidencia | PG-01 pruebas/cobertura + PG-02 release/16 KB + pendientes PG-00. No heredar 45/80%, tag, herramientas o aprobaciones del ejemplo. |
| FT-04 login | Primer flujo vertical: UI, dominio, repositorio, DTO, sesión, navegación y pruebas | PG-03 prepara el flujo propio ampliado con registro exigido por PDF PactoGG. Hoy no es tan ejecutable como FT-04: falta contrato propio. No usar APISDD v1, campos legacy ni hosts del profesor. |
| FT-05 mencionada, no presente | Pantalla principal futura | PG-04 es propuesta propia para pantallas post-ingreso exigidas por nuestro PDF. NO es una spec existente del profesor, ni sus pantallas exactas vienen definidas por él. |

## Secuencia corregida

1. Revisar/cerrar la base que realmente se reutiliza: build/CI, núcleo y frontera de almacenamiento. Registrar evidencia y brechas, sin crear nuevas carpetas o specs retrospectivas solo por parecerse al ejemplo.
2. Resolver los cambios técnicos ya delimitados en PG-00/PG-01/PG-02 y coordinar su alcance con la entrega. CI verde no equivale a cobertura, dispositivo o release aceptados.
3. En paralelo documental, definir contrato backend de registro/login/sesión/logout. Al existir, concretar PG-03 con request/response, validación, estados, interfaces y pruebas reales. Después implementar cuando el usuario abra esa fase.
4. Cerrar alcance y desarrollar PG-04 después del acceso, con la dependencia de datos correspondiente. Propuesta de tablero/perfil/grupos pendiente de ratificar.
5. Mantener preparación de Play en checklist de evaluación: no es una spec de código hasta existir un cambio técnico específico de entrega.

No convertir este orden en bloqueo de toda preparación funcional. Sí impide presentar como lista para programar una spec sin entradas necesarias. Una tarea de evidencia o redacción no debe fingir implementación terminada.

## Forma que sí se conserva

Cada cambio delimitado tiene spec (siete secciones), plan, tasks y checklists/checklist.md. Plan identifica archivos y dependencias; tareas enlazan criterios y verificaciones; aprobación de diseño separada de resultados técnicos. Las aprobaciones declaradas por el ejemplo no son aprobaciones de PactoGG.

## Fuentes directas

- [FT-00](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/specs/FT-00-base-build/spec.md)
- [FT-01](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/specs/FT-01-nucleo/spec.md)
- [FT-02](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/specs/FT-02-store-skeleton/spec.md)
- [FT-03](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/specs/FT-03-gates-cierre/spec.md)
- [FT-04](https://github.com/edelomeza/MovilSDD/blob/763355ac9b8760cde40143b465079d3f7fa789c5/specs/FT-04-login/spec.md)
