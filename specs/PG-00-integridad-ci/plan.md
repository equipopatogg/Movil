# Plan — PG-00

Estado: propuesta de implementación futura. [Spec](spec.md). La preparación no ejecuta pasos.

Actualización 2026-11-03: pasos de configuración/referencias/conservación parcialmente implementados bajo el encargo pre-Git; verificaciones locales afectadas autorizadas. Las rutas `.github/workflows/` de este plan son **desde PactoGG raíz**; configs y app siguen en Movil. Estado actual (referencia histórica externa). No sustituye ejecución de acción/motores/runner requerida para aceptación completa.

## 1. Diseño y secuencia

| Paso | Cambio / ruta | Dependencia | Criterios |
|---|---|---|---|
| 1 | Revisar ambos YAML, archivos que disparan jobs y consumidores del Wrapper | Reutilización de la base y fase técnica acordadas | PG00-AC1, PG00-AC5 |
| 2 | Resolver/corregir commit oficial de setup-gradle y comprobar validación integrada del JAR bajo Movil/ previa a Gradle; no añadir acción redundante | Fuente oficial y revisión de alcance/compatibilidad | PG00-AC1 |
| 3 | Exportar informes de los escáneres existentes y manifiesto por ejecución en android-security.yml | Formatos, redacción y retención acordados | PG00-AC2 |
| 4 | Recoger/subir informes conservando códigos de salida y resumen de ausencia/error | Paso 3; nunca convertir fallo en éxito | PG00-AC3 |
| 5 | Añadir fixtures sintéticas de reglas en .semgrep-tests/ y comprobar muestras positivas/negativas | Formato compatible verificado, sin secretos reales | PG00-AC4 |
| 6 | Revisar permisos/filtros, verificar y actualizar docs/seguridad/controles.md con evidencia real | Pasos anteriores y repositorio/CI autorizados | Todos |

No usar continue-on-error como aceptación final. Si se necesita diferir el fallo para recoger informes, un paso final restablece el resultado y distingue omisión de ejecución. No crear automáticamente issues, PR, tags o publicaciones.

## 2. UI, navegación y DI

No aplica: no cambia Kotlin de aplicación, Hilt o navegación. La elección de nuevas capacidades SAST se revisa aparte; no sustituir Semgrep por copiar una herramienta externa.

## 3. Verificación y recuperación

Verificar en copia aislada el Wrapper alterado y las salidas de error; los archivos originales permanecen íntegros. Comprobar informes descargables/redactados para éxito, hallazgo, error y ausencia. La ejecución en GitHub espera autorización de Git y entorno; una revisión local de YAML no la reemplaza.

Recuperación: revertir exclusivamente la configuración afectada si falla compatibilidad; registrar el control no ejecutado y su causa, sin declarar la base segura. La reversión no autoriza saltarse validación para liberar.

## 4. Gates y cierre

Debe cumplirse PG00-AC1–AC5 con versiones y alcance registrados. Fuente y artefactos identificados en cada ejecución; aplicar retención ratificada y evitar salida sensible. Conciliar [tareas](tasks.md), [checklist](checklists/checklist.md), aprobación y Memoria.md. Sin ejecución en la fase actual.

## Ajuste de ubicación resuelto en preparación Git

La reorganización inicial conservó workflows en PactoGG/Movil/.github como antecedente. El encargo posterior los traslada/ajusta a **PactoGG/.github/workflows/** y retira los dos YAML anteriores; filtros, directorios, mounts y rutas de informes revisados. No se crea Git ni se acredita funcionamiento remoto por ese traslado. El negativo de acción Wrapper y fixtures de motores no se sustituyen por revisar fuentes o probar parsers.


Actualización autorizada 2026-11-04: reutilizar proyecto/pruebas, verificar matriz de 19 operaciones, separar categorías, medir JaCoCo integrado y ejecutar cinco pruebas Android offline; validar gate con fallos/ausencias/omisiones/artefactos viejos y APK diferente. Conservar informes y preparar único grafo CI sin CD. Ver matriz/pipeline en docs/pruebas y comparación fijada del profesor. No atribuir ejecución remota al YAML local ni copiar umbrales/stack ajenos.
