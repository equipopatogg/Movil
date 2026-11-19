# Plan

- `feature/demo/presentation/DemoScript.kt`: guion puro de la animación (`demoFrame(tick)`), probado en JVM.
- `feature/demo/presentation/DemoScreen.kt`: cinco pasos con MakeKit; estado local con `rememberSaveable`; la animación avanza con `LaunchedEffect` y `delay`.
- `ConnectedApp.SignedOut`: decide entre acceso y demo con un estado local; el enlace entra por el espacio `tools` de `AccessScreen`, que no se modifica. El ViewModel no participa, así que la demo no puede tocar sesión ni red.
