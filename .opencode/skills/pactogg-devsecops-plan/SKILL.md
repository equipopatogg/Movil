---
name: pactogg-devsecops-plan
description: Preparar o revisar controles y compuertas Android/API de PactoGG. Usar
  para planes de SAST, SCA/SBOM, secretos, firma o entrega.
---

# pactogg-devsecops-plan

Raíz del proyecto: tres niveles arriba de esta carpeta. Aplicar la fase vigente de AGENTS.md y la constitución; esta skill no amplía la autorización del usuario.

Leer docs/preparacion/controles-y-compuertas.md. Para cada control indicar superficie, artefacto, herramienta candidata, criterio, estado y evidencia. SAST, SCA y SBOM son resultados distintos; DAST de API no verifica todo el APK; firma Android no es Cosign.
Conservar secretos fuera del APK/repositorio, HTTPS validado, permisos mínimos, revisión de componentes y privacidad. Tratar compatibilidad nativa 16 KB como pendiente de entrega, no resolverla ocultando el aviso.
Comprobar que el gate realmente falla cuando procede y que ausencia/error no se cuenta como cero hallazgos. Versiones, excepciones, umbrales y supresiones requieren trazabilidad. Salida documental; no activar pipelines, firmar ni escanear servicios durante preparación.

Consultar el [plan de pruebas](../../../docs/preparacion/plan-de-pruebas.md) y las [compuertas/entrega](../../../docs/preparacion/controles-y-compuertas.md). Solo proponer una spec de CI si delimita qué configuración/artefactos se crearán o cambiarán y cómo se verificarán. Enlazar controles con reportes y candidatos reales; separar runtime del APK, herramientas de build y backend. Cuenta, claves y canal no se consideran configurados por redactar el plan. No ejecutar los workflows anteriores por completar documentación.
