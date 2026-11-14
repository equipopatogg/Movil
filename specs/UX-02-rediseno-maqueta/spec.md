# UX-02 — Rediseño de las pantallas conectadas según la maqueta

Fecha: 2026-11-14. Estado: **APROBADA POR EL PO (David, 2026-11-14) / implementación local**. Fuente visual: maqueta Figma Make exportada (`G:\PactoGGMovil\MaquetaFigmaMake`, commit `02db210`, `src/App.tsx` + `src/index.css`). Inventario: [docs/integracion/inventario-maqueta-2026-11-14.md](../../docs/integracion/inventario-maqueta-2026-11-14.md).

## 1. Objetivo
Que las pantallas que ya funcionan contra la API se vean como la maqueta. **Solo cambia la apariencia**: mismas acciones del ViewModel, mismas llamadas, mismas validaciones y mismos mensajes de error. El acceso (login y registro) ya fue aprobado por el PO y no se toca.

## 2. Correspondencia
| Pantalla de la app | Maqueta | Elementos a reproducir |
|---|---|---|
| Inicio (`HOME`) | `Home` | selector de grupo, título "Juega con ganas. / Cumple tu palabra.", 3 accesos rápidos, "Lo que sigue", enlace de torneos, guía "¿Cómo funciona un pacto?" |
| Grupos y miembros | `Groups` | encabezado, banner "Encuentra tu squad.", pestañas Mis grupos / Unirme / Crear grupo, tarjeta por grupo con etiqueta PRIVADO |
| Tablero de retos | `ChallengeCard` | tarjeta con etiqueta de estado, insignia del juego, condición, fianza y "Ver reto" |
| Crear reto | `Create` | pasos numerados 01/02/03, resumen, aviso de puntos virtuales |
| Contrato y evidencia | `Challenge`, evidencia | tarjeta del reto, datos en renglones, pestañas Foto/Enlace, "¿Qué puede revisar?" |
| Torneos | `Tournaments` | encabezado "Compite con tu squad", pestañas de estado, tarjetas de torneo |
| Perfil | `Profile` | tarjeta de identidad, "Tu palabra tiene valor", "Dentro de PactoGG", privacidad, cerrar sesión con confirmación |

## 3. Requisitos
- RF1: Piezas comunes en `core/designsystem/MakeKit.kt` (encabezado, letreros, tarjetas, etiquetas, insignia de juego, botones primario/secundario/fantasma, aviso, pestañas, estado vacío, campo).
- RF2: Los estados que llegan del servidor se muestran en español (`statusLabel`) y las fechas en hora local.
- RF3: Se conservan los textos que usan las pruebas de UI (`Elegir foto`, `Enviar foto`, `Quitar foto`, `Revisar enlace`, `Actualizar evidencias`, `Aceptar reto`, `Cerrar sesión`, `Grupos`, textos del bracket).
- RF4: Nada de datos de ejemplo en las pantallas conectadas; donde la maqueta muestra ejemplos, la app muestra los datos reales o un estado vacío.

## 4. Criterios de aceptación
| ID | Resultado |
|---|---|
| UX-02-AC1 | Cada pantalla de la tabla se parece a su par de la maqueta (revisión visual del PO). |
| UX-02-AC2 | Las pruebas JVM y de UI existentes siguen pasando sin cambiar su lógica. |
| UX-02-AC3 | Detekt y Lint sin hallazgos nuevos. |
