# UX-01 — mensajes y espacio seguro

Fuente: petición expresa del usuario, 2026-11-05. Ajuste visual local del móvil existente; no añade endpoints ni cambia reglas del backend. Se registra su cronología real, sin atribuir esta mejora a specs anteriores.

## Alcance

El contenido respeta barras del sistema y recortes de cámara mediante insets dinámicos. El fondo conserva los colores actuales hasta los bordes. Las páginas conservan desplazamiento y tratamiento del teclado.

Errores generales en modal descartable; errores de correo, credenciales y apodo junto al campo correspondiente. Confirmaciones de aceptar/rechazar reto y cerrar sesión en modal con acción explícita y Cancelar. Éxitos en aviso breve descartable. No reintentar automáticamente una escritura incierta desde los avisos. Conservar formularios y no mostrar un error de otra pantalla.

## Criterios

| ID | Condición | Comprobación |
|---|---|---|
| UX-01-AC1 | Contenido fuera de barras/cámara en vertical y horizontal, sin márgenes fijos por modelo | SafeAreaTest.contentAvoidsSystemBarsAndCutoutInPortraitAndLandscape |
| UX-01-AC2 | Error general descartable y errores de campos localizados | FeedbackDialogsTest.generalErrorIsDismissibleWithoutRetryingOperation y occupiedNicknameIsShownAtFieldWithoutGeneralDialog; AccessNavigationTest.emptyLoginCannotBypassAuthentication |
| UX-01-AC3 | Cancelar no ejecuta acción y sigue permitido cuando confirmar está bloqueado | FeedbackDialogsTest.invalidConfirmationCanStillBeCancelledWithoutExecutingAction; políticas de reto existentes |
| UX-01-AC4 | Aviso de éxito descartable sin navegación ni pérdida del borrador; cierre antiguo no borra uno nuevo | FeedbackDialogsTest.successNoticeCanBeDismissedWithoutChangingScreen; ConnectedFlowTest.dismissingOldFeedbackCannotClearNewFeedbackOrFormData y fieldErrorsStayWithTheirScreenAndDoNotHideOtherErrors |

## Límites

Probar insets en el emulador y conservar limitación frente a otros modelos reales. No cambia contratos, permisos, privacidad provisional, avatares pendientes o funciones futuras. Sin Git, Play o aceptación humana inventada.

| Estado | Revisores | Fecha | Detalle |
|---|---|---|---|
| Verificada localmente; revisión visual pendiente | Pendiente de revisión visual del usuario | 2026-11-05 | Autorización expresa; aceptación visual posterior |

[Plan](plan.md), [tareas](tasks.md), [checklist](checklists/checklist.md).
