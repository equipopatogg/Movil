# Incorporación local por ramas — 2026-11-04

El usuario autoriza copia previa y commits para organizar la entrega. No se hace push ni se altera main publicado. Cada rama nace de la anterior y necesita su base; no son cinco implementaciones independientes.

| Rama | Alcance |
|---|---|
| codex/base-ui | Prototipo visual, componentes y recursos; no API de producto |
| codex/acceso-sesion | Contrato compartido, cifrado/refresh y acceso real; registro/login/logout |
| codex/grupos | Pantalla de creación de grupo y resultado real |
| codex/consulta-torneos | Consulta y bracket solo lectura; sin crear/inscribir |
| codex/retos-evidencias-pendientes | UI de retos/evidencias y contexto conectado completo; bloqueo 503 del proveedor |

Los DTO, interfaces, repositorio, estado y transporte actuales son compartidos: se incorporan como soporte contractual en acceso/sesión para que cada rama compile sin duplicarlos. Las siguientes ramas habilitan su presentación correspondiente. No se afirma que cada endpoint haya sido desarrollado de forma independiente desde el principio. El historial de UI anterior al contrato se conserva.

Se comprueban compilación debug, JVM, Lint/Detekt y compilación androidTest por etapa, junto con candidatos/secretos. Las pruebas live de backend requieren opt-in explícito y datos QA privados; no se ejecutan automáticamente al preparar commits. La evidencia física anterior identifica su propio candidato, no un CI nuevo.

Estados de specs: acceso, sesión y grupos tienen flujos reales positivos comprobados; torneos solo respuesta vacía; retos fallan con 503 MODERATION_UNAVAILABLE y evidencias no pudieron comprobarse. Se actualiza el estado sin cerrar criterios negativos, cobertura, release/16 KB, Play, aceptación humana o CI remoto que no estén demostrados. **Commit preparado no equivale a spec integral terminada.**

La copia previa conserva fuentes/configuración/notas locales e historial Git móvil, excluyendo caches/builds regenerables. Se guarda fuera del proyecto, en respaldos locales de PactoGG; no se versiona. Credenciales de QA, guías personales y material backend quedan fuera del repositorio móvil.


## Commits locales preparados

- codex/base-ui: `5fa2c67`; debug, 9 JVM, Lint, Detekt, androidTest compilado y secretos PASS.
- codex/acceso-sesion: `e684fa1`; debug, 24 JVM, Lint, Detekt, androidTest compilado, actionlint y secretos de candidatos PASS.
- codex/acceso-sesion: `cea05e1`; debug, 24 JVM, Lint, Detekt, androidTest compilado, actionlint y secretos de candidatos PASS.
- codex/grupos: `0f78e89`; debug, 24 JVM, Lint, Detekt y androidTest compilado PASS.
- codex/consulta-torneos: `5bceeb2`; debug, 24 JVM, Lint, Detekt y androidTest compilado PASS.
- codex/retos-evidencias-pendientes: `0567435`; debug, 24 JVM, Lint, Detekt y androidTest compilado PASS.

Base publicada conservada: 2a9fb5e. Ramas dependientes verificadas por etapa; no push/PR/merge remoto. Gitleaks de los candidatos completos sin hallazgos. main no se adelanta a los pendientes funcionales. La siguiente revisión parte de codex/consulta-torneos para lo disponible; codex/retos-evidencias-pendientes conserva el trabajo bloqueado.
