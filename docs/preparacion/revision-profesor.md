> Alcance de esta primera extracción: WebAPIDevSecOpsScalling. La revisión ampliada encontró la referencia móvil SDD y sus agentes/skills; consultar [repositorios móviles y agentes](repositorios-moviles-y-agentes.md).

# Extracción y adaptación del profesor

Revisión fijada: `f1e54328562b20e08a9c1d9824083dd388adf204`. Inventario: [fuente.json](../../referencias/profesor/fuente.json).
Se inspeccionaron documentos, configuración/estructura de CI, reglas y muestras técnicas. No se ejecutó la referencia ni se auditó cada archivo del repositorio.
La extracción es una adaptación redactada para PactoGG; no una copia de API, scripts o instrucciones externas. Las prácticas son candidatas hasta ratificarlas. La referencia es backend DevSecOps; la estructura SDD propia organiza su aplicación.

| ID | Origen | Qué aprovechar y adaptar | Superficie/estado |
|---|---|---|---|
| PROF-01 | AGENTS.md | Contexto, convenciones e instrucciones acotadas por fase | Método preparado en AGENTS propio |
| PROF-02 | CHECKLIST_PR.md y PR template | Revisión con fuente, spec, criterio, evidencia y pendientes | Plantilla documental adaptada |
| PROF-03 | MemoriaPlan.md | Etapas y dependencias convertidas en spec/plan/tasks | Método propio preparado |
| PROF-04 | agent.md y MemoriaDespliegue.md | Registrar causa, entorno y evidencia; distinguir herramienta de producto | Plantilla de evidencia preparada |
| PROF-05 | ci-cd.yml: changes | Seleccionar controles por Android/backend/infra y dependencias compartidas | Diseño futuro |
| PROF-06 | build-and-test | Separar compilación y suites, conservar resultados | Gradle móvil y dotnet backend, propuesto |
| PROF-07 | UnitTest/IntegrationTest/SecurityTest | Suites por responsabilidad, sin equivaler dobles a servicio real | Diseño de pruebas preparado |
| PROF-08 | Directory.Build.props | Analizadores durante compilación | .NET backend; Lint/Detekt Android; propuesta |
| PROF-09 | .semgrep/semgrep.yaml | Reglas específicas y casos positivos/negativos | Traducir riesgos C# a Kotlin/API; no copiar reglas sin validar |
| PROF-10 | SCA y SBOM del CI | Dependencias transitivas e inventario por artefacto | Gradle runtime/variante y NuGet por separado; propuesto |
| PROF-11 | dependabot.yml | Revisión periódica de dependencias | Gradle/Actions; NuGet cuando exista; cadencia pendiente |
| PROF-12 | Sonar/cobertura | Medición y revisión de código nuevo | Línea base, exclusiones y umbral propio por ratificar |
| PROF-13 | Dockerfile, docker-build, Dockle | Construcción por etapas y ejecución sin privilegios | Solo backend/infra; propuesto |
| PROF-14 | Cosign y verificación de imagen | Verificar digest e identidad esperados antes de usar artefacto | Backend; Android exige firma AAB/Play distinta |
| PROF-15 | dast/zap-pr | Análisis dinámico en entorno aislado con API descrita | Backend autenticado; no verifica el APK completo |
| PROF-16 | fuzz y fuzzing/ | Entradas inesperadas desde contrato, con cuotas/datos sintéticos | API, pospuesto a integración |
| PROF-17 | ContractTest/ProviderTests.cs | Verificar contrato consumidor/proveedor | OpenAPI/fixtures primero; Pact selectivo por justificar |
| PROF-18 | DatabaseTest/MigrationsTests.cs | Persistencia y cambios/migraciones reales | Room y NoSQL elegida; no heredar MSSQL/EF |
| PROF-19 | mutation-test/mutation-nightly | Capacidad de pruebas para detectar cambios defectuosos | Opcional posterior; herramienta Android sin elegir |
| PROF-20 | PerformanceTest | Escenarios y presupuestos medidos | API carga; Android arranque/memoria/fluidez; sin cifras heredadas |
| PROF-21 | QualityMetricsService/dashboard/collect-quality-metrics.sh | Métricas con fuente, unidad, fecha y versión | Diseño futuro, sin reemplazar desconocido por cero |
| PROF-22 | CorrelationId/ExceptionHandlingMiddleware | Diagnóstico con correlación y errores seguros | Contrato API/móvil por definir, sin PII ni detalles internos |
| PROF-23 | AuthorizationTests/JwtAlgorithmConfusionTests | Casos adversos de permisos y tokens | Backend por grupo/recurso; APK no firma JWT |
| PROF-24 | RefreshIntegrationTests/TokenBlacklistService | Rotación y revocación bajo fallos | Backend distribuido; fallback local no garantiza consistencia global |
| PROF-25 | CacheService/RedisFailureTests | TTL, invalidación y recuperación de caché | Separar contenido y decisiones de seguridad; valores pendientes |
| PROF-26 | AuditHashChain | Detectar alteración de secuencia | Diseñar persistencia, orden, reinicios y privacidad; no asumir inmutabilidad global |
| PROF-27 | deploy.yml/deploy/aws/deploy-app.sh | Separar infraestructura, aplicación y smoke test | Plan operativo futuro; sin heredar stacks ni ejecutar |
| PROF-28 | Despliegue mediante SSM | Acceso operativo temporal y mínimo privilegio | Evaluar al definir nube; no crear accesos |
| PROF-29 | ChaosTest/chaos-nightly | Ensayos controlados de recuperación | Conservar concepto; caos avanzado pospuesto |
| PROF-30 | schedule-deploy/schedule-destroy | Responsables, costo y ciclo de vida | Solo runbook futuro; no heredar horarios o destrucción automática |
| PROF-31 | hardening-report/hardening_summary.py | Consolidación de resultados y evidencia | Inventario esperado con estado explícito para controles ausentes |

