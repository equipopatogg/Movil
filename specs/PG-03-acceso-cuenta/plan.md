# Plan — PG-03

Estado: propuesta condicionada. [Spec](spec.md). No implementar antes de resolver sus entradas.

## 1. Diseño y secuencia

| Paso | Cambio / ruta | Dependencia | Criterios |
|---|---|---|---|
| 1 | Enlazar contrato y boceto recibido; cerrar reglas, campos, consentimiento, sesión y destino | Responsables de backend/producto; sin crear código | PG03-AC1 |
| 2 | Definir modelos/interfaces en feature/auth/domain y DTO/mappers/servicio en data | PG03-AC1; decidir ubicación de sesión compartida | PG03-AC2, PG03-AC3 |
| 3 | Implementar repositorio y custodia sensible/limpieza con política acordada | Contrato y almacenamiento compatible ratificados | PG03-AC3, PG03-AC4 |
| 4 | Implementar estado/ViewModel y pantallas registro/login con strings y accesibilidad | Diseño/validación y componentes anteriores | PG03-AC2, PG03-AC3, PG03-AC5 |
| 5 | Conectar Hilt y navegación de arranque/primer ingreso/logout | Destino y políticas resueltos; sin pasar secretos | PG03-AC4, PG03-AC5 |
| 6 | Implementar/verificar PF-C1–PF-C6 y pruebas con proveedor autorizado | Fixtures/cuentas de prueba y entorno; PG-00/PG-01 útiles, no presumidos | PG03-AC2–AC6 |
| 7 | Conciliar alcance, contrato, evidencia y aceptación | Criterios completos, fallos/omisiones visibles | Todos |

Puede dividirse la implementación en registro, login y sesión manteniendo dependencias y aceptación por criterio; no marcar el conjunto terminado por entregar una sola pantalla.

## 2. UI, navegación y DI

Reutilizar Compose/Material 3 y el diseño oscuro naranja propio. Screen representa estado; ViewModel coordina eventos, sin contraseñas en navegación/estado persistido. Política de restauración y limpieza de entradas sensibles por cerrar según amenazas/ciclo de vida. Hilt proporciona repositorio y custodio elegido; no introducir Koin ni singleton estático por copia.

Inicio resuelve sesión antes del destino autenticado, sin mostrar contenido protegido transitorio. El destino exacto y posibles pasos de correo/apodo/perfil se fijan con producto/contrato antes del código.

## 3. Verificación y recuperación

Pruebas puras de reglas ratificadas y transiciones; MockWebServer para éxito/error/timeouts/respuestas inválidas y doble envío; Compose/dispositivo para estado/accesibilidad/ciclo de vida. Custodia sensible y borrado requieren pruebas del mecanismo Android real, no solo un almacén en memoria. Probar integración con API autorizada y correlación segura.

No guardar contraseñas para recuperar operaciones. Tratamiento de registro duplicado, reintento y fallo de logout proviene del contrato. Ante incompatibilidad, registrar error recuperable sin inventar una sesión; no cambiar backend o desactivar TLS. Evitar caché sensible no acordada. Documentar cualquier cambio de esquema local y su recuperación si la solución lo requiere.

## 4. Gates y cierre

Primero satisfacer PG03-AC1 y revisar ejemplos/tareas afectados. Después demostrar PG03-AC2–AC6 con contrato/maqueta/versiones identificados, pruebas y controles pertinentes. Conciliar [tareas](tasks.md), [checklist](checklists/checklist.md), aceptación y memoria. La fase actual solo prepara este diseño; no añade dependencias ni código.

## Coordinación actual

Boceto disponible; conciliación visual pendiente, no nueva solicitud de maqueta. Implementar acceso y transición según los expedientes de operaciones disponibles. Evidencia CI técnica real en docs/preparacion/ci-remoto-android.md no satisface integración funcional. Cerrar contrato como primer paso, con una entrada por operación y ejemplos sintéticos.

## Primera etapa acotada: diseño y UI futura

Preparación actual: únicamente el diseño enlazado desde la spec, sin crear Kotlin. Secuencia futura: componentes/strings y pantallas puras → variantes aisladas de estados → navegación de presentación y pruebas → integración contractual independiente. Rutas de destino propuestas: feature/auth/presentation/ y recursos propios; catálogo solo debug si se autoriza. Sin servicios HTTP, repositorios fake de producción ni sesión falsa en la etapa visual.

La implementación funcional de la tabla anterior permanece posterior al contrato. El código de UI no debe depender de nombres HTTP todavía desconocidos.


## Actualización vigente — contrato recibido, 2026-11-04

Backend externo fijado al commit f6c37d8a70de43765f870b78b452832a2c0842b9; ver [alineación canónica](../../docs/integracion/alineacion-backend-2026-11-04.md). Ya no falta recibir el contrato: falta conciliar las entradas específicas y verificar integración. El texto anterior conserva etapas históricas, no un estado vigente de ausencia total de contrato.

PG-03 mantiene sus IDs PG03-AC1–AC6 y coordinación, sin duplicar implementación. El detalle se divide en [MO-USR-01](../MO-USR-01-registro/spec.md) (AC2), [MO-AUTH-01](../MO-AUTH-01-login/spec.md) (AC3), [MO-AUTH-03](../MO-AUTH-03-sesion/spec.md) y [MO-AUTH-04](../MO-AUTH-04-logout/spec.md) (AC4); el destino se define en las operaciones de acceso (AC5). AC1 queda PARCIAL documental por recepción y dudas abiertas; AC2–AC6 no están verificados. No marcar aceptación de integración por apariencia o pruebas del prototipo. T03.1 recibe fuente pero conserva conciliación pendiente; T03.2–T03.8 se desglosan en MO-* sin duplicar servicios/custodia. Sin código nuevo por esta actualización.
