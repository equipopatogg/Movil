# Plan — UX-01

1. Aplicar safeDrawingPadding una vez en la raíz compartida; mantener fondo completo y consumo de insets hacia Scaffold/páginas.
2. Sustituir Feedback de texto flotante en el contenido por AppFeedback: modal general y Snackbar descartable. No introducir reintentos automáticos.
3. Localizar errores de acceso/apodo y conservar borradores. Reutilizar los criterios actuales de validación.
4. Convertir confirmaciones existentes a AlertDialog y añadir confirmación de cierre de sesión; cancelación separada de habilitación de confirmar.
5. Ejecutar JVM, Android aislado, Build/Lint/Detekt y validadores de evidencia; registrar alcance real en el checklist.

Rutas: feature/connected/presentation, feature/auth/presentation/AccessScreen.kt, prototype/AppUi.kt y pruebas correspondientes. No dependencias nuevas ni cambios backend. La matriz de 19 operaciones conserva su alcance; UX-01 es transversal, no una operación API nueva.
