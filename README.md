# PactoGG Android

## Configuracion local de Firebase

El archivo Android `google-services.json` se conserva localmente en `app/` y no se incluye en Git. Al clonar, obtener la configuracion Android del proyecto Firebase para `com.pactogg.app` y colocarla en esa ruta. Sin ese archivo el proyecto compila con Google/push sin configurar; el acceso habitual con correo sigue disponible. El archivo de cuenta de servicio es exclusivo del servidor y nunca se copia al proyecto movil. Firebase del servidor y Google Sign-In conservan su aceptacion real pendiente.

Push Android, 2026-11-08: integracion local parcial de FCM, canal/permiso y registro/rotacion/baja. [Evidencia y pendientes](docs/integracion/push-android-2026-11-08.md). Compilacion Android y entrega real pendientes; Google espera habilitacion del servidor. Sin cambios backend ni GitHub.

## Estado de la ampliación - 2026-11-06

Las 36 rutas tienen consumidores en el código. Se incluyen pantallas de jurado, puntos/ranking, compromisos, avisos y fama; creación de retos por grupo y filtro de torneos por grupo. La compilación y validación JVM/Android de este candidato siguen pendientes por un fallo de conexión con el daemon de Gradle en la sesión de revisión. Ver [resultado y comandos](docs/pruebas/ampliacion-local-2026-11-06.md).

Ejecutar `scripts/check-android.ps1` desde la terminal de Android Studio para continuar la comprobación. No se subieron cambios a GitHub ni se modificó el backend.

Los apartados siguientes conservan antecedentes del alcance anterior; no sustituyen este estado.


Cliente Android Kotlin con Compose, Hilt y Retrofit. El recorrido conectado utiliza datos reales del backend; las pantallas del prototipo se conservan sin introducir muestras en ese recorrido.

## Alcance actual

Acceso y sesión, perfil/apodo, grupos y miembros, creación/listado/detalle/aceptación/rechazo de retos, evidencias y consulta de torneos. Se mantienen las 19 operaciones principales de la [matriz de pruebas](docs/pruebas/operaciones.json). GET de evidencias se añade como lectura auxiliar confirmada para imágenes dentro de [MO-EVI-02](specs/MO-EVI-02-evidencias/spec.md).

La barra inferior sigue Make: Inicio, Explorar, Crear, Torneos y Grupos. Explorar muestra un aviso de próxima disponibilidad. Perfil está en la cabecera. Las fotos se eligen desde la galería, se preparan y previsualizan antes de enviarse; no hay cámara integrada ni carga de video.

**Pendiente del proveedor comprobado el 2026-11-05:** la API desplegada devuelve 405 en GET /api/v1/contracts/{id}/evidence, aunque la ruta está en su contrato/repositorio. La app muestra indisponibilidad y conserva los envíos confirmados. No se presenta ese resultado como historial vacío ni se repite POST. Las reglas de primer voto y estados también requieren conciliación del servidor con su mensaje reciente.

S3 directo con URL firmada (ID156), aviso definitivo y catálogo de avatares esperan al backend. Jurado, puntos/ranking y otras adiciones del repositorio backend no se activan por su mera presencia. Google Play queda fuera de esta etapa.

## Trabajar con el proyecto

Abrir esta carpeta en Android Studio y usar su JDK integrado. URL HTTPS de la API mediante la propiedad Gradle `pactogg.apiBaseUrl`; no introducir credenciales ni claves AWS en el cliente.

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:detekt --max-workers=1
```

Las pruebas de operaciones están separadas en validación, API y seguridad; [matriz y escenarios](docs/pruebas/matriz-operaciones.md). Usan fixtures HTTPS y no crean datos reales. Las pruebas Android offline se ejecutan en emulador aislado con [run-instrumented.py](scripts/run-instrumented.py); los sondeos live son explícitos, con cuentas QA externas al proyecto. No pasar credenciales en Git.

El flujo sigue spec → plan/tareas → implementación y pruebas pertinentes → resultados reales → revisión. [Specs activas](specs/README.md) · [constitución](constitution.md) · [diseño](DESIGN.md) · [estado actual](Memoria.md). Esta revisión general no crea una spec ni un expediente de relevo: únicamente las imágenes amplían MO-EVI-02.

## Verificaciones y antecedentes

El resultado del candidato de imágenes se registra en el [checklist de evidencias](specs/MO-EVI-02-evidencias/checklists/checklist.md), la [trazabilidad por criterio](docs/pruebas/trazabilidad-criterios.json) y Memoria. Las ejecuciones de GitHub de versiones anteriores son historia y no acreditan los cambios locales actuales.

- [Mensajes y zona segura anteriores](docs/pruebas/ajustes-ux-2026-11-05.md).
- [Pruebas pendientes anteriores](docs/pruebas/pruebas-pendientes-2026-11-05.md) y [organización por operación](docs/pruebas/reorganizacion-especifica-2026-11-05.md).
- [Revisión de criterios](docs/pruebas/revision-specs-2026-11-05.md) y [estado histórico](docs/pruebas/estado-criterios-2026-11-04.md).
- [Integración social y límites](docs/integracion/integracion-social-2026-11-04.md), [correspondencia con Sistema.docx](docs/integracion/specs-confirmadas-sistema-2026-11-04.md) y [prueba física histórica](docs/integracion/verificacion-telefono-2026-11-04.md).
- [Calidad anterior](docs/pruebas/verificacion-calidad-2026-11-04.md), [entrega anterior por PR](docs/pruebas/entrega-github-2026-11-04.md), [release anterior](docs/pruebas/cierre-pendientes-2026-11-04.md) y [pipeline](docs/pruebas/pipeline-movil.md).

Backend, cuentas QA, claves de firma, referencias personales, APK y resultados generados permanecen fuera del repositorio móvil. Las verificaciones locales no sustituyen la aceptación del proveedor ni la revisión humana.
