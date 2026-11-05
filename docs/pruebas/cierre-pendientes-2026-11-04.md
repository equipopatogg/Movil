# Cierre de pendientes verificables — 2026-11-04

Usuario autoriza resolver lo posible de los pendientes posteriores a main fd7658bc8ff7a5fe4de579e1bdc50a46d15f4625. Rama codex/cierre-pendientes-calidad. Esta fase agrega pruebas, herramientas y evidencia; no cambia código de producción Kotlin, versiones ni reglas backend.

| Control | Resultado local y alcance |
|---|---|
| Suite JVM | PASS: 192 casos, cero fallos/omisiones: 54 unitarias, 42 integración y 96 seguridad |
| Cobertura | Local 748/2756 (27,14 %); CI 743/2756 (26,96 %), frente a 537/2756 (19,48 %). Mismo denominador, sin filtros nuevos ni mínimo ratificado |
| Estados de pantalla | 19 casos nuevos: formularios, duplicados, errores, paginación, filtros, identidad, confirmación/fianza y bytes de foto borrados incluso con fallo |
| Fronteras por operación | 57 casos adicionales: 503, 429 y éxito malformado por cada una de las 19 operaciones; sin reintento ni confirmación falsa |
| Validadores Python | 26 correctos: 19 anteriores, cinco ELF/RELRO y dos de detecciones/ámbito Semgrep |
| Build release | PASS: assembleRelease, SBOM y dependencyInsight graphics-path |
| Ocho bibliotecas del release | LOAD/RELRO y empaquetado ZIP PASS; zipalign -c -P 16 4 con salida 0 |
| Runtime release 16 KB | PASS: emulador aislado x86_64 API37/imagen37.1, PAGE_SIZE=16384; instalación, arranque, login→registro→aviso provisional, sin envíos live |
| Instalación y arranque release 4 KB | PASS en teléfono conectado: am start Status ok, proceso vivo y PAGE_SIZE=4096 |

La firma usada para la prueba local es la identidad debug existente. No es identidad de publicación. Después se restauró el APK debug mediante install -r; sin borrar datos, cerrar sesiones, crear cuentas o realizar escrituras funcionales live. Ese arranque no acredita navegación completa ni aceptación humana.

Release sin firma SHA256: e894d3ac403b7ac1961386681503265f9f55f91fece511676aba8e65e5888651. Candidato firmado local de pruebas instalado SHA256: 890f2ccdfef4047ba17a5e48095450b208c60ce047d792f31b6df8100a5c0282. Informes por biblioteca y manifiesto runtime en artifacts/release/, ignorado. SBOM en app/build/reports/cyclonedx-direct/bom.json. Logs locales en ../artifacts/cierre-release-build.log y cierre-pruebas.log, fuera del repositorio móvil.

## Corrección del diagnóstico 16 KB

