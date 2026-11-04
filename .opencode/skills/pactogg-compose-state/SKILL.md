---
name: pactogg-compose-state
description: Diseñar o revisar estados, eventos y navegación Compose conforme a una
  spec y maqueta de PactoGG. Usar para planes de UI y ciclo de vida.
---

# pactogg-compose-state

Raíz del proyecto: tres niveles arriba de esta carpeta. Aplicar la fase vigente de AGENTS.md y la constitución; esta skill no amplía la autorización del usuario.

Separar estado visible, acciones del usuario y efectos. Definir carga, vacío, error, ausencia de red, denegación de permisos y restauración solo cuando apliquen al flujo aprobado.
Evaluar cómo se conservan/consumen efectos durante pausa, rotación y pérdida de proceso; no imponer SharedFlow como solución a cualquier navegación ni transmitir secretos mediante argumentos. Mantener un contenedor de navegación coherente con la maqueta.
Preservar significado HTTP: 401 y 403 no son intercambiables; falta de permiso no obliga automáticamente a cerrar sesión. Entregar tabla estado→acción→efecto→criterio y escenarios de prueba, sin implementar pantallas en esta fase.
