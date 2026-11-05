# Entrega por PR autorizada — 2026-11-04

El usuario autoriza subir el trabajo local por ramas/PR, comprobar y corregir CI remoto, configurar la compuerta obligatoria y fusionar solo con controles correctos. Cuenta/repositorio: equipopatogg/Movil; no publicar en otras cuentas ni incluir backend, credenciales o artefactos privados. Esta instrucción posterior sustituye las restricciones de Git de la fase local conservadas debajo. Sin despliegue, firma de producción, instalaciones globales o aceptación humana inventada. No cerrar criterios funcionales solo por un pipeline verde.

# Fase vigente — calidad local del móvil, 2026-11-04

El consumidor de 19 operaciones existe y el usuario autoriza completar pruebas/CI antes de entregar. Se conservan las 23 specs y el diseño, se reutilizan pruebas y se completan unitarias/integración/seguridad por operación. [Matriz](docs/pruebas/matriz-operaciones.md), [pipeline](docs/pruebas/pipeline-movil.md), [comparación del profesor](docs/preparacion/comparacion-profesor-calidad-2026-11-04.md).

PG-00 y PG-01 se actualizan con cambios concretos y evidencia. PG-02 mantiene su fase release/16 KB separada. No modificar backend, publicar, crear commits/push ni copiar versiones/umbrales/herramientas externas por esta preparación. El estado actual está al principio de Memoria.md; lo que sigue registra la preparación previa y no sustituye esa fase vigente.

---

## Prueba física posterior — 2026-11-04

Cuenta QA y API real comprobadas en teléfono. Acceso, grupos, consulta vacía de torneos, refresh y logout correctos; **crear reto devuelve 503 MODERATION_UNAVAILABLE**, evidencias bloqueadas. APK final instalado; specs abiertas. [Evidencia y pendientes](docs/integracion/verificacion-telefono-2026-11-04.md). Sin push.

## Implementación vigente — 2026-11-04

Usuario autorizó ejecutar las ocho operaciones consumidor: acceso real, sesión, grupo/reto/evidencia y consulta de torneos. MainActivity abre recorrido conectado; diseño y pantallas adelantadas conservados sin entradas. Aviso básico provisional autorizado. [Resultados y límites](docs/integracion/verificacion-consumidor-2026-11-04.md): debug/24 JVM/Lint/Detekt/secretos correctos; falta ejecución Android y verificación funcional con cuentas del proveedor. Sin commit/push. Los estados documentales/prototipo inferiores conservan cronología y quedan superados en este alcance.

## Actualización vigente — 2026-11-04

Contrato backend recibido y preparación documental alineada: [matriz de operaciones/pantallas y pendientes](docs/integracion/alineacion-backend-2026-11-04.md), [Specs activas](specs/README.md). Ocho specs consumidor correspondientes a operaciones existentes. UI conservada; sin spec adicional de disponibilidad ni cambios de navegación. Los párrafos de contrato no recibido son antecedentes. Sin código nuevo ni ejecución/publicación por esta actualización.

# PactoGG — preparación SDD

**Fase vigente: documentación, planes y tareas.** Se conserva la base Android anterior sin ampliar código, dependencias o workflows. Las reglas de fase están en [AGENTS](AGENTS.md) y [.specify/memory/constitution.md](.specify/memory/constitution.md); los principios comunes, en [constitution.md](constitution.md).

Excepción posterior de preparación Git (2026-11-03): se autoriza ajustar/centralizar workflows y verificar configuración/exclusiones/secretos; no funciones ni versiones de app. Cierre común (referencia histórica externa). El párrafo anterior conserva el alcance documental de preparación previa.

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

Todo este expediente está en PactoGG/Movil, junto al proyecto Android, sin carpeta anidada. Decisiones/informes previos conservan rutas/recuentos históricos; D-SDD-08 registra la separación. Backend (referencia histórica externa) conserva contraste API_SDD y base técnica local posterior. Workflows Android ahora en raíz, conforme a D-SDD-16; sin Git creado o ejecución remota acreditada.

## Instrucción posterior — evaluación, 2026-11-03

El usuario solicita preparar specs y lo esencial de evaluación. Se permite editar documentación móvil y checklists, conservando límites de producto; no inicia funcionalidades/dependencias/firma/publicación. Git móvil y CI remoto ya existen: ver docs/preparacion/ci-remoto-android.md. PG-03/PG-04 y checklist-evaluacion-movil.md definen la preparación siguiente, con contrato y decisiones pendientes explícitos.
