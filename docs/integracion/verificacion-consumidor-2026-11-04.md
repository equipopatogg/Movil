# Verificación del consumidor Android — 2026-11-04

Estado: **implementación local y controles debug correctos; aceptación/integración con proveedor pendientes**. Usuario autorizó empezar las specs y usar aviso básico. Sin commit o push. No se ejecutan scripts del backend ni se modifica su carpeta.

## Qué cambió

MainActivity abre ConnectedApp/ConnectedViewModel con Hilt. El recorrido utiliza estado de sesión y respuestas reales, sin Submit que omita autenticación. Se conservan componentes, iconos, fuentes y colores previos. Otras pantallas permanecen en código sin entrada desde el recorrido conectado; los recordatorios personales no se importan ni enlazan. No se crea PG-08.

Ocho operaciones consumidor implementadas: registro, login, refresh/custodia, logout, crear grupo, crear reto manual para group, evidencia por enlace/foto, consulta paginada de torneos/bracket de solo lectura. Ruta plana de contratos; alias no necesarios para duplicar funcionalidades. Dominio independiente de Android/Retrofit, DTO/mappers explícitos en datos; pequeño corte agrupado en feature/connected con auth/core/session compartidos. Sin nuevas dependencias ni cambios de versiones.

Sesión: AES-GCM y clave Android Keystore, archivo atómico en noBackupFilesDir, sin contraseña persistida. Arranque renueva expirados antes de mostrar identidad; mutex coordina renovación y generación invalida resultados anteriores al logout. No se reenvían POST automáticamente; un 401 de escritura renueva y pide reenvío manual. GET admite un solo reintento tras refresh. Sin redirects ni reintentos de conexión automáticos, sin logs de tokens y sin TrustAll. Un 403 no elimina sesión. Fallo de refresh rotatorio exige acceso seguro para no insistir con token posiblemente consumido. Logout borra localmente incluso si no se confirma revocación remota.

Privacidad: texto provisional local en privacy.xml, casilla inicialmente desmarcada y lectura disponible antes de aceptar. Contacto indicado por equipo. Propiedad pactogg.privacyVersion conserva identificador requerido por guía (2026-10-01); no representa aprobación jurídica ni fecha de redacción. Revisión de equipo/publicación pendiente. URL pública configurable por pactogg.apiBaseUrl; valor de guía HTTPS por defecto, sin secretos.

Fotos: selector del sistema para una imagen, JPEG/PNG/WebP máximo 5 × 1024 × 1024 bytes (confirmado en EvidenceOptions del proveedor). Lectura acotada, sin cámara, video ni permiso general de biblioteca. La selección inicia envío con etiqueta explícita; éxito solo después de respuesta 201/moderación aprobada. Autorización y moderación son del servidor. Enlaces normalizan www./m. y validan hosts exactos según EvidenceUrlValidator; no se acepta host impostor.

## Comprobaciones ejecutadas

| Control | Resultado | Alcance |
|---|---|---|
| assembleDebug | PASS | APK del candidato generado |
| testDebugUnitTest | PASS | 24 pruebas, 0 fallos/errores/omitidas |
| lintDebug | PASS | 0 errores, 0 advertencias; 2 hints anteriores de autoboxing del prototipo |
| detekt | PASS | Sin nuevos umbrales, baselines o supresiones |
| assembleDebugAndroidTest | PASS | APK de pruebas compilado; no ejecución Android |
| Gitleaks 8.30.1 sobre app/src | PASS, salida 0 | Configuración existente, redacción 100 %, sin hallazgos finales |
| GET HTTPS /health | 200 Healthy | Servidor real; comprobación de lectura |
| GET HTTPS /api/v1/tournaments sin token | 401 UNAUTHORIZED | Servidor real; no acceso anónimo |

Comando final desde Movil:

```powershell
./gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:detekt :app:assembleDebugAndroidTest --continue --console=plain
```

Resultado final BUILD SUCCESSFUL en 45 s. Log local ignorado: artifacts/integracion-movil-final.log en raíz del espacio. Manifiesto local ignorado: artifacts/integracion-movil-manifiesto.json, hashes SHA-256 de fuentes/tests seleccionados y artefacto. Intentos anteriores fallidos se conservan como diagnóstico, no evidencia PASS: incluyeron reglas de estilo, SAN del certificado de prueba incompleto y un import de prueba corregidos sin debilitar controles.

Artefacto: app/build/outputs/apk/debug/app-debug.apk. SHA-256 público del APK: `cfc10251e60f8842e749b0cf15009d6a01d9742364062f3f121f63fe452625e8`. No es secreto, token ni firma de aprobación.

## Qué prueban los casos nuevos

ApiIntegrationTest: 12 casos TLS/MockWebServer, contrato de registro→grupo→reto→enlace/foto→torneos→logout; refresh concurrente; 403 con details array sin pérdida de sesión; 401 escritura sin replay; rotación fallida sin bucle; respuesta tardía tras logout; sesión incompleta/fallo de custodia; logout con servicio fallido; paginación incoherente; GET 401 con un reintento y posterior limpieza; sesión restaurada expirada; redirect sin propagación Bearer.

InputRulesTest: 3 casos de mínimos distintos registro/login y consentimiento, dominios impostores/credenciales/HTTP/puertos, fianzas no recibidas/fecha imposible/dinero. Nueve pruebas previas conservadas. Estas pruebas usan datos sintéticos y certificado local confiado únicamente en test; no crean cuentas reales ni acreditan moderación del proveedor.

AccessNavigationTest actualizado para impedir bypass con login vacío y leer privacidad antes de envío. EncryptedSessionStoreTest usa carpeta/alias exclusivos de prueba, verifica cifrado real, roundtrip, limpieza, corrupción y clave ausente. Ambos compilan; **no se ejecutaron** porque adb no detectó dispositivo. No se abrió emulador con memoria disponible insuficiente (aprox. 0.6 GiB). No se borraron datos del teléfono ni se instaló este candidato.

## Pendientes y límites

- Ejecución Android: cifrado/Keystore, selector, recreación, accesibilidad y revisión de apariencia de las vistas conectadas. La evidencia visual anterior corresponde al prototipo.
- Pruebas funcionales con cuentas autorizadas del backend; no se ha ejecutado registro/login real, escrituras ni sesión E2E contra producción. La salud/401 no demuestran esos flujos.
- No hay GET de grupos/miembros/contratos/evidencias: solo se conserva contexto recién creado en memoria. Tras proceso muerto la sesión puede recuperarse, pero ese historial no; la app lo explica.
- Regla de settings editable, nulabilidad/error y alias siguen pendientes de conciliación donde corresponda; cliente omite settings y toma fianzas devueltas.
- No se ofrecen creación/inscripción de torneos, votar, aceptar retos, recuperar contraseña, edición de perfil, notificaciones, exploración o voz sin API disponible.
- Aviso definitivo, controles PG-00/01/02, cobertura, 16 KB, release, Play y aceptación humana mantienen su alcance propio. No se repite aquí la seguridad completa ni se transfiere CI remoto antiguo al candidato local.

Referencias técnicas contrastadas: [Android Keystore](https://developer.android.com/privacy-and-security/keystore) y [selector de fotos](https://developer.android.com/training/data-storage/shared/photo-picker). Fuente proveedor y discrepancias: [alineación](alineacion-backend-2026-11-04.md). [Specs activas](../../specs/README.md).
