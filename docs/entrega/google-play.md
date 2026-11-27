# Entrega en Google Play

| Dato | Valor |
|---|---|
| Paquete | `com.pactogg.app` |
| Ficha | https://play.google.com/store/apps/details?id=com.pactogg.app |
| Responsable | Diego Salinas (cuenta de desarrollador del equipo) |
| Estado | Prueba cerrada con testers (subida el 12-nov-2026). Google pide 12 testers durante 14 días seguidos antes de abrir producción |
| Firma | Firma de la app administrada por Google Play; el equipo sube con su llave de subida |
| Versión | `versionName` 1.0.0; `versionCode` = número de ejecución de CI + 100 (`docs/versiones.md`) |
| Aviso de privacidad | https://black-river-065cb1710.6.azurestaticapps.net/privacidad |
| Eliminar cuenta (formulario de Play) | https://black-river-065cb1710.6.azurestaticapps.net/privacidad#eliminar-cuenta |

## Cómo se instala para la evaluación

1. El docente entra con su cuenta de Google a la liga de prueba que comparte el equipo (o se agrega a la lista de testers).
2. Descarga PactoGG desde la ficha de Google Play.

## Entrega continua desde GitHub (requisito 16)

`docs/cd/android-release.yml` → copiar a `.github/workflows/android-release.yml`. Al publicar una etiqueta `v*` corre pruebas, Lint y Detekt, firma el AAB y lo sube a la pista interna con `scripts/play-upload.py`. Secretos: `PACTOGG_UPLOAD_KEYSTORE_BASE64`, `PACTOGG_UPLOAD_STORE_PASSWORD`, `PACTOGG_UPLOAD_KEY_ALIAS`, `PACTOGG_UPLOAD_KEY_PASSWORD`, `PLAY_SERVICE_ACCOUNT_JSON` y `GOOGLE_SERVICES_JSON_BASE64` (el `google-services.json` de Firebase en base64; el workflow lo escribe antes de compilar, valida que sea de `com.pactogg.app` y lo borra al terminar).
