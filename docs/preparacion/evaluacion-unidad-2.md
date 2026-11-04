# Preparación para el corte del 3 y 4 de noviembre de 2026

Revisión inicial 2026-10-30; contraste completo actualizado **2026-11-03** con `Requerimientos para el desarrollo del proyecto Móvil v3.pdf`, cuatro páginas, facilitado por el usuario. La página 4 denomina el corte **segunda unidad, 3 y 4 de noviembre**, y separa la evaluación general del **2 de diciembre de 2026**. Preparar Git/CI no satisface los entregables funcionales ni autoriza desplegar.

## Entregables del corte y estado comprobable

| Área | Requisito de la página 4 | Situación de esta preparación |
|---|---|---|
| Backend | Aplicación desplegada; URL de nube verificable | Base técnica BE-00–BE-03 local existente; nube/URL y evidencia de despliegue pendientes |
| Backend | Registro de usuario, Login y Logout publicados | Specs y contrato pendientes; no copiar rutas antiguas como definitivas |
| Backend | Cuatro endpoints adicionales a los anteriores | Selección/alcance del equipo pendientes; no se eligen por inferencia |
| Móvil | Registro de usuario y Login funcionales | No implementados en esta preparación; contrato y flujo pendientes |
| Móvil | Pantallas posteriores al ingreso por primera vez | Alcance/maqueta por ratificar; las vistas técnicas no acreditan el recorrido |
| Móvil | Inicio del registro y configuración en Google Play | Estado de la cuenta/configuración sin comprobar; debe aportarlo su responsable |

La documentación preparada y los tests técnicos anteriores no satisfacen por sí solos estos entregables funcionales. No se afirma una conexión de extremo a extremo ni una publicación.

Actualización de responsabilidad: [Backend](../../../Backend/README.md) ya tiene implementación/verificación técnica local en este espacio; no se espera una implementación funcional externa. Sus sondas y pruebas técnicas **no sustituyen registro/login/logout o los cuatro endpoints adicionales**. La preparación Git/CI no cambia fechas ni acredita nube, funciones, Play o aceptación del corte.

## Incógnitas que condicionan el corte

- Delimitar specs funcionales backend y resolver la tabla de contrato en docs/integracion/contrato-api.md; BE-00–BE-03 ya existen, pero no definen registro/login/logout.
- Definir el recorrido exacto tras el primer registro y el papel de verificación de correo, apodo/avatar y grupos.
- Identificar maqueta visual y responsables de cuenta Play/applicationId/canal.
- Confirmar con el docente el alcance de distribución: la lista específica pide iniciar configuración de Play, mientras el punto 1 general habla de iniciar evaluación descargando desde tienda. No se presume que ambos textos equivalgan a publicación pública para noviembre.
- Abrir la implementación por instrucción del usuario cuando el alcance/contratos estén listos; esta preparación no ejecuta código o cuentas para cumplir una fecha.

## Requisitos generales que deben conservarse en el plan

| Puntos v3 | Ámbito y preparación pendiente |
|---|---|
| 1–6 | Evaluación interactiva y del proyecto, composición nube/BD/backend/móvil/landing, fidelidad a maqueta, SDD/DevSecOps y desarrollo con agentes; OpenCode/WARP son sugerencias, no una migración autorizada de herramientas |
| 7–8 | Pruebas unitarias, seguridad e integración y CI/CD en ambos proyectos; las guías propias de pruebas y compuertas preparan la parte móvil |
| 9–11 | Caché por capas, logout y matriz de versiones previa al desarrollo; no confundir caché móvil con Offline-First o revocación con simple borrado local |
| 12 | MCP opcional cuando lo ofrezcan servicios; no se instala por esta lista |
| 13–15 | Servicios y balanceo en nube, HTTPS, NoSQL, backend en al menos dos instancias, nube permitida/costo cero solicitado, observabilidad, JWT/Redis, rendimiento y creación/despliegue/eliminación de infraestructura; corresponden al equipo backend/nube. Terraform es recomendación |
| 16 | Moderación antes de almacenar, push, privacidad, eliminación de cuenta, permisos, continuidad de sesión y funciones ML/IA. Almacenamiento SQLite/sincronización condicionado a necesidad |
| 17–18 | Landing con acceso a tienda y privacidad/datos, analítica solicitada y proyectos en una cuenta GitHub del equipo; configuración/cuentas y tratamiento de datos pendientes de coordinar |
| 19–20 | ML/IA con el mismo método, evaluación general del 2 de diciembre y entrega de proyectos en .rar/script de BD; no se ejecuta empaquetado ahora |

El requisito de costo cero necesita verificación de servicios/cuenta/cuotas del equipo; no es una garantía técnica. El almacenamiento NoSQL del servidor no contradice SQLite/Room local. «Al menos una función ML e IA generativa» necesita concretar capacidades y su evidencia; no se da por cumplido por mencionar voz o una API.

### Pendientes generales detallados frente a la base actual

