# Plan — PG-03

Estado: propuesta condicionada. [Spec](spec.md). No implementar antes de resolver sus entradas.

## 1. Diseño y secuencia

| Paso | Cambio / ruta | Dependencia | Criterios |
|---|---|---|---|
| 1 | Recibir/enlazar contrato y maqueta; cerrar reglas, campos, consentimiento, sesión y destino | Responsables de backend/producto; sin crear código | PG03-AC1 |
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
