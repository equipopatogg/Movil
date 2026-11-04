# PactoGG — preparación SDD

**Fase vigente: documentación, planes y tareas.** Se conserva la base Android anterior sin ampliar código, dependencias o workflows. Las reglas de fase están en [AGENTS](AGENTS.md) y [.specify/memory/constitution.md](.specify/memory/constitution.md); los principios comunes, en [constitution.md](constitution.md).

Excepción posterior de preparación Git (2026-11-03): se autoriza ajustar/centralizar workflows y verificar configuración/exclusiones/secretos; no funciones ni versiones de app. [Cierre común](../PREPARACION_GIT.md). El párrafo anterior conserva el alcance documental de preparación previa.

## Orden de lectura

1. [Memoria](Memoria.md) y [decisiones](docs/decisiones/registro.md): estado y acuerdos.
2. [Specs](specs/README.md): PG-00 CI/evidencias, PG-01 pruebas/cobertura, PG-02 release 16 KB y PG-03 acceso a cuenta. Todas en borrador; cada una conserva spec, plan, tareas y checklists/checklist.md.
3. [Pendientes](docs/preparacion/pendientes.md), [primer flujo](docs/preparacion/primer-flujo-movil.md) y [evaluación](docs/preparacion/evaluacion-unidad-2.md): entradas necesarias y alcance del corte.
4. [Mapa](docs/preparacion/mapa-estructura-sdd.md): arquitectura, contratos, diseño, pruebas, seguridad y método, cada asunto en su documento.
5. [Checklist de preparación](CHECKLIST_PREPARACION.md), [trazabilidad](docs/preparacion/trazabilidad.md) y [verificación consolidada](docs/preparacion/verificacion.md): qué se preparó y qué se comprobó.

## Siguiente paso

Backend BE-00–BE-03 ya implementado/verificado localmente en ../Backend/. Definir todavía su contrato funcional, URL de pruebas desplegada y reglas de sesión; cerrar maqueta/recorrido del primer ingreso para PG-03. No se presupone una implementación funcional externa. Las otras specs mantienen pendientes propios de compatibilidad, entorno y evidencia.

La secuencia es especificación → revisión de decisiones → plan → tareas → implementación solicitada → verificación. La preparación no ejecuta tareas técnicas ni configura cuentas, Git, firma o publicación.

## Fuentes y ayudas

La [comparación del profesor](docs/preparacion/repositorios-moviles-y-agentes.md) y los [inventarios](referencias/profesor/README.md) permanecen fuera de specs. Los [perfiles y skills](.opencode/README.md) se seleccionan mediante la [matriz por spec](docs/preparacion/matriz-skills-por-spec.md); no se invocan o descargan automáticamente. Las [plantillas](.specify/README.md) conservan el expediente de cuatro archivos.

El historial de preparación, retirada de borradores generales y creación de los cuatro cambios concretos está en decisiones D-SDD-01–D-SDD-06. Los resultados técnicos anteriores permanecen en docs/verificacion-local.md; no se transfieren como aceptación nueva.

## Ubicación desde el 2026-11-03

Todo este expediente está en PactoGG/Movil, junto al proyecto Android, sin carpeta anidada. Decisiones/informes previos conservan rutas/recuentos históricos; D-SDD-08 registra la separación. [Backend](../Backend/README.md) conserva contraste API_SDD y base técnica local posterior. Workflows Android ahora en raíz, conforme a D-SDD-16; sin Git creado o ejecución remota acreditada.
