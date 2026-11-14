# Plan — UX-02
1. `MakeKit.kt` con las piezas de la maqueta (colores de `ui/theme/Color.kt`, que ya son los de la maqueta).
2. Reescribir solo los `@Composable` de `feature/connected/presentation/` (Inicio, Grupos, Miembros, Tablero, Crear, Contrato, Evidencia, Torneos, Perfil). Sin tocar `ConnectedViewModel` ni acciones.
3. Compilar en Android Studio (`gradlew :app:compileDebugKotlin`), luego `testDebugUnitTest`, `detekt` y `lintDebug`.
