---
name: pactogg-api-contracts
description: Revisar contrato móvil/API y mapeo de DTO de PactoGG. Usar antes de consumir
  un endpoint o cambiar payloads, errores o autorización.
---

# pactogg-api-contracts

Raíz del proyecto: tres niveles arriba de esta carpeta. Aplicar la fase vigente de AGENTS.md y la constitución; esta skill no amplía la autorización del usuario.

Leer OpenAPI ratificado si existe; si falta, registrar el contrato pendiente sin inventar rutas, nombres de campos, IDs o rowVersion del ejemplo. Relacionar método/recurso, permisos, validación, respuestas, paginación e idempotencia cuando corresponda.
Describir DTO y mapeadores de fechas, opcionales y errores; separar nombres del transporte de modelos del cliente. Tratar 401, 403, 409, 429 y fallos de servicio según su significado y política acordada.
No reutilizar hosts/IP, credenciales, login de ventas ni prefijos de columnas del profesor. Entregar matriz de contrato, fuentes y fixtures necesarios para pruebas futuras; no generar interfaces HTTP sin contrato aprobado.

Completar la [tabla de recepción backend](../../../docs/integracion/contrato-api.md) antes de cerrar la primera spec funcional. Relacionar historias/componentes del [primer flujo](../../../docs/preparacion/primer-flujo-movil.md), sin importar rutas del Excel como contrato aprobado. Registro/login no justifican omitir decisión de refresh, revocación o primer ingreso.
