# UI de acceso y primer ingreso — verificación local

Fecha: 2026-11-03. El usuario autoriza implementar pantallas sin backend. Esta evidencia corresponde al árbol local sin commit; el CI remoto anterior no acredita estos cambios.

## Implementado

- Acceso/registro puros en feature/auth/presentation: estado/eventos, correo, contraseña/visibilidad, apodo en registro, cambio de formulario y aviso de conexión pendiente al enviar. Sin validaciones HTTP inventadas, cuenta creada ni sesión simulada.
- MainActivity abre acceso sin conexión; la navegación técnica anterior se conserva en código, sin usarse para fingir acceso autenticado.
- Tablero/perfil/grupos y vacío/carga/error en feature/home/presentation; solo invocados por catálogo de debug, sin datos de usuario.
- Actividad del catálogo en src/debug, no exportada; src/release no ofrece entrada. Se contrastaron manifiestos fusionados: UiCatalogActivity presente en debug y ausente en release.
- Sin cambios de dependencias/versiones/permisos/API ni código backend. Contraseñas solo en estado de composición efímero; cambio de formulario las descarta, sin rememberSaveable, logs o almacenamiento.

## Comprobaciones ejecutadas

Gradle local con JDK de Android Studio, sin instalación global. Comando principal: assembleDebug, assembleRelease, testDebugUnitTest, lintDebug, detekt y assembleDebugAndroidTest. Resultado principal BUILD SUCCESSFUL. Release sin firma de producción.

| Control | Resultado / alcance |
|---|---|
| Debug y release | Compilados; no publicación ni validación final de 16 KB |
| JVM | 7 casos, 0 fallos, 0 errores, 0 omitidos; incluye cambio de formulario/limpieza y envío sin transición de sesión |
| Lint | 0 incidencias en XML del resultado |
| Detekt | Correcto en fuentes main según alcance de la tarea existente |
| Secretos | Gitleaks 8.30.1, exit 0, fuentes app/src; no escaneo de historial ni seguridad completa |
| Separación catálogo | Manifiesto debug contiene actividad no exportada; release no la contiene |
| Pruebas de interfaz | Casos preparados para formularios y navegación de catálogo; ejecución en dispositivo PENDIENTE |

APK debug SHA256: 66c1712aa707e3fde337adff7f5c87b1beb43f1343e19623f40bd49e7dff2d57.
APK release sin firma SHA256: 3c954b1ef3bdad142cf3b956f1ad4882a75c28b397d9e5cb396feb07dac0698f.
Salidas generadas en app/build y artifacts raíz ignorados. Compilación instrumentada final, tras precisar selectores y corregir un import de prueba: assembleDebugAndroidTest BUILD SUCCESSFUL en 37 s. No se ejecutó la suite en dispositivo.

## No acreditado

No había dispositivo conectado y el host tenía poca memoria libre. No se abrió emulador ni se ejecutaron connectedDebugAndroidTest, revisión visual real, teclado, rotación, contraste o fuente ampliada. Las pantallas están implementadas pero no aceptadas visualmente. No cobertura medida nueva, integración/auth real, cuentas, autorización por grupo, Play, firma de producción o CI nuevo.

El catálogo es herramienta de revisión local/debug, no demo autenticada del producto. Los criterios AC funcionales de PG-03/04 siguen abiertos. Los criterios UI que dependen de pruebas/dispositivo permanecen parciales.

## Corrección posterior de colores — 2026-11-03

Paleta recuperada de CSS del archivo Make aportado y aplicada a Theme/Color, título con tramo lavanda y campos/tarjeta de acceso. assembleDebug/testDebugUnitTest/lintDebug/detekt: BUILD SUCCESSFUL en 1m30s tras corregir una línea de formato. JVM: {'tests': 7, 'failures': 0, 'errors': 0, 'skipped': 0}. Lint sin incidencias. Debug SHA256 actual: d3a10b950d6a4c1741153dfef3148e92b32f3b07702ad4efd913287d89d95499.

El hash debug anterior pertenece al estado previo de colores. No se recompiló release ni androidTest por este ajuste exclusivamente visual; sus resultados anteriores no acreditan este candidato nuevo. Revisión en dispositivo sigue pendiente. Sin push.

## Iconos y arte de acceso — 2026-11-03

Cinco vectores adaptados del Make y composición decorativa Compose incorporados según recursos-ui-acceso.md. assembleDebug/lintDebug/detekt BUILD SUCCESSFUL en 2m9s; Lint sin incidencias. SHA256 debug actual: a5c31ef265e96232ef2831e62564282bfa6d0863f352198eadbd2245d303573c. No se repiten JVM por recursos/ilustración sin lógica nueva. Pruebas instrumentadas, revisión visual real y release actual no ejecutados por este cambio. Hashes anteriores corresponden a candidatos previos. Sin push.

## Revisión visual del usuario — 2026-11-03

El usuario confirma que revisó las pantallas y que el inicio de sesión «se ve igualito». Se registra conformidad visual del acceso para la versión que el usuario revisó, sin identificar dispositivo/compilación exactos por inferencia. Sobre «Revisar diseño» comenta que la funcionalidad está «un poco rara»; catálogo revisado con observación de interacción pendiente de precisar. No se acredita aceptación de todo el catálogo, pruebas instrumentadas, registro/login real o integración backend.

