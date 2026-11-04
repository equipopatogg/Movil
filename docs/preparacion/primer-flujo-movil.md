# Preparación del primer flujo móvil

Fecha inicial: 2026-10-30; revisión 2026-11-03. Estado: preparación documental con boceto disponible; contrato backend y cierre de recorrido pendientes. No es una spec funcional aprobada ni implementa pantallas. Este documento reúne lo necesario para redactarlas cuando llegue el contrato.

## Fuentes y alcance revisado

Historias_de_usuario_PactoGG.xlsx, hoja Historia de usuario Agile: IDs en columna B y narrativas en G. Componentes_Backend_y_Movil_PactoGG.xlsx, hoja Móvil: IDs MV y descripciones. PactoGG — Descripción del Móvil.docx, secciones 6–9. Requisitos Móvil v3, corte en página 4. Los originales no se modifican.

La descripción DOCX revisada no contiene imágenes incrustadas. Posteriormente se recibió y recorrió Figma Make: sirve de boceto visual; los documentos y acuerdos prevalecen. Falta conciliar el diseño aplicable y no se acredita aún fidelidad de una implementación.

## Historias originales y componentes relacionados

| Tema | Historias originales | Componentes de la hoja Móvil | Estado para el corte |
|---|---|---|---|
| Demo y acceso inicial | 1–10 | MV-12 a MV-18 | Contexto previo al registro; confirmar qué recorrido entra en la evaluación |
| Registro con correo/contraseña | 11, 18, 19 | MV-19 | Exigido registro por v3; campos/reglas/consentimiento y respuestas pendientes de contrato |
| Registro con Google | 12 | MV-18 y módulo BE-11 | Está en las fuentes; confirmar inclusión y proveedor para este corte, sin activarlo por inferencia |
| Login | 13 | MV-21 | Exigido por v3; credenciales, sesión y ruta siguiente pendientes |
| Persistencia de sesión | 14 | MV-68 | Requisito general; refresh/revocación/expiración por acordar con backend |
| Logout | 15 | MV-63, MV-68 | Exigido en backend para el corte y en ambos por requisitos generales; coordinar alcance móvil |
| Recuperación/verificación | 16, 17 | MV-20 y flujos descritos | Confirmar si condicionan poder registrarse/ingresar; no inventar rutas |
| Apodo/avatar | 21, 22 | MV-28 | Apodo requerido por decisión del líder; momento de captura y recursos por confirmar |
| Primer grupo | 30, 31, 32 | Flujos de grupos descritos | Candidato posterior al ingreso; confirmar qué pantallas revisará el profesor |
| Privacidad/eliminación | 19, 29 | MV-63, MV-65 | Obligaciones generales por planear; no declarar funciones existentes |

IDs MV/BE son de componentes; los números de historia son los originales, sin convertirlos en IDs ficticios de funcionalidad. Las prioridades originales no se cambian ni se promete que todos estos flujos entren al corte.

## Recorrido por confirmar

Las fuentes describen Splash → Demo/Bienvenida → Registro → Verificación → Apodo/avatar/Perfil → Grupo → Tablero. Login recurrente puede tener otro destino según sesión y perfil. Falta acordar qué parte exacta significa «pantallas posteriores al ingreso por primera vez». Las pestañas Retos/Torneos/Cuentas/Perfil son estructura conocida, no prueba de todos esos flujos.

Discrepancias a resolver: la descripción habla de cuatro pasos de demo y los componentes enumeran MV-13 a MV-17; puede tratarse de pasos más cierre, pero no se da por resuelto. MV-12 menciona brillo que no se hereda: aplica DESIGN vigente. La galería de ocho avatares y reglas de contraseña del texto antiguo requieren confirmar recursos y contrato, no generar valores por copia.

## Estados que se prepararán por pantalla

