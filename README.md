# PactoGG Android — consumidor conectado

Organización local de pruebas actualizada: cada operación tiene archivos específicos de validación, API y seguridad. [Abrir tabla por operación](docs/pruebas/matriz-operaciones.md) · [Verificación de conservación de los 196 casos](docs/pruebas/reorganizacion-especifica-2026-11-05.md). Esta reorganización todavía no se sube a GitHub.

Integración local de las 19 operaciones confirmadas por backend: ocho del corte anterior y once sociales nuevas. Perfil/apodo, grupos/miembros, tablero/detalle y aceptar/rechazar están conectados con datos reales. Se conserva diseño y funciones adelantadas fuera del recorrido. Avatar pendiente de catálogo, sin selección activa.

Calidad local actual: 192 pruebas JVM (54 unitarias, 42 de integración y 96 de seguridad). Cinco pruebas Android offline y CI previo correctos; candidato ampliado sujeto al PR de esta fase. Cobertura JVM medida: 27,14 % local y 26,96 % en CI, sin mínimo ratificado. CI del candidato verificado en GitHub con gate PASS y APK descargado. [Entrega por PR y límites](docs/pruebas/entrega-github-2026-11-04.md). [Informe de calidad](docs/pruebas/verificacion-calidad-2026-11-04.md) · [matriz de operaciones](docs/pruebas/matriz-operaciones.md) · [pipeline](docs/pruebas/pipeline-movil.md).

Historial funcional: recorrido positivo real de once operaciones y regresión de acceso correctos en teléfono. **Esto no cierra todos los criterios ni la aceptación de las specs.** [Informe y límites](docs/integracion/integracion-social-2026-11-04.md).

[Specs y estados](specs/README.md) · [Correspondencia con Sistema.docx](docs/integracion/specs-confirmadas-sistema-2026-11-04.md) · [Historial de prueba física](docs/integracion/verificacion-telefono-2026-11-04.md).

Backend, credenciales, notas privadas y builds quedan fuera del repositorio móvil. Las cinco ramas anteriores ya se fusionaron; esta integración se entrega en los PR dependientes 6, 7 y 8. Consultar sus estados de merge y ejecuciones en GitHub; no equivalen a aceptación integral.


[Cierre de pendientes y release](docs/pruebas/cierre-pendientes-2026-11-04.md) · [Estado de criterios](docs/pruebas/estado-criterios-2026-11-04.md).


## Revisión documental actual — 2026-11-05

[Revisión de las 19 specs y sus 84 criterios](docs/pruebas/revision-specs-2026-11-05.md): 60 criterios parcialmente comprobados, cinco sin caso específico y 19 criterios de calidad con controles locales parciales. Se vinculan tareas, casos y resultados existentes; no se ejecutaron nuevas pruebas por esta edición. Los estados anteriores se conservan como historial. No hay aceptación completa ni publicación.


## Pruebas pendientes ejecutadas localmente — 2026-11-05

[Resultado actual](docs/pruebas/pruebas-pendientes-2026-11-05.md): 237 JVM y nueve Android correctos; 41 casos JVM nuevos. Los cinco criterios sin caso específico tienen comprobación local de consumidor. No equivale a cerrar íntegramente las 19 specs; se conservan límites por criterio y aceptación humana/proveedor pendiente. Sin cambios visuales ni entrega a GitHub. Los recuentos de revisiones anteriores son históricos.


## Ajustes visuales locales — 2026-11-05

Zona segura dinámica, mensajes contextuales y confirmaciones modales implementados. [Resultado UX-01](docs/pruebas/ajustes-ux-2026-11-05.md): 239 JVM, 14 Android y controles locales correctos. Revisión visual del usuario pendiente. Sin Git/publicación; recuentos anteriores conservados como históricos.
