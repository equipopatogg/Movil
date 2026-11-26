# Matriz inicial de seguridad Android

Fecha de consulta: 2026-10-29. Referencias: [OWASP MASVS](https://mas.owasp.org/MASVS/) para requisitos móviles y [MASTG](https://mas.owasp.org/MASTG/) para orientar su verificación. Esta matriz agrupa temas; no enumera todos los requisitos ni acredita conformidad o certificación.

La selección de versión/perfil, IDs concretos y pruebas MASTG aplicables queda pendiente para la spec y artefacto que se evalúen. No se traslada la checklist ASVS L2 ni sus casillas marcadas del profesor.

## Verificación ejecutada — 2026-11-19

Cada grupo se verificó con pruebas automáticas que corren en CI (`android-ci.yml`) y en el teléfono (Motorola edge 40 neo, Android 15: 23 pasan, 6 omitidas por requerir cuenta QA, 0 fallan). Estado: **CUMPLE** (con evidencia), **RIESGO ACEPTADO** (justificado).

| Grupo MASVS | Control aplicado en PactoGG | Evidencia (prueba o archivo) | Estado |
|---|---|---|---|
| STORAGE | Sesión cifrada con AES-GCM y llave en Android Keystore; `allowBackup=false` y reglas de extracción vacías; la copia sin conexión (Room) se borra al cerrar sesión y es por cuenta; sin datos sensibles en logs | `EncryptedSessionStoreTest` (instrumentada), `OfflineCacheTest.PL_12_anotherAccountNeverSeesSavedDataAndClearRemovesIt`, `AccessNavigationTest.rotationAndRecreationKeepDraftWithoutSavingPasswordOrAuthenticating`, Semgrep `pactogg-no-log-sensitive` y `pactogg-no-world-accessible-files` | CUMPLE |
| CRYPTO | Solo primitivas de la plataforma (Keystore, AES-GCM); sin llaves ni secretos en el APK | `EncryptedSessionStoreTest.corruptedOrMissingKeyDoesNotRestoreAnAccount`, Gitleaks en CI | CUMPLE |
| AUTH | Token corto con renovación serializada; sesión revocada se borra; escrituras no se repiten tras renovar; logout con endpoint y baja del push | `PlaySecurityTest` (35 operaciones), `AccountSecurityTest`, `RefreshSessionSecurityTest`, `LogoutSecurityTest`, `DeviceApiTest.PL_10_logoutRunsDeviceRemovalBeforeClearingAndSurvivesItsFailure` | CUMPLE |
| NETWORK | Solo HTTPS (`cleartextTrafficPermitted=false`), certificados fijados para la API (raíces de Let's Encrypt y ZeroSSL, con expiración), sin `hostnameVerifier` ni `TrustManager` permisivos | `NetworkSecurityConfigTest`, `ApiClientFactoryTest`, Semgrep `pactogg-no-cleartext-source-url`, `pactogg-no-trust-all-hostname-verifier` y `pactogg-no-empty-trust-manager` | CUMPLE |
| PLATFORM | Permisos mínimos (`INTERNET`, `POST_NOTIFICATIONS` en tiempo de ejecución); fotos por Photo Picker y voz por el reconocedor del sistema (sin cámara ni micrófono); sin WebView ni puentes JavaScript; deep link de restablecer contraseña valida el token | `AndroidManifest.xml`, Semgrep `pactogg-no-javascript-interface` y `pactogg-no-webview-file-access`, `PasswordRecoveryApiTest.PL_11_badEmailTokenOrShortPasswordNeverReachTheNetwork` | CUMPLE |
| CODE | Validación de entradas antes de la red (ids, textos, fotos); dependencias con SBOM y Trivy; Detekt y Lint con advertencias como error | `PlayRulesTest`, `PlayValidationTest`, `AccountRulesTest`, `*ValidationTest`, Trivy + CycloneDX (`android-security.yml`) | CUMPLE |
| RESILIENCE | R8 con ofuscación y reducción en release; aviso de root en "Privacidad y seguridad" (desde 1.1.0). Sin Play Integrity ni antitampering: la app no maneja dinero ni datos financieros, las reglas viven en el servidor y Play Integrity exige verificación en el backend | `app/build.gradle.kts` (`optimization`), `proguard-rules.pro`, `DeviceIntegrityTest` | PARCIAL / RIESGO ACEPTADO |
| PRIVACY | Aviso en la app y en la landing (`/privacidad`, misma versión 2026-10-01), consentimiento al registrarse, eliminar cuenta desde el perfil, datos mínimos | `AccessNavigationTest.registerDisplaysProvisionalPrivacyBeforeAnySubmission`, `PlayApiTest.PL_09_deleteAccountSendsConfirmationInDeleteBody`, landing `tests/landing.test.js` | CUMPLE |

La resiliencia se selecciona según riesgo: con el análisis anterior se decidió fijar certificados y no instalar antitampering ni detección de root. La firma Android y la autorización del servidor siguen siendo asuntos propios, con evidencia diferenciada.

Clasificación por [OWASP Mobile Top 10](owasp-mobile-top10.md) y cálculo del [porcentaje de seguridad](porcentaje-seguridad.md).

Los resultados técnicos históricos están en [verificación local](../verificacion-local.md). No se reejecutaron aquí ni acreditan todos los requisitos de una categoría. Cada evaluación futura registrará requisito/versión, artefacto, prueba concreta, resultado y límites con la [plantilla de evidencia](../../.specify/templates/evidencia-template.md).

## Backend y controles complementarios

[ASVS](https://asvs.owasp.org/) se dirige a aplicaciones web y servicios; su uso en el backend .NET requiere seleccionar versión, nivel y requisitos con sus responsables. No se declara ASVS L2 aprobado o cumplido ni se reutilizan IDs antiguos sin verificar versión.

SAST, SCA/SBOM, secretos, firma y seguridad del pipeline complementan la evaluación móvil; no sustituyen pruebas dinámicas. ZAP sobre una API no constituye una evaluación completa del APK. Véanse [modelo de amenazas](modelo-amenazas.md), [pruebas](../preparacion/plan-de-pruebas.md) y [compuertas](../preparacion/controles-y-compuertas.md).