| Situación | Comportamiento por especificar | Dependencia |
|---|---|---|
| Inicio/envío en curso | Estado visible, envío único y controles coherentes | Contrato y criterio de UI |
| Entrada inválida | Mensaje junto al campo según regla ratificada; sin enviar lo inválido | Reglas de backend, sin regex/minimos inventados |
| Registro aceptado | Transición al siguiente estado real; conservar solo datos necesarios | Verificación, perfil y sesión |
| Login aceptado | Sesión gestionada según contrato y navegación acordada | URL/JSON/seguridad |
| Credenciales o permiso rechazados | Diferenciar autenticación de autorización; sin revelar datos internos | Códigos/cuerpo de error |
| Red/timeout/servidor | Mensaje recuperable, fin de carga y alternativa segura | Reintento/idempotencia acordados |
| Rotación/proceso/retorno | Estado y efectos tratados sin doble envío o exposición sensible | Diseño de ciclo de vida y sesión |
| Logout/fallo de revocación | Limpieza local y tratamiento remoto por contrato; no asumir cierre de Activity | Política compartida pendiente |

Registro/login no justifican pedir cámara, contactos o ubicación. Micrófono/push pertenecen a otros flujos y conservan su alcance pendiente. La pantalla no almacena credenciales en navegación ni logs.

## Matriz inicial de pruebas, sin ejecución

| Caso de diseño | Criterio a cerrar | Nivel propuesto | Dependencia |
|---|---|---|---|
| PF-C1 Registro válido/inválido | Resultado y validaciones de historia 11/18/19 | JVM + HTTP + UI | Reglas, contrato, consentimiento |
| PF-C2 Login correcto/rechazado | Historia 13 y errores diferenciados | JVM + MockWebServer + UI | Códigos/JSON/sesión |
| PF-C3 Sin red/timeout/respuesta incompleta | UI recuperable y ausencia de sesión falsa | HTTP + ViewModel/UI | Contrato y política de errores |
| PF-C4 Doble toque, rotación y retorno | Una operación por intención y navegación coherente | JVM + Compose/dispositivo | Estrategia de estado/efectos |
| PF-C5 Sesión y logout | Historias 14/15, expiración/revocación y datos locales | Cliente real + API por coordinar | Contrato de sesión y almacenamiento |
| PF-C6 Primer ingreso y accesibilidad | Pantallas acordadas, labels, foco, fuente y contraste | Compose/dispositivo + revisión visual | Maqueta/alcance ratificados |

PF identifica casos de diseño, no pruebas escritas ni historias nuevas. Todos están NO_EJECUTADO. Las pruebas técnicas anteriores no acreditan registro o login.

## Qué falta recibir

Spec backend y contrato versionado; URL de entorno; ejemplos sintéticos de éxito/error; reglas de registro/sesión y recursos de prueba acordados; criterio de primer ingreso y conciliación del boceto disponible. Véase [contrato API](../integracion/contrato-api.md). No se crearán specs funcionales cerradas o clientes hasta disponer de esas entradas.

Se inspeccionaron además los nombres del RAR Integradora y su README, sin extraer o ejecutar aplicaciones: se identifica una landing HTML y una descripción de stack/estilo, no una referencia visual móvil ratificada. No se auditó visualmente todo su contenido ni se adoptaron sus servicios/configuración como decisiones nuevas.

## Actualización de fuente visual — 2026-11-03

Ya se recibió y recorrió el boceto [Figma Make](https://www.figma.com/make/jSaAZiJ20jLRdwieMnJTLp/App-PactoGG-de-Retos-Gamer). La ausencia visual descrita arriba es histórica. No se importan reglas o valores de la demo. Falta conciliar estilo/navegación y cerrar el recorrido funcional del corte con contrato backend.

Expedientes: PG-03 acceso y sesión; PG-04 primer ingreso. Propuesta mínima por revisar: tablero, perfil/apodo y grupos con estados reales. No es una selección textual del profesor ni ratificación del usuario. Play se prepara mediante checklist, no una spec de negocio inventada.
