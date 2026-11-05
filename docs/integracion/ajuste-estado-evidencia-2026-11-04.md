# Ajuste de estado posterior a evidencia — 2026-11-04

Estado: implementado y verificado localmente; aceptación integral pendiente. Cambio autorizado dentro de MO-EVI-02 y conciliación de MO-CON-01. No se añaden expedientes ni pantallas. Backend fuente 8baa9e6d04038c1d6cf9588c5526ad63b94c3f87.

Después de POST confirmado, guardar evidencia y consultar GET /api/v1/contracts/{id} con transporte/sesión existentes; verificar ID y grupo. Mostrar solo estado recibido. Si la lectura falla, conservar confirmación, marcar estado pendiente y advertir que no se repita envío. Cancelación se propaga; respuesta tardía no repone contexto tras logout. No modificar reglas de aceptación ni integrar las once operaciones completas. Sin versiones/dependencias nuevas o cambios de diseño.

## Verificación del candidato

- JVM: 28 pruebas, 0 fallidas, 0 errores, 0 omitidas. Incluye cuatro métodos nuevos: tres de estado (actualización, fallo conservando envío, logout durante lectura) y uno HTTP con ruta/Bearer y rechazo de IDs/grupo ajenos.
- Debug, Lint, Detekt y compilación androidTest: PASS, BUILD SUCCESSFUL; no supresiones ni umbrales cambiados.
- APK actualizado instalado con conservación de datos en teléfono; prueba real de logout, contraseña incorrecta 401 y login válido correcta. Cuenta QA queda iniciada.
- No se repitió creación de grupos ni subidas contra proveedor para no consumir más cupos/datos de QA. La actualización posterior a evidencia se verificó con fixtures y estado; su recorrido real en teléfono queda pendiente. La prueba anterior de subida real corresponde al candidato anterior y conserva su propio hash.
- No se volvió a ejecutar Gitleaks: la herramienta portable anterior no está disponible en su ubicación; no se instaló otra. No atribuir escaneo de secretos anterior a este candidato.
- Sin commit, push, CI nuevo ni cambios AWS/backend.

APK debug, SHA-256 público: `d0c729a8235eef54972318ea2251b35d4487652e729fbc627d17d7fe53d76e28`.

Evidencia ignorada local bajo PactoGG/artifacts/: ajuste-evidencia-build.log y ajuste-evidencia-acceso-telefono.log. Logs HTTP de tests usan fixtures y no credenciales reales. AC5 mantiene validación local positiva y aceptación de proveedor pendiente; restantes AC siguen abiertos.
