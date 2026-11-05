# Giro de pantalla, cambio de cuenta y permisos — 2026-11-05

Las tres comprobaciones solicitadas pasaron en el alcance descrito. Se conservaron la sesión del teléfono y las funciones actuales; privacidad definitiva y avatares esperan al backend. Google Play queda fuera.

| Comprobación | Resultado real |
|---|---|
| Giro y recreación | En emulador API35/x86_64 se giró a horizontal y se recreó la actividad. El formulario conservó sus valores transitorios, no abrió pantallas protegidas y la contraseña sintética no apareció en el estado serializado real de Android |
| Cambio de formulario | Pasar entre registro e inicio de sesión limpió correo, contraseña y consentimiento anterior |
| Cambio de cuenta | Los tests del estado móvil limpiaron grupo, reto, borrador, filtros, mensajes y credenciales anteriores; errores y respuestas tardíos de A no sustituyeron los datos de B |
| Custodia Android | Reabrir el almacenamiento cifrado después de escribir B recuperó únicamente B, sin copia anterior residual; el borrado posterior dejó la custodia vacía |
| Permisos del servidor | Con dos cuentas QA existentes, el miembro recibió 200 y el no miembro 403 para el detalle de un mismo grupo privado, sus miembros y su tablero de retos |

## Resultados y evidencia

- JVM: **196 casos**, cero fallos/omisiones (56 unitarias, 44 integración y 96 seguridad).
- Android offline: **8 casos**, cero fallos/omisiones, con ejecución real en el emulador aislado API35.
- Validadores Python: **26 casos**, correctos.
- Prueba manual contra proveedor: **12 comprobaciones correctas**: dos logins, dos perfiles de identidad propia, dos consultas de grupos y seis lecturas miembro/no miembro. Los dos cierres de las sesiones creadas para esta prueba devolvieron 204, con all=false.

Evidencia generada e ignorada: app/build/test-results/testDebugUnitTest/, app/build/outputs/instrumented-offline/ y artifacts/live-access/permissions.json. Logs locales fuera del repositorio móvil en ../artifacts/pruebas-rotacion-sesion-build.log y pruebas-rotacion-emulator.log. El runner comprueba los ocho nombres completos; la matriz exige también los cuatro casos JVM añadidos.

La aplicación probada tuvo SHA256 04969ab22dc4b30bdcfe840221ba689b1f412e54455acae41d3777d661aeba1d; APK de pruebas dd768ad4e969811cef47a4d877f6feab00350c3a15b9dfdf4ed85f341b15a487. Informes y huellas corresponden al código de pruebas local de esta rama; no se acredita una nueva ejecución remota por este documento.

## Reproducción y límites

Las pruebas de interfaz están en AccessNavigationTest; las de custodia en EncryptedSessionStoreTest; las de concurrencia/cambio de cuenta en ApiIntegrationTest y ConnectedFlowTest. El verificador scripts/run-instrumented.py selecciona explícitamente estas clases offline en un dispositivo indicado. Usar un emulador de pruebas sin cuenta; no borrar la sesión de un teléfono para conseguir ese estado.

El verificador manual scripts/check-live-access.py requiere --accounts-dir con la ubicación privada de las dos cuentas QA. No ejecutarlo automáticamente en CI. Credenciales y tokens no se imprimen ni se guardan en informes; TLS se valida y los redirects están desactivados. Solo se leen grupos existentes; no se crean grupos, retos ni se alteran puntos. Se cierran únicamente las sesiones recién creadas por la prueba.

Esto acredita aislamiento entre miembro y no miembro para esas tres lecturas. No certifica todos los roles administrativos, todas las escrituras, todos los dispositivos ni destrucción completa del proceso por el sistema. La comprobación de giro sí incluye una recreación y la inspección posterior del estado guardado real; no se confunde con un simple cambio visual del diseño. No se realizó publicación ni se usaron credenciales de producción de firma.
