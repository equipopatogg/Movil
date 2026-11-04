---
name: pactogg-android-architecture
description: Revisar capas y plan técnico Kotlin/Compose de PactoGG, incluidos DTO,
  mapeadores y casos de uso selectivos. Usar en arquitectura Android, no para crear
  funcionalidades sin spec.
---

# pactogg-android-architecture

Raíz del proyecto: tres niveles arriba de esta carpeta. Aplicar la fase vigente de AGENTS.md y la constitución; esta skill no amplía la autorización del usuario.

Relacionar responsabilidad de presentación, abstracciones interiores, datos y composición con la estructura existente de Movil/. Mantener dominio sin Android, JSON ni cliente HTTP. DTO de transporte se traducen explícitamente a modelos interiores.
Justificar cada caso de uso por regla/reutilización/composición; no exigir una clase por cada operación trivial ni crear modelos vacíos para cumplir un molde. Identificar efectos de ciclo de vida, cancelación y datos guardados tras pérdida de proceso.
El profesor usa Koin y un árbol distinto: adaptar el principio a Hilt y a los paquetes actuales, sin renombrar todo preventivamente. Salida: dependencias permitidas, decisiones, riesgos y verificación futura por criterio; sin editar código en preparación.
