# Tareas MO-VOT-04

- [x] T1: especificación y fuente fijada.
- [x] T2: DTO, API y repositorio.
- [x] T3: estado, navegación y pantalla implementados localmente; validación de ejecución pendiente en T5.
- [ ] T4: pruebas específicas C1–C6 y matriz.
- [ ] T5: ejecución y evidencia local.
- [ ] T6: aceptación externa.

Revisión 2026-11-06: T4 conserva pendientes de cobertura por criterio; T5 está bloqueada por Gradle y T6 no tiene aceptación externa. Ver ../../docs/pruebas/ampliacion-local-2026-11-06.md.


## Actualizacion de contrato recibida - 2026-11-06

La lista de 54 rutas del usuario exige Idempotency-Key en esta operacion. El consumidor genera un UUID por invocacion antes de SessionManager y lo envia como cabecera obligatoria. No activa reintentos automaticos ni persiste claves en logs. La prueba HTTPS existente verifica presencia y formato UUID junto con autorizacion, ruta y numero de peticiones. Implementacion local; ejecucion del candidato pendiente. La semantica de reenvio manual despues de respuesta perdida y la vigencia del registro del servidor requieren el contrato actualizado.
