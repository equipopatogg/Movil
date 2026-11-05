# Entrega por ramas y PR — 2026-11-04

Autorización: usuario pide ejecutar la entrega propuesta, con pruebas remotas y corrección de fallos. Se utiliza equipopatogg/Movil y su identidad local del repositorio; no se publica backend ni archivos ignorados.

| PR | Alcance | Base inicial |
|---|---|---|
| [6](https://github.com/equipopatogg/Movil/pull/6) | Operaciones sociales confirmadas | main |
| [7](https://github.com/equipopatogg/Movil/pull/7) | Pruebas, matriz y specs | Rama de operaciones |
| [8](https://github.com/equipopatogg/Movil/pull/8) | CI y compuerta Android | Rama de pruebas |

Son PR dependientes: el candidato del último incluye los tres commits, separados por alcance. Se comprueba el conjunto completo antes de integrar cambios en main. No se falsea la cronología de código, specs ni pruebas.

Protección configurada y leída mediante GitHub API: main exige Android PR quality gate emitido por GitHub Actions (app 15368), rama actualizada, conversaciones resueltas y aplica la regla también a administradores; fuerza de push y borrado deshabilitados. Sin revisión humana acreditada; configurar un revisor independiente requiere participación real de otro miembro.

Candidato remoto verificado: [PR 8, ejecución 41969860447](https://github.com/equipopatogg/Movil/actions/runs/41969860447), todos los trabajos obligatorios correctos, gate PASS y artefacto validado descargado. También pasó la ejecución push 41969816950 del mismo cambio. Ningún estado pendiente o cancelado acredita PASS.

| Resultado remoto | Evidencia comprobada |
|---|---|
| Tres categorías y conjunto JVM | 35 unitarias, 42 integración, 39 seguridad; 116 en conjunto, cero fallos/omisiones |
| Android API35/google_atd/x86_64 | Cinco pruebas reales de Room, Keystore y acceso, cero omisiones |
| Wrapper, Lint, Detekt y validadores | Trabajos correctos; 19 pruebas de política/recogida |
| Gitleaks/Semgrep/Trivy | Salida 0, sin hallazgos bloqueantes; Trivy limita el gate a HIGH/CRITICAL |
| SBOM CycloneDX | 192 componentes; inventario descargado y ligado al scanner |
| Gate | Nueve dependencias correctas y nueve controles PASS; informes y contexto íntegros |
| APK validado descargado | SHA256 6f9fc325ca7b38de9a629b68c8c66a394fbb889819eb95cdfb1e10ae5b847d52, coincide con el instalado en el emulador |

La fusión conserva controles obligatorios y se comprueba de nuevo en el PR final y main. Se pueden consultar sus ejecuciones finales en [Actions de main](https://github.com/equipopatogg/Movil/actions?query=branch%3Amain) y en el PR 6; este informe acredita específicamente el candidato anterior, sin anticipar el resultado de ejecuciones futuras. Cobertura local 19,48 %: medición, no suficiencia. Reglas/umbrales de producto y aceptación humana permanecen separados.


Pendientes que no desaparecen por entregar: cobertura suficiente y mínimo ratificado, negativos de la acción Wrapper con archivo corrupto y fixtures de reglas Semgrep, casos funcionales/proveedor restantes, catálogo de avatares, privacidad definitiva y aceptación humana. Los motores reales ya se ejecutaron; no confundir esos pendientes con ausencia total de SAST/SCA.
