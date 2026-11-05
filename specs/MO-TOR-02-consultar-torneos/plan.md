# Plan — MO-TOR-02

Estado: propuesta, sin ejecución. [Spec](spec.md).

1. Revisar fuentes fijadas y resolver bloqueantes específicos de la sección 7 y matriz de integración; no copiar la aceptación backend.
2. Revisar UI y contratos internos existentes; concretar DTO/mappers/repositorio en feature/tournaments, con core/network y core/session compartidos. Elegir adaptaciones pequeñas, sin nuevos módulos por plantilla.
3. Implementar transporte/estado y conectar la pantalla al repositorio real; dependencia de sesión MO-AUTH-03. Sin reemplazar diseño aprobado ni borrar pantallas futuras.
4. Verificar cada AC con JVM/MockWebServer y Compose/dispositivo según superficie; probar errores y resultados inciertos, no solo camino feliz.
5. Comprobar contra proveedor autorizado y controles pertinentes del mismo candidato. Registrar informes, límites y aceptación separadamente.

Reversión: si no existe contrato/servicio compatible, acción queda no disponible; no volver silenciosamente al éxito de muestra. No alterar backend ni TLS para hacer pasar el consumidor. Entrega y criterio de cierre: todos los AC aplicables demostrados y deuda explícita; redactar no cierra tareas.


Implementación autorizada 2026-11-04: transporte/sesión compartidos y feature/connected agrupan el pequeño corte de producto; auth separado. No crear un repositorio/custodio por pantalla. Plan iniciado, ver reporte del consumidor para comprobaciones reales.


Actualización de calidad solicitada: reutilizar pruebas existentes y ejecutar las tres categorías de la matriz antes de entregar esta operación; corregir errores, vincular informes al candidato y mantener pendientes no demostrados. La CI local preparada no acredita ejecución remota.
