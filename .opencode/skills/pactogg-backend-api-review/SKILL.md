---
name: pactogg-backend-api-review
description: Preparar o revisar arquitectura .NET y contrato compartido de PactoGG.
  Usar para adaptar prácticas backend del profesor sin heredar su negocio SQL.
---

# pactogg-backend-api-review

Raíz del proyecto: tres niveles arriba de esta carpeta. Aplicar la fase vigente de AGENTS.md y la constitución; esta skill no amplía la autorización del usuario.

Separar API, aplicación, reglas, persistencia y proveedores según decisiones de PactoGG. .NET está confirmado; versión, NoSQL concreta y servicios externos requieren cierre de su spec. No adoptar SQL Server/EF, saga de ventas ni límites de JWT de las referencias.
Revisar autorización por grupo/recurso, transiciones e idempotencia cuando las reglas estén ratificadas. Caché de contenido y revocación distribuida no comparten automáticamente política de fallback. Una cadena de hash por proceso no acredita auditoría persistente global.
Entregar alternativas, impactos móvil/backend, dependencias y pruebas propuestas. No crear solución, endpoints, migraciones, contenedores o infraestructura en preparación.