## Límites que hay que corregir al adaptar

- Cobertura: checklist/plantilla declaran 75%; script y CI usan 45%. No existe un único umbral coherente para heredar.
- Trivy está configurado para devolver éxito aun con hallazgos. Sonar Quality Gate, contratos y partes de ZAP/fuzz toleran fallos. Esos reportes no equivalen a bloqueo obligatorio.
- Semgrep usa imagen latest; hay que fijar versión/digest y demostrar comportamiento de salida ante hallazgos.
- El resumen omite reportes ausentes. La adaptación debe mostrar NO_EJECUTADO/ERROR y no insinuar cero hallazgos de controles faltantes.
- Listar vulnerabilidades con una CLI no demuestra que exista una política de bloqueo por severidad; se debe ensayar y comprobar.
- El fallback a memoria para revocación depende de una instancia. No garantiza la misma decisión de sesión en otro nodo.
- La cadena de auditoría usa estado estático de proceso; no demuestra persistencia, orden global o resistencia a reescritura de almacenamiento.
- Exclusiones amplias de archivos y reglas estáticas genéricas necesitan justificación y pruebas de falsos positivos/negativos.

## Material específico que no se convierte en requisito de PactoGG

Ventas/clientes/productos/saga, SQL Server/EF, pagos/facturación, dominios, cuentas, nombres de stack, horarios de nube, límites de carga y tiempos JWT del ejemplo.
`opencode.json` conecta MCP de DigitalOcean: es configuración ajena, no metodología SDD; no se adopta ni activa.
Tampoco basta copiar CORS o validadores SQL para proteger una app Android y una API NoSQL.

## Controles propios que el ejemplo backend no resuelve

MASVS/MASTG móvil, Keystore, permisos, Photo Picker, ciclo de vida, UX/accesibilidad, firma AAB/Play, ABI/16 KB, ML/voz, salida de IA, moderación antes de guardar, privacidad/retención/borrado y autorización de grupos/retos/evidencias/torneos requieren especificación propia.

## Fuentes exactas

- [AGENTS.md](https://github.com/edelomeza/WebAPIDevSecOpsScalling/blob/f1e54328562b20e08a9c1d9824083dd388adf204/AGENTS.md)
- [CHECKLIST_PR.md](https://github.com/edelomeza/WebAPIDevSecOpsScalling/blob/f1e54328562b20e08a9c1d9824083dd388adf204/CHECKLIST_PR.md)
- [.github/workflows/ci-cd.yml](https://github.com/edelomeza/WebAPIDevSecOpsScalling/blob/f1e54328562b20e08a9c1d9824083dd388adf204/.github/workflows/ci-cd.yml)
- [.semgrep/semgrep.yaml](https://github.com/edelomeza/WebAPIDevSecOpsScalling/blob/f1e54328562b20e08a9c1d9824083dd388adf204/.semgrep/semgrep.yaml)
- [scripts/check_coverage.py](https://github.com/edelomeza/WebAPIDevSecOpsScalling/blob/f1e54328562b20e08a9c1d9824083dd388adf204/scripts/check_coverage.py)
- [scripts/hardening_summary.py](https://github.com/edelomeza/WebAPIDevSecOpsScalling/blob/f1e54328562b20e08a9c1d9824083dd388adf204/scripts/hardening_summary.py)
- [scripts/collect-quality-metrics.sh](https://github.com/edelomeza/WebAPIDevSecOpsScalling/blob/f1e54328562b20e08a9c1d9824083dd388adf204/scripts/collect-quality-metrics.sh)
- [WebAPIDevSecOps/Services/TokenBlacklistService.cs](https://github.com/edelomeza/WebAPIDevSecOpsScalling/blob/f1e54328562b20e08a9c1d9824083dd388adf204/WebAPIDevSecOps/Services/TokenBlacklistService.cs)
- [WebAPIDevSecOps/Middleware/AuditHashChain.cs](https://github.com/edelomeza/WebAPIDevSecOpsScalling/blob/f1e54328562b20e08a9c1d9824083dd388adf204/WebAPIDevSecOps/Middleware/AuditHashChain.cs)
- [opencode.json](https://github.com/edelomeza/WebAPIDevSecOpsScalling/blob/f1e54328562b20e08a9c1d9824083dd388adf204/opencode.json)