## Avance técnico parcial — 2026-11-03

PG-05 implementada solo como UI: ExploreScreen/ExploreState, ExploreCatalog en debug e icono búsqueda. El catálogo muestra fixtures y filtra en memoria; detalle/usar idea muestran diálogo sin crear reto. La nueva entrada se llama «Explorar». No se cambió el arranque de producción ni se creó cliente/DTO/API.

Comprobaciones locales: assembleDebug/testDebugUnitTest/lintDebug/detekt BUILD SUCCESSFUL en 1m53s. Totales JVM: {'tests': 9, 'failures': 0, 'errors': 0, 'skipped': 0}; dos casos nuevos comprueban filtro combinado, búsqueda sin coincidencias y vacíos. Debug SHA256: 79eb4085842c90ce9046e881030258ca08b00ab79e6966ad21028f08698c4d66. Lint sin incidencias.

Sin ejecución de UI en dispositivo, aceptación visual, contrato o integración. No se recompiló release ni se ejecutaron pruebas instrumentadas por esta etapa; separación de fixtures revisada en fuentes debug, sin afirmar un candidato release nuevo verificado. **Spec ABIERTA**: no cerrar casillas ni criterios por estos resultados parciales. Sin commit/push.

## Avance parcial comprobado

UI/controlador debug de PG-06 creados. assembleDebug/lintDebug/detekt BUILD SUCCESSFUL en 1m24s tras retirar un recurso obsoleto, sin supresiones. APK debug SHA256 5f665ed5af101f3b34b039664f693de8f32770d34b4785b7c0878e95a8a21de3. Sin nuevos cambios de lógica de producción; no se repiten JVM por navegación efímera debug. No se ejecutan instrumentación/revisión de botones en dispositivo, API o release nuevo; aceptación y contrato pendientes. Casillas/especificación abiertas, sin commit/push.

## Candidato local integrado — 2026-11-03

Catálogo, actividad y lanzador retirados. MainActivity abre AppRoot con acceso y navegación inferior. Recursos Make y componentes Compose utilizados; sin WebView/JS. Datos efímeros locales y aviso de conexión pendiente, no sesión autenticada.

Comprobación final debug/Lint/Detekt/JVM/androidTest-compile: BUILD SUCCESSFUL en 1m52s. JVM {'tests': 9, 'failures': 0, 'errors': 0, 'skipped': 0}. SHA256 debug: 86a8a069b283cb1c4cb515a7963314b0220935d820be7c7a261360e24510aed4. Release compiló en la pasada previa; no se atribuye ese artefacto al último ajuste de formato/texto. No se ejecutó instrumentación en dispositivo ni revisión visual comparativa de todas las vistas. Se retiraron recursos sin uso y se corrigieron líneas de formato sin deshabilitar controles.

Pendientes explícitos: detalle visual uno a uno contra Make, fuentes/espaciado/ilustraciones adicionales, extracción completa de textos a recursos, foto/selector y moderación, integración IA real, datos/contratos y continuidad real de recorridos entre vistas. Las pantallas están integradas, pero no se afirma migración visual exacta de todo el código fuente. Specs ABIERTAS. Sin push.

## Revisión de diseño final — 2026-11-03

Cambios por pantalla en revision-diseno-make.md; fuentes e iconografía del boceto, tarjeta de perfil/avatar, accesos de Inicio en tres columnas, ilustración original de práctica convertida a vector, podio vacío y cuadro horizontal por rondas. Se corrigieron tamaño de imagen, salto de línea/acento del acceso y exceso de botones primarios tras inspeccionar capturas nativas.

Build final: assembleDebug/lintDebug/detekt/testDebugUnitTest/assembleDebugAndroidTest BUILD SUCCESSFUL en 2m08s. JVM: {'tests': 9, 'failures': 0, 'errors': 0, 'skipped': 0}. Lint sin errores ni advertencias. Gitleaks sobre app/src exit0. Sin dependencias de librería nuevas; se añadieron archivos de fuentes con licencias OFL de origen oficial.

APK instalado en el teléfono conectado sin borrar datos. Instrumentación física final: DesignSnapshots + AccessNavigationTest, OK (2 tests), 15.768 s. La primera prueba renderiza 19 vistas; la segunda recorre Inicio → Explorar → Grupos y captura la app con encabezado/navegación. Se recuperaron y revisaron 20 capturas en artifacts/design-review-final/ (ignoradas), sin datos personales. No equivale a cubrir todo estado, scroll, teclado o reglas funcionales.

SHA256 debug: 51a584ef2e5dd1ecc5b3f1f8d3d094ba63629da46be539f25925828442fc9da3. Los hashes previos son históricos y no identifican este candidato. Release no se recompiló en esta pasada visual; no se afirma que tenga el nuevo diseño. Backend, contrato, aprobación visual del usuario y criterios funcionales pendientes. Specs ABIERTAS; sin commit/push.
