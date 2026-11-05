# Plan — MO-CON-06

Estado: preparación documental solicitada, no ejecución nueva. [Spec](spec.md).

1. Conciliar `POST /api/v1/contracts/{id}/accept` con [spec backend](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/specs/CON-06-accept-contract/spec.md), OpenAPI y límites de sección 7; confirmar despliegue antes de prueba real.
2. Reutilizar transporte/sesión y DTO/repositorio existentes; concretar campos, nulos y mapeos específicos de RF1–RF5. No crear módulos por plantilla.
3. Adaptar recorrido Compose real de esta operación con estados honestos, limpieza por cuenta y controles de doble toque/respuestas tardías. Dependencias: [MO-CON-03-detalle-reto](../MO-CON-03-detalle-reto/spec.md) · [MO-GRP-03-detalle-grupo](../MO-GRP-03-detalle-grupo/spec.md).
4. Implementar casos por cada AC con HTTP/estado y UI/dispositivo; comprobar éxito, errores y cambios de sesión; no copiar fixtures de backend como prueba propia.
5. Verificar candidato y proveedor autorizados, registrar informes, completar checklist solo por evidencia y separar aprobación de aceptación.

Reversión: desactivar entrada de la operación incompleta conservando pantallas; sin éxito de muestra. No modificar backend, TLS, versiones/dependencias ni publicar por preparar este expediente.

Deuda específica: Confirmar despliegue transaccional con dos cuentas QA autorizadas; fakes backend no prueban transacción Mongo real. No implementar liberación de fianza, voto o liquidación futura.


Plan iniciado por autorización expresa de implementar (2026-11-04). Arquitectura final compartida en spec: SocialRepository/ApiSocialRepository, interfaces API agrupadas y estado/pantallas conectadas; sin nuevos módulos vacíos ni dependencias. Pruebas finales y límites se vincularán al informe del candidato.


Actualización de calidad solicitada: reutilizar pruebas existentes y ejecutar las tres categorías de la matriz antes de entregar esta operación; corregir errores, vincular informes al candidato y mantener pendientes no demostrados. La CI local preparada no acredita ejecución remota.
