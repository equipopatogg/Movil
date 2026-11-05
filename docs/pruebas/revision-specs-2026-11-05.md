# Revisión de las 19 specs — 2026-11-05

Se revisaron las **84 condiciones de aceptación** de las 19 operaciones y se relacionaron con sus tareas, pruebas concretas y resultados existentes. Esta revisión es documental: no se agregaron funciones ni pruebas ni se ejecutaron peticiones nuevas al servidor. Todo continúa local, sin commits/push.

El total de pruebas aprobadas no prueba por sí mismo cada condición. Se leyó qué ejercita cada caso, no solo su nombre. Los criterios compuestos permanecen parciales cuando hay alguna parte sin demostrar. Ningún hallazgo se declara fallo de la app sin reproducirlo.

## Resultado de la revisión

- 60 condiciones funcionales tienen evidencia concreta de una parte y escenarios pendientes.
- 5 condiciones no tienen un caso específico de su secuencia completa; los apoyos genéricos se señalan aparte.
- 19 condiciones de calidad tienen controles locales comprobados y conservan la verificación de entrega/otros controles aplicables pendientes. GitHub está diferido por instrucción del usuario.

| Spec | Condiciones | Casos específicos faltantes | Revisión local |
|---|---|---|---|
| [MO-USR-01](../../specs/MO-USR-01-registro/spec.md) | 4 | — | [Tareas, pruebas y alcance](../../specs/MO-USR-01-registro/checklists/checklist.md) |
| [MO-AUTH-01](../../specs/MO-AUTH-01-login/spec.md) | 4 | — | [Tareas, pruebas y alcance](../../specs/MO-AUTH-01-login/checklists/checklist.md) |
| [MO-AUTH-03](../../specs/MO-AUTH-03-sesion/spec.md) | 5 | — | [Tareas, pruebas y alcance](../../specs/MO-AUTH-03-sesion/checklists/checklist.md) |
| [MO-AUTH-04](../../specs/MO-AUTH-04-logout/spec.md) | 4 | — | [Tareas, pruebas y alcance](../../specs/MO-AUTH-04-logout/checklists/checklist.md) |
| [MO-GRP-01](../../specs/MO-GRP-01-crear-grupo/spec.md) | 4 | — | [Tareas, pruebas y alcance](../../specs/MO-GRP-01-crear-grupo/checklists/checklist.md) |
| [MO-CON-01](../../specs/MO-CON-01-crear-reto/spec.md) | 5 | — | [Tareas, pruebas y alcance](../../specs/MO-CON-01-crear-reto/checklists/checklist.md) |
| [MO-EVI-02](../../specs/MO-EVI-02-evidencias/spec.md) | 5 | — | [Tareas, pruebas y alcance](../../specs/MO-EVI-02-evidencias/checklists/checklist.md) |
| [MO-TOR-02](../../specs/MO-TOR-02-consultar-torneos/spec.md) | 5 | AC3 | [Tareas, pruebas y alcance](../../specs/MO-TOR-02-consultar-torneos/checklists/checklist.md) |
| [MO-USR-02](../../specs/MO-USR-02-consultar-perfil/spec.md) | 4 | — | [Tareas, pruebas y alcance](../../specs/MO-USR-02-consultar-perfil/checklists/checklist.md) |
| [MO-USR-03](../../specs/MO-USR-03-editar-perfil/spec.md) | 5 | AC2 | [Tareas, pruebas y alcance](../../specs/MO-USR-03-editar-perfil/checklists/checklist.md) |
| [MO-USR-06](../../specs/MO-USR-06-disponibilidad-apodo/spec.md) | 4 | — | [Tareas, pruebas y alcance](../../specs/MO-USR-06-disponibilidad-apodo/checklists/checklist.md) |
| [MO-GRP-02](../../specs/MO-GRP-02-listar-grupos/spec.md) | 4 | — | [Tareas, pruebas y alcance](../../specs/MO-GRP-02-listar-grupos/checklists/checklist.md) |
| [MO-GRP-03](../../specs/MO-GRP-03-detalle-grupo/spec.md) | 3 | — | [Tareas, pruebas y alcance](../../specs/MO-GRP-03-detalle-grupo/checklists/checklist.md) |
| [MO-MEM-01](../../specs/MO-MEM-01-unirse-grupo/spec.md) | 5 | AC2 | [Tareas, pruebas y alcance](../../specs/MO-MEM-01-unirse-grupo/checklists/checklist.md) |
| [MO-MEM-02](../../specs/MO-MEM-02-listar-miembros/spec.md) | 5 | AC3 | [Tareas, pruebas y alcance](../../specs/MO-MEM-02-listar-miembros/checklists/checklist.md) |
| [MO-CON-02](../../specs/MO-CON-02-listar-retos/spec.md) | 4 | — | [Tareas, pruebas y alcance](../../specs/MO-CON-02-listar-retos/checklists/checklist.md) |
| [MO-CON-03](../../specs/MO-CON-03-detalle-reto/spec.md) | 4 | — | [Tareas, pruebas y alcance](../../specs/MO-CON-03-detalle-reto/checklists/checklist.md) |
| [MO-CON-06](../../specs/MO-CON-06-aceptar-reto/spec.md) | 6 | — | [Tareas, pruebas y alcance](../../specs/MO-CON-06-aceptar-reto/checklists/checklist.md) |
| [MO-CON-07](../../specs/MO-CON-07-rechazar-reto/spec.md) | 4 | AC3 | [Tareas, pruebas y alcance](../../specs/MO-CON-07-rechazar-reto/checklists/checklist.md) |