La inspección actual de graphics-path y DataStore no confirma incompatibilidad estática. El fin de RELRO de graphics-path de 64 bits es 0x6000; aunque no es múltiplo de 0x4000, coincide con el final del LOAD contenedor, sin datos escribibles posteriores en ese LOAD. Cumple la tercera condición de la [guía oficial Android](https://developer.android.com/guide/practices/page-sizes#relro). Se comprueban LOAD con mínimo 16384 en ABI de 64 bits, el criterio aplicable de 4096 en ABI de 32 bits y offsets ZIP de todas las bibliotecas. No se parchean binarios ni cambian dependencias.

El mismo APK firmado local también se comprobó en un emulador propio de 16 KB (PAGE_SIZE=16384), con arranque y navegación de acceso. El manifiesto ignorado artifacts/release/manifest.json liga fuente/configuración, SBOM, inspecciones, ambos APK y contextos de 4/16 KB por hash. No acredita todos los flujos autenticados, todos los dispositivos/ABI, un AAB de Play o identidad de publicación. Se detuvo únicamente ese emulador al terminar.

## Regresiones reales de CI

Integridad valida primero el Wrapper auténtico. Luego altera un byte de una copia aislada, que nunca ejecuta, y llama a wrapper-validation del mismo SHA oficial usado por setup-gradle. Solo acepta el negativo si failed-wrapper identifica exactamente esa copia y el original no cambió; un error de red sin esa salida no cuenta. Retira la copia y conserva un informe JSON.

Semgrep ejecuta el mismo motor y reglas de producción contra fixtures en un directorio temporal con forma de app/src/main y app/external. Exige tres detecciones positivas exactas, cero falsos positivos en tres ejemplos seguros y cero detecciones fuera de producción. Sin excepciones nuevas ni ejemplos inseguros compilados en Android. [Salida oficial failed-wrapper](https://github.com/gradle/actions/blob/8da46b539cd8fe6ab27ca75128919a18d757604c/wrapper-validation/action.yml) y [referencia de pruebas Semgrep](https://docs.semgrep.dev/writing-rules/testing-rules).

Regresiones y conjunto acreditados por [run41977160594](https://github.com/equipopatogg/Movil/actions/runs/41977160594), fuente 082b52b343bdd8db1567ece652d8dcef863c6c01: 13 trabajos correctos, 192 JVM, cinco Android offline, tres scanners/SBOM y gate PASS. Se descargaron informes de reglas, negativo Wrapper, cobertura y gate. Los cambios posteriores de inicialización de tests/documentación vuelven a comprobarse en el [PR9](https://github.com/equipopatogg/Movil/pull/9) antes de fusionar; no anticipar ese resultado.

## Entradas pendientes

- Privacidad definitiva: responsable, contacto, jurisdicción, conservación/derechos y aviso aprobado con URL o documento. El aviso provisional existente sigue identificado como tal.
- Avatares: contrato con identificador, sin catálogo ratificado de IDs/recursos. Selector desactivado hasta disponer de él.
- Producción: cuenta Play, gestión de identidad de firma, ficha y datos de privacidad requieren información y decisiones reales. Sin publicación por inferencia.
- Aceptación funcional completa: faltan escenarios de recreación Android, accesibilidad, errores reales de proveedor y datasets de torneos/paginación. Fixtures y pruebas de estado no acreditan todo el recorrido.
- Backend avanzó a 451c8ae1bfae3df64cf95f5a26ace7b1eacb3b1e (v0.6.0), con quince expedientes de evidencia/listado, votación, puntos, compromisos, avisos y fama. Se mantiene el corte móvil de 19 operaciones; las adiciones requieren revisar contratos/specs como siguiente alcance.

La propuesta de cobertura es priorizar flujo/riesgo y errores antes de ratificar un porcentaje mínimo. La mejora no acredita suficiencia ni aceptación humana.


Wrapper negativo real ya comprobado en run41976610916: el validador rechazó únicamente la copia alterada y conservó el digest original. El primer intento de verificación de fixtures Semgrep terminó en ERROR; no se aceptó como PASS ni se debilitó la regla. El motor infería la raíz del proyecto a partir del directorio temporal sin Git, dejando fuera los ejemplos por sus rutas. Se fija explícitamente /src como raíz y se conservan los IDs literales; las reglas no cambian. Se repite antes de fusionar. Las 76 regresiones nuevas son obligatorias por nombre completo en la matriz, no solo permitidas por categoría.


## Cierre de comprobaciones — 2026-11-05

Se ejecutaron quince peticiones reales sin Authorization a las operaciones protegidas del corte: las quince devolvieron 401. Sin credenciales, TLS normal, IDs sintéticos y cuerpos vacíos para escrituras; no se crearon/modificaron datos. Evidencia ignorada artifacts/live-negative/no-auth.json. Esto acredita rechazo sin sesión, no autorización entre usuarios/roles ni respuestas 403/404 completas.

La inicialización del ViewModel en los tests nuevos espera explícitamente sesión/restauración antes de comprobar estados. Segunda ejecución local: 192 PASS, sin omisiones, cobertura 27,14 %. La diferencia de cinco líneas cubiertas respecto a CI queda expuesta; no se redondea ni se reduce el denominador para simular igualdad.
