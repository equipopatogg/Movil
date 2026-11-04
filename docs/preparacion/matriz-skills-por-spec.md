# Selección de ayudas por especificación

Esta tabla prepara el uso; no registra ejecución ni aprobación funcional.

| Spec/alcance | Perfiles pertinentes | Skills locales | Descarga externa |
|---|---|---|---|
| Preparación documental vigente, sin spec de corroboración | sdd-coordinator, spec-drafter, traceability-reviewer, docs-keeper | sdd-base, coherencia, traceability-evidence, skill-discovery | No requerida para lo ya definido |
| PG-00 integridad/evidencias CI | security-reviewer, release-reviewer, test-designer | devsecops-plan, traceability-evidence | No requerida para redactar; revisar capacidad al implementar |
| PG-01 pruebas/medición Android | test-designer, plan-architect | android-test-plan, android-architecture | Según motor/entorno elegido; sin descarga actual |
| PG-02 corrección release 16 KB | release-reviewer, layer-guardian | android-architecture, devsecops-plan | Solo ante capacidad faltante de inspección verificada |
| PG-03 contrato móvil/API | api-contract-reviewer, backend-architect | api-contracts, backend-api-review | Pendiente de contrato; no generar rutas |
| PG-03 sesión | security-reviewer, backend-architect | session-design, local-storage | Según contrato y almacenamiento elegido |
| PG-03 UI de registro/login | ui-reviewer, plan-architect | mobile-ui-review, compose-state | Según maqueta ratificada, sin importar estilos ajenos |

Todos los nombres locales llevan prefijo pactogg-. No se reemplazan IDs originales de historias con estos renglones orientativos.

Los [expedientes actuales](../../specs/README.md) son borradores autorizados por D-SDD-06. Esta selección no invoca perfiles, cambia sus permisos ni instala skills. Las guías generales y los borradores retirados conservan su alcance; cada cambio concreto tiene su checklist en checklists/checklist.md.
