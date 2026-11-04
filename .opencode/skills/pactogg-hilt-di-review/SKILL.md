---
name: pactogg-hilt-di-review
description: Revisar composición y alcances Hilt en planes Android de PactoGG. Usar
  cuando una referencia Koin o una nueva dependencia afecte la arquitectura.
---

# pactogg-hilt-di-review

Raíz del proyecto: tres niveles arriba de esta carpeta. Aplicar la fase vigente de AGENTS.md y la constitución; esta skill no amplía la autorización del usuario.

Tomar Hilt como elección de la base actual; cambiar DI requiere una decisión explícita. Traducir la responsabilidad del ejemplo Koin, no sus single/factory a texto literal en Hilt.
Justificar duración y propietario de clientes, bases, repositorios y ViewModels; mantener la configuración reemplazable en pruebas. Evitar estado global oculto, localizadores de servicio y dependencias Android en dominio.
Comprobar compatibilidad futura AGP/Kotlin/KSP/Hilt con fuentes oficiales al necesitar una versión. Entregar grafo de responsabilidades y decisiones; no instalar paquetes ni modificar Gradle por una revisión documental.
