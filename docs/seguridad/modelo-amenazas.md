# Modelo de amenazas inicial de PactoGG

Estado: hipótesis para revisión documental, 2026-10-29. No describe una arquitectura desplegada ni hallazgos comprobados. Las medidas siguientes son propuestas de análisis; no activan herramientas ni funciones.

## Activos y fronteras

Activos por revisar: sesión, identidad/apodo, pertenencia a grupos, contenido y enlaces, puntos virtuales y resultados de retos/torneos, claves de firma y evidencias técnicas. Los datos definitivos y su clasificación dependen de fuentes y contratos ratificados.

Fronteras previstas: usuario/sistema Android → cliente; cliente → backend HTTPS; backend → persistencia y proveedores; fuentes/dependencias → futura compilación; artefactos → firma/distribución. La UI no es una frontera de autorización. La base técnica actual no demuestra la existencia de esos flujos funcionales.

## Escenarios que requieren diseño y pruebas

| Escenario propuesto | Revisión Android | Dependencia backend/equipo | Evidencia futura |
|---|---|---|---|
| Acceso a datos de otro grupo cambiando un ID | Manejar denegación sin exponer contenido | Autorización por objeto y pertenencia en cada operación | Casos entre identidades y grupos distintos |
| Robo o reutilización de sesión | Almacenamiento, logs, backups, ciclo de vida y cierre local | Expiración/revocación y contrato ante fallo | Inspección local y pruebas de sesión acordadas |
| Enlace o archivo malicioso | Validación de entrada, apertura segura, formatos y límites | Análisis de contenido/URLs, SSRF si servidor los recupera, transporte temporal | Casos de abuso en un entorno autorizado |
| Contenido persistido antes de moderar | No asumir que una URL firmada cumple análisis previo | Definir transitorio, persistencia definitiva, fallo del proveedor y borrado | Revisión del flujo y pruebas por estado |
| Manipulación de puntos/resultados o repetición de solicitudes | No usar estado local como autoridad | Invariantes, concurrencia, idempotencia y auditoría | Pruebas de negocio con reglas ratificadas |
| Exposición por componentes Android | Permisos, componentes exportados, intents, portapapeles y capturas según riesgo | Contratos de compartición de datos | Inspección y pruebas dinámicas aplicables |
| Salida de IA manipulada o errónea | No tratar inferencias como aprobación de acciones | Validación de salida, límites y ratificación de reglas | Casos adversos y de indisponibilidad del proveedor |
| Dependencia o pipeline comprometidos | Inventario, SAST/SCA, SBOM del artefacto y secretos | Permisos mínimos y custodia de CI/firma | Evidencias de cadena de suministro y firma |
| Pérdida de red/proceso durante una escritura | Estado honesto, cancelación y recuperación | Confirmar resultado e impedir duplicados | Pruebas de interrupción según contrato |
| Exposición por retención o borrado incompletos | Cachés, datos locales y sesión entre cuentas | Borrado/anonimización, copias, auditoría y proveedores | Guion de privacidad extremo a extremo |

No se asignan severidades definitivas sin estimar impacto, probabilidad y exposición del diseño concreto. El equipo deberá designar responsables; no se inventan nombres, SLAs o aprobaciones. Las claves del proveedor de IA y la firma nunca se colocan en el cliente como solución de integración.

## Siguiente revisión

Vincular cada escenario aplicable a fuente, spec, decisión, control y criterio de prueba cuando esa spec se abra. Cerrar supuestos de sesión, moderación, datos, reglas y firma. Registrar aceptación o mitigación del riesgo, responsable y revisión; no cerrar un riesgo solo por instalar un escáner.

Consultar [MASVS](masvs-checklist.md), [compuertas](../preparacion/controles-y-compuertas.md), [contrato API](../integracion/contrato-api.md) y [pendientes](../preparacion/pendientes.md).