| Requisito general del PDF | Situación y límite actuales |
|---|---|
| Descarga desde Google Play, evaluación interactiva y de fuentes, fidelidad a maqueta | Publicación/recorrido/funciones no aceptados. Para noviembre la lista específica solo exige comenzar registro/configuración Play; conservar la duda de interpretación con el docente |
| SDD/DevSecOps y desarrollo backend/móvil con agentes | Expedientes y trabajo con agente existentes; no acreditan producto terminado. OpenCode/WARP son herramientas sugeridas, no obligatorias |
| Pruebas unitarias/seguridad/integración y CI-CD en ambos | Base backend verificada localmente; base móvil tiene evidencia histórica. Workflows raíz definidos, sin CI remoto/CD o seguridad funcional aceptados |
| Caché BD/backend/frontend, logout ambos y matriz de versiones por despliegue | Caching de CI no equivale a caché del producto. Sin BD/sesión/logout; matriz documental móvil existente no acredita su aplicación en despliegues |
| Nube, balanceo, HTTPS; backend al menos dos instancias, AWS/Azure/Google Cloud y costo cero solicitado | No desplegado. Servicios/costos/cuotas/cuentas por verificar, sin promesa de gratuidad. TestServer no acredita HTTPS exterior o réplicas |
| NoSQL y servicios/datos centrales; imágenes en CDN de nube si se suben | Proveedor/modelo/índices/almacenamiento/CDN y recuperación pendientes. Sin SQL/EF por copiar ejemplos |
| Observabilidad cloud, JWT/Redis distribuido; caché, rate limiting, middleware, paginación, índices, JSON y compresión | Solo middleware/JSON/log técnico mínimo en backend local. No auth, estado compartido, controles de producto o servicio cloud; no se dan por satisfechos los demás elementos |
| Paso de creación/despliegue/eliminación de infraestructura y versionamiento | Pendiente; estos workflows construyen/analizan, no despliegan. Terraform/IaC se **recomienda**, no es una herramienta obligatoria del PDF |
| Moderación previa, push, privacidad/datos vinculados a landing, eliminar cuenta, permisos y sesión viva salvo logout | No implementados como flujos. Fotos/enlaces sin cámara ni carga de video son decisiones de PactoGG; no añadir video por la redacción genérica. Push exigido mantiene conflicto con FCM pospuesto; sesión/renovación/revocación por diseñar |
| SQLite/sincronización si el proyecto lo requiere; función ML e IA generativa | Sincronización es condicional; no activa automáticamente WorkManager/Offline-First. ML/IA/proveedores/evidencia pendientes, con prácticas SDD/DevSecOps propias |
| Landing con tienda/privacidad/datos y vinculación de correo/Google Analytics | Pendiente; no se configura analítica/cuenta por esta preparación. Hosting libre |
| Proyectos en una sola cuenta GitHub, carpeta compartida del líder | Cuenta/accesos/coordinación pendientes; no se crea Git/remoto. Correo único para todos los servicios es **sugerencia**, no obligación ni credencial a guardar |
| Entrega .rar y script de BD; evaluación general 2 de diciembre | Pendiente del producto/NoSQL elegido; no inventar un script SQL o empaquetado de entrega. Reglas de remedial/extraordinario del PDF se conservan |

MCP es opcional si el servicio lo ofrece; despliegue GitHub→Play también es sugerido. Ninguna recomendación abre cuentas, instalaciones globales, publicación o funciones. Los pendientes generales no se reducen al corte de noviembre ni se declaran cumplidos por preparar CI.

## Conflictos vigentes

Push exigido frente a FCM pospuesto; continuidad de sesión frente a contrato de expiración/refresh aún abierto; sincronización condicional frente a WorkManager/Offline-First pospuestos; fidelidad a maqueta frente a discrepancias de fuentes. Los requisitos generales no incorporan cámara o video a PactoGG: se respetan las decisiones de producto y se registra la discrepancia con el alcance genérico.

La eliminación de carpetas de reserva no elimina responsabilidades del equipo. Nube, backend y landing se coordinan documentalmente sin crear proyectos vacíos en el workspace móvil. Las decisiones se cierran con sus responsables; el usuario mantiene la instrucción de no programar, instalar, publicar o subir a Git en esta fase.

## Preparación disponible y siguiente cambio

Las guías de [arquitectura](../arquitectura/android.md), [pruebas](plan-de-pruebas.md) y [compuertas/entrega](controles-y-compuertas.md) conservan los lineamientos. Los [cuatro expedientes concretos](../../specs/README.md) preparan cambios de CI, pruebas, release y acceso a cuenta. PG-03 parte del [primer flujo](primer-flujo-movil.md) y mantiene pendientes contrato backend y alcance/maqueta; su diseño funcional debe cerrarse al recibirlos.

Cada cambio real tendrá especificación, plan, tareas y checklists/checklist.md según la plantilla. Las verificaciones del corte requieren funcionamiento y evidencia; no se completan por organizar carpetas.
