# Cambios de la app PactoGG (Android)

Versión semántica en `app/build.gradle.kts` (`appVersionName`); el `versionCode` lo pone CI. Cada versión publicada lleva una etiqueta `vX.Y.Z` en Git. Política completa en `docs/versiones.md`.

## 1.1.0 — 2026-11-26
- ML en la app: al reportar el resultado de una partida, ML Kit lee el marcador de la captura en el teléfono (PL-14).
- IA generativa en la app: "Ideas con IA" en Explorar con Gemini (Firebase AI Logic); solo se envía el nombre del juego (PL-14).
- Tutorial de primer uso: explica para qué sirve cada botón; se vuelve a abrir desde Perfil · Cómo usar PactoGG (PL-15).
- Aviso de root en "Privacidad y seguridad".
- Matriz OWASP Mobile Top 10 y porcentaje de seguridad (`docs/seguridad/`).
- Crear reto: el rival ya no queda en "Todo el grupo" sin querer. Hay que elegir rival o tocar "Abrir a todo el grupo"; si no, "Revisar reto" lo pide (`ConnectedFlowTest.reviewRequiresChoosingRivalOrOpeningToWholeGroup`).

## 1.0.0 — 2026-11-19
- Fijación de certificados para la API y reglas Semgrep nuevas (logs sensibles, archivos públicos, TrustManager vacío, WebView con archivos).
- Pruebas de seguridad de todas las operaciones de juego y cuenta (`PlaySecurityTest`, `AccountSecurityTest`, `NetworkSecurityConfigTest`) e integración de los 61 endpoints (`docs/pruebas/matriz-endpoints.md`).
- Pruebas instrumentadas sin sesión previa; versión 1.0.0 y entrega a Google Play preparada.

## 0.4.0 — 2026-11-18
- Reto por voz con IA (Groq), botón de Ranking, "Los Tramposos", correo verificado obligatorio, desbloquear, reportar evidencia y aviso de privacidad actualizado.

## 0.3.0 — 2026-11-16/17
- Push (FCM), recuperar contraseña, lectura sin conexión con Room, calendario y reloj, votación explicada, foto de perfil y atajos del inicio.

## 0.2.0 — 2026-11-14/15
- Rediseño según la maqueta: jurado, avisos, palabra y ranking, torneos, voz, Explorar, demo, Google, cuenta y seguridad.

## 0.1.0 — 2026-11-13
- Base SDD: registro, login, sesión, logout, grupos, retos, evidencias, perfil y torneos (19 operaciones con prueba unitaria, de integración y de seguridad).
