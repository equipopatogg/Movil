# Revisión de diseño contra Make — 2026-11-03

Petición: revisar todas las pantallas porque la adaptación conservaba navegación pero no suficiente composición, iconografía e imágenes. Se contrastó Figma visible (acceso e Inicio) y los estilos/componentes de la fuente local suministrada, commit interno 02db2100b75b1c0a49d722ae06a0c236766d58fd. No se ejecutó su código React.

## Cambios por área

| Pantallas | Referencia fuente | Adaptación nativa |
|---|---|---|
| Acceso/registro | welcome.css y Access | Inter/Space Grotesk, título centrado en dos tonos, ilustración orbital, tarjeta/formulario y vectores |
| Inicio | home.css y Home | Selector compacto de grupo, accesos en tres columnas con iconos, jerarquía de títulos y tarjetas/estados |
| Explorar | explore.css | Panel con título/acento e ilustración, buscador, chips, iconos de ideas y tarjetas |
| Crear, resumen | creation.css | Panel de paso, campos oscuros, indicadores de etapa y acciones |
| Perfil | profile.css | Portada, avatar con color seleccionado, identidad, acción secundaria y enlaces en filas |
| Grupos | refinements.css y Groups | Encabezado, panel ilustrado, selector de modo y tarjetas |
| Torneos, nuevo torneo | refinements.css | Arte geométrico con copa, tarjeta destacada, metadatos, opciones y formulario |
| Cuadro/partida | App.tsx y estilos de torneo | Columnas horizontales por ronda, acceso a partidas, reporte local |
| Contrato/evidencia/jurado | evidence-voice.css | Panel contextual, modos foto/enlace, iconografía y geometría vectorial de la ilustración original de práctica |
| Voz | evidence-voice.css | Panel de entrada y revisión del ejemplo; IA permanece sin conexión |
| Ranking/libro/fama/integridad | standings.css | Grupo, panel del índice, pestañas y podio vacío sin posiciones inventadas |
| Notificaciones | App.tsx | Aviso de jurado y filas de novedades con iconos |
| Recuperación/bienvenida | welcome.css y App.tsx | Tipografía/componentes comunes y flujo local, sin correo enviado |

## Recursos y licencias

Iconos y geometría ilustrativa recuperados del archivo Make. No fue necesario introducir un paquete externo de iconos. Se mantienen restricciones de fotos/enlaces, sin cámara/video y sin dinero real. Las dos imágenes adjuntas al archivo no se usan como fotos reales del producto al no confirmarse su función en pantalla.

Se incorporan Inter y Space Grotesk desde google/fonts fijado por commit, con sus licencias OFL y hashes en docs/licencias/fuentes/. No se descargan fuentes en ejecución. En Android anteriores a O se conserva fallback SansSerif conforme a la [guía oficial](https://developer.android.com/develop/ui/compose/text/fonts). No se instaló fontTools o dependencia nueva para esta adaptación.

## Verificación y límites

Se creó una prueba de renderizado para 19 vistas de componentes; capturas en artifacts/design-review/, ignoradas. La primera ejecución física pasó y permitió corregir salto de línea del acceso, tamaño de la ilustración y estilos de navegación del perfil. La ejecución final y su candidato se documentan en verificacion-ui-local.md.

Las capturas aisladas no equivalen a validar todos los estados, scroll, teclado, escalado o back stack. La navegación real se comprueba aparte. La fidelidad visual es una adaptación Android, no se declara igualdad píxel a píxel ni aceptación del usuario. Foto/selector, IA, sesión/contratos y operaciones de proveedor siguen pendientes; este cambio no los sustituye. PG-07 y expedientes funcionales continúan abiertos. Sin commit/push.
