# Integración de once operaciones confirmadas — 2026-11-04

Estado: **implementadas localmente; recorrido positivo real PASS**, con límites de aceptación pendientes. Usuario solicitó integrar las once specs derivadas de Sistema.docx. Backend contractual fijado al commit 8baa9e6d04038c1d6cf9588c5526ad63b94c3f87; no se modificó backend/AWS ni se presupone que el servidor publique ese SHA en cada respuesta.

## Candidato y alcance

Base Git móvil: `3b128f94fd2c68ece9ec5d9639f5ccc4cd0ad566`, con cambios locales sin commit/push. APK debug instalado en teléfono; SHA-256 público: `04969ab22dc4b30bdcfe840221ba689b1f412e54455acae41d3777d661aeba1d`. Relación de archivos de app/src y huellas públicas en artifacts/social-fuentes-sha256.json (ignorado). El informe no atribuye ejecución remota al candidato sin publicar.

PactoApi reutiliza GET de reto y hereda ProfileApi, GroupReadApi, ContractActionsApi. ApiSocialRepository/SocialRepository agrupan las nuevas operaciones; ApiProductRepository conserva creación/evidencia/torneos. SessionManager único actualiza identidad en archivo cifrado preservando tokens. Sin versiones o dependencias nuevas, permisos, firma release o alteraciones backend.

UI conectada: perfil real y edición de apodo; disponibilidad pública con espera/cancelación/enfriamiento; lista/detalle de grupos, unión por código y miembros; tablero filtrable/paginado, detalle ampliado con identidad/contador/versión/fechas; aceptar/rechazar con confirmación y versión vista. Crear reto permite rival real distinto del usuario. Recuperar listas desde servidor sustituye la restricción anterior a contextos recién creados; no se activa Offline-First.

## Operaciones y evidencia real

| Spec | Resultado observado en teléfono / API real |
|---|---|
| MO-USR-02 | Perfil y estadísticas recibidos; consulta posterior refleja cambio de apodo |
| MO-USR-03 | Apodo temporal confirmado por PATCH y GET; apodo original restaurado al final |
| MO-USR-06 | Consulta de disponibilidad del apodo temporal correcta, sin Bearer |
| MO-GRP-02 | Lista real de cinco grupos QA recuperada después de arrancar nueva sesión de actividad |
| MO-GRP-03 | Grupo existente seleccionado y consultado por ID |
| MO-MEM-01 | Cuenta QA auxiliar unida por código en minúsculas al grupo existente; la corrida final reutiliza pertenencia ya creada |
| MO-MEM-02 | Miembros reales recuperados y representación de apodo comprobada |
| MO-CON-02 | Tablero real incluye el reto dirigido creado por la otra cuenta |
| MO-CON-03 | Detalle confirma destinatario y versión del reto real |
| MO-CON-06 | Confirmación desde ViewModel conectado devuelve in_progress; saldo coincide con saldo anterior menos fianza recibida del grupo |
| MO-CON-07 | Otro reto dirigido pasa a cancelled/closed_reason=rejected, conservando saldo previo |

SocialBackendTest es opt-in (pactogg.social.live=true) y exige identidad QA existente. Perfil/grupos/miembros/tablero/detalle/acciones recorren ViewModel/estado reales dentro de la app; se comprueban nodos Compose. Unión de la segunda cuenta y creación de retos dirigidos se realizan mediante repositorios Android con sesión auxiliar solo en memoria: no se presenta como prueba manual de cada toque del formulario de unión/creación. La segunda sesión se cierra al terminar. Dos retos QA nuevos quedan registrados (uno aceptado y otro rechazado); no se inventó devolución de fianza ni borrado porque esos endpoints no existen. Se reutilizó un grupo QA y no se crearon más grupos; cuenta original tiene cinco, por lo que no conviene repetir la antigua prueba que crea grupos indiscriminadamente.

Cuenta auxiliar de prueba creada una sola vez; credenciales de ambas fuera de proyecto/Git, en directorio QA local. No se imprimieron passwords/tokens ni se accedió a fotos personales. La primera corrida llegó a unión/miembros y falló por selector Compose ambiguo (apodo en cabecera y tarjeta); se corrigió la prueba sin alterar producto para ocultarlo. social-telefono-real.log conserva el fallo original; social-telefono-final.log contiene **OK (1 test), 12.801 s** y los once pasos positivos.

Regresión de acceso del mismo candidato: logout, contraseña inválida con 401 y login válido correctos en teléfono (**OK (1 test), 6.759 s**). Al finalizar permanece cuenta QA original iniciada con su apodo original.

## Controles locales

- **44 JVM correctas, 0 fallidas/errores/omitidas.** Diez métodos nuevos HTTP para operaciones/contratos, tres de elegibilidad/paginación y tres de espera/cancelación/429 de apodo, además de los 28 previos.
- Build debug, Lint, Detekt y compilación androidTest: **PASS**. Fuente estable de cierre en social-implementacion-build-cierre.log (BUILD SUCCESSFUL). Sin supresiones, cambios de reglas o umbrales.
- Los primeros fixtures HTTP fallaron por SAN incompleto del certificado local; se agregó localhost/127.0.0.1/::1 en tests, sin cambiar confianza TLS de producción. Un intento de Lint falló internamente por símbolo descartado; reinicio y análisis estable posterior pasaron. Un método UI se separó para cumplir complejidad, sin elevar límites.
- Gitleaks 8.30.1 portable oficial: checksum del ZIP verificado con manifiesto oficial; escaneo de fuentes/doc sin hallazgos. Se recuperó localmente la herramienta faltante bajo herramientas de PactoGG en AppData, sin instalación global. Evidencia social-gitleaks-cierre.log (incluye conciliación documental final); ninguna exclusión nueva.
- Git diff --check correcto. No se ejecutó CI remoto, SAST Semgrep, pruebas release/16 KB o publicación por esta integración. SCA no se repitió: dependencias/locks no cambiaron.

## Pendientes que no se declaran aceptados

1. Selector de avatar/catálogo: desactivado; edición de apodo sí funcional. USR-03 sigue parcial en avatar.
2. Completar UI manual/accesibilidad/ciclo de vida, paginación/filtros con más datos reales y casos negativos/autorización entre cuentas por cada AC. Fixtures de 402/403/409 y control de versión no prueban todos esos rechazos en servidor real.
3. No se volvieron a enviar fotos/enlaces en esta corrida para no consumir cupos. Su prueba real anterior y el refresco auxiliar tienen evidencia propia; no trasladar aquella prueba a este nuevo APK sin ejecución.
4. Conciliar regla de evidencia permitida en open, semántica frente a aceptación previa, schemas, avatares y recuperación de fotos señalados en revisión de contratos.
5. Aviso definitivo, Play, aceptación humana y controles generales de PG-00/01/02/03 siguen independientes. No marcar specs completas por un recorrido positivo ni heredar aceptación backend.

Evidencia ignorada bajo PactoGG/artifacts/: social-implementacion-build-cierre.log, social-telefono-real.log, social-telefono-final.log, social-regresion-acceso.log, social-gitleaks-cierre.log y social-fuentes-sha256.json. Fuentes y estados actualizados en specs; no publicación.

Comprobación documental de cierre: 479 enlaces locales revisados, cero rotos y 23 expedientes activos.
