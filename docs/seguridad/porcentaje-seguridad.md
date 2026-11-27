# Porcentaje de seguridad — PactoGG Android

Fecha: 2026-11-26. Versión: 1.1.0.

**Cómo se calcula:** % seguridad = controles que **cumplen** con evidencia automática ÷ controles aplicables × 100. Un control **parcial** cuenta como 0.5 y un **riesgo aceptado** cuenta como 0, aunque esté justificado. Los controles salen de los 8 grupos de [OWASP MASVS](https://mas.owasp.org/MASVS/) y cada uno se relaciona con el [OWASP Mobile Top 10](owasp-mobile-top10.md).

## Resultado

| Grupo MASVS | Controles | Cumple | Parcial | Riesgo aceptado |
|---|---|---|---|---|
| STORAGE | 5 | 5 | 0 | 0 |
| CRYPTO | 2 | 2 | 0 | 0 |
| AUTH | 4 | 4 | 0 | 0 |
| NETWORK | 3 | 3 | 0 | 0 |
| PLATFORM | 4 | 4 | 0 | 0 |
| CODE | 5 | 5 | 0 | 0 |
| RESILIENCE | 4 | 1 | 1 | 2 |
| PRIVACY | 4 | 4 | 0 | 0 |
| **Total** | **31** | **28** | **1** | **2** |

**% seguridad = (28 + 0.5) ÷ 31 = 91.9 %.** Sin contar resiliencia (la app no maneja dinero): 27 ÷ 27 = **100 %**.

**Vulnerabilidades críticas abiertas: 0.** Es un criterio de compuerta, no un número escrito a mano: el PR no se puede fusionar si Gitleaks encuentra un secreto, Semgrep da un ERROR, Trivy encuentra una vulnerabilidad CRITICAL o HIGH con corrección, Lint o Detekt dan una advertencia, o falla cualquier prueba de seguridad (compuerta de calidad de `android-ci.yml`).

## Detalle de controles

| ID | Control | Evidencia | Estado |
|---|---|---|---|
| S1 | Sesión cifrada con AES-GCM y llave en Android Keystore | `EncryptedSessionStoreTest` | Cumple |
| S2 | `allowBackup=false` y reglas de extracción vacías | `AndroidManifest.xml` | Cumple |
| S3 | Copia sin conexión por cuenta, sin tokens ni escrituras, borrada al cerrar sesión | `OfflineCacheTest` | Cumple |
| S4 | Sin datos sensibles en logs | Semgrep `pactogg-no-log-sensitive` | Cumple |
| S5 | Bytes de fotos borrados de memoria después de subir o al salir | `ConnectedFlowTest.photoBytesAreErasedEvenWhenUploadFails` | Cumple |
| C1 | Solo primitivas de la plataforma | `EncryptedSessionStoreTest.corruptedOrMissingKeyDoesNotRestoreAnAccount` | Cumple |
| C2 | Sin llaves ni secretos en el APK (IA sin llave en la app) | Gitleaks, `FirebaseIdeaGenerator` | Cumple |
| A1 | Token corto y renovación rotativa serializada | `RefreshSessionSecurityTest` | Cumple |
| A2 | Logout revoca en el servidor y da de baja el push | `LogoutSecurityTest`, `DeviceApiTest` | Cumple |
| A3 | Sesión revocada o 401 borra la sesión y no repite escrituras | `PlaySecurityTest`, `AccountSecurityTest` | Cumple |
| A4 | Eliminar cuenta exige contraseña y confirmación | `PlayApiTest.PL_09_deleteAccountSendsConfirmationInDeleteBody` | Cumple |
| N1 | Solo HTTPS | `NetworkSecurityConfigTest` | Cumple |
| N2 | Certificados fijados para la API con expiración | `NetworkSecurityConfigTest` | Cumple |
| N3 | Sin `TrustManager`/`HostnameVerifier` permisivos | Semgrep (2 reglas), `ApiClientFactoryTest` | Cumple |
| P1 | Permisos mínimos y en tiempo de ejecución | `AndroidManifest.xml` | Cumple |
| P2 | Sin WebView ni puentes JavaScript | Semgrep `pactogg-no-javascript-interface`, `pactogg-no-webview-file-access` | Cumple |
| P3 | Deep links validan el token antes de la red | `PasswordRecoveryApiTest.PL_11_badEmailTokenOrShortPasswordNeverReachTheNetwork` | Cumple |
| P4 | Fotos por Photo Picker (sin permiso de almacenamiento ni cámara) | `MatchScreen.kt`, `ConnectedEvidence.kt` | Cumple |
| K1 | Validación de entradas antes de la red | `PlayRulesTest`, `InputRulesTest`, `*ValidationTest` | Cumple |
| K2 | SCA con Trivy y SBOM CycloneDX | `android-security.yml` | Cumple |
| K3 | SAST: Detekt, Lint (advertencias como error) y Semgrep | job `static-analysis` | Cumple |
| K4 | Dependencias con versión fija y Wrapper verificado | `libs.versions.toml`, job `integrity` | Cumple |
| K5 | IA: entrada mínima y salida filtrada | `IdeaPromptTest` | Cumple |
| R1 | R8 con ofuscación y reducción en release | `app/build.gradle.kts` | Cumple |
| R2 | Detección de root (aviso, no bloqueo) | `DeviceIntegrityTest` | Parcial |
| R3 | Play Integrity verificada en el servidor | — | Riesgo aceptado: sin dinero; reglas en el servidor |
| R4 | Antitampering / antidepuración | — | Riesgo aceptado: mismo motivo |
| V1 | Aviso de privacidad en la app y la landing (misma versión) | `AccessNavigationTest`, landing `tests/landing.test.js` | Cumple |
| V2 | Consentimiento explícito al registrarse | `AccessNavigationTest.registerDisplaysProvisionalPrivacyBeforeAnySubmission` | Cumple |
| V3 | Datos mínimos a terceros (Gemini solo recibe el juego; ML Kit en el teléfono) | `IdeaPromptTest`, `MlKitScoreReader` | Cumple |
| V4 | Eliminar cuenta y datos desde el perfil | `PlayApiTest.PL_09_deleteAccountSendsConfirmationInDeleteBody` | Cumple |

## DAST

La API que usa la app se prueba dinámicamente con OWASP ZAP después de cada despliegue del backend (job `zap-dast` del repositorio `backend`). El APK no tiene una prueba dinámica automática propia; la verificación en teléfono está en las pruebas instrumentadas (`connectedDebugAndroidTest`).
