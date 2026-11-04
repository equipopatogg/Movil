# Matriz inicial de seguridad Android

Fecha de consulta: 2026-10-29. Referencias: [OWASP MASVS](https://mas.owasp.org/MASVS/) para requisitos móviles y [MASTG](https://mas.owasp.org/MASTG/) para orientar su verificación. Esta matriz agrupa temas; no enumera todos los requisitos ni acredita conformidad o certificación.

La selección de versión/perfil, IDs concretos y pruebas MASTG aplicables queda pendiente para la spec y artefacto que se evalúen. No se traslada la checklist ASVS L2 ni sus casillas marcadas del profesor.

| Grupo MASVS | Revisión propuesta para PactoGG | Evidencia que se planeará | Estado de esta integración |
|---|---|---|---|
| STORAGE | Sesión, caché, logs y backups sin exposición sensible | Inspección de datos y pruebas de limpieza | NO_EJECUTADO |
| CRYPTO | Claves y uso de criptografía adecuados al diseño | Revisión de custodia/Keystore y fallos | NO_EJECUTADO |
| AUTH | Sesión y separación de autenticación/autorización | Casos de expiración, revocación y denegación | NO_EJECUTADO |
| NETWORK | Transporte seguro y autenticación del servidor | Pruebas de confianza TLS y configuración release | NO_EJECUTADO |
| PLATFORM | Permisos, componentes e intercambio con otras apps | Revisión de manifiesto/intents y dinámica | NO_EJECUTADO |
| CODE | Dependencias, entradas y mantenimiento seguro | SAST/SCA y tratamiento de hallazgos | NO_EJECUTADO |
| RESILIENCE | Riesgo de modificación y análisis del cliente | Revisión de necesidad y pruebas justificadas | NO_EJECUTADO |
| PRIVACY | Minimización, transparencia y control de datos | Revisión de flujos y borrado | NO_EJECUTADO |

La resiliencia se selecciona según riesgo; no autoriza instalar antitampering, root detection, pinning u otras dependencias por defecto. La firma Android y la autorización del servidor siguen siendo asuntos propios, con evidencia diferenciada.

Los resultados técnicos históricos están en [verificación local](../verificacion-local.md). No se reejecutaron aquí ni acreditan todos los requisitos de una categoría. Cada evaluación futura registrará requisito/versión, artefacto, prueba concreta, resultado y límites con la [plantilla de evidencia](../../.specify/templates/evidencia-template.md).

## Backend y controles complementarios

[ASVS](https://asvs.owasp.org/) se dirige a aplicaciones web y servicios; su uso en el backend .NET requiere seleccionar versión, nivel y requisitos con sus responsables. No se declara ASVS L2 aprobado o cumplido ni se reutilizan IDs antiguos sin verificar versión.

SAST, SCA/SBOM, secretos, firma y seguridad del pipeline complementan la evaluación móvil; no sustituyen pruebas dinámicas. ZAP sobre una API no constituye una evaluación completa del APK. Véanse [modelo de amenazas](modelo-amenazas.md), [pruebas](../preparacion/plan-de-pruebas.md) y [compuertas](../preparacion/controles-y-compuertas.md).