## Huecos concretos priorizados

1. **Sesión:** dos 401 simultáneos no son lo mismo que dos solicitudes con sesión ya expirada; falta logout durante refresh y fallo atómico de rotación.
2. **Errores por operación:** 503/429 genéricos no sustituyen 400/404/409/422 de cada función. Se señalan los códigos exactos que aún no se prueban.
3. **Secuencias:** falta rechazar un reto y luego intentar aceptarlo, y pertenencia previa 409 al unirse a un grupo.
4. **Páginas y torneos:** falta miembros con limit=1/cursor y bracket con partidos no vacíos. El test FollowCursorContract actual no recorre una segunda página de miembros.
5. **Interfaz y proveedor:** guardar estado o serializar una petición no demuestra todos los toques de pantalla, los roles de servidor ni accesibilidad.
6. **Apodo:** falta ocupado 409 durante PATCH y consulta de nombre existente con distinta capitalización; cooldown necesita comprobar recuperación final.

## Evidencia comprobada

- **196 casos JVM PASS**, cero fallos/omisiones, nombres completos contrastados contra la matriz actual. Registros existentes en app/build/test-results/testDebugUnitTest/ e integridad en artifacts/android-ci/all.json.
- **8 casos Android PASS** del registro previo del emulador: giro/recreación, formularios, cifrado, reemplazo de identidad y Room. No se volvieron a ejecutar por editar documentos.
- **12 comprobaciones reales de pertenencia PASS** del registro previo: miembro 200/no miembro 403 para grupo, miembros y tablero; sesiones de prueba cerradas. No prueba otras escrituras/roles.
- **15 rechazos reales sin sesión, 401**, del registro previo. No equivalen a autorización entre usuarios.
- Revisión de fuentes de la fase anterior: 450 archivos y cero secretos; no se atribuye a SAST/SCA del árbol nuevo. El CI anterior se mantiene como histórico, no como ejecución de este cambio local.

La [trazabilidad estructurada](trazabilidad-criterios.json) registra casos, archivo/línea de fuente observada, hashes de fuente e informes, tarea y lo que falta para cada condición. Los reportes generados están ignorados; su ausencia en otro equipo no debe interpretarse como PASS. [Organización de pruebas y conservación](reorganizacion-especifica-2026-11-05.md).

## Diferidos por indicación del usuario

- **Privacidad definitiva:** esperar al backend; conservar aviso provisional, sin convertirlo en definitivo.
- **Catálogo de avatares:** esperar al backend; selector desactivado. No cuenta como una prueba de selección ya superada.
- **Google Play y publicación en GitHub:** no se realizan en esta revisión.
- Las quince funciones nuevas del backend siguen fuera del corte móvil de 19 operaciones.

## Regla para el siguiente cambio

Antes de cerrar una tarea: identificar condición → tarea → caso concreto → ejecución/resultado → parte aún pendiente. Crear/actualizar código y pruebas en la misma tarea autorizada. Un nombre de test, archivo existente o checkbox no sustituye evidencia; un caso compartido solo se vincula si realmente comprueba esa condición. La aprobación humana queda separada.


## Actualización posterior: ejecución autorizada

Esta revisión documental es histórica. Los cinco casos específicos ya fueron implementados y comprobados localmente, junto con otros escenarios: [resultado actual](pruebas-pendientes-2026-11-05.md). Los checklists y la trazabilidad contienen los estados actuales; no interpretar los cinco huecos iniciales como todavía sin prueba.
