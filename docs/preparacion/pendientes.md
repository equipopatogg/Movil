# Pendientes para la siguiente revisión SDD

Estas decisiones quedan visibles para revisión; no detienen la preparación documental solicitada.

## Alcance y gobierno

- Ratificar qué código/configuraciones anteriores se reutilizarán, simplificarán o sustituirán al retomar implementación.
- Cerrar alcance de primera entrega y a qué puntos se refiere «todo lo demás se quita» en las fuentes originales.
- Confirmar si CLARIFICACION.docx está aprobado íntegramente o es borrador; sus cifras no se heredan por defecto.
- Relacionar historias, aceptación y maqueta por sus IDs reales, resolviendo discrepancias antes de construir flujos.

## Tecnología y operación

- Backend .NET10/SDK declarado 10.0.401 ya elegido y verificado localmente; quedan base NoSQL, OpenAPI funcional, proveedores/entornos y despliegue. SQL Server del profesor no decide la base PactoGG.
- Resolver push requerido por docente frente a FCM aplazado; SQLite/sincronización condicional frente a WorkManager/Offline-First aplazados.
- Deep Links siguen pospuestos; OCR opcional y herramientas avanzadas de contrato/mutación/caos pendientes.
- Responsable de Play, identificador definitivo, firma, privacidad/dominio y presupuesto de servicios.

## Reglas, privacidad y seguridad

- Sesión, refresh/revocación y política ante fallo de Redis con varias instancias.
- Estados del reto, jurado/consenso, puntos/fianzas, torneos por grupos y fórmula del Índice de Palabra.
- Análisis antes de guardar fotos/textos; definición de transporte temporal y cuarentena.
- Validación/moderación de enlaces, proveedores IA/ML, minimización de datos y salida del modelo no confiable.
- Retención, borrado/anonimización, auditoría persistente, reportes/abuso y responsables.
- Umbrales propios y cuáles controles bloquean entrega; excepciones con responsable y vencimiento.

## Incidencias previas

Compatibilidad de 16 KB y suite instrumentada final requieren resolución en una fase técnica posterior.
Los workflows Android se trasladaron a .github/workflows raíz y se ajustaron en la preparación Git del 2026-11-03. Su configuración/comprobación local no equivale a CI remoto, control exhaustivo o adopción del ejemplo.

La referencia setup-gradle inválida se sustituyó por v6.4.1 oficial fijada a SHA; se verificaron el alcance recursivo del JAR y checksum de distribución separados. Se configuraron informes sanitizados/conservación ante fallos y se probaron parsers/estados localmente. **Siguen pendientes** ejecución real de la acción y negativo de Wrapper en ella, fixtures reales Semgrep/escáneres en Linux, CI/descarga, cobertura JVM, instrumentación y release/16 KB. El contraste histórico permanece en [comparación con el profesor](repositorios-moviles-y-agentes.md); estado vigente (referencia histórica externa).

La preparación entrega este expediente; las decisiones se cierran mediante ADR y fuente de ratificación cuando corresponda. La implementación se retoma por instrucción del usuario.

## Entradas concretas para continuar — 2026-10-30

- Backend: specs/OpenAPI versionados, URL de entorno, registro/login/logout, JSON sintéticos, errores y política de sesión/refresh/revocación; selección de cuatro endpoints adicionales del corte por el equipo.
- Producto/diseño: maqueta visual identificada y recorrido exacto tras primer ingreso; verificar correo, apodo/avatar y grupos sin asumir todas las variantes del documento antiguo.
- Entrega: titular/tipo/estado de cuenta Play, applicationId definitivo y canal/evaluador; confirmar interpretación de distribución para noviembre frente al requisito general de descarga desde tienda.
- Revisar los [cuatro cambios delimitados](../../specs/README.md) y acordar sus decisiones de versiones/compuertas/reutilización. PG-00/PG-01 necesitan seleccionar validación/medición/entorno; PG-02 una solución nativa verificada; PG-03 contrato y maqueta. Los antiguos borradores generales siguen consolidados en guías. Las entradas bloquean tareas concretas, no toda preparación documental.

Las reglas genéricas del PDF v3 y decisiones específicas que entren en conflicto siguen pendientes de resolución; no se añaden cámara/video, push/FCM u Offline-First por esta ampliación.
